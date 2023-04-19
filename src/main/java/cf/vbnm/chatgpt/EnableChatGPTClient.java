package cf.vbnm.chatgpt;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(ChatGPTSupport.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableChatGPTClient {

    int connectTimeoutMillis() default 7 * 1000;

    int readTimeoutMillis() default 60 * 1000;

    String[] keys();

    String host() default "https://api.openai.com";

}
