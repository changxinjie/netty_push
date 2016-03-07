package netty.demo.ssl;

import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import netty.demo.damain.DeviceToken;
import netty.demo.dao.DeviceTokenDao;
import netty.demo.msg.Message;
import netty.demo.msg.MsgType;

public class NettyServerHandler extends SimpleChannelInboundHandler<Message>
{
    static Logger logger = Logger.getLogger(NettyServerHandler.class.getName());

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
	NettyChannelMap.remove(ctx.channel().id());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message obj) throws Exception
    {
	SocketChannel channel = (SocketChannel) channelHandlerContext.channel();
	Message message = obj;
	MsgType type = MsgType.convert(message.getCommand());
	logger.info(type.toString());
	if (type.equals(MsgType.Heat_beat))
	{
	} else if (type.equals(MsgType.Login))
	{
	    Integer device_token_id = message.getDevice_token_id();
	    NettyChannelMap.add(device_token_id, channel.id());
	    channels.add(channel);
	} else
	{
	    DeviceTokenDao deviceTokenDao = DeviceTokenDao.newInstance();
	    JSONObject json = new JSONObject(message.getData());
	    Integer accountId = json.getInt("account_id_new");
	    Integer keyholderId = json.getInt("account_keyholder_id");
	    List<DeviceToken> deviceTokenList = deviceTokenDao.findDevice(accountId, keyholderId);
	    for (DeviceToken deviceToken : deviceTokenList)
	    {
		logger.info("send to app client-->" + json.toString());
		Message response = new Message(1, 0, type.getValue(), json.toString(), deviceToken.getDeviceToken());
		ChannelId channelId = NettyChannelMap.getChannelId(deviceToken.getId());
		if (channelId == null)
		    break;
		Channel appClient = channels.find(channelId);
		if (appClient != null)
		{
		    appClient.writeAndFlush(response);
		}
	    }
	}
	ReferenceCountUtil.release(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
	if (cause instanceof DecoderException)
	{
	    logger.warning("DecodeException ::" + cause.getLocalizedMessage());
	    return;
	}
	System.out.println("client " + ctx.channel().id() + " has been removed.");
	cause.printStackTrace();
    }
}