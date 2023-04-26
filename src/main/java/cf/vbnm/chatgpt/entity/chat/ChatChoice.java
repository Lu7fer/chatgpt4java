package cf.vbnm.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author plexpt
 */
public class ChatChoice {
    private long index;
    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    private ChatMessage delta;
    /**
     * 请求参数stream为false返回是message
     */
    @JsonProperty("message")
    private ChatMessage chatMessage;
    @JsonProperty("finish_reason")
    private String finishReason;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public ChatMessage getDelta() {
        return delta;
    }

    public void setDelta(ChatMessage delta) {
        this.delta = delta;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
