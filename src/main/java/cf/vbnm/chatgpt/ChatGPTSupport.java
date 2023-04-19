package cf.vbnm.chatgpt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegistryChatGPT.class)
@ComponentScan("cf.vbnm.chatgpt.client")
public class ChatGPTSupport {
}
