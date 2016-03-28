package netty.demo.dao;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import netty.demo.util.SpringJdbc;
import netty.demo.util.StringUtil;

public class PanelDao
{
    private static PanelDao panelDao = new PanelDao();
    private static JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();

    private PanelDao()
    {
    }

    public static PanelDao instance()
    {
	return panelDao;
    }

    public JSONObject getPanelStauts(Integer account_id_new)
    {
	try
	{
	    String sql = "SELECT STATUS FROM account_panel WHERE account_id_new =" + account_id_new;
	    String string = jdbcTemplate.queryForObject(sql, String.class);
	    Map<String, String> map = StringUtil.convertParasm2Map(string);
	    return new JSONObject(map);
	} catch (EmptyResultDataAccessException e)
	{
	    return null;
	}
    }
}
