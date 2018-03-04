package requirements;

public class Request {

    private int code;
    private String method;
    private String path;

    public Request(int code, String method, String path) {
        this.code = code;
        this.method = method;
        this.path = path;
    }

    public Request(int code) {
        this.code = code;
    }

    public Request() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
