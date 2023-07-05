package ca.sozoservers.dev.server;

public class HTTPResponse{

    private String key;
    private Object value;

    public HTTPResponse(String key, Object value){
       this.key = key;
       this.value = value;
    }

    public String toString(){
        return key.toString() + ": " + value.toString();
    }
}
