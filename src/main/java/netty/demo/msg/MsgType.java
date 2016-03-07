package netty.demo.msg;

import netty.demo.exception.UnknownTypeException;

public enum MsgType
{
    Heat_beat(0x00),
    Panel_status(0x01),
    Zone_status(0x02),
    Zwave_control_status(0x03),
    Zwave_device_status(0x04),
    Camera_status(0x05),
    Login(0x06);
    
    private byte value;
    private MsgType(int val){
	this.value = (byte) val;
    }
    public byte getValue()
    {
        return value;
    }
    public void setValue(byte value)
    {
        this.value = value;
    }
    public static MsgType convert(byte command)
    {
	switch (command)
	{
	case 0x00:
	    return MsgType.Heat_beat;
	case 0x01:
	    return MsgType.Panel_status;
	case 0x02:
	    return MsgType.Zone_status;
	case 0x03:
	    return MsgType.Heat_beat;
	case 0x04:
	    return MsgType.Heat_beat;
	case 0x05:
	    return MsgType.Heat_beat;
	case 0x06:
	    return MsgType.Heat_beat;
	default:
	    throw new UnknownTypeException(command, MsgType.class);
	}
    }
}