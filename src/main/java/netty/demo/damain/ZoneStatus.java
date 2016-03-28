package netty.demo.damain;

import java.util.Set;

public class ZoneStatus
{
    private Integer status;
    private Set<Zone> zones;
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    public Set<Zone> getZones()
    {
        return zones;
    }
    public void setZones(Set<Zone> zones)
    {
        this.zones = zones;
    }
    
}
