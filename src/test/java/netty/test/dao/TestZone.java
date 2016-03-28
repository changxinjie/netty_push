package netty.test.dao;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import netty.demo.damain.Zone;
import netty.demo.dao.ZoneDao;

public class TestZone
{
    @Test
    public void zoneList()
    {
	ZoneDao zoneDao2 =ZoneDao.instance();
	List<Zone> zones=  zoneDao2.getZoneList(419784);
	
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("status", "1");
	jsonObject.put("zones", zones);
	System.out.println(jsonObject.toString());
    }
}
