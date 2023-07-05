package ca.sozoservers.dev.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HTTPServer implements ThreadFactory {

    private int port, backlog;
    private boolean running = false;
    private ServerSocket socket;
    private HashMap<String, HTTPHandler> resources = new HashMap<>();
    private Executor executor = Executors.newCachedThreadPool(this);

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
                            executor.execute(()  -> resources.get(request.resouce().path()).handle(connection.toHTTPExchange()));
                        }else{
                            String response = "404, page not found";
                            connection.sendResponseHeaders(HTTPStatus.NOT_FOUND_404, HTTPHeader.ContentLength(response.length()));
                            connection.sendResponseBody(response);
                            connection.closeAllStreams();
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
       Thread thread = new Thread(startHTTP());
       thread.setDaemon(true);
       thread.start();
    }

    public void createResource(String path, HTTPHandler handler){
        resources.put(path, handler);
    }

    public void stop(){
        running = false;
    }

    @Override
    public Thread newThread(Runnable arg0) {
        Thread thread = new Thread(arg0);
        thread.setDaemon(true);
        return thread;
    }
}
