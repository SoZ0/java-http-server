package ca.sozoservers.dev.server;

public class HTTPHeader {

    private String key;
    private Object value;

    public HTTPHeader(String key, Object value){
       this.key = key;
       this.value = value;
    }

    public String toString(){
        return key.toString() + ": " + value.toString();
    }

    public static HTTPHeader ContentLength(long length){
        return new HTTPHeader("Content-length", length);
    }
}        

