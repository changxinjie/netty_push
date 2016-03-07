package netty.demo.codec;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.demo.msg.Message;
import netty.demo.util.ByteUtil;
import netty.demo.util.CheckSumUtil;

public class NettyEncode extends MessageToByteEncoder<Message>
{
    private static Logger logger = Logger.getLogger(NettyEncode.class.getName());
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception
    {
	byte[] src = msg.build();
	src = ByteUtil.combine(src, new byte[]{CheckSumUtil.build(src)});
	logger.info("\n encode data -->");
	for (byte b : src)
	{
	    System.out.print(b);
	}
	System.out.println("\n");
	out.writeBytes(src);
    }
}
