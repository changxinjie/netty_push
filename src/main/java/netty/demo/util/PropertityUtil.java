package netty.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import netty.demo.exception.ResourceNotFoundException;

public class PropertityUtil
{
    private static Properties properties;
    
    public static Properties loadPropertity(String fileName) throws ResourceNotFoundException
    {
	properties = new Properties();
	InputStream inputStream = Object.class.getResourceAsStream("/" + fileName);
	try
	{
	    properties.load(inputStream);
	} catch (IOException e)
	{
	    throw new ResourceNotFoundException(fileName);
	}
	return properties;
    }

    public static Properties loadConfig()
    {
	try
	{
	    if (properties==null)
	    {
		properties = loadPropertity("config.properties");
	    }
	    return properties;
	} catch (ResourceNotFoundException e)
	{
	    e.printStackTrace();
	}
	return null;
    }
}
