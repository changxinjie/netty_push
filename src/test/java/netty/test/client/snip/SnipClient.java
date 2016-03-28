package netty.test.client.snip;

import java.util.Properties;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateHandler;
import netty.demo.codec.NettyCodec;
import netty.demo.msg.Message;
import netty.demo.msg.MsgType;
import netty.demo.util.PropertityUtil;
import netty.test.client.AppClientHandler;

public class SnipClient
{
    private static Logger logger = Logger.getLogger(SnipClient.class.getName());
    private static SocketChannel socketChannel;

    private static void initSocketChannel()
    {
	Properties properties = PropertityUtil.loadConfig();
	int PORT = Integer.valueOf(properties.getProperty("netty.app.port"));
	String HOST = properties.getProperty("netty.app.host");
	EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	Bootstrap bootstrap = new Bootstrap();
	bootstrap.channel(NioSocketChannel.class);
	bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	bootstrap.group(eventLoopGroup);
	bootstrap.handler(new ChannelInitializer<SocketChannel>()
	{
	    @Override
	    protected void initChannel(SocketChannel socketChannel) throws Exception
	    {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new IdleStateHandler(200,100, 0));
		pipeline.addLast(new NettyCodec());
		pipeline.addLast(new AppClientHandler());
	    }
	});
	try
	{
	    ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
	    if (future.isSuccess())
	    {
		socketChannel = (SocketChannel) future.channel();
		logger.info("connect to server successful ---------");
	    }
	} catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
    }
    
    public static SocketChannel getSocketChannel(){
	if (socketChannel==null)
	{
	    initSocketChannel();
	}
	return socketChannel;
    }
    
    public static void panelStatusUpdated(int account_id_new)
    {
	
	Message pushMessage = new Message((byte) 0, MsgType.Panel_status.getValue(), "");
	socketChannel.writeAndFlush(pushMessage);
    }
}
