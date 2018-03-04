package parsers;

public class Config {

    private String port;
    private String thread_limit;
    private String document_root;

    public Config(String port, String thread_limit, String document_root) {
        this.port = port;
        this.thread_limit = thread_limit;
        this.document_root = document_root;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getThread_limit() {
        return thread_limit;
    }

    public void setThread_limit(String thread_limit) {
        this.thread_limit = thread_limit;
    }

    public String getDocument_root() {
        return document_root;
    }

    public void setDocument_root(String document_root) {
        this.document_root = document_root;
    }
}
