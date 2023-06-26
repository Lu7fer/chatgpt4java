package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.ChatGPTClient;
import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.listener.ConsoleListener;
import cf.vbnm.chatgpt.spi.GeneralSupport;
import cf.vbnm.chatgpt.spi.LiteralArgs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * 测试类
 *
 * @author plexpt
 */
public class StreamTest {
    @EnableChatGPTClient(@LiteralArgs(completionPath = "https://.openai.azure.com/openai/deployments//chat/completions?api-version=2023-03-15-preview",
            headerName = {"api-key", "Content-Type"}, headerValue = {"", MediaType.APPLICATION_JSON_VALUE}))
    public static class InnerConfig {
        @Bean
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }

    }

    public static class Support implements GeneralSupport {

        @Override
        public String completionPath() {
            return "https://.openai.azure.com/openai/deployments//chat/completions?api-version=2023-03-15-preview";
        }

        @Override
        public String generateImagePath() {
            return "https://api.openai.com/v1/images/generations";
        }

        @Override
        public HttpHeaders headers() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", "");
            headers.setContentType(MediaType.APPLICATION_JSON);
            return headers;
        }
    }

    private ChatGPTClient chatGPTStream;


    @Before
    public void before() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(InnerConfig.class);
        chatGPTStream = annotationConfigApplicationContext.getBean(ChatGPTClient.class);
    }


    @Test
    public void chatCompletions() throws JsonProcessingException {
        ChatMessage system = ChatMessage.ofSystem("你现在是一个诗人，专门写七言绝句");
        ChatMessage chatMessage = ChatMessage.of("写首诗，题目是好热");
        ChatCompletion chatCompletion = new ChatCompletion()
                .chatMessages(Arrays.asList(system, chatMessage));
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
