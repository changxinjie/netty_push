package netty.demo.damain;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="account_keyholder_device_access_token")
public class DeviceToken implements Serializable
{
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    private Integer id;
    @DatabaseField(columnName="account_id_new",canBeNull = false)
    private Integer accountIdNew;
    @DatabaseField(columnName="account_keyholder_id",canBeNull = false)
    private Integer accountKeyholderId;
    @DatabaseField(columnName="app_id",canBeNull = false)
    private Integer appId;
    @DatabaseField(columnName="app_version",canBeNull = false)
    private String appVersion;
    @DatabaseField(columnName="created_time")
    private Long createdTime;
    @DatabaseField(columnName="device_key")
    private String deviceKey;
    @DatabaseField(columnName="device_token")
    private String deviceToken;
    @DatabaseField(columnName="device_type")
    private String deviceType;

    public Integer getId()
    {
	return id;
    }

    public void setId(Integer id)
    {
	this.id = id;
    }

    public Integer getAccountIdNew()
    {
	return accountIdNew;
    }

    public void setAccountIdNew(Integer accountIdNew)
    {
	this.accountIdNew = accountIdNew;
    }

    public Integer getAccountKeyholderId()
    {
	return accountKeyholderId;
    }

    public void setAccountKeyholderId(Integer accountKeyholderId)
    {
	this.accountKeyholderId = accountKeyholderId;
    }

    public Integer getAppId()
    {
	return appId;
    }

    public void setAppId(Integer appId)
    {
	this.appId = appId;
    }

    public String getAppVersion()
    {
	return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
	this.appVersion = appVersion;
    }

    public Long getCreatedTime()
    {
	return createdTime;
    }

    public void setCreatedTime(Long createdTime)
    {
	this.createdTime = createdTime;
    }

    public String getDeviceKey()
    {
	return deviceKey;
    }

    public void setDeviceKey(String deviceKey)
    {
	this.deviceKey = deviceKey;
    }

    public String getDeviceToken()
    {
	return deviceToken;
    }

    public void setDeviceToken(String bs)
    {
	this.deviceToken = bs;
    }

    public String getDeviceType()
    {
	return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
	this.deviceType = deviceType;
    }
}