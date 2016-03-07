package netty.demo.client.snip;

import java.util.Properties;

import javax.net.ssl.SSLException;

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
import io.netty.handler.timeout.IdleStateHandler;
import netty.demo.client.app.AppClientHandler;
import netty.demo.codec.NettyCodec;
import netty.demo.codec.NettyDecode;
import netty.demo.codec.NettyEncode;
import netty.demo.msg.Message;
import netty.demo.msg.MsgType;
import netty.demo.util.PropertityUtil;

public class SnipClientBootstrap
{
    private static int PORT;
    private static String HOST;
    static{
	Properties properties = PropertityUtil.loadConfig();
	PORT = Integer.valueOf(properties.getProperty("netty.app.port"));
	HOST = properties.getProperty("netty.app.host");
    }
    private SocketChannel socketChannel;

    private void run() throws InterruptedException, SSLException
    {
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
		pipeline.addLast(new IdleStateHandler(20, 10, 0));
//		pipeline.addLast(new NettyEncode());
//		pipeline.addLast(new NettyDecode());
		pipeline.addLast(new NettyCodec());
		pipeline.addLast(new AppClientHandler());
	    }
	});
	ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
	if (future.isSuccess())
	{
	    socketChannel = (SocketChannel) future.channel();
	    System.out.println("connect to server successful ---------");
	}
    }

    public static void main(String[] args) throws InterruptedException, SSLException
    {
	SnipClientBootstrap bootstrap = new SnipClientBootstrap();
	bootstrap.run();

	SocketChannel channel = bootstrap.socketChannel;
	JsonObject jsonObject = new JsonObject();
	jsonObject.addProperty("account_id_new", 419784);
	jsonObject.addProperty("account_keyholder_id", 0);
	jsonObject.addProperty("pannel", "pannel is low_battery");
	Message pushMessage = new Message((byte)0, MsgType.Panel_status.getValue(), jsonObject.toString());
	while (true)
	{
	    channel.writeAndFlush(pushMessage);
	    Thread.sleep(3000);
	}
    }
}
