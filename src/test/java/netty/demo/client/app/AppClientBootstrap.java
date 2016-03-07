package netty.demo.client.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLException;

import com.google.gson.Gson;

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
import netty.demo.codec.NettyCodec;
import netty.demo.codec.NettyDecode;
import netty.demo.codec.NettyEncode;
import netty.demo.damain.DeviceToken;
import netty.demo.dao.DeviceTokenDao;
import netty.demo.msg.Message;
import netty.demo.msg.MsgType;
import netty.demo.util.PropertityUtil;

public class AppClientBootstrap
{
    static Logger logger = Logger.getLogger(AppClientBootstrap.class.getName());
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
	// final SslContext sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
	EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	Bootstrap bootstrap = new Bootstrap();
	bootstrap.channel(NioSocketChannel.class);
	bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	bootstrap.group(eventLoopGroup);
	// bootstrap.remoteAddress(HOST, PORT);
	bootstrap.handler(new ChannelInitializer<SocketChannel>()
	{
	    @Override
	    protected void initChannel(SocketChannel socketChannel) throws Exception
	    {
		ChannelPipeline pipeline = socketChannel.pipeline();
		// SSLEngine engine = SslContextFactory.getClientContext().createSSLEngine();
		// engine.setUseClientMode(true);
		// pipeline.addLast("ssl", new SslHandler(engine));
		// pipeline.addLast(sslCtx.newHandler(socketChannel.alloc(),HOST,PORT));
		pipeline.addLast(new IdleStateHandler(20, 10, 0));
//		pipeline.addLast(new NettyEncode());
//		pipeline.addLast(new NettyDecode());
		pipeline.addLast(new NettyCodec());
		pipeline.addLast(new AppClientHandler());
	    }
	});
	System.out.println("connect to server successful ---------");
	socketChannel = (SocketChannel) bootstrap.connect(HOST, PORT).sync().channel();
    }

    public static void main(String[] args) throws InterruptedException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, IOException, SQLException
    {
	AppClientBootstrap bootstrap = new AppClientBootstrap();
	bootstrap.run();
	SocketChannel channel = bootstrap.socketChannel;
	DeviceTokenDao deviceTokenDao = DeviceTokenDao.newInstance();
	
	// the params comes from login api, hard code here.
	DeviceToken device_token = deviceTokenDao.queryForId(1);
	Message loginMsg = new Message(0, 0, device_token.getId(), MsgType.Login.getValue());
	logger.info("send Login message by device -->" + new Gson().toJson(loginMsg));
	channel.writeAndFlush(loginMsg);
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	for (;;)
	{
	    ChannelFuture lastWriteFuture = null;
	    String line = "null";
	    try
	    {
		line = in.readLine();
		lastWriteFuture = channel.writeAndFlush(line);
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    }
	    if (line == null)
	    {
		break;
	    }
	    if ("bye".equals(line.toLowerCase()))
	    {
		channel.closeFuture().sync();
		break;
	    }
	    // Wait until all messages are flushed before closing the channel.
	    if (lastWriteFuture != null)
	    {
		lastWriteFuture.sync();
	    }
	}

    }
}