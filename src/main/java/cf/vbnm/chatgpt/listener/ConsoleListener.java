package cf.vbnm.chatgpt.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * sse
 *
 * @author plexpt
 */
@Slf4j
public class ConsoleListener extends StreamListener {

    public ConsoleListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void onComplete(String message) {
        System.err.println("onComplete");
        System.err.println(message);
    }


    @Override
    public void onMsg(String message) {
//        System.out.print(message);
    }

    @Override
    public void onError(String response) {
        System.out.println(response);
    }

}
