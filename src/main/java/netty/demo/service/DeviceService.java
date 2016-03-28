package netty.demo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import netty.demo.damain.Dvr;
import netty.demo.dao.DeviceDao;
import netty.demo.dao.GatewayDao;
import netty.demo.dao.LookupZwaveGenericTypeDao;
import netty.demo.util.JsonUtil;
import netty.demo.util.SpringJdbc;
import netty.demo.util.StringUtil;

public class DeviceService
{
    private static JdbcTemplate jdbcTemplate = SpringJdbc.getSpringJdbcTemplate();
    private static DeviceDao deviceDao = DeviceDao.instance();
    private static GatewayDao gatewayDao = GatewayDao.instance();
    private static LookupZwaveGenericTypeDao lookupZwaveGenericTypeDao = LookupZwaveGenericTypeDao.instance();
    
    private static DeviceService deviceService = new DeviceService();
    public static DeviceService instance()
    {
	return deviceService;
    }
    private DeviceService()
    {
    }
    public JSONObject getDeviceStatus(Integer account_id_new)
    {
	JSONObject result = new JSONObject();
	result.put("status", "1");
	JSONArray nodes = new JSONArray();
	JSONObject gateway = gatewayDao.getGateway(account_id_new);
	JSONArray deviceArr = deviceDao.getDevice(gateway.getInt("id"));
	for (int i = 0; i < deviceArr.length(); i++)
	{
	    Dvr node = new Dvr();
	    JSONObject device = deviceArr.getJSONObject(i);
	    String zwave_type = getGenericTypeName(device);

	    int device_manufacturer_id;
	    String device_product_type = "";
	    int device_product_id;

	    if (StringUtil.isEmpty(zwave_type) || "GENERIC_CONTROLLER".equals(zwave_type) || "STATIC_CONTROLLER".equals(zwave_type) || "1".equals(zwave_type))
	    {
	    } else
	    {
		device_manufacturer_id = device.getInt("manufacturer_id");
		device_product_type = device.getString("product_type");
		device_product_id = device.getInt("product_id");

		String product_manufacturer_product_type_name = getZwaveType(device_manufacturer_id, device_product_type, device_product_id);
		node.setId(JsonUtil.toString(device, "id"));
		node.setName(JsonUtil.toString(device, "name"));
		node.setLevel(JsonUtil.toString(device, "basic_level", "null"));
		node.setGeneric_type(JsonUtil.toString(device, "", "null"));
		node.setSpecific_type(JsonUtil.toString(device, "", "null"));
		node.setProduct_manufacturer_id(device_manufacturer_id + "");
		node.setProduct_type_id(device_product_type);
		node.setProduct_product_id(device_product_id + "");
		node.setProduct_manufacturer_name(getZwaveManufacturer(device_manufacturer_id));
		node.setProduct_product_name(getZwaveProduct(device_manufacturer_id, device_product_type, device_product_id));
		node.setProduct_manufacturer_product_type_name(
			product_manufacturer_product_type_name == null ? "null" : product_manufacturer_product_type_name);
		String command_all = device.getString("");
		node.setCommands(command_all);
		if (!StringUtil.isEmpty(command_all))
		{
		    if (command_all.matches("/^.*,50,.*$/i") || command_all.matches("/^50,.*/i"))
		    {
			node.setMeter(true);
		    }
		}

	    }
	    if ("SIREN".equals(zwave_type))
	    {
		node.setName(StringUtil.isEmpty(node.getName())?"Siren "+node.getId():node.getName());
	    }
	    if ("SWITCH_MULTILEVEL".equals(zwave_type)||"DIMMER".equals(zwave_type))
	    { 
		node.setType("SWITCH_MULTILEVEL");
		node.setName(StringUtil.isEmpty(node.getName())?"Light "+node.getId():node.getName());
	    }else if ("SWITCH_MULTILEVEL".equals(zwave_type)||"DIMMER".equals(zwave_type)) {
		node.setType("SWITCH_BINARY");
		node.setName(StringUtil.isEmpty(node.getName())?"Switch "+node.getId():node.getName());
	    }else if ("ENTRY_CONTROL".equals(zwave_type)||"LOCK".equals(zwave_type)) {
		node.setType("ENTRY_CONTROL");
		node.setName(StringUtil.isEmpty(node.getName())?"Lock "+node.getId():node.getName());
	    }else if ("THERMOSTAT".equals(zwave_type)){
		node.setType("THERMOSTAT");
		node.setName(StringUtil.isEmpty(node.getName())?"Thermostat "+node.getId():node.getName());
		node.setOperating_state(JsonUtil.toString(device, "","null"));
		node.setFan_mode(JsonUtil.toString(device, "", "null"));
		node.setMode(JsonUtil.toString(device, "", "null"));
		node.setFan_state(JsonUtil.toString(device, "", "null"));
		node.setUom(JsonUtil.toString(device, "", "null"));
		
		JSONObject current_temperature = new JSONObject();
		current_temperature.put("uom", JsonUtil.toString(device, "", "null"));
		current_temperature.put("temperature", JsonUtil.toString(device, "", "null"));
		node.setCurrent_temperature(current_temperature);
		
		JSONObject set_point = new JSONObject();
		JSONObject point1 = new JSONObject();
		point1.put("@attributes",new JSONObject("type:'HEAT'"));
		point1.put("type", "HEAT");
		point1.put("uom", JsonUtil.toString(device, "temp_uom", "null"));
		point1.put("temperature", JsonUtil.toString(device, "temp_current", "null"));
		set_point.append("set_point", point1);
		
		JSONObject point2 = new JSONObject();
		point2.put("@attributes", new JSONObject("type:'COOL'"));
		point2.put("type", "COOL");
		point2.put("uom", JsonUtil.toString(device, "temp_uom", "null"));
		point2.put("temperature", JsonUtil.toString(device, "temp_target_cool", "null"));
		set_point.append("set_point", point2);
		JSONObject point3 = new JSONObject();
		point3.put("@attributes", new JSONObject("type:'HEAT_SAVE'"));
		point3.put("type", "HEAT_SAVE");
		point3.put("uom", JsonUtil.toString(device, "temp_uom", "null"));
		point3.put("temperature", JsonUtil.toString(device, "temp_target_heat_save", "null"));
		set_point.append("set_point", point3);
		JSONObject point4 = new JSONObject();
		point4.put("@attributes", new JSONObject("type:'COOL_SAVE'"));
		point4.put("type", "COOL_SAVE");
		point4.put("uom", JsonUtil.toString(device, "temp_uom", "null"));
		point4.put("temperature", JsonUtil.toString(device, "temp_target_cool_save", "null"));
		set_point.append("set_point", point4);
		
		node.setCurrent_temperature(current_temperature);
		
	    }else if ("SENSOR_BINARY".equals(zwave_type)){
		node.setType("SENSOR_BINARY");
		node.setName(StringUtil.isEmpty(node.getName())?"Sensor "+node.getId():node.getName());
		JSONObject current_temperature = new JSONObject();
		current_temperature.put("uom", JsonUtil.toString(device, "", "null"));
		current_temperature.put("temperature", JsonUtil.toString(device, "", "null"));
		node.setCurrent_temperature(current_temperature);
	    }else if ("SENSOR_ALARM".equals(zwave_type)){
		node.setType("SENSOR_ALARM");
		node.setName(StringUtil.isEmpty(node.getName())?"Alarm Sensor "+node.getId():node.getName());
	    }else{
		node.setType(zwave_type);
		node.setName(StringUtil.isEmpty(node.getName())?"Device "+node.getId():node.getName());
	    }
	    node.setBattery_level(JsonUtil.toString(device, ""));
	    node.setOut_of_range(JsonUtil.toString(device, ""));
	    node.setHumidity(JsonUtil.toString(device, "humidity"));
	    node.setHumidity_uom(JsonUtil.toString(device, "humidity_uom"));
	    node.setLuminance(JsonUtil.toString(device, "luminance"));
	    node.setLuminance_uom(JsonUtil.toString(device, "luminance_uom"));
	    
	    nodes.put(0, new JSONObject("{@attributes:{index:'1'}}"));
	    nodes.put(new JSONObject(node));
	}
	result.put("payload", new JSONObject().put("nodes", nodes));
	return result;
    }
    private JSONArray getDeviceLockCodes(int gatewayId, String deviceId)
    {
	String sql = "SELECT lock_code_id as id, name, code FROM account_device_lock_code WHERE account_gateway_id = ? and account_device_id = ?";
	
	return new JSONArray(jdbcTemplate.queryForList(sql, new Object[]{gatewayId,deviceId}));
    }

    private String getZwaveProduct(int device_manufacturer_id, String device_product_type, int device_product_id)
    {
	String sql = "SELECT product_name FROM zwave_manufacturer_product WHERE manufacturer_id = ? and product_type = ? and product_id = ? LIMIT 1;";
	return jdbcTemplate.queryForObject(sql, new Object[]
	{ device_manufacturer_id, device_product_type, device_product_id }, String.class);
    }

    private String getZwaveManufacturer(int device_manufacturer_id)
    {
	String sql = "SELECT name FROM zwave_manufacturer WHERE id = " + device_manufacturer_id + " LIMIT 1";
	return jdbcTemplate.queryForObject(sql, String.class);
    }

    private String getZwaveType(int device_manufacturer_id, String device_product_type, int device_product_id)
    {
	String sql = "SELECT type FROM zwave_manufacturer_product WHERE manufacturer_id = ? and product_type = ? and product_id = ? LIMIT 1";
	String type = jdbcTemplate.queryForObject(sql, new Object[]{device_manufacturer_id, device_product_type, device_product_id }, String.class);
	return type;
    }

    private String getGenericTypeName(JSONObject device)
    {
	String generic_type_name = lookupZwaveGenericTypeDao.getGenericName(device.getInt("generic_type"));
	String specific_type = device.get("specific_type").toString();

	Integer device_manufacturer_id = device.has("")&& device.isNull("") ?0: device.getInt("");
	String device_product_type = device.getString("product_type");
	int device_product_id = device.getInt("product_id");
	String sql_0 = "SELECT type FROM zwave_manufacturer_product WHERE manufacturer_id = ? and product_type = ? and product_id = ? LIMIT 1";
	String product_manufacturer_product_type_name = jdbcTemplate.queryForObject(sql_0, new Object[]
	{ device_manufacturer_id, device_product_type, device_product_id }, String.class);
	if (product_manufacturer_product_type_name.matches("/.*siren.*/i"))
	{
	    generic_type_name = "SIREN";
	}
	if (!StringUtil.isEmpty(specific_type))
	{
	    if ("ENTRY_CONTROL".equals(generic_type_name) && (("5".equals(specific_type)) || ("6".equals(specific_type)) || ("7".equals(specific_type))
		    || ("8".equals(specific_type)) || ("9".equals(specific_type))))
	    {
		generic_type_name = "ENTRY_CONTROL_GARAGE_DOOR";
	    }
	}
	return generic_type_name;
    }

}
