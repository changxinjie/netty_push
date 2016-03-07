package netty.demo.ssl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelId;

public class NettyChannelMap {
    private static Map<Integer,ChannelId> map= new ConcurrentHashMap<Integer,ChannelId>();
    public static void add(Integer device_token_id,ChannelId clientId){
        map.put(device_token_id,clientId);
    }
    public static ChannelId getChannelId(Integer device_token_id){
       return map.get(device_token_id);
    }
    public static void remove(ChannelId channelId){
	Integer key = null;
	for (Entry<Integer, ChannelId> entry : map.entrySet())
	{
	    if( entry.getValue().equals(channelId)){
		key = entry.getKey();
	    }
	}
	map.remove(key);
    }
}