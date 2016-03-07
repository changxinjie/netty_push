package netty.demo.msg;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.sun.corba.se.impl.ior.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import netty.demo.util.ByteUtil;
import netty.demo.util.ThreeDes;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.util.logging.resources.logging;

/**
 * the message to send to app client.
 * 
 * @author changxinjie
 */
public class Message implements Serializable
{
    private static final long serialVersionUID = 1L;
    private byte[] header;	// 0x00 for plain text,0x01 cipher text
    private byte[] device_token_id = {0,0,0,0};
    private byte[] length;
    private byte checksum;
    private byte command;
    private byte[] data;
    
    int all_length = 0;

    /**
     * Push Message to app client.
     * 
     * @param isPlain
     * @param data_version
     * @param device_token_id
     * @param command
     * @param json_data
     * @param device_token
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public Message(int isPlain, int data_version, int command, String json_data, String device_token)
	    throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
    {
	this.header = new byte[]
	{ (byte) isPlain, (byte) data_version };

	this.command = (byte) command;
	if (isPlain == 0x01)	
	{	//密文
	    byte[] en_data = ThreeDes.encrypt(new BASE64Decoder().decodeBuffer(device_token), json_data.getBytes());
	    this.data = en_data;
	    this.length = new byte[]
	    { (byte) (en_data.length >> 8), (byte) (en_data.length % 256) };
	    all_length = en_data.length + 10;
	} else
	{	//明文
	    this.data = json_data.getBytes();
	    this.length = new byte[]
	    { (byte) (json_data.getBytes().length >> 8), (byte) (json_data.getBytes().length % 256) };
	    all_length = json_data.getBytes().length + 10;
	}
	//TODO 奇校验
	this.checksum = (byte) (all_length % 2);
    }

    /**
     * Push message from snip client
     * 明文传输
     * @param command
     * @param json_data
     */
    public Message(byte data_version, byte command, String json_data)
    {
	this.header = new byte[]{ 0, data_version };
	this.command = command;
	this.data = json_data.getBytes();
	this.length = new byte[]
	{ (byte) (json_data.getBytes().length >> 8), (byte) (json_data.getBytes().length % 256) };
	all_length = json_data.getBytes().length + 10;
	this.checksum = (byte) (all_length % 2);
    }

    /**
     * ping message from client both snip and app
     * 明文
     * @param isPlain
     * @param data_version
     * @param command
     * @param json_data
     */
    public Message(int data_version)
    {
	this.header = new byte[]{0, (byte) data_version };
	this.command = 0;
	this.length = new byte[]{ 0,0 };
	all_length = 6;
	//TODO 奇校验
	this.checksum = (byte) (all_length % 2);
    }

    /**
     * login message for app client
     * 
     * @param header
     * @param device_token_id
     * @param command
     */
    public Message(int isPlain, int data_version, Integer device_token_id, byte command)
    {
	this.header = new byte[]{ (byte) isPlain, (byte) data_version };
	byte[] bytes = new byte[4];
	bytes[0] = (byte) (device_token_id >> 24);
	bytes[1] = (byte) (device_token_id >> 16);
	bytes[2] = (byte) (device_token_id >> 8);
	bytes[3] = (byte) (device_token_id % 256);
	this.device_token_id = bytes;
	this.length = new byte[]{ 0,0 };
	this.command = command;
	//TODO 奇校验
	this.checksum = 0;
    }

    public byte[] getHeader()
    {
	return header;
    }

    public Integer getDevice_token_id()
    {
	if (this.device_token_id == null)
	{
	    return null;
	}
	byte[] _device_token_id = this.device_token_id;
	return (_device_token_id[0] << 24) + (_device_token_id[1] << 16) + (_device_token_id[2] << 8) + _device_token_id[3];
    }

    public void setDevice_token_id(Integer device_token_id)
    {
	byte[] bytes = new byte[4];
	bytes[0] = (byte) (device_token_id >> 24);
	bytes[1] = (byte) (device_token_id >> 16);
	bytes[2] = (byte) (device_token_id >> 8);
	bytes[3] = (byte) (device_token_id % 256);
	this.device_token_id = bytes;
    }

    public int getLength()
    {
	return (length[0] << 8) + length[1];
    }
    /**
     * 密文数据
     * @param device_token
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String getData(byte[] device_token)
	    throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
	return new String(ThreeDes.decrypt(device_token, this.data));
    }
    
    /**
     * 
     * @return
     */
    public byte[] build()
    {
	return ByteUtil.combine(header, device_token_id,new byte[]{command},length,data);
    }
    /**
     * data from snip client
     * @param src
     * @return
     */
    public static Message parse(byte[] src){
	//TODO validate
	byte command = src[6];
	byte[] device_token_id = Arrays.copyOfRange(src, 2,6);
	int length = (src[7] << 8) + src[8];
	byte[] data = Arrays.copyOfRange(src, 9,9+length);
	return new Message(command,device_token_id,data);
    }
    /**
     * parsed Data
     * @param command
     * @param data
     */
    private Message(int command,byte[] device_token_id,byte[] data){
	this.command = (byte) command;
	this.device_token_id = device_token_id;
	this.data = data;
    }
    public Message()
    {
    }

    public byte getChecksum()
    {
	return checksum;
    }

    public MsgType getMsgType()
    {
	return MsgType.convert(command);
    }
    /**
     * 明文数据
     * @return
     */
    public String getData()
    {
	return new String(this.data);
    }

    public byte getCommand()
    {
	return command;
    }

    public void setHeader(byte[] header)
    {
        this.header = header;
    }

    public void setDevice_token_id(byte[] device_token_id)
    {
        this.device_token_id = device_token_id;
    }

    public void setLength(byte[] length)
    {
        this.length = length;
    }

    public void setChecksum(byte checksum)
    {
        this.checksum = checksum;
    }

    public void setCommand(byte command)
    {
        this.command = command;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }
    
}
