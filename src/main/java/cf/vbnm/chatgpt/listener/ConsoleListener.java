package cf.vbnm.chatgpt.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sse
 *
 * @author plexpt
 */
public class ConsoleListener extends StreamListener {
    private final Logger log = LoggerFactory.getLogger(ConsoleListener.class);

    public ConsoleListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void onComplete(String message) {
    }


    @Override
    public void onMsg(String message) {
        System.out.print(message);
    }

    @Override
    public void onError(String response) {
        System.out.println(response);
    }

}
