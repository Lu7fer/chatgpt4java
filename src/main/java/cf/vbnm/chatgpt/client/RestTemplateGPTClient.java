package cf.vbnm.chatgpt.client;

import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.entity.image.ImageReq;
import cf.vbnm.chatgpt.entity.image.ImageResp;
import cf.vbnm.chatgpt.entity.image.ImageSize;
import cf.vbnm.chatgpt.listener.StreamListener;
import cf.vbnm.chatgpt.spi.GeneralSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * open ai 客户端
 *
 * @author plexpt
 */
public class RestTemplateGPTClient implements ChatGPTClient {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateGPTClient.class);

    private List<String> completionPath;
    private List<String> imagePath;
    private List<HttpHeaders> headers;
    private GeneralSupport generalSupport;
    private final Random random = new Random();
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestTemplateGPTClient(List<String> completionPath, List<String> imagePath, List<HttpHeaders> headers,
                                 RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.completionPath = completionPath;
        this.imagePath = imagePath;
        this.headers = headers;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        log.info("Created a RestTemplateGPTClient.");
    }

    public RestTemplateGPTClient(GeneralSupport generalSupport, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.generalSupport = generalSupport;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        log.info("Created a RestTemplateGPTClient.");
    }

    public static class Request {
        public String completionPath;
        public String imagePath;
        public HttpHeaders headers;
    }

    public Request getRequestInfo() {
        Request request = new Request();
        HttpHeaders headers = new HttpHeaders();
        request.headers = headers;
        if (generalSupport == null) {
            int i = 0;
            if (headers.size() != 1) {
                i = random.nextInt(this.headers.size());
            }
            request.completionPath = completionPath.get(i);
            request.imagePath = imagePath.get(i);
            headers.addAll(this.headers.get(i));
            return request;
        }
        if (generalSupport.isGetAllArgsAtOnce()) {
            GeneralSupport.Args args = generalSupport.getAllArgsAtOnce();
            request.completionPath = args.getCompletionPath();
            request.imagePath = args.getGenerateImagePath();
            headers.addAll(args.getHeaders());
            return request;
        }
        request.completionPath = generalSupport.completionPath();
        request.imagePath = generalSupport.generateImagePath();
        headers.addAll(generalSupport.headers());
        return request;
    }


    public ImageResp generationImages(String prompt, int n, ImageSize size, String responseFormat, String user) {
        if (!("url".equals(responseFormat) || "b64_json".equals(responseFormat))) {
            throw new IllegalArgumentException("response_format only support 'url' or 'b64_json'");
        }
        Request request = getRequestInfo();
        HttpEntity<ImageReq> entity = new HttpEntity<>(new ImageReq(prompt, n, size, responseFormat, user), request.headers);
        return restTemplate.exchange(request.imagePath, HttpMethod.POST, entity, ImageResp.class).getBody();
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
        Request request = getRequestInfo();
        HttpEntity<ChatCompletion> requestEntity = new HttpEntity<>(chatCompletion, request.headers);
        return restTemplate.exchange(request.completionPath, HttpMethod.POST, requestEntity, ChatCompletionResponse.class).getBody();
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
    public String streamChatCompletion(ChatCompletion chatCompletion, StreamListener eventSourceListener, HttpHeaders headers) {

        chatCompletion.stream(true);
        Request req = getRequestInfo();
        return restTemplate.execute(req.completionPath, HttpMethod.POST, request -> {
            request.getHeaders().addAll(req.headers);
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
    public String streamChatCompletion(ChatCompletion chatCompletion, StreamListener eventSourceListener) {
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
    public String streamChatCompletion(List<ChatMessage> chatMessages, StreamListener eventSourceListener) {
        ChatCompletion chatCompletion = new ChatCompletion().chatMessages(chatMessages).stream(true);
        return streamChatCompletion(chatCompletion, eventSourceListener);
    }
}
