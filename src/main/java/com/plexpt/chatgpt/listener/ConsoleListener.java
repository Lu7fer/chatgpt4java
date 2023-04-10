package com.plexpt.chatgpt.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * sse
 *
 * @author plexpt
 */
@Slf4j
@Component
public abstract class ConsoleListener extends StreamListener {

    public ConsoleListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }


    @Override
    public void onMsg(String message) {
        System.out.println(message);
    }

    @Override
    public void onError(String response) {
        System.out.println(response);
    }

}
