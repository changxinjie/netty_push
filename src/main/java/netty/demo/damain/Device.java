package netty.demo.damain;

public class Device
{
    private Integer id;
    private String name;
    private String location;
    private Integer account_gateway_id;
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public Integer getAccount_gateway_id()
    {
        return account_gateway_id;
    }
    public void setAccount_gateway_id(Integer account_gateway_id)
    {
        this.account_gateway_id = account_gateway_id;
    }
    
}
