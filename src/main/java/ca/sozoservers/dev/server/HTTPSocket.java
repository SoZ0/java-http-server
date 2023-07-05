package ca.sozoservers.dev.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class HTTPSocket {
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private HTTPRequest request;
    private String preResponse = "";
    private static final String NEWLINE="\r\n";

    public HTTPSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedOutputStream(socket.getOutputStream());
    }

    public HTTPRequest getHTTPRequest() throws IOException{
        return request = request == null ? HTTPRequest.getRequest(in.readLine()) : request;
    }

    public HTTPExchange toHTTPExchange() {
        try{
            return new HTTPExchange(this, getHTTPRequest());
        }catch(IOException e){
            return new HTTPExchange(this, null);
        }
    }

    public void closeInputStream() throws IOException{
        in.close();
    }

    public void closeOutputStream() throws IOException{
        out.close();
    }

    public void closeAllStreams() throws IOException{
        closeInputStream();
        closeOutputStream();
    }

    public void sendResponseHeaders(HTTPStatus status, HTTPHeader... headers) throws IOException{
        String response = new String();
        response += status.getResponse()+NEWLINE;
        for (HTTPHeader httpHeader : headers) {
            response += httpHeader.toString() + NEWLINE;
        }
        response += NEWLINE;
        out.write(response.getBytes());
        out.flush();

    }

    public void addResponseBody(String body){
        preResponse += body + NEWLINE;
    }

    public void sendResponseBody(byte[] body, HTTPResponse... responses) throws IOException{
        String response = preResponse;
        for (HTTPResponse httpResponse : responses) {
            response += httpResponse.toString()+NEWLINE;
        }       
        response += NEWLINE;

        out.write(response.getBytes());
        out.write(body);
        out.write(NEWLINE.getBytes());
        out.write(NEWLINE.getBytes());
        out.flush();
        System.out.print(response + body+NEWLINE);
        preResponse = "";
    }

    public void sendResponseBody(String body, HTTPResponse... responses) throws IOException{
        sendResponseBody(body.getBytes(), responses);
    }

    public Socket getSocket(){
        return socket;
    }

    public void consumeInput() throws IOException{
        while(in.ready()){
            in.readLine();
        }
    }

    public boolean isConnectionClosed(){
        return socket.isInputShutdown() || socket.isClosed() || socket.isOutputShutdown();
    }
}
