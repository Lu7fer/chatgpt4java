package cf.vbnm.chatgpt;

import cf.vbnm.chatgpt.client.ChatGPTClient;
import cf.vbnm.chatgpt.client.RestTemplateGPTClient;
import cf.vbnm.chatgpt.spi.GeneralSupport;
import cf.vbnm.chatgpt.spi.LiteralArgs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
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
        // set proxy for RequestFactory
        if (annotation.proxyType() != Proxy.Type.DIRECT) {
            InetSocketAddress socketAddress = InetSocketAddress.createUnresolved(annotation.proxyHost(), annotation.proxyPort());
            requestFactory.setProxy(new Proxy(annotation.proxyType(), socketAddress));
        }
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ObjectMapper objectMapper = beanFactory.getBean(ObjectMapper.class);

        if (annotation.generalSupport() != GeneralSupport.NotSupport.class) {
            Class<? extends GeneralSupport> generalSupport = annotation.generalSupport();
            try {
                Constructor<? extends GeneralSupport> constructor = generalSupport.getConstructor();
                ChatGPTClient chatGPT = new RestTemplateGPTClient(constructor.newInstance(), restTemplate, objectMapper);
                beanFactory.registerSingleton("chatGPTClient", chatGPT);
            } catch (Throwable e) {
                throw new RuntimeException("Instance class failed, is this class have a public no argument constructor.", e);
            }
        } else {
            LiteralArgs[] args = annotation.value();
            if (args.length == 0) {
                throw new IllegalArgumentException("@EnableChatGPTClient is not configured properly.");
            }
            List<String> chatPath = new ArrayList<>(), imagePath = new ArrayList<>();
            List<HttpHeaders> headers = new ArrayList<>();
            for (LiteralArgs arg : args) {
                chatPath.add(arg.completionPath());
                imagePath.add(arg.generateImagePath());
                HttpHeaders httpHeaders = new HttpHeaders();
                headers.add(httpHeaders);
                if (arg.headerName().length != arg.headerValue().length) {
                    throw new IllegalArgumentException("Header names and header values not match.");
                }
                for (int i = 0; i < arg.headerName().length; i++) {
                    httpHeaders.add(arg.headerName()[i], arg.headerValue()[i]);
                }
            }
            ChatGPTClient chatGPT = new RestTemplateGPTClient(chatPath, imagePath, headers, restTemplate, objectMapper);
            beanFactory.registerSingleton("chatGPTClient", chatGPT);
        }

    }

}
