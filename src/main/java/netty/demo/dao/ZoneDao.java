package netty.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import netty.demo.damain.Zone;
import netty.demo.util.SpringJdbc;

public class ZoneDao
{
    private static JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();

    private static ZoneDao zoneDao = new ZoneDao();

    private ZoneDao()
    {
    }

    public static ZoneDao instance()
    {
	return zoneDao;
    }

    public List<Zone> getZoneList(Integer account_id_new)
    {
	String sql = "SELECT * FROM account_zone WHERE account_id_new = " + account_id_new
		+ " AND ( account_zone.id >= '1' AND account_zone.id <= '99' ) AND (is_active(account_zone.active_from_date, account_zone.inactive_from_date))";
	try
	{
	    return jdbcTemplate.query(sql, new ZoneRowmapper());
	} catch (EmptyResultDataAccessException e)
	{
	    return new ArrayList<>();
	}
    }

    public class ZoneRowmapper implements RowMapper<Zone>
    {

	@Override
	public Zone mapRow(ResultSet rs, int rowNum) throws SQLException
	{
	    Zone zone = new Zone();
	    zone.setId(rs.getInt("id"));
	    zone.setArea_id(rs.getInt("account_area_id"));
	    zone.setName(rs.getString("name"));
	    zone.setAlarm(rs.getString("alarm"));
	    zone.setOpen(rs.getString("open"));
	    String bypass = rs.getString("bypass");
	    bypass = bypass == null ? "0" : bypass;
	    zone.setBypass(bypass);
	    zone.setIs_bypass_allowed(bypass);
	    zone.setTamper(rs.getString("tamper"));
	    zone.setLow_battery(rs.getString("low_battery"));
	    String loss_of_supervision = rs.getString("loss_of_supervision");
	    loss_of_supervision = loss_of_supervision == null ? "0" : "1";
	    zone.setLs_loss_of_supervision(loss_of_supervision);
	    return zone;
	}

    }
}
