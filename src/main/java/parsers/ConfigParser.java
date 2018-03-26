package parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigParser {

    private static String PORT = "80";
    private static String THREAD_LIMIT = "thread_limit";
    private static String DOCUMENT_ROOT = "document_root";

    public static Config parse(String path) {
        File mFile = new File(path);
        Properties mConfigProperties = new Properties();
        try {
            InputStream mInputStream = new FileInputStream(mFile);
            mConfigProperties.load(mInputStream);

//            String port = mConfigProperties.getProperty(PORT).split("#")[0].trim();
            String thread_limit = mConfigProperties.getProperty(THREAD_LIMIT).split("#")[0].trim();
            String document_root = mConfigProperties.getProperty(DOCUMENT_ROOT).split("#")[0].trim();

            Config mConfig = new Config(PORT, thread_limit, document_root);

            mInputStream.close();
            return mConfig;

        } catch (IOException e) {
            return null;
        }
    }

}

