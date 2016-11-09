import java.util.HashMap;

public class HashMapBuilder {

	public static HashMap<String, String> build(String... data){
	    HashMap<String, String> result = new HashMap<String, String>();

	    if(data.length % 2 != 0) 
	        throw new IllegalArgumentException("Odd number of arguments");      

	    String key = null;
	    Integer step = -1;

	    for(String value : data){
	        step++;
	        switch(step % 2){
	        case 0: 
	            if(value == null)
	                throw new IllegalArgumentException("Null key value"); 
	            key = value;
	            continue;
	        case 1:             
	            result.put(key, value);
	            break;
	        }
	    }

	    return result;
	}
}
