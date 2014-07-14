package stock.service;


import java.util.*;

import com.zgtx.common.client.ProxyItem;

import stock.db.CommonStorage;
import stock.db.StockDB;
import stock.item.StockItem;

public class CommonService {

	private static List<StockItem> StockPoolList = new ArrayList<StockItem>();
	
	private static List<StockItem> CNStockPoolList = new ArrayList<StockItem>();

	private static List<ProxyItem> ProxyList = new ArrayList<ProxyItem>();
	
	private static HashMap<String, String> StockNameHash = new HashMap<String, String>();
	
	private static HashMap<String, String> VacationHash = new HashMap<String, String>();
	
	private static int ProxySeq = 0;
	
	static{
		reloadStockPool();
		reloadProxy();
	}
	
	public static  void reloadStockPool() {
		StockPoolList = StockDB.getStockPoolList();
		for (StockItem stockItem : StockPoolList)
		{
			StockNameHash.put(stockItem.getStockId(), stockItem.getStockName());
		}	
		CNStockPoolList = StockDB.getCNStockPoolList();
		for (StockItem stockItem : CNStockPoolList)
		{
			StockNameHash.put(stockItem.getStockId(), stockItem.getStockName());
		}
	}
	
	public static  void reloadProxy() {
		ProxyList = CommonStorage.getAllProxyList();
	}

	public static List<ProxyItem> getProxyList() {
		return ProxyList;
	}
	
	public static List<StockItem> getStockPoolList() {
		return StockPoolList;
	}

	public static List<StockItem> getCNStockPoolList() {
		return CNStockPoolList;
	}
	
	public static boolean isVacation(String date) {
		// 判断是否为其它假日
		if (VacationHash == null) {
			return false;
		} else {
			if (VacationHash.get(date) == null) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	
	public static String getStockName(String stockId)
	{
		String stockName = "未知";
		
		if (StockNameHash.get(stockId) != null)
		{
			stockName = StockNameHash.get(stockId);
		}
		
		return stockName;
	}
	
	public static ProxyItem getProxy()
	{
		
		if ((ProxyList != null) && (ProxyList.size() > 0))
		{
			for(int i = 1; i <= ProxyList.size(); i++)
			{
				int index = ProxySeq % ProxyList.size();
				ProxySeq++;
				ProxyItem proxyItem = ProxyList.get(index);
				if (proxyItem.getFailTimes() == 0)
				{
					return proxyItem;
				}
				
			}
		}
		
		return null;
	}
}
