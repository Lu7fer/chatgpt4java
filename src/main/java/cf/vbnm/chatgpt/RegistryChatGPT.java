package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.RestTemplateGPT;
import cf.vbnm.chatgpt.util.ProxyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;


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
        Class<? extends Supplier<String>> keySupplier = annotation.keySupplier();
        Proxy.Type proxyType = annotation.proxyType();
        String proxyHost = annotation.proxyHost();
        int proxyPort = annotation.proxyPort();
        List<String> keys = Arrays.asList(annotation.keys());
        String host = annotation.host();
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        String apiHost = host;
        if (!proxyType.equals(Proxy.Type.DIRECT)) {
            switch (proxyType) {
                case HTTP:
                    requestFactory.setProxy(ProxyUtil.http(proxyHost, proxyPort));
                    break;
                case SOCKS:
                    requestFactory.setProxy(ProxyUtil.socks5(proxyHost, proxyPort));
            }
        }
        if (keySupplier.equals(EnableChatGPTClient.NoSupply.class)) {
            if (keys.size() > 1) {
                RestTemplateGPT chatGPT = new RestTemplateGPT(keys, restTemplate, objectMapper);
                chatGPT.setApiHost(apiHost);
                beanFactory.registerSingleton("chatGPT", chatGPT);
                return;
            } else if (keys.size() == 1) {
                RestTemplateGPT chatGPT = new RestTemplateGPT(keys.get(0), restTemplate, objectMapper);
                chatGPT.setApiHost(apiHost);
                beanFactory.registerSingleton("chatGPT", chatGPT);
                return;
            }
            throw new RuntimeException("Must have at least 1 key");
        } else {
            RestTemplateGPT chatGPT = new RestTemplateGPT(keys, restTemplate, objectMapper);
            chatGPT.setApiHost(apiHost);
            try {
                Constructor<? extends Supplier<String>> constructor = keySupplier.getConstructor();
                Supplier<String> supplier = constructor.newInstance();
                chatGPT.setKeySupplier(supplier);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException("Only public no args constructor supported", e);
            } catch (InstantiationException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            beanFactory.registerSingleton("chatGPT", chatGPT);
        }

    }

}
