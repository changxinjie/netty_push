package netty.demo.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SpringJdbc
{
    private static JdbcTemplate jdbcTemplate;

    private static JdbcTemplate initConnect()
    {
	Properties prop = PropertityUtil.loadConfig();
	JdbcTemplate jdbcTemplate = new JdbcTemplate();
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	dataSource.setDriverClassName(prop.getProperty("jdbc.driver"));
	dataSource.setUrl(prop.getProperty("jdbc.url"));
	dataSource.setUsername(prop.getProperty("jdbc.username"));
	dataSource.setPassword(prop.getProperty("jdbc.password"));
	jdbcTemplate.setDataSource(dataSource);
	return jdbcTemplate;
    }

    public static JdbcTemplate getSpringJdbcTemplate()
    {
	JdbcTemplate template = jdbcTemplate == null ? initConnect() : jdbcTemplate;
	return template;
    }

    @Test
    public void test()
    {
	JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
	List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from account_keyholder where account_id_new = 419784");
	for (Map<String, Object> map : mapList)
	{
	    for (Entry<String, Object> entry : map.entrySet())
	    {
		System.out.println(entry.getKey() + "<<>>" + entry.getValue());
	    }
	}
    }
}
