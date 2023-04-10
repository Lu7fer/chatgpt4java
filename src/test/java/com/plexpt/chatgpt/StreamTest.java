package com.plexpt.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.StreamListener;
import com.plexpt.chatgpt.util.ProxyUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

}
