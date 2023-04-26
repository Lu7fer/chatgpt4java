package cf.vbnm.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author plexpt
 * @author Yttrium
 */
public class Grants {
    private String object;
    @JsonProperty("data")
    private List<Datum> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
