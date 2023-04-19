package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.RestTemplateChatGPT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class RegistryChatGPT implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> withAnnotation = beanFactory.getBeansWithAnnotation(EnableChatGPTClient.class);
        Optional<Object> first = withAnnotation.values().stream().findFirst();
        if (!first.isPresent()) {
            return;
        }
        Object o = first.get();
        EnableChatGPTClient annotation = o.getClass().getAnnotation(EnableChatGPTClient.class);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(annotation.connectTimeoutMillis());
        requestFactory.setReadTimeout(annotation.readTimeoutMillis());
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ObjectMapper objectMapper = beanFactory.getBean(ObjectMapper.class);
        List<String> keys = Arrays.asList(annotation.keys());
        String host = annotation.host();
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        String apiHost = host;
        try {
            requestFactory.setProxy(beanFactory.getBean(Proxy.class));
        } catch (BeansException ignored) {
        }

        if (keys.size() > 1) {
            RestTemplateChatGPT chatGPT = new RestTemplateChatGPT(keys, restTemplate, objectMapper);
            chatGPT.setApiHost(apiHost);
            beanFactory.registerSingleton("chatGPT", chatGPT);
            return;
        } else if (keys.size() == 1) {
            RestTemplateChatGPT chatGPT = new RestTemplateChatGPT(keys.get(0), restTemplate, objectMapper);
            chatGPT.setApiHost(apiHost);
            beanFactory.registerSingleton("chatGPT", chatGPT);
            return;
        }
        throw new RuntimeException("Must have at least 1 key");
    }

}
