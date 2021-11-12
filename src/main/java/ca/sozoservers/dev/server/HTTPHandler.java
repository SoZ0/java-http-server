package ca.sozoservers.dev.server;

public interface HTTPHandler {
    
    void handle(HTTPExchange exchange);
}
