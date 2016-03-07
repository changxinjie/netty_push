package netty.demo.util;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

public class JdbcUtil
{
   static Logger logger = Logger.getLogger(JdbcUtil.class.getName());
    public static JdbcPooledConnectionSource createConnectionSource()
    {
	try
	{
	    Properties properties = PropertityUtil.loadConfig();
	    logger.info(new Gson().toJson(properties));
	    JdbcPooledConnectionSource pooledConnectionSource = new JdbcPooledConnectionSource();
	    pooledConnectionSource.setUrl(properties.getProperty("jdbc.url"));
	    pooledConnectionSource.setUsername(properties.getProperty("jdbc.username"));
	    pooledConnectionSource.setPassword(properties.getProperty("jdbc.password"));
	    pooledConnectionSource.setDatabaseType(new MysqlDatabaseType());
	    pooledConnectionSource.initialize();
	    pooledConnectionSource.setMaxConnectionsFree(Integer.valueOf(properties.getProperty("jdbc.pool.max")));
	    return pooledConnectionSource;
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}
	return null;
    }
}
