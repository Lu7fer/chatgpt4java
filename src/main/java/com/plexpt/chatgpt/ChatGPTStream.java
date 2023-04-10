package com.plexpt.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.StreamListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.util.List;
import java.util.Random;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTStream {

    private String apiKey;
    private List<String> apiKeyList;
    private Random random = new Random();
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    /**
     * 连接超时
     */
    @Builder.Default
    private long timeout = 90;

    /**
     * 网络代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;
    /**
     * 反向代理
     */
    @Builder.Default
    private String apiHost = DEFAULT_API_HOST;
    public static final String DEFAULT_API_HOST = "https://api.openai.com/";

    /**
     * 初始化
     */
    public ChatGPTStream init() {

        return this;
    }


    /**
     * 流式输出
     */
    public void streamChatCompletion(ChatCompletion chatCompletion,
                                     StreamListener streamListener) {

        chatCompletion.setStream(true);
        String key;
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = apiKeyList.get(random.nextInt(0, apiKeyList.size()));
        } else {
            key = apiKey;
        }
        try {


            restTemplate.execute(apiHost + "/v1/chat/completions", HttpMethod.POST, request -> {
                request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + key);
                request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE);
                objectMapper.writeValue(request.getBody(), chatCompletion);
            }, streamListener);

        } catch (Exception e) {
            log.error("请求出错：{}", (Object) e);
        }
    }

    /**
     * 流式输出
     */
    public void streamChatCompletion(List<Message> messages,
                                     StreamListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        streamChatCompletion(chatCompletion, eventSourceListener);
    }


}
