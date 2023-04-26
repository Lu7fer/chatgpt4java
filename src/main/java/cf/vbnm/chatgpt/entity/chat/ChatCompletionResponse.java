package cf.vbnm.chatgpt.entity.chat;

import cf.vbnm.chatgpt.entity.billing.Usage;

import java.util.List;

/**
 * chat答案类
 *
 * @author plexpt
 */
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setChoices(List<ChatChoice> choices) {
        this.choices = choices;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public List<ChatChoice> getChoices() {
        return choices;
    }

    public Usage getUsage() {
        return usage;
    }
}
