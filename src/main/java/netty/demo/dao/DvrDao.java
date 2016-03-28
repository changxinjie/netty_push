package netty.demo.dao;

import org.json.JSONArray;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import netty.demo.util.SpringJdbc;

public class DvrDao
{
    private static JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
    private static DvrDao deviceDao= new DvrDao();
    private DvrDao(){}
    public static DvrDao instance()
    {
	return deviceDao;
    }
    
    public JSONArray getDvr(Integer account_id_new) throws DataAccessException{
	String sql = "select * from account_dvr where account_id_new = "+account_id_new;
	return new JSONArray(jdbcTemplate.queryForList(sql));
    }
}
