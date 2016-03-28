package netty.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import netty.demo.damain.Dvr;
import netty.demo.dao.DvrDao;

public class DvrService
{
    private static DvrService dvrService = new DvrService();
    private static DvrDao dvrDao;
    private DvrService()
    {
    }
    public static DvrService instance()
    {
	dvrDao = DvrDao.instance();
	return dvrService;
    }
    public String getDvrInfo(Integer account_id_new){
	JSONArray dvrArr =  dvrDao.getDvr(account_id_new);
	List<Dvr> dvrs = new ArrayList<>();
	for (int i = 0; i < dvrArr.length(); i++)
	{
	    Dvr dvr = new Dvr();
	    JSONObject json = dvrArr.getJSONObject(i);
	    
	}
	return "";
    }
}
