package requirements;

public class Types {

    private static final String HTML = "text/html";
    private static final String CSS = "text/css";
    private static final String JS = "application/javascript";
    private static final String JPG = "image/jpg";
    private static final String JPEG = "image/jpeg";
    private static final String PNG = "image/png";
    private static final String GIF = "image/gif";
    private static final String SWF = "application/x-shockwave-flash";
    private static final String plain = "text/plain";
    private static final String octet_stream = "application/octet-stream";

    public static String getType(String ext) {
        switch (ext) {
            case "htm":
                return HTML;
            case "css":
                return CSS;
            case "js":
                return JS;
            case "jpg":
                return JPEG;
            case "jpeg":
                return JPEG;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            case "html":
                return HTML;
            case "swf":
                return SWF;
            case "txt":
                return plain;
            default:
                return octet_stream;
        }
    }

}
