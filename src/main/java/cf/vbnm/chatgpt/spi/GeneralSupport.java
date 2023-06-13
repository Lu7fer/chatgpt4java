package cf.vbnm.chatgpt.spi;

import org.springframework.http.HttpHeaders;

public interface GeneralSupport {

    String completionPath();

    String generateImagePath();

    default HttpHeaders headers() {
        return new HttpHeaders();
    }

    default boolean isGetAllArgsAtOnce() {
        return false;
    }

    default Args getAllArgsAtOnce() {
        return null;
    }


    class Args {
        private String completionPath;

        private String generateImagePath;

        private HttpHeaders headers;


        public String getCompletionPath() {
            return completionPath;
        }

        public void setCompletionPath(String completionPath) {
            this.completionPath = completionPath;
        }

        public String getGenerateImagePath() {
            return generateImagePath;
        }

        public void setGenerateImagePath(String generateImagePath) {
            this.generateImagePath = generateImagePath;
        }

        public HttpHeaders getHeaders() {
            return headers;
        }

        public void setHeaders(HttpHeaders headers) {
            this.headers = headers;
        }
    }

    class NotSupport implements GeneralSupport {
        @Override
        public String completionPath() {
            return null;
        }

        @Override
        public String generateImagePath() {
            return null;
        }

    }
}
