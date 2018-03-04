package requirements;

public class Statuses {
    public static final int CODE_OK = 200;
    private static final String OK = "OK";

    public static final int CODE_FORBIDDEN = 403;
    private static final String FORBIDDEN = "FORBIDDEN";


    public static final int CODE_NOT_FOUND = 404;
    private static final String NOT_FOUND = "Not Found";

    public static final int CODE_METHOD_NOT_ALLOWED = 405;
    private static final String METHOD_NOT_ALLOWED = "Method not Allowed";

    public static String getStatus(int code) {
        switch (code) {
            case CODE_OK:
                return OK;
            case CODE_FORBIDDEN:
                return FORBIDDEN;
            case CODE_NOT_FOUND:
                return NOT_FOUND;
            case CODE_METHOD_NOT_ALLOWED:
                return METHOD_NOT_ALLOWED;
            default:
                return OK;
        }
    }
}
