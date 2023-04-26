package cf.vbnm.chatgpt.entity.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageReq {
    @JsonProperty("prompt")
    public String prompt;

    @JsonProperty("n")
    public int n = 1;

    @JsonProperty("size")
    public ImageSize size;

    @JsonProperty("response_format")
    public String responseFormat;

    @JsonProperty("user")
    public String user;

    public ImageReq() {
    }

    public ImageReq(String prompt, int n, ImageSize size, String responseFormat, String user) {
        this.prompt = prompt;
        this.n = n;
        this.size = size;
        this.responseFormat = responseFormat;
        this.user = user;
    }
}

