package ca.sozoservers.dev.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer {

    private int port, backlog;
    private boolean running = false;
    private ServerSocket socket;
    private HashMap<String, HTTPHandler> resources = new HashMap<>();

    public HTTPServer(int port, int backlog){
        this.port = port;
        this.backlog = backlog;
    }

    private Runnable startHTTP() throws IOException{
        socket = getServerSocket();
        return new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try{
                        HTTPSocket connection = new HTTPSocket(socket.accept());

                        HTTPRequest request = connection.getHTTPRequest();
                        if(!request.isValidRequest()) continue;

                        if(resources.containsKey(request.resouce().path())){
                            resources.get(request.resouce().path()).handle(connection.toHTTPExchange());
                        }else{
                            String response = "404, page not found";
                            connection.sendResponseHeaders(404, response.length());
                            connection.sendResponseBody(response);
                            connection.closePrintStream();
                        }

                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    private ServerSocket getServerSocket() throws IOException{
        return socket = socket == null ? new ServerSocket(port, backlog) : socket;
    }

    public void start() throws IOException{
        running = true;
        new Thread(startHTTP()).start(); 
    }

    public void createResource(String path, HTTPHandler handler){
        resources.put(path, handler);
    }

    public void stop(){
        running = false;
    }
}
