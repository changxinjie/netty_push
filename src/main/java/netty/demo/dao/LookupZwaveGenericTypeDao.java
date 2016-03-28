package netty.demo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import netty.demo.util.SpringJdbc;

public class LookupZwaveGenericTypeDao
{
    private static JdbcTemplate	jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
    private static LookupZwaveGenericTypeDao lookupZwaveGenericTypeDao= new LookupZwaveGenericTypeDao();
    private LookupZwaveGenericTypeDao()
    {
    }
    public static LookupZwaveGenericTypeDao instance(){
	return lookupZwaveGenericTypeDao;
    }
    public String getGenericName(Integer generic_type_id){
	String sql = "select name from lookup_zwave_generic_type where id = "+generic_type_id+" limit 1";
	return jdbcTemplate.queryForObject(sql, String.class);
    }
}
