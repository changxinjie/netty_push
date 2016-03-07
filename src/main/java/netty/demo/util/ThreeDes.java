package netty.demo.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.SingletonMap;

import com.google.gson.Gson;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ThreeDes
{

    private static final String Algorithm = "DESede"; // DES,DESede,Blowfish,etc

    static
    {
	Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

    public static byte[] encrypt(byte[] keybyte, byte[] src)
	    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
	System.out.print("src bytes "+src.length+" arr-->");
	for (byte b : src)
	{
	    System.out.print(b+",");
	}
	System.out.print("\nkeybyte bytes "+keybyte.length+" arr-->");
	for (byte b : keybyte)
	{
	    System.out.print(b+",");
	}
	System.out.print("\n");
	SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
	Cipher c1 = Cipher.getInstance(Algorithm);
	c1.init(Cipher.ENCRYPT_MODE, deskey);
	return c1.doFinal(src);
    }

    public static byte[] decrypt(byte[] keybyte, byte[] src)
	    throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
    {
	SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
	Cipher c1 = Cipher.getInstance(Algorithm);
	c1.init(Cipher.DECRYPT_MODE, deskey);
	return c1.doFinal(src);
    }

    public static void main(String[] args)
	    throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
    {

	final byte[] keyBytes = RandomUtil.buildByteArrays(24);  // 24字节的密钥
	Map<String, Object> map = new HashMap<String,Object>();
	map.put("account_id_new", 419784);
	map.put("account_keyholder", 0);
	map.put("zone",new SingletonMap("id", 2) );
	String szSrc = new Gson().toJson(map);
	System.out.println("加密前的字符串:" + szSrc+"\t"+szSrc.getBytes().length);
	byte[] encoded = encrypt(keyBytes, szSrc.getBytes());
	System.out.println("加密后的字符串:" + new String(encoded));
	
	String encode64 = new BASE64Encoder().encode(encoded);
	System.out.println("Base64 encode--->"+encode64);
	
	byte[] srcBytes = decrypt(keyBytes, new BASE64Decoder().decodeBuffer(encode64));
	System.out.println("解密后的字符串:" + new String(srcBytes));
    }
}