package stock.db;

import java.util.*;
import javax.sql.rowset.CachedRowSet;
import stock.item.DealItem;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.sql.DBEngine;
import vc.pe.jutil.sql.DBFactory;
import vc.pe.jutil.string.StringUtil;


public class DealStorage {

	private static DBEngine dbengine = DBFactory.getDBEngine("STOCK_DB_Pool");

	private static final Logger debugLog = Logger.getLogger("stock_db");

	
	private static String getSinaDealTable(String date) {
		int month = 0;
		int day = 0;
		String [] s = StringUtil.split(date, "-");
		if ((s != null) && (s.length == 3))
		{
			month = Integer.valueOf(s[1]);
			day = Integer.valueOf(s[2]);
		}
		
		String tableName = "data_sina_" + month + ".deal_" + day;
		
		return tableName;
	}
	
	private static String getSinaSuperDealTable(String date) {
		int month = 0;
		int day = 0;
		String [] s = StringUtil.split(date, "-");
		if ((s != null) && (s.length == 3))
		{
			month = Integer.valueOf(s[1]);
			day = Integer.valueOf(s[2]);
		}
		
		String tableName = "data_sina_" + month + ".super_deal_" + day;
		
		return tableName;
	}

	public static List<DealItem> getDealList(String stockId, String date,
			int source) {
		List<DealItem> dealList = new ArrayList<DealItem>();

		String tableName = getSinaDealTable(date);
		if (source == 0)
		{
			tableName = getSinaSuperDealTable(date);
		}

		try {
			String sql = "select distinct price, quantum, time  from "
					+ tableName + " where stockId = '" + stockId
					+ "' and date = '" + date + "' order by time";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				DealItem dealItem = new DealItem();
				String time = rs.getString("time");
				dealItem.setDate(date);
				dealItem.setPrice(rs.getInt("price"));
				dealItem.setQuantum(rs.getInt("quantum"));
				dealItem.setStockId(stockId);
				dealItem.setTime(time);

				dealList.add(dealItem);
			}
		} catch (Exception e) {
			debugLog.debug("error in DealStorage::getDealList()", e);
		}

		return dealList;
	}


	
	private static String getSinaDealTable(int month, int day) {
		String tableName = "data_sina_" + month + ".deal_" + day;
		
		return tableName;
	}
	
	public static boolean insertSinaDealBatch(int month, int day,List<DealItem> dealList) {

		String tableName = getSinaDealTable(month,day);
		StringBuilder sb = new StringBuilder();
		sb.append("insert into " + tableName + "(stockId,date,time,price,quantum) values ");
		
		int index = 0;
		for (DealItem dealItem : dealList)
		{
			index++;
			sb.append("('" + dealItem.getStockId() + "','" + dealItem.getDate() + "','" + dealItem.getTime() + "','" + dealItem.getPrice() + "','" + dealItem.getQuantum() + "')");
			
			if (index == dealList.size())
			{
				sb.append(";");
			}
			else
			{
				sb.append(",");
			}
		}
		
		try {
			dbengine.executeUpdate(sb.toString());
		} catch (Exception e) {

			debugLog.debug("error in DealStorage::insertSinaDealBatch()", e);

			return false;
		}

		return true;
	}
	
	public static List<DealItem> getSinaDealList(String stockId,int month, int day) {
		List<DealItem> dealList = new ArrayList<DealItem>();

		String tableName = getSinaDealTable(month,day);
		try {
			String sql = "select date,time,price,quantum from "
					+ tableName + " where stockId = '" + stockId
					+ "' order by time";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				DealItem dealItem = new DealItem();
				dealItem.setDate(rs.getString("date"));
				dealItem.setPrice(rs.getInt("price"));
				dealItem.setQuantum(rs.getInt("quantum"));
				dealItem.setStockId(stockId);
				dealItem.setTime(rs.getString("time"));

				dealList.add(dealItem);
			}
		} catch (Exception e) {
			debugLog.debug("error in DealStorage::getSinaDealList()", e);
		}

		return dealList;
	}
	
	private static String getSinaSuperDealTable(int month, int day) {
		String tableName = "data_sina_" + month + ".super_deal_" + day;
		
		return tableName;
	}
	
	public static boolean insertSinaSuperDealBatch(int month, int day,List<DealItem> dealList) {

		String tableName = getSinaSuperDealTable(month,day);
		StringBuilder sb = new StringBuilder();
		sb.append("insert into " + tableName + "(stockId,date,time,price,quantum) values ");
		
		int index = 0;
		for (DealItem dealItem : dealList)
		{
			index++;
			sb.append("('" + dealItem.getStockId() + "','" + dealItem.getDate() + "','" + dealItem.getTime() + "','" + dealItem.getPrice() + "','" + dealItem.getQuantum() + "')");
			
			if (index == dealList.size())
			{
				sb.append(";");
			}
			else
			{
				sb.append(",");
			}
		}
		
		try {
			dbengine.executeUpdate(sb.toString());
		} catch (Exception e) {

			debugLog.debug("error in DealStorage::insertSinaSuperDealBatch()", e);

			return false;
		}

		return true;
	}
	
	public static List<DealItem> getSinaSuperDealList(String stockId,int month, int day) {
		List<DealItem> dealList = new ArrayList<DealItem>();

		String tableName = getSinaSuperDealTable(month,day);
		try {
			String sql = "select date,time,price,quantum from "
					+ tableName + " where stockId = '" + stockId
					+ "' order by time";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				DealItem dealItem = new DealItem();
				dealItem.setDate(rs.getString("date"));
				dealItem.setPrice(rs.getInt("price"));
				dealItem.setQuantum(rs.getInt("quantum"));
				dealItem.setStockId(stockId);
				dealItem.setTime(rs.getString("time"));

				dealList.add(dealItem);
			}
		} catch (Exception e) {
			debugLog.debug("error in DealStorage::getSinaSuperDealList()", e);
		}

		return dealList;
	}
	
}
