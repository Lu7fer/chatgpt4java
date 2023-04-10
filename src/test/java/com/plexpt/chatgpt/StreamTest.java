package com.plexpt.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.StreamListener;
import com.plexpt.chatgpt.listener.ConsoleListener;
import com.plexpt.chatgpt.util.ProxyUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * 测试类
 *
 * @author plexpt
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StreamTest {

    private ChatGPTStream chatGPTStream;

    @Before
    public void before() {
        java.net.Proxy proxy = ProxyUtil.http("127.0.0.1", 1080);

        chatGPTStream = ChatGPTStream.builder()
                .apiKey("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")
                .proxy(proxy)
                .timeout(600)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

    }


    @Test
    public void chatCompletions() {
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Collections.singletonList(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, new StreamListener(new ObjectMapper()) {
            @Override
            protected void onComplete(String message) {

            }
        });

        //卡住测试
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String prompt) {

        SseEmitter sseEmitter = new SseEmitter(-1L);

        ConsoleListener listener = new ConsoleListener(new ObjectMapper(), sseEmitter) {

            @Override
            protected void onComplete(String message) {
                System.out.println(message);
            }
        };
        Message message = Message.of(prompt);
        chatGPTStream.streamChatCompletion(Collections.singletonList(message), listener);


        return sseEmitter;
    }

}
