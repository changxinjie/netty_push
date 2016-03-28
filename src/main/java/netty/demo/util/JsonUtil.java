package netty.demo.util;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil
{
    public static String toString(JSONObject source, String key)
    {
	String result = "";
	if (source.has(key))
	{
	    try
	    {
		result = source.get(key).toString();
	    } catch (JSONException e)
	    {
	    }
	}
	return result;
    }

    public static String toString(JSONObject source, String key, String defaultVal)
    {
	String result = defaultVal;
	if (source.has(key))
	{
	    try
	    {
		result = source.get(key).toString();
	    } catch (JSONException e)
	    {
	    }
	}
	return result;
    }
}
