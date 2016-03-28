package netty.demo.service;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import netty.demo.damain.GatwayCtrl;
import netty.demo.dao.GatewayDao;
import netty.demo.dao.PanelDao;
import netty.demo.util.StringUtil;

public class GatewayService
{
    private static GatewayService gatewayService = new GatewayService();
    private  GatewayService()
    {
    }
    public static GatewayService instance()
    {
	return gatewayService;
    }
    public String getGatewayStatus(Integer account_id)
    {
	String stat = "", status_code = "1", status_message = "";
	String fixed_pin_length = "0", ac_power = "On", system_battery = "Low", interactive_services = "Offline", central_station_connection = "Offline",
		instant_arm = "0", silent_arm = "0", chime_status = "0", support_protest = "0", is_support_chime = "0", is_support_zone_bypass = "0",
		is_support_force_arm = "1", signal_quality = "";
	String[] helix_alarm_type =
	{ "No alarm", "Fire", "Carbon Monoxide", "Panic", "Intrusion", "Tamper", "Auxiliary", "Low Temperature", "Hight Temperature", "Water Level" };
	PanelDao panelDao = PanelDao.instance();
	GatewayDao gatewayDao = GatewayDao.instance();
	 GatwayCtrl gatwayCtrl = new GatwayCtrl();
	try
	{
	    JSONObject status = panelDao.getPanelStauts(account_id);
	    JSONObject gateway = gatewayDao.getGateway(account_id);
	    JSONObject gatewayParamters = gatewayDao.getGatewayParams(account_id);
	    String panel_type = gatewayParamters.getString("panel_type").toUpperCase();
	    if ("RESOLUTION".equals(panel_type))
	    {
		support_protest = "1";
		instant_arm = "1";
		silent_arm = "1";
		is_support_chime = "1";
		is_support_zone_bypass = "1";
	    }
	    if ("SIMON".equals(panel_type))
	    {
		fixed_pin_length = "4";
	    }
	    if ("VISTA".equals(panel_type))
	    {
		is_support_zone_bypass = "1";
	    }
	    if ("GOCONTROL".equals(panel_type))
	    {
		is_support_zone_bypass = "1";
		fixed_pin_length = "4";
		instant_arm = "1";
		is_support_force_arm = "0";
	    }
	    if ("DSC".equals(panel_type))
	    {
		is_support_force_arm = "0";
		fixed_pin_length = "4";
	    }
	    if ("CADDX".equals(panel_type))
	    {
		is_support_zone_bypass = "0";
	    }
	    if ("CONCORD".equals(panel_type))
	    {
		instant_arm = "1";
		support_protest = "1";
		is_support_force_arm = "0";
	    }
	    String param_hw = gatewayParamters.has("hw") ? gatewayParamters.getString("hw").toUpperCase() : "";
	    if (param_hw.equals("CDMA") || param_hw.contains("GSM"))
	    {
		String cell_type = "GSM";
		if (param_hw.equals("CDMA"))
		    cell_type = "CDMA";
		if (param_hw.contains("GSM_3G"))
		    cell_type = "GSM_3G";
		int cell_signal =gateway.has("cell_signal")? gateway.getInt("cell_signal"):0;
		if (cell_signal < 12)
		    signal_quality = cell_type + " Signal Strength Poor (" + cell_signal + ")";
		else if (cell_signal < 17)
		    signal_quality = cell_type + " Signal Strength Good (" + cell_signal + ")";
		else
		    signal_quality = cell_type + " Signal Strength Excellent (" + cell_signal + ")";
		signal_quality = cell_signal + "";
	    }
	    if (panel_type.equals("RESOLUTION") && status.has("alarm_type") && status.getInt("alarm_type") < 10)
	    {
		int alarm_type_int = status.getInt("alarm_type");
		status.put("alarm_type", helix_alarm_type[alarm_type_int]);
	    }
	   
	    ac_power = status.has("ac_failure") && !status.getBoolean("ac_failure") ? "On" : "Off";
	    system_battery = status.has("low_battery") && !status.getBoolean("low_battery") ? "Ok" : "Low";
	    interactive_services = status.has("server_comm_fail") && !status.getBoolean("server_comm_fail") ? "Online" : "Offline";
	    central_station_connection = status.has("cs_comm_fail") && !status.getBoolean("cs_comm_fail") ? "Online" : "Offline";
	    instant_arm = status.has("instant_arm") && status.getInt("instant_arm") == 1 ? "1" : instant_arm;
	    silent_arm = status.has("silent_arm") && status.getInt("silent_arm") == 1 ? "1" : silent_arm;
	    chime_status =status.has("chime_mode_enabled") && status.getBoolean("chime_mode_enabled") ? "1" : chime_status;
	    String arming_level = status.getString("arming_level");
	    switch (arming_level)
	    {
	    case "1":
		arming_level = "Disarmed";
		break;
	    case "2":
		arming_level = "Armed Stay";
		break;
	    case "3":
		if ("RESOLUTION".equals(panel_type))
		{
		    arming_level = "Night Arm";
		} else
		{
		    arming_level = "Armed Away";
		}
		break;
	    case "4":
		arming_level = "Armed Away";
		break;
	    case "5":
		arming_level = "All Off";
		break;
	    case "armed_stay":
		arming_level = "Armed Stay";
		break;
	    case "armed_away":
		arming_level = "Armed Away";
		break;
	    case "disarmed":
		arming_level = "Disarmed";
		break;
	    }
	    if (!StringUtil.isEmpty(signal_quality))
	    {
		status.put("signal_quality", signal_quality);
	    }
	    if (status.has("alarm")&&( status.getBoolean("alarm") || status.getString("alarm") == "1"))
	    {
		stat = "alarm";
	    } else if (status.has("exit_delay") && status.getBoolean("exit_delay"))
	    {
		stat = "exit_delay";
	    } else if (status.has("entry_delay") && status.getBoolean("entry_delay"))
	    {
		stat = "entry_delay";
	    } else if (!StringUtil.isEmpty(arming_level))
	    {
		stat = gatwayCtrl.getArming_level();
	    } else
	    {
		stat = "unknown";
		status_code = "0";
		status_message = "arming level unknown";
	    }
	    gatwayCtrl.setStatus(stat);
	    gatwayCtrl.setStatus_code(status_code);
	    gatwayCtrl.setStatus_message(status_message);
	    gatwayCtrl.setArming_level(arming_level);
	    gatwayCtrl.setAc_power(ac_power);
	    gatwayCtrl.setSystem_battery(system_battery);
	    gatwayCtrl.setInteractive_services(interactive_services);
	    gatwayCtrl.setCentral_station_connection(central_station_connection);
	    gatwayCtrl.setInstant_arm(instant_arm);
	    gatwayCtrl.setSilent_arm(silent_arm);
	    gatwayCtrl.setChime_status(chime_status);
	    gatwayCtrl.setSupport_protest(support_protest);
	    gatwayCtrl.setAlarm_type_name(panel_type);
	    gatwayCtrl.setIs_support_chime(is_support_chime);
	    gatwayCtrl.setIs_support_zone_bypass(is_support_zone_bypass);
	    gatwayCtrl.setFixed_pin_length(fixed_pin_length);
	    gatwayCtrl.setIs_support_force_arm(is_support_force_arm);
	    gatwayCtrl.setPanel_status(status);
	} catch (DataAccessException e)
	{
	    status_code = "0";
	    status_message = "";
	    gatwayCtrl.setStatus("");
	    gatwayCtrl.setStatus_code(status_code);
	    gatwayCtrl.setStatus_message(status_message);
	}
	return new JSONObject(gatwayCtrl).toString();
    }
}
