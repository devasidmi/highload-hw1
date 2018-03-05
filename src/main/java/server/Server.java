package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static final String indexFile = "index.html";
    private static final int bufferSize = 10 * 1024 * 1024;
    static final String serverName = "vasidmi server";
    static final String connection = "close";
    static final String encoding = "ASCII";
    static final String http_version = "HTTP/1.1";

    private String path;
    private int port;
    private int threadCount;


    public Server(String path, int port, int threadCount) {
        this.path = path;
        this.port = port;
        this.threadCount = threadCount;
    }

    public void start() throws IOException, InterruptedException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            final List<Thread> threads = createThreads(serverSocket);
            threads.forEach(Thread::start);
            for (int i = 0; i != threads.size(); ++i) {
                threads.get(i).join();
            }
        } catch (Exception ignored) {
        }
    }

    private List<Thread> createThreads(ServerSocket serverSocket) {
        final List<Thread> result = new ArrayList<>();
        for (int i = 0; i != threadCount; ++i) {
            result.add(new Thread(new Worker(serverSocket, path, bufferSize)));
        }
        return result;
    }

}
