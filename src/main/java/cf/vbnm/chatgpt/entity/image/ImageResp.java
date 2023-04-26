package cf.vbnm.chatgpt.entity.image;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImageResp {
    @JsonProperty("created")
    private final int created;
    @JsonProperty("data")
    private final List<Data> data;

    public ImageResp(int created, List<Data> data) {
        this.created = created;
        this.data = data;
    }

    public int getCreated() {
        return created;
    }

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        @JsonProperty("url")
        private final String url;

        public Data(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
