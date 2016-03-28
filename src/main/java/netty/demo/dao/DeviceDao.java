package netty.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import netty.demo.damain.Device;
import netty.demo.util.SpringJdbc;

public class DeviceDao
{
    private static JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
    private static DeviceDao deviceDao= new DeviceDao();
    private DeviceDao(){}
    public static DeviceDao instance()
    {
	return deviceDao;
    }
    
    public JSONArray getDevice(Integer account_gateway_id) throws DataAccessException{
	String sql = "SELECT * FROM account_device WHERE account_gateway_id ="+account_gateway_id;
	return new JSONArray(jdbcTemplate.queryForList(sql));
    }
    public class DeviceRowMapper implements RowMapper<Device>{

	@Override
	public Device mapRow(ResultSet rs, int rowNum) throws SQLException
	{
	    Device device = new Device();
	    device.setId(rs.getInt("id"));
	    device.setName(rs.getString("name"));
	    device.setLocation(rs.getString("location"));
	    return device;
	}
	
    }
}
