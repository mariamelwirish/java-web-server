import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest implements Runnable {
    private Socket socket;
    final String CRLF = "\r\n";

    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // run function for Runnable (for threads).
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println("Error while processing request: " + e);
        }
    }

    // function to process all upcoming HTTP requests.
    private void processRequest() throws Exception {
        // Input Stream from client.
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        // Output Stream to client.
        OutputStream os = socket.getOutputStream();

        // Read HTTP Request (synchronized to avoid the mixed output).
        synchronized(System.out) {
            System.out.println("New connection received from client: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            System.out.println("----------------- HTTP Request -----------------");

            // read the HTTP request line.
            String requestLine = br.readLine();
            System.out.println(requestLine);

            // read the remaining HTTP header lines.
            String headerLine = null;
            while((headerLine = br.readLine()).length() != 0) {
                System.out.println(headerLine);
            }
            System.out.println();

            // Read the requested file.
            StringTokenizer tokens = new StringTokenizer(requestLine);
            tokens.nextToken(); // skip the request method (assumed to be GET).
            String fileName = tokens.nextToken();
            if (fileName.equals("/")) {
                fileName = "/index.html";
            }
            fileName = "." + fileName; // file requested from current directory
            System.out.println("Requested file: " + fileName);

            // Open the requested file, if exists
            FileInputStream fis = null;
            boolean fileExists = true;
            try {
                fis = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                fis = new FileInputStream("./error404.html");
                fileExists = false;
            }

            // Determine the type of response message.
            String statusLine = null;
            String contentTypeLine = null;
            if(fileExists) {
                statusLine = "HTTP/1.1 200 OK" + CRLF;
                contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
            } else {
                statusLine = "HTTP/1.0 404 Not Found" + CRLF;
                contentTypeLine = "Content-type: text/html" + CRLF;
            }

            // Construct response (status line, header line(s), CRLF, body).
            os.write(statusLine.getBytes());
            os.write(contentTypeLine.getBytes());
            os.write(CRLF.getBytes());
            sendBytes(fis, os);
            fis.close();


        }

        // Close streams and socket.
        try {
            is.close();
            os.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error closing resources: " + e);
        }
    }

    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }

        if (fileName.endsWith(".png")) {
            return "image/png";
        }

        if (fileName.endsWith(".css")) {
            return "text/css";
        }


        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        // Default binary type
        return "application/octet-stream";
    }


    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
    {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        }
    }
}
