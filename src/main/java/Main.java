import parsers.Config;
import parsers.ConfigParser;
import server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String path = "/etc/httpd.conf";
        Config configFile = ConfigParser.parse(path);
        if (configFile == null) {
            System.out.println("Can't find config file");
            return;
        }
        int port = Integer.parseInt(configFile.getPort());
        String root = configFile.getDocument_root();
        int threadCount = Integer.parseInt(configFile.getThread_limit());

        Server mServer = new Server(root, port, threadCount);
        System.out.println("Server is running...");
        mServer.start();
    }
}
