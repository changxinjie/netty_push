package netty.demo.util;

import org.apache.commons.lang3.RandomUtils;

public class RandomUtil
{
    public static byte[] buildByteArrays(int length)
    {
	byte[] bytes = new byte[length];
	byte[] byteArr =  RandomUtils.nextBytes(length);
	for (int i = 0; i <length; i++)
	{
	  bytes[i] = (byte) Math.abs(byteArr[i]);
	}
	return bytes;
    }
}

