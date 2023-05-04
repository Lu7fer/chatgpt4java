package cf.vbnm.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * chat
 *
 * @author plexpt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletion implements Serializable {

    @JsonProperty("model")
    private String model = Model.GPT_3_5_TURBO.getName();

    @JsonProperty("messages")
    private List<ChatMessage> chatMessages;
    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守。
     * <p>
     * 不要同时改这个和topP
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output
     * more random, while lower values like 0.2 will make it more focused and deterministic.
     * <p>
     * We generally recommend altering this or top_p but not both.
     */
    private double temperature = 0.9;

    /**
     * 0-1
     * 建议0.9
     * 不要同时改这个和temperature
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers
     * the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
     * <p>
     * We generally recommend altering this or temperature but not both.
     */
    @JsonProperty("top_p")
    private double topP = 0.9;


    /**
     * 结果数。
     */
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     */
    private boolean stream = false;
    /**
     * 停用词
     */
    private List<String> stop;
    /**
     * 3.5 最大支持4096
     * 4.0 最大32k
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether
     * they appear in the text so far, increasing the model's likelihood to talk about new topics.
     */
    @JsonProperty("presence_penalty")
    private double presencePenalty;

    /**
     * -2.0 ~~ 2.0
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing
     * frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
     */
    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    public ChatCompletion() {
    }

    public ChatCompletion(String model, List<ChatMessage> chatMessages,
                          double temperature, double topP, Integer n,
                          boolean stream, List<String> stop, Integer maxTokens,
                          double presencePenalty, double frequencyPenalty,
                          Map<String, Integer> logitBias, String user) {
        this.model = model;
        this.chatMessages = chatMessages;
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.stream = stream;
        this.stop = stop;
        this.maxTokens = maxTokens;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.logitBias = logitBias;
        this.user = user;
    }

    public String getModel() {
        return model;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTopP() {
        return topP;
    }

    public Integer getN() {
        return n;
    }

    public boolean isStream() {
        return stream;
    }

    public List<String> getStop() {
        return stop;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public double getPresencePenalty() {
        return presencePenalty;
    }

    public double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public Map<String, Integer> getLogitBias() {
        return logitBias;
    }

    public String getUser() {
        return user;
    }

    public ChatCompletion model(String model) {
        this.model = model;
        return this;
    }

    public ChatCompletion chatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        return this;
    }

    public ChatCompletion temperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ChatCompletion topP(double topP) {
        this.topP = topP;
        return this;
    }

    public ChatCompletion n(Integer n) {
        this.n = n;
        return this;
    }

    public ChatCompletion stream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public ChatCompletion stop(List<String> stop) {
        this.stop = stop;
        return this;
    }

    public ChatCompletion maxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }

    public ChatCompletion presencePenalty(double presencePenalty) {
        this.presencePenalty = presencePenalty;
        return this;
    }

    public ChatCompletion frequencyPenalty(double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }

    public ChatCompletion logitBias(Map<String, Integer> logitBias) {
        this.logitBias = logitBias;
        return this;
    }

    public ChatCompletion user(String user) {
        this.user = user;
        return this;
    }

    public enum Model {
        /**
         *
         */
        TEXT_ADA_001("text-ada-001"),
        /**
         *
         */
        TEXT_CURIE_001("text-curie-001"),
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        /**
         * 临时模型，不建议使用
         */
        GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
        /**
         * GPT4.0
         */
        GPT_4("gpt-4"),

        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        ;
        private final String name;

        Model(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}


