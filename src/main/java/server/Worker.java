package server;

import requirements.Methods;
import requirements.Statuses;
import requirements.Types;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static server.Server.*;

public class Worker implements Runnable {

    private ServerSocket serverSocket;
    private String path;
    private int bufferSize;

    Worker(ServerSocket serverSocket, String path, int bufferSize) {
        this.serverSocket = serverSocket;
        this.path = path;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = getSocket()) {
                final OutputStream os = socket.getOutputStream();
                final InputStream is = socket.getInputStream();
                final BufferedReader br = new BufferedReader(new InputStreamReader(is));
                final ArrayList<String> requestData = getRequestData(br);
                try {
                    parseRequest(requestData, os);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                br.close();
                is.close();
                os.close();
            } catch (IOException e) {
            }
        }
    }

    private Socket getSocket() throws IOException {
        return serverSocket.accept();
    }

    private ArrayList<String> getRequestData(BufferedReader br) throws IOException {
        ArrayList<String> params = new ArrayList<>();
        String param = br.readLine();
        while (param != null && !(param.equals(""))) {
            params.add(param);
            param = br.readLine();
        }
        return params;
    }

    private void parseRequest(ArrayList<String> request, OutputStream os) throws IOException, ParseException {

        String[] requestParts = request.get(0).split(" ");
        String method = requestParts[0];
        String path = URLDecoder.decode(requestParts[1], encoding);
        if (!method.equals(Methods.GET) && !method.equals(Methods.HEAD)) {
            addHeader(getMainHeaders(), Statuses.CODE_METHOD_NOT_ALLOWED, os);
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            addHeader(getMainHeaders(), Statuses.CODE_NOT_FOUND, os);
            return;
        }
        String lenghtHeader = getFileContentLength(file.length());
        String file_extHeader = getFileExtension(file.getName().replace(".", " ").split(" ")[1]);
        String headers = lenghtHeader + file_extHeader;
        addHeader(headers, Statuses.CODE_OK, os);

        InputStream is = new FileInputStream(file);
        sendFile(is, os);
        os.flush();
    }

//    private boolean checkFileExists(String path) {
//        File file = new File(path);
//        if (!file.exists())
//            return false;
//        return true;
//    }

    private String getMainHeaders() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateString = String.valueOf(dateFormatGmt.parse(dateFormatGmt.format(new Date())));
        return String.format(
                "Server: %s\r\nConnection: %s\r\nDate: %s\r\n",
                serverName, connection, dateString
        );
    }

    private void sendFile(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len = is.read(buffer);
        while (len != -1) {
            os.write(buffer, 0, len);
            len = is.read(buffer);
        }
    }

    private String getFileContentLength(long fileSize) {
        return String.format("Content-Length: %d\r\n", fileSize);
    }

    private String getFileExtension(String ext) {
        return String.format("Content-Type: %s\r\n", Types.getType(ext));
    }

    private void addHeader(String headers, int statusCode, OutputStream os) throws IOException {
        String statusString = String.format("%s %d %s\r\n", http_version, statusCode, Statuses.getStatus(statusCode));
        final String msg = statusString + headers;
        os.write(msg.getBytes());
    }
}
