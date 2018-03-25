package server;

import requirements.Methods;
import requirements.Statuses;
import requirements.Types;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
        while (true) try (Socket socket = getSocket()) {
            final OutputStream os = socket.getOutputStream();
            final InputStream is = socket.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            final ArrayList<String> requestData = getRequestData(br);
            try {
                parseRequest(requestData, os);
            } catch (ParseException | IndexOutOfBoundsException ignored) {
            }
            br.close();
            is.close();
            os.close();
        } catch (IOException ignored) {
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
        String _path = path + URLDecoder.decode(requestParts[1].replace("?", " ").split(" ")[0], encoding);
        if (requestParts[1].contains("../")) {
            addHeader(getMainHeaders(), Statuses.CODE_FORBIDDEN, os);
            return;
        }
        if (!method.equals(Methods.GET) && !method.equals(Methods.HEAD)) {
            addHeader(getMainHeaders(), Statuses.CODE_METHOD_NOT_ALLOWED, os);
            return;
        }
        File file = new File(_path);
        if (!file.exists()) {
            addHeader(getMainHeaders(), Statuses.CODE_NOT_FOUND, os);
            return;
        }
        if (file.isFile()) {
            String lenghtHeader = getFileContentLength(file.length());
            String[] extSplit = file.getName().replace(".", " ").split(" ");
            String file_extHeader = getFileExtension(extSplit[extSplit.length - 1]);
            String headers = getMainHeaders() + file_extHeader + lenghtHeader;
            addHeader(headers, Statuses.CODE_OK, os);
            if (method.equals(Methods.GET))
                sendFileToBody(file, os);
        } else {
            findIndexFile(_path + indexFile, os, method);
        }
    }

    private void sendFileToBody(File file, OutputStream os) throws IOException {
        FileInputStream fs = new FileInputStream(file);
        sendFile(fs, os);
        os.flush();
    }

    private void findIndexFile(String _path, OutputStream os, String method) throws IOException {
        File file = new File(_path);
        if (!file.exists()) {
            addHeader(getMainHeaders(), Statuses.CODE_FORBIDDEN, os);
            return;
        }
        String lenghtHeader = getFileContentLength(file.length());
        String[] extSplit = file.getName().replace(".", " ").split(" ");
        String file_extHeader = getFileExtension(extSplit[extSplit.length - 1]);
        String headers = getMainHeaders() + file_extHeader + lenghtHeader;
        addHeader(headers, Statuses.CODE_OK, os);

        if (method.equals(Methods.GET))
            sendFileToBody(file, os);
    }

    private String getMainHeaders() {

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());

        return String.format(
                "Server: %s\r\nConnection: %s\r\nDate: %s\r\n",
                serverName, connection, cal.getTime().toString()
        );
    }

    private void sendFile(FileInputStream fs, OutputStream os) throws IOException {
        final byte[] buff = new byte[bufferSize];
        int read = 0;
        while (read != -1) {
            read = fs.read(buff);
            if (read != -1) {
                os.write(buff, 0, read);
            }
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
        final String msg = statusString + headers + "\r\n";
        os.write(msg.getBytes());
    }
}
