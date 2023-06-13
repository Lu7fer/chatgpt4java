package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.RestTemplateGPTClient;
import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.listener.ConsoleListener;
import cf.vbnm.chatgpt.spi.GeneralSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

/**
 * 测试类
 *
 * @author plexpt
 */
public class StreamTest {
    @EnableChatGPTClient(generalSupport = Support.class)
    public static class InnerConfig {
        @Bean
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }

    }

    public static class Support implements GeneralSupport {

        @Override
        public String completionPath() {
            return "https://api.openai.com/v1/chat/completions";
        }

        @Override
        public String generateImagePath() {
            return "https://api.openai.com/v1/images/generations";
        }

        @Override
        public HttpHeaders headers() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("");
            return headers;
        }
    }

    private RestTemplateGPTClient chatGPTStream;


    @Before
    public void before() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ChatGPTSupport.class, InnerConfig.class);
        chatGPTStream = annotationConfigApplicationContext.getBean(RestTemplateGPTClient.class);
    }


    @Test
    public void chatCompletions() throws JsonProcessingException {
        ChatMessage chatMessage = ChatMessage.of("写首诗，题目是好热");
        ChatCompletion chatCompletion = new ChatCompletion()
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
