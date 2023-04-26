package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.RestTemplateGPT;
import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.listener.ConsoleListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

/**
 * 测试类
 *
 * @author plexpt
 */
public class StreamTest {
    @EnableChatGPTClient(keys = {""}, host = "")
    public static class InnerConfig {
        @Bean
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }

    }

    private RestTemplateGPT chatGPTStream;


    @Before
    public void before() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ChatGPTSupport.class, InnerConfig.class);
        chatGPTStream = annotationConfigApplicationContext.getBean(RestTemplateGPT.class);
    }


    @Test
    public void chatCompletions() throws JsonProcessingException {
        ChatMessage chatMessage = ChatMessage.of("写首诗，题目是好热");
        ChatCompletion chatCompletion =new ChatCompletion()
                .chatMessages(Collections.singletonList(chatMessage));
        System.out.println(new ObjectMapper().writeValueAsString(chatCompletion));
        chatGPTStream.streamChatCompletion(chatCompletion, new ConsoleListener(new ObjectMapper()));

        //卡住测试
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
