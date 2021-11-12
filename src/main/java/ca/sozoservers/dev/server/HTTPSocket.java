package ca.sozoservers.dev.server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HTTPSocket {
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private PrintStream print;   
    private HTTPRequest request;
    private static final String newLine="\r\n";
    
    public HTTPSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedOutputStream(socket.getOutputStream());
        print = new PrintStream(out);
    }

    public HTTPRequest getHTTPRequest() throws IOException{
        return request = request == null ? HTTPRequest.getRequest(in.readLine()) : request;
    }

    public HTTPExchange toHTTPExchange() throws IOException{
        return new HTTPExchange(this, getHTTPRequest());
    }

    public void closeInputStream() throws IOException{
        in.close();
    }

    public void closeOutputStream() throws IOException{
        out.close();
    }

    public void closePrintStream(){
        print.close();
    }

    public void closeAllStreams() throws IOException{
        closeInputStream();
        closeOutputStream();
        closePrintStream();
    }

    public void sendResponseHeaders(int code, long responseLength){
        String response = new String();
        response += "HTTP/1.0 "+code+newLine;
        response += "Content-length: "+responseLength+newLine;
        print.print(response);
    }

    public void sendResponseBody(String response){
        String body = new String();
        body += "Content-Type: text/plain"+newLine;
        body += "Date: "+new Date()+newLine+newLine;
        body += response;
        print.print(body);
    }

    public Socket getSocket(){
        return socket;
    }
}
