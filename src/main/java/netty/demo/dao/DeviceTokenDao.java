package netty.demo.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import netty.demo.damain.DeviceToken;
import netty.demo.util.JdbcUtil;

public class DeviceTokenDao extends BaseDaoImpl<DeviceToken, Integer>
{
    private static DeviceTokenDao deviceTokenDao;
    public static DeviceTokenDao newInstance()
    {
	try
	{
	    if (deviceTokenDao==null)
	    {
		deviceTokenDao = new DeviceTokenDao(JdbcUtil.createConnectionSource(), DeviceToken.class);
	    }
	    return deviceTokenDao;
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    public DeviceTokenDao(ConnectionSource connectionSource, Class<DeviceToken> dataClass) throws SQLException
    {
	super(connectionSource, dataClass);
    }

    public List<DeviceToken> findDevice(Integer accountId, Integer keyholderId) throws SQLException
    {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("account_id_new", accountId);
	map.put("account_keyholder_id", keyholderId);
	return this.queryForFieldValues(map);
    }
}
