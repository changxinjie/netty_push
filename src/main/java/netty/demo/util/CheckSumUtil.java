package netty.demo.util;

public class CheckSumUtil
{
    public static byte build(byte[] src)
    {
	byte checksum = (byte) 0xff;
	for (int i = 0; i < src.length; i++)
	{
	    checksum ^= src[i];
	}
	return checksum;
    }
    public static boolean check(byte[] data)
    {
	System.out.println("checksum begin--->");
	byte checksum = (byte) 0xff;
	for (int i = 0; i < data.length -1; i++)
	{
	    System.out.print(data[i]);
	    checksum ^= data[i];
	}
	System.out.println("end--------->");
	boolean result = checksum == data[data.length-1];
	System.out.println("check result\t"+result);
	return result;
    }
    
    public static void main(String[] args)
    {
	byte[] arr = new byte[]{1,2,3,4,5,6};
	byte checksum = build(arr);
	System.out.println(checksum);
	arr = ByteUtil.combine(arr,new byte[]{checksum});
	for (byte b : arr)
	{
	    System.out.print(b);
	}
	System.out.println(check(arr));
    }
}
