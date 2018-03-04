package parsers;

import java.io.*;
import java.util.Properties;


public class ConfigParser {

    private static String PORT = "port";
    private static String THREAD_LIMIT = "thread_limit";
    private static String DOCUMENT_ROOT = "document_root";

    public static Config parse(String path) {
        File mFile = new File(path);
        Properties mConfigProperties = new Properties();
        try {
            InputStream mInputStream = new FileInputStream(mFile);
            mConfigProperties.load(mInputStream);

            String port = mConfigProperties.getProperty(PORT).split(" #")[0].replace(" ","");
            String thread_limit = mConfigProperties.getProperty(THREAD_LIMIT).split(" #")[0].replace(" ","");
            String document_root = mConfigProperties.getProperty(DOCUMENT_ROOT).split(" #")[0].replace(" ","");

            Config mConfig = new Config(port, thread_limit, document_root);

            mInputStream.close();
            return mConfig;

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

}

