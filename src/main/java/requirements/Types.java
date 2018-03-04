package requirements;

public class Types {

    public static final String HTML = "text/html";
    public static final String CSS = "text/css";
    public static final String JS = "application/javascript";
    public static final String JPG = "image/jpg";
    public static final String JPEG = "image/jpeg";
    public static final String PNG = "image/png";
    public static final String GIF = "image/gif";
    public static final String SWF = "application/x-shockwave-flash";
    public static final String plain = "text/plain";
    public static final String octet_stream = "application/octet-stream";

    public static String getType(String ext) {
        switch (ext) {
            case "htm":
                return HTML;
            case "css":
                return CSS;
            case "js":
                return JS;
            case "jpg":
                return JPG;
            case "jpeg":
                return JPEG;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            case "html":
                return HTML;
            case "txt":
                return plain;
            default:
                return octet_stream;
        }
    }

}
