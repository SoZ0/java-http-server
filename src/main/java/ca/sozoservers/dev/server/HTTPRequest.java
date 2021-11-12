package ca.sozoservers.dev.server;

public record HTTPRequest(HTTPMethod method, HTTPResource resouce, HTTPProtocol protocol){

    public static HTTPRequest getRequest(String request){
        try{
            String headers[] = request.split(" ");
            return new HTTPRequest(HTTPMethod.parseMethod(headers[0]),HTTPResource.parseResource(headers[1]), HTTPProtocol.parseProtocol(headers[2]));
        }catch(NullPointerException | ArrayIndexOutOfBoundsException ex){
            return new HTTPRequest(null, null, null);
        }
    }

    public boolean isValidRequest(){
        return !(method == null || protocol == null || resouce == null);
    }

    public String toString(){
        String out = new String();
        if(method != null) out += method.toString() + "\n";
        if(resouce != null) out += resouce.toString()+ "\n";
        if(protocol != null) out += protocol.toString()+ "\n";
        return out;
    }
}
