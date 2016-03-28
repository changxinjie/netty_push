package netty.demo.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class HttpUtil
{
    private static Logger logger = Logger.getLogger(HttpUtil.class.getName());
    private static HttpClient httpClient;

    public static HttpClient getClient()
    {
	if (httpClient == null)
	{
	    httpClient = HttpClients.createDefault();
	}
	return httpClient;
    }

    public static JSONObject getRequest(String url, HashMap<String, Object> reqParams)
    {
	if (reqParams != null)
	    url+="?";
	    for (Entry<String, Object> entry : reqParams.entrySet())
	    {
		url += entry.getKey() + "=" + entry.getValue()+"&";
	    }
	logger.fine("Request::>>"+url);
	HttpGet httpGet = new HttpGet(url);
	CloseableHttpResponse response = null;
	String result = null;
	try
	{
	   response = (CloseableHttpResponse) getClient().execute(httpGet);
	   result= EntityUtils.toString(response.getEntity());
	   return new JSONObject(result);
	}catch(JSONException e){
	    logger.warning("Http Response Error>>>");
	    logger.warning(result);
	} catch (Exception e)
	{
	    logger.warning("Http Request Error>>>");
	    e.printStackTrace();
	    
	}
	return null;
    }
    @Test
    public  void test()
    {
	String base = "http://cloud.securenettech.com/mobile/touch-0.5/";
	String item = base + "get_zones_statuses.php";
	HashMap<String, Object> map = new HashMap<>();
	map.put("a","0d1e8c9089c36806bcc82502a5b0b0fc");
	map.put("keyholder_id", 1);
	JSONObject jsonObject = getRequest(item, map);
	if (jsonObject!=null)
	{
	    System.out.println(jsonObject.toString());
	}else{
	    System.out.println("null");
	}
    }
}
