package netty.test.dao;

import org.junit.Test;

import netty.demo.service.GatewayService;

public class TestPanelStatus
{
    @Test
    public void getPanelStatus()
    {
	String jsonObject = GatewayService.instance().getGatewayStatus(419793);
	System.out.println("response-->"+jsonObject.toString());
    }
}
