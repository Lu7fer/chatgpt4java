package cf.vbnm.chatgpt.client;

import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.entity.image.ImageReq;
import cf.vbnm.chatgpt.entity.image.ImageResp;
import cf.vbnm.chatgpt.entity.image.ImageSize;
import cf.vbnm.chatgpt.listener.StreamListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;


/**
 * open ai 客户端
 *
 * @author plexpt
 */
public class RestTemplateGPT implements ChatGPT {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateGPT.class);
    private final String apiKey;
    /**
     * keys
     */
    private final List<String> apiKeyList;

    private Supplier<String> keySupplier;

    private final Random random = new Random();
    /**
     * 自定义api host
     */
    private String apiHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public RestTemplateGPT(String apiKey, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        apiKeyList = null;
    }

    public RestTemplateGPT(List<String> apiKeyList, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        assert apiKeyList != null && apiKeyList.size() != 0;
        this.apiKeyList = apiKeyList;
        apiKey = null;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    private String getKey() {
        String key;
        if (keySupplier != null) {
            String s = keySupplier.get();
            if (s != null)
                return s;
        }
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = apiKeyList.get(random.nextInt(apiKeyList.size()));
        } else {
            key = apiKey;
        }
        log.debug("selected key: {}", key);
        return key;
    }

    public void setKeySupplier(Supplier<String> keySupplier) {
        this.keySupplier = keySupplier;
    }

    public ImageResp generationImages(String prompt, int n, ImageSize size, String responseFormat, String user) {
        if (!("url".equals(responseFormat) || "b64_json".equals(responseFormat))) {
            throw new IllegalArgumentException("response_format only support 'url' or 'b64_json'");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ImageReq> entity = new HttpEntity<>(new ImageReq(prompt, n, size, responseFormat, user), headers);
        return restTemplate.exchange(apiHost + "/v1/images/generations", HttpMethod.POST, entity, ImageResp.class).getBody();
    }

    public ImageResp generationImage(String prompt) {
        return generationImages(prompt, 1, ImageSize.SIZE_512, "url", null);
    }

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion 问答参数
     * @return 答案
     */
    @Override
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        String key = getKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(key);
        HttpEntity<ChatCompletion> requestEntity = new HttpEntity<>(chatCompletion, headers);
        return restTemplate.exchange(apiHost + "/v1/chat/completions", HttpMethod.POST, requestEntity,
                ChatCompletionResponse.class).getBody();
    }

    /**
     * 简易版
     *
     * @param chatMessages 问答参数
     * @return respose
     */
    @Override
    public ChatCompletionResponse chatCompletion(List<ChatMessage> chatMessages) {
        ChatCompletion chatCompletion = new ChatCompletion().chatMessages(chatMessages);
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 直接问
     *
     * @param message ask the message
     * @return gpt's answer
     */
    @Override
    public String chat(String message) {
        ChatCompletion chatCompletion = new ChatCompletion().chatMessages(Collections.singletonList(ChatMessage.of(message)));
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getChatMessage().getContent();
    }


    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }


    /**
     * 流式输出
     * 添加自定义header
     * it's a block method
     *
     * @param chatCompletion      question
     * @param headers             custom headers
     * @param eventSourceListener listener
     * @return answer
     */
    @Override
    public String streamChatCompletion(ChatCompletion chatCompletion,
                                       StreamListener eventSourceListener, HttpHeaders headers) {

        chatCompletion.stream(true);
        String key = getKey();
        return restTemplate.execute(apiHost + "/v1/chat/completions", HttpMethod.POST, request -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + key);
            request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            if (headers != null) {
                request.getHeaders().addAll(headers);
            }
            objectMapper.writeValue(request.getBody(), chatCompletion);
        }, eventSourceListener);
    }

    /**
     * 流式输出
     * 添加自定义header
     * it's a block method
     *
     * @param chatCompletion      question
     * @param eventSourceListener listener
     * @return answer
     */
    @Override
    public String streamChatCompletion(ChatCompletion chatCompletion,
                                       StreamListener eventSourceListener) {
        return streamChatCompletion(chatCompletion, eventSourceListener, null);
    }

    /**
     * 流式输出
     * 添加自定义header
     * it's a block method
     *
     * @param chatMessages        more questions
     * @param eventSourceListener listener
     * @return answer
     */
    @Override
    public String streamChatCompletion(List<ChatMessage> chatMessages,
                                       StreamListener eventSourceListener) {
        ChatCompletion chatCompletion = new ChatCompletion()
                .chatMessages(chatMessages)
                .stream(true);
        return streamChatCompletion(chatCompletion, eventSourceListener);
    }
}
