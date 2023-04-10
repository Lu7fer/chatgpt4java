package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * chat
 *
 * @author plexpt
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = Model.GPT_3_5_TURBO.getName();

    @NonNull
    private List<Message> messages;
    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守。
     * <p>
     * 不要同时改这个和topP
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output
     * more random, while lower values like 0.2 will make it more focused and deterministic.
     * <p>
     * We generally recommend altering this or top_p but not both.
     */
    @Builder.Default
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
    @Builder.Default
    private double topP = 0.9;


    /**
     * 结果数。
     */
    @Builder.Default
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     */
    @Builder.Default
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


    @Getter
    @AllArgsConstructor
    public enum Model {
        /**
         * */
        TEXT_ADA_001("text-ada-001"),
        /**
         * */
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
         * 临时模型，不建议使用
         */
        GPT_4_0314("gpt-4-0314"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_32K_0314("gpt-4-32k-0314"),
        ;
        private final String name;
    }

}


