package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.RestTemplateGPT;
import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Slf4j
public class TestChatgpt {


    public static void main(String[] args) {

        System.out.println("test");
    }

    private RestTemplateGPT chatGPT;

    @Before
    public void before() {
//        java.net.Proxy proxy = ProxyUtil.http("127.0.0.1", 1080);

        chatGPT = new RestTemplateGPT("", new RestTemplate(), new ObjectMapper());

    }

    @org.junit.Test
    public void chat() {
        ChatMessage system = ChatMessage.ofSystem("你现在是一个诗人，专门写七言绝句");
        ChatMessage chatMessage = ChatMessage.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion =new ChatCompletion()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .chatMessages(Arrays.asList(system, chatMessage))
                .maxTokens(3000)
                .temperature(0.9);
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatMessage res = response.getChoices().get(0).getChatMessage();
        System.out.println(res);
    }

    @org.junit.Test
    public void chatMsg() {
        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);
    }

}
