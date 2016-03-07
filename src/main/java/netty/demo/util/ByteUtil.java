package netty.demo.util;

import java.util.Arrays;

public class ByteUtil
{
    public static byte[] combine(byte[] src1,byte[]... src2)
    {
	int all_length = src1.length;
	int offset = src1.length;
	for (byte[] bs : src2)
	{
	    if (bs==null) continue;
	    all_length+=bs.length;
	}
	byte[] result = Arrays.copyOf(src1, all_length);
	for (byte[] bs : src2)
	{
	    if (bs==null) continue;
	    System.arraycopy(bs, 0, result, offset, bs.length);
	    offset+=bs.length;
	}
	return result;
    }
}
