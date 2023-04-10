package com.plexpt.chatgpt.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * EventSource listener for chat-related events.
 *
 * @author plexpt
 */
@Slf4j
public abstract class StreamListener implements ResponseExtractor<String> {
    private final ObjectMapper objectMapper;

    public StreamListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected String lastMessage = "";


    /**
     * Called when all new message are received.
     *
     * @param message the new message
     */
    protected abstract void onComplete(String message);

    /**
     * Called when a new message is received.
     * 收到消息 单个字
     *
     * @param message the new message
     */
    public void onMsg(String message) {
    }

    /**
     * Called when an error occurs.
     * 出错时调用
     *
     * @param response the response associated with the error, if any
     */
    public void onError(String response) {

    }


    public void onEvent(String data) {
        if (data.equals("[DONE]")) {
            onComplete(lastMessage);
            return;
        }
        ChatCompletionResponse response;
        try {
            response = objectMapper.readValue(data, ChatCompletionResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 读取Json
        List<ChatChoice> choices = response.getChoices();
        if (choices == null || choices.isEmpty()) {
            return;
        }
        Message delta = choices.get(0).getDelta();
        String text = delta.getContent();

        if (text != null) {
            lastMessage += text;
            onMsg(text);
        }

    }

    @Override
    public final String extractData(ClientHttpResponse response) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            onFailure(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        }
        InputStream stream = response.getBody();
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = reader.read()) != -1) {
            builder.append((char) read);
            if (!reader.ready()) {
                String s = builder.toString();
                onEvent(s);
                result.append(s);
                builder.setLength(0);
            }
        }
        String resultString = result.toString();
        onComplete(resultString);
        return resultString;
    }

    @SneakyThrows
    public void onFailure(String response) {

        if (response == null) {
            return;
        }

        log.error("response：{}", response);

        String forbiddenText = "Your access was terminated due to violation of our policies";

        if (response.contains(forbiddenText)) {
            log.error("Chat session has been terminated due to policy violation");
            log.error("检测到号被封了");
        }

        String overloadedText = "That model is currently overloaded with other requests.";

        if (response.contains(overloadedText)) {
            log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
        }

        this.onError(response);

    }
}
