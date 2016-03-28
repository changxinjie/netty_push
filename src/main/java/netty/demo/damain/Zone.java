package netty.demo.damain;

import java.util.Set;

public class Zone
{
    private Integer id;
    private Integer area_id;
    private String name;
    private String alarm;
    private String open;
    private String bypass;
    private String tamper;
    private String low_battery;
    private String is_bypass_allowed;
    private String ls_loss_of_supervision;
    private Set<?> histories;
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public Integer getArea_id()
    {
        return area_id;
    }
    public void setArea_id(Integer area_id)
    {
        this.area_id = area_id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getAlarm()
    {
        return alarm;
    }
    public void setAlarm(String alarm)
    {
        this.alarm = alarm==null?"0":alarm;
    }
    public String getOpen()
    {
        return open;
    }
    public void setOpen(String open)
    {
        this.open = open==null?"0":open;
    }
    public String getBypass()
    {
        return bypass;
    }
    public void setBypass(String bypass)
    {
        this.bypass = bypass==null?"0":bypass;;
    }
    public String getTamper()
    {
        return tamper;
    }
    public void setTamper(String tamper)
    {
        this.tamper = tamper==null?"0":tamper;;
    }
    public String getLow_battery()
    {
        return low_battery;
    }
    public void setLow_battery(String low_battery)
    {
	this.low_battery = low_battery==null?"0":low_battery;;
    }
    public String getIs_bypass_allowed()
    {
        return is_bypass_allowed;
    }
    public void setIs_bypass_allowed(String is_bypass_allowed)
    {
        this.is_bypass_allowed = is_bypass_allowed;
    }
    public String getLs_loss_of_supervision()
    {
        return ls_loss_of_supervision;
    }
    public void setLs_loss_of_supervision(String ls_loss_of_supervision)
    {
        this.ls_loss_of_supervision = ls_loss_of_supervision;
    }
    public Set<?> getHistories()
    {
        return histories;
    }
    public void setHistories(Set<?> histories)
    {
        this.histories = histories;
    }
    
}
