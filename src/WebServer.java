import java.net.*;

public class WebServer {
    public static void main(String[] args) throws Exception {
        // Choosing the port for the server to run on.
        int port = 6789;

        // Welcome Messages for Web Server.
        System.out.println("Welcome to CMPS 242 Web Server! :D. Prepared by Mariam Elwirish & Elie Jbara.");
        System.out.println("Web Server Starting...");

        // Binding a listening socket ti the port.
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Web Server is listening on port " + port);
        System.out.println("Access the server at: http://localhost:" + port + "/\n");
        System.out.println("Waiting for connections...\n");

        while(true) {
            // Waiting for client to connect (blocking).
            Socket client = socket.accept();

            // Creating a thread per request.
            HttpRequest request = new HttpRequest(client);
            Thread thread = new Thread(request);
            thread.start();

        }
    }
}