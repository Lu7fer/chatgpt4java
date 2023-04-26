package cf.vbnm.chatgpt.entity.chat;

/**
 * @author plexpt
 */

public class ChatMessage {
    /**
     * 目前支持三种角色参考官网，进行情景输入：<a href="https://platform.openai.com/docs/guides/chat/introduction">Chat completions</a>
     */
    private String role;
    private String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public static ChatMessage of(String content) {

        return new ChatMessage(ChatMessage.Role.USER.getValue(), content);
    }

    public static ChatMessage ofSystem(String content) {

        return new ChatMessage(Role.SYSTEM.getValue(), content);
    }

    public static ChatMessage ofAssistant(String content) {

        return new ChatMessage(Role.ASSISTANT.getValue(), content);
    }

    public enum Role {

        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;
        private final String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
