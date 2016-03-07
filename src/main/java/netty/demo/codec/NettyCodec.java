package netty.demo.codec;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import netty.demo.msg.Message;
import netty.demo.util.ByteUtil;
import netty.demo.util.CheckSumUtil;

public class NettyCodec extends ByteToMessageCodec<Message>
{
    private static Logger logger = Logger.getLogger(NettyCodec.class.getName());

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception
    {
	byte[] src = msg.build();
	src = ByteUtil.combine(src, new byte[]
	{ CheckSumUtil.build(src) });
	out.writeBytes(src);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
	int buf_length = in.readableBytes();
	logger.fine("decode now -->" + buf_length);
	if (in.isReadable())
	{
	    if (buf_length < 10)
		return;
	    byte[] src = new byte[buf_length];
	    in.readBytes(src);
	    if (!CheckSumUtil.check(src))
		return;
	    Message message = new Message();
	    message.setHeader(Arrays.copyOfRange(src, 0, 2));
	    message.setDevice_token_id(Arrays.copyOfRange(src, 2, 6));
	    message.setCommand(src[6]);
	    message.setLength(Arrays.copyOfRange(src, 7, 9));
	    int data_length = message.getLength();
	    message.setData(Arrays.copyOfRange(src, 9, 9 + data_length));
	    message.setChecksum(src[src.length - 1]);
	    out.add(message);
	}
    }

}
