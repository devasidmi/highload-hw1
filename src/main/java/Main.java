import parsers.Config;
import parsers.ConfigParser;
import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String path = "/Users/vasidmi/Documents/http-server/src/main/resources/kek.conf";
        Config configFile = ConfigParser.parse(path);
        int port = Integer.parseInt(configFile.getPort());
        String root = configFile.getDocument_root();
        int threadCount = Integer.parseInt(configFile.getThread_limit());

        Server mServer = new Server(root, port, threadCount);
        System.out.println("Server is running...");
        mServer.start();
    }
}
