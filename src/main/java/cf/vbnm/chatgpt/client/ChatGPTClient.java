package cf.vbnm.chatgpt.client;

import cf.vbnm.chatgpt.entity.chat.ChatCompletion;
import cf.vbnm.chatgpt.entity.chat.ChatCompletionResponse;
import cf.vbnm.chatgpt.entity.chat.ChatMessage;
import cf.vbnm.chatgpt.entity.image.ImageResp;
import cf.vbnm.chatgpt.entity.image.ImageSize;
import cf.vbnm.chatgpt.listener.StreamListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatGPTClient {
    /**
     * 直接生成回答, 阻塞式的, 超时时间要设置长一点
     *
     * @param chatCompletion 接口参数
     */
    ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion);

    /**
     * 直接生成回答, 阻塞式的, 超时时间要设置长一点
     *
     * @param chatMessages 消息表
     */
    ChatCompletionResponse chatCompletion(List<ChatMessage> chatMessages);

    /**
     * 一句话问答
     */
    String chat(String message);

    /**
     * 流式的对话接口
     *
     * @param chatCompletion      参数
     * @param eventSourceListener 流式消息监听
     * @param headers             自定义header
     */
    String streamChatCompletion(ChatCompletion chatCompletion,
                                StreamListener eventSourceListener, HttpHeaders headers);

    /**
     * 流式的对话接口
     *
     * @param chatCompletion      参数
     * @param eventSourceListener 流式消息监听
     */
    String streamChatCompletion(ChatCompletion chatCompletion, StreamListener eventSourceListener);

    /**
     * 流式的对话接口
     *
     * @param chatMessages        消息列表
     * @param eventSourceListener 流式消息监听
     */
    String streamChatCompletion(List<ChatMessage> chatMessages, StreamListener eventSourceListener);

    /**
     * 图像生成接口
     *
     * @param prompt prompt
     */
    ImageResp generationImage(String prompt);

    /**
     * 图像生成接口
     *
     * @param prompt         prompt
     * @param n              几张图片
     * @param size           图像大小
     * @param user           用户名, 相当于一个echo字段
     * @param responseFormat 返回数据类型, url 或 b64_json, b64_json 还没有适配
     */
    ImageResp generationImages(String prompt, int n, ImageSize size, String responseFormat, String user);
}
