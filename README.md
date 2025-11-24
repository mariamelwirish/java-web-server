# Java Web Server

A simple Java-based web server for serving static files (HTML, TXT, etc.).

## Features

- Serves static files (HTML, TXT, etc.) from the project directory
- Handles basic HTTP GET requests
- Returns custom error pages (e.g., `error404.html` for not found)
- Simple request parsing and response handling
- Multi-threaded: handles multiple clients
- Logs requests to the console
- Can serve any file in the project folder

## Files

- `src/WebServer.java`: Main server class, starts the server and listens for connections
- `src/HttpRequest.java`: Parses and processes HTTP requests
- `index.html`: Default homepage
- `error404.html`: Custom 404 error page
- `test1.txt`, `test2.html`: Example files for testing

## Usage

1. Compile the Java files in the `src` directory.
2. Run `WebServer.java` to start the server.
3. After starting, the server will display the access URL in the console (e.g., `Access the server at: http://localhost:6789/`).
4. You can request any file in the project directory (e.g., `test1.txt`, `test2.html`).


