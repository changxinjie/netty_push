package netty.demo.codec;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import netty.demo.msg.Message;
import netty.demo.util.CheckSumUtil;

public class NettyDecode extends ByteToMessageDecoder
{
    private static Logger logger = Logger.getLogger(NettyDecode.class.getName());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
	int buf_length = in.readableBytes();
	System.out.println("decode now -->"+buf_length);
	if (in.isReadable())
	{
	    try
	    {
		if (buf_length < 10)
		    return;
		byte[] src = new byte[buf_length];
		in.readBytes(src);
		if (!CheckSumUtil.check(src))
		    return;
		logger.info("\ndecode data -->");
		for (byte b : src)
		{
		    System.out.print(Integer.toHexString(b)+",");
		}
		logger.info("\n");
		Message message = new Message();
		message.setHeader(Arrays.copyOfRange(src, 0, 2));
		message.setDevice_token_id(Arrays.copyOfRange(src, 2, 6));
		message.setCommand(src[6]);
		message.setLength(Arrays.copyOfRange(src, 7, 9));
		int data_length = message.getLength();
		message.setData(Arrays.copyOfRange(src, 9, 9 + data_length));
		message.setChecksum(src[src.length - 1]);
		System.out.println(new Gson().toJson(message));
		out.add(message);
	    } finally
	    {
//		in.resetReaderIndex();
	    }
	}
    }
}
