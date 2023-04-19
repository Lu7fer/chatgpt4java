package cf.vbnm.chatgpt.client;

import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.Message;
import cf.vbnm.chatgpt.listener.StreamListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatGPT {
    ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion);

    ChatCompletionResponse chatCompletion(List<Message> messages);

    String chat(String message);

    String streamChatCompletion(ChatCompletion chatCompletion,
                                StreamListener eventSourceListener, HttpHeaders headers);

    String streamChatCompletion(ChatCompletion chatCompletion, StreamListener eventSourceListener);

    String streamChatCompletion(List<Message> messages, StreamListener eventSourceListener);
}
