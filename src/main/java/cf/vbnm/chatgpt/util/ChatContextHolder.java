package cf.vbnm.chatgpt.util;

import cf.vbnm.chatgpt.entity.chat.Message;

import java.util.*;

/**
 * 对话上下文
 */
public class ChatContextHolder {

    private static final Map<String, List<Message>> context = new HashMap<>();


    /**
     * 获取对话历史
     *
     * @param id 对话id
     * @return 消息集合
     */
    public static List<Message> get(String id) {

        return context.computeIfAbsent(id, k -> new ArrayList<>());
    }


    /**
     * 添加对话
     *
     * @param id  对话id
     * @param msg 消息内容
     */
    public static void add(String id, String msg) {

        Message message = Message.builder().content(msg).build();
        add(id, message);
    }


    /**
     * 添加对话
     *
     * @param id      对话id
     * @param message 对话内容
     */
    public static void add(String id, Message message) {
        List<Message> messages = context.computeIfAbsent(id, k -> new ArrayList<>());
        messages.add(message);
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
