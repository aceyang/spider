package stock.service;


import java.util.*;
import stock.db.StockDB;
import stock.item.StockItem;

public class CommonService {

	private static List<StockItem> StockPoolList = new ArrayList<StockItem>();

	private static HashMap<String, String> StockNameHash = new HashMap<String, String>();
	
	private static HashMap<String, String> VacationHash = new HashMap<String, String>();
	
	static{
		reloadStockPool();
	}
	
	public static  void reloadStockPool() {
		StockDB stockDB = new StockDB();
		StockPoolList = stockDB.getStockPoolList();
		for (StockItem stockItem : StockPoolList)
		{
			StockNameHash.put(stockItem.getStockId(), stockItem.getStockName());
		}
		
	}
	

	
	public static List<StockItem> getStockPoolList() {
		return StockPoolList;
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
}
