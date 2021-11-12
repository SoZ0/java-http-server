package ca.sozoservers.dev.server;

public record HTTPMethod (Method method) {
    public enum Method{
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
        CONNECT,
        OPTIONS,
        TRACE,
        PATCH
    }

    public static HTTPMethod parseMethod(String method){
        try{
            return new HTTPMethod(Method.valueOf(method));
        }catch (IllegalArgumentException ex){
            return null;
        }
    }

    public String toString(){
        String out = new String();
        out += "method:"+method;
        return out;
    }
}
