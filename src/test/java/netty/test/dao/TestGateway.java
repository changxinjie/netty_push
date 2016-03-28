package netty.test.dao;

import org.json.JSONObject;
import org.junit.Test;

import netty.demo.dao.GatewayDao;
import netty.demo.dao.PanelDao;

public class TestGateway
{
    private final static Integer accountIdNew = 419793;
    @Test
    public void getGatewayStatus()
    {
	JSONObject jsonObject = GatewayDao.instance().getGatewayStatus(accountIdNew);
	System.out.println(jsonObject.toString());
    }
    @Test
    public void getGatewayParams()
    {
	JSONObject jsonObject = GatewayDao.instance().getGatewayParams(accountIdNew);
	System.out.println(jsonObject.toString());
    }
}
