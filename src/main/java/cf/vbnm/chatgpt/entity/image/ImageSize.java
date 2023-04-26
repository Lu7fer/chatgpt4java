package cf.vbnm.chatgpt.entity.image;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = ImageSize.Serializer.class)
public enum ImageSize {
    SIZE_256("256x256", "small"),
    SIZE_512("512x512", "middle"),
    SIZE_1024("1024x1024", "large");
    private final String size;
    private final String sizeName;

    ImageSize(String size, String sizeName) {
        this.size = size;
        this.sizeName = sizeName;
    }

    public String getSize() {
        return size;
    }

    public String getSizeName() {
        return sizeName;
    }

    public static class Serializer extends JsonSerializer<ImageSize> {
        public void serialize(ImageSize value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.size);
        }
    }

    public static ImageSize parseSize(String size) {
        if (size == null) {
            return null;
        }
        ImageSize[] values = ImageSize.values();
        for (ImageSize imageSize : values) {
            if (imageSize.sizeName.equals(size)) {
                return imageSize;
            }
        }
        return null;
    }

    public static ImageSize parseSizeOrDefault(String size, ImageSize imageSize) {
        ImageSize parsed = parseSize(size);
        return parsed == null ? imageSize : parsed;
    }
}
