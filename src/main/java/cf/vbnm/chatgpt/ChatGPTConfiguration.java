package cf.vbnm.chatgpt;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegistryChatGPT.class)
public class ChatGPTConfiguration {
//    @Bean
//    public ChatGPT chatGPTFactoryBean(RegistryChatGPT.ChatGPTFactoryBean factoryBean) {
//        return factoryBean.getObject();
//    }
}
