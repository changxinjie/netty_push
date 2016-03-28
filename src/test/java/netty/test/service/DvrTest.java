package netty.test.service;

import org.json.JSONObject;
import org.junit.Test;

import netty.demo.service.DeviceService;

public class DvrTest
{
    private static DeviceService deviceService = DeviceService.instance();
    private final static Integer accountIdNew = 419168;
    @Test
    public void getDvrStatus()
    {
	JSONObject jsonObject = deviceService.getDeviceStatus(accountIdNew);
	System.out.println(jsonObject.toString());
    }
}
