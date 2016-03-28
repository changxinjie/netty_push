package netty.demo.util;

import java.util.HashMap;
import java.util.Map;

public class StringUtil
{
    public static boolean isEmpty(String string)
    {
	if (string == null || string.length() == 0)
	{
	    return true;
	}
	return false;
    }
    
    public static Map<String, String> convertParasm2Map(String params)
    {
	Map<String, String> map = new HashMap<>();
	    String[] arr = params.split(",");
	    for (String string : arr)
	    {
		String[] keyOrval = string.split("=");
		map.put(keyOrval[0], keyOrval.length==1?null:keyOrval[1]);
	    }
	return map;
    }
}
