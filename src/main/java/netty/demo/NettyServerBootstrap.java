package netty.demo;

import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.demo.codec.NettyCodec;
import netty.demo.ssl.NettyServerHandler;
import netty.demo.util.PropertityUtil;

public class NettyServerBootstrap
{
    private static int PORT;
    static{
	PORT = Integer.valueOf(PropertityUtil.loadConfig().getProperty("netty.server.port"));
    }
    private static Logger logger = Logger.getLogger(NettyServerBootstrap.class.getName());

    private void run() throws InterruptedException
    {
	EventLoopGroup boss = new NioEventLoopGroup();
	EventLoopGroup worker = new NioEventLoopGroup();
	ServerBootstrap bootstrap = new ServerBootstrap();
	bootstrap.group(boss, worker);
	bootstrap.channel(NioServerSocketChannel.class);
	bootstrap.option(ChannelOption.SO_BACKLOG, 128);
	bootstrap.option(ChannelOption.TCP_NODELAY, true);
	bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
	bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
	{
	    @Override
	    protected void initChannel(SocketChannel socketChannel) throws Exception
	    {
		ChannelPipeline pipeline = socketChannel.pipeline();

		pipeline.addLast(new NettyCodec());
		pipeline.addLast(new NettyServerHandler());
	    }
	});
	logger.info("server is running on the port :" + PORT);
	bootstrap.bind(PORT).sync().channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException
    {
	NettyServerBootstrap bootstrap = new NettyServerBootstrap();
	bootstrap.run();
    }
}