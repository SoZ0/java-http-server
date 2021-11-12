package ca.sozoservers.dev.server;

public record HTTPProtocol(Protocol protocol) {
    public enum Protocol{
        HTTP_1_0("HTTP/1.0"),
        HTTP_1_1("HTTP/1.1");

        String protocol;
        private Protocol(String protocol){
            this.protocol = protocol;
        }

        public String get(){
            return protocol;
        }
    }

    public static HTTPProtocol parseProtocol(String protocol){
        try{
            return new HTTPProtocol(Protocol.valueOf(protocol.replaceAll("/", "_").replace(".", "_")));
        }catch (IllegalArgumentException ex){
            return null;
        }
    }

    public String toString(){
        String out = new String();
        out += "protocol:"+protocol.get();
        return out;
    }
}
