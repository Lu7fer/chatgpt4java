package com.plexpt.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPT {


    /**
     * keys
     */
    private String apiKey;

    private List<String> apiKeyList;
    private Random random = new Random();
    /**
     * 自定义api host使用builder的方式构造client
     */
    @Builder.Default
    private String apiHost = DEFAULT_API_HOST;
    public static final String DEFAULT_API_HOST = "https://api.openai.com/";
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    /**
     * 超时 默认300
     */
    @Builder.Default
    private long timeout = 300;
    /**
     * okhttp 代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;

    private String getKey() {
        String key;
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = apiKeyList.get(random.nextInt(0, apiKeyList.size()));
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
                .messages(List.of(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }


    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }
}
