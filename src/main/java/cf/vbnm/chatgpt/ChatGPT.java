package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.Message;
import cf.vbnm.chatgpt.listener.StreamListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j
public class ChatGPT {
    private final String apiKey;
    /**
     * keys
     */
    private final List<String> apiKeyList;
    private final Random random = new Random();
    /**
     * 自定义api host使用builder的方式构造client
     */
    private String apiHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public ChatGPT(String apiKey, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        apiKeyList = null;
    }

    public ChatGPT(List<String> apiKeyList, RestTemplate restTemplate, ObjectMapper objectMapper) {
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
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = apiKeyList.get(random.nextInt(apiKeyList.size()));
        } else {
            key = apiKey;
        }
        return key;
    }


    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion 问答参数
     * @return 答案
     */
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        String key = getKey();
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + key);
        HttpEntity<ChatCompletion> requestEntity = new HttpEntity<>(chatCompletion, headers);
        return restTemplate.exchange(apiHost + "/v1/chat/completions", HttpMethod.POST, requestEntity,
                ChatCompletionResponse.class).getBody();
    }

    /**
     * 简易版
     *
     * @param messages 问答参数
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 直接问
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Collections.singletonList(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }


    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }


    /**
     * 流式输出
     * 添加自定义header
     */
    public String streamChatCompletion(ChatCompletion chatCompletion,
                                       StreamListener eventSourceListener, HttpHeaders headers) {

        chatCompletion.setStream(true);
        String key;
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = apiKeyList.get(random.nextInt(apiKeyList.size()));
        } else {
            key = apiKey;
        }
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
     */
    public String streamChatCompletion(ChatCompletion chatCompletion,
                                       StreamListener eventSourceListener) {
        return streamChatCompletion(chatCompletion, eventSourceListener, null);
    }

    /**
     * 流式输出
     */
    public String streamChatCompletion(List<Message> messages,
                                       StreamListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        return streamChatCompletion(chatCompletion, eventSourceListener);
    }
}
