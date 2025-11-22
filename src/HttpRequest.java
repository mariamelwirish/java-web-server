import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest implements Runnable {
    private Socket socket;
    final String CRLF = "\r\n";

    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println("Error while processing request: " + e);
        }
    }

    private void processRequest() throws Exception {
        // Input Stream from client.
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        // Output Stream to client.
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        // Read HTTP Request.
        synchronized(System.out) {
            System.out.println("New connection received from client: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            System.out.println("----------------- HTTP Request -----------------");

            String requestLine = br.readLine();

            System.out.println(requestLine);

            String headerLine = null;
            while((headerLine = br.readLine()).length() != 0) {
                System.out.println(headerLine);
            }
            System.out.println();
        }

        // Close streams and socket.
        try {
            os.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error closing resources: " + e);
        }
    }
}
