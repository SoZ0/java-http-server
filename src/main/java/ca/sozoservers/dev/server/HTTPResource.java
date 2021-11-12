package ca.sozoservers.dev.server;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

public record HTTPResource(String path, HashMap<String, String> queries) {
    
    public static HTTPResource parseResource(String resource){
        HashMap<String, String> map = new HashMap<>();
        String array[] = resource.split("\\?");
        try{
            if(array.length <= 1) return new HTTPResource(array[0], map);
            String queries[] = array[1].split("&");
            for (String query : queries) {
                String entry[] = query.split("=");
                if(entry.length <= 1) continue;
                map.put(entry[0], entry[1]);
            }
            return new HTTPResource(array[0], map);
        }catch(PatternSyntaxException | ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            return new HTTPResource(array[0], map);
        }
       
    }

    public String toString(){
        String out = new String();
        for (Entry<String, String> entry : queries.entrySet()) {
            out += entry.getKey() + ":" + entry.getValue() + "\n";
        }
        out += "path:" + path;
        return out;
    }
}
