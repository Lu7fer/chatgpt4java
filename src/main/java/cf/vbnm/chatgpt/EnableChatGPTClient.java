package cf.vbnm.chatgpt;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.Proxy;
import java.util.function.Supplier;

@Import(ChatGPTSupport.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableChatGPTClient {

    int connectTimeoutMillis() default 7 * 1000;

    int readTimeoutMillis() default 60 * 1000;

    String[] keys() default "";

    Class<? extends Supplier<String>> keySupplier() default NoSupply.class;

    String host() default "https://api.openai.com";

    Proxy.Type proxyType() default Proxy.Type.DIRECT;

    String proxyHost() default "127.0.0.1";

    int proxyPort() default 0;

    static class NoSupply implements Supplier<String> {

        @Override
        public String get() {
            return null;
        }
    }
}
