package netty.demo.dao;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import netty.demo.util.SpringJdbc;
import netty.demo.util.StringUtil;

public class GatewayDao
{
    private static JdbcTemplate	jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
    private static GatewayDao gatewayDao= new GatewayDao();
    private GatewayDao()
    {
    }
    public static GatewayDao instance(){
	return gatewayDao;
    }
    public JSONObject getGateway(Integer account_id_new) throws DataAccessException{
	String sql = "select * from account_gateway where account_id_new = "+account_id_new+" limit 1";
	    return new JSONObject(jdbcTemplate.queryForMap(sql));
    }
    public JSONObject getGatewayStatus(Integer account_id_new){
	String sql = "SELECT STATUS FROM account_gateway WHERE account_id_new ="+account_id_new+" limit 1";
	String string = jdbcTemplate.queryForObject(sql, String.class);
	Map<String, String> map = StringUtil.convertParasm2Map(string);
	return new JSONObject(map);
    }
    
    public JSONObject getGatewayParams(Integer account_id_new){
	String sql = "SELECT parameters FROM account_gateway WHERE account_id_new ="+account_id_new+" limit 1";
	String string = jdbcTemplate.queryForObject(sql, String.class);
	Map<String, String> map = StringUtil.convertParasm2Map(string);
	return new JSONObject(map);
    }
    public static void main(String[] args)
    {
	System.out.println(GatewayDao.instance().getGateway(419793).toString());
    }
}
