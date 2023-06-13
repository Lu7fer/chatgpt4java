package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.spi.GeneralSupport;
import cf.vbnm.chatgpt.spi.LiteralArgs;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.Proxy;

@Import(ChatGPTSupport.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableChatGPTClient {

    LiteralArgs[] value() default {};

    int connectTimeoutMillis() default 7 * 1000;

    int readTimeoutMillis() default 60 * 1000;

    Proxy.Type proxyType() default Proxy.Type.DIRECT;

    String proxyHost() default "127.0.0.1";

    int proxyPort() default 0;

    Class<? extends GeneralSupport> generalSupport() default GeneralSupport.NotSupport.class;

}
