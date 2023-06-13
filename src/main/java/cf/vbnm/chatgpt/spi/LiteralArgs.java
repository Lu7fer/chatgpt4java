package cf.vbnm.chatgpt.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LiteralArgs {

    String completionPath() default "https://api.openai.com/v1/chat/completions";

    String generateImagePath() default "https://api.openai.com/v1/images/generations";

    String[] headerName() default {};

    String[] headerValue() default {};

}
