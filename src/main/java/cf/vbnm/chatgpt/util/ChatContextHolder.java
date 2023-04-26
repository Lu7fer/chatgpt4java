package cf.vbnm.chatgpt.util;

import cf.vbnm.chatgpt.entity.chat.ChatMessage;

import java.util.*;

/**
 * 对话上下文
 */
public class ChatContextHolder {

    private static final Map<String, List<ChatMessage>> context = new HashMap<>();


    /**
     * 获取对话历史
     *
     * @param id 对话id
     * @return 消息集合
     */
    public static List<ChatMessage> get(String id) {

        return context.computeIfAbsent(id, k -> new ArrayList<>());
    }


    /**
     * 添加对话
     *
     * @param id  对话id
     * @param msg 消息内容
     */
    public static void add(String id, String msg) {

        ChatMessage chatMessage = ChatMessage.of(msg);
        add(id, chatMessage);
    }


    /**
     * 添加对话
     *
     * @param id          对话id
     * @param chatMessage 对话内容
     */
    public static void add(String id, ChatMessage chatMessage) {
        List<ChatMessage> chatMessages = context.computeIfAbsent(id, k -> new ArrayList<>());
        chatMessages.add(chatMessage);
    }

    /**
     * 清除对话
     *
     * @param id 对话id
     */
    public static void remove(String id) {
        context.remove(id);
    }
}
