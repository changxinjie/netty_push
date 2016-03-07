package netty.demo.client.app;

import java.util.logging.Logger;

import com.google.gson.Gson;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import netty.demo.damain.DeviceToken;
import netty.demo.dao.DeviceTokenDao;
import netty.demo.msg.Message;
import netty.demo.msg.MsgType;
import sun.misc.BASE64Decoder;

public class AppClientHandler extends SimpleChannelInboundHandler<Message>
{

    private static Logger logger = Logger.getLogger(AppClientHandler.class.getName());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
	if (evt instanceof IdleStateEvent)
	{
	    IdleStateEvent e = (IdleStateEvent) evt;
	    switch (e.state())
	    {
	    case WRITER_IDLE:
		Message pingMsg = new Message(0);
		ctx.channel().writeAndFlush(pingMsg);
		break;
	    default:
		break;
	    }
	}
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message msg) throws Exception
    {
	System.out.println("[receive data from server]"+new Gson().toJson(msg));
	MsgType msgType = MsgType.convert(msg.getCommand());
	DeviceTokenDao deviceTokenDao = DeviceTokenDao.newInstance();
	DeviceToken deviceToken = deviceTokenDao.queryForId(1);
	byte[] tokenArr = new BASE64Decoder().decodeBuffer(deviceToken.getDeviceToken());
	
	String data =  msg.getData(tokenArr);
	switch (msgType)
	{
	case Panel_status:
	    logger.info("[app client]--------receive " + msgType.name() + " msg from snip ------------");
	    logger.info(data);
	    break;
	case Zone_status:
	    logger.info("[app client]--------receive " + msgType.name() + " msg from snip ------------");
	    logger.info(data);
	    break;
	case Zwave_control_status:
	    logger.info("[app client]--------receive " + msgType.name() + " msg from snip ------------");
	    logger.info(data);
	    break;
	case Zwave_device_status:
	    logger.info("[app client]--------receive " + msgType.name() + " msg from snip ------------");
	    logger.info(data);
	    break;
	case Camera_status:
	    logger.info("[app client]--------receive " + msgType.name() + " msg from snip ------------");
	    logger.info(data);
	    break;
	default:
	    break;
	}
	ReferenceCountUtil.release(msgType);
    }
}