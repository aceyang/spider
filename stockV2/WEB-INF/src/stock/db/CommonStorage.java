package stock.db;

import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.sql.DBEngine;
import vc.pe.jutil.sql.DBFactory;
import vc.pe.jutil.string.StringUtil;
import com.zgtx.common.client.ProxyItem;
import com.zgtx.common.client.Top10StockHolderItem;

public class CommonStorage {

	private static DBEngine dbDBEngine = DBFactory.getDBEngine("STOCK_DB_Pool");
	
	private static final Logger sqlLog = Logger.getLogger("common_storage_sql");

	private static final Logger errorLog = Logger.getLogger("common_storage_error");
	
	public static int addProxy(String ip,int port, String remark){
		int ret = 0;
		try {
			String sql = "insert into zgtx.proxy set ip = '" + StringUtil.encodeSQL(ip)
					+ "',port = '" + port 
					+ "',remark = '" + StringUtil.encodeSQL(remark)
					+ "',add_time = now();";
			sqlLog.info(sql);
			dbDBEngine.executeUpdate(sql);
		} catch (Exception e) {
			ret = -1000;
			errorLog.error("error in addProxy()", e);
		}
		
		return ret;
	}
	
	private static ProxyItem buildProxyItem(CachedRowSet rs) {
		if (rs == null) {
			return null;
		}

		try {
			ProxyItem item = new ProxyItem();
			item.setId(rs.getInt("id"));
			item.setAddTime(rs.getString("add_time"));
			item.setFailTimes(rs.getInt("fail_times"));
			item.setIp(rs.getString("ip"));
			item.setPort(rs.getInt("port"));
			item.setRemark(rs.getString("remark"));

			return item;
		} catch (Exception e) {
			errorLog.error("error in buildProxyItem()", e);
		}

		return null;
	}
	
	public static ProxyItem getProxyById(int id) {
		try {
			String sql = "select * from zgtx.proxy where id = " + id;
			sqlLog.info(sql);
			CachedRowSet rs = dbDBEngine.executeQuery(sql);
			if (rs.next()) {
				return buildProxyItem(rs);
			}
		} catch (Exception e) {
			errorLog.error("error in getProxyById()", e);
		}
		return null;
	}
	
	public static List<ProxyItem> getAllProxyList() {
		List<ProxyItem> list = new ArrayList<ProxyItem>();
		try {
			String sql = "select * from zgtx.proxy";
			sqlLog.info(sql);
			CachedRowSet rs = dbDBEngine.executeQuery(sql);
			while (rs.next()) {
				list.add(buildProxyItem(rs));
			}
		} catch (Exception e) {
			errorLog.error("error in getAllProxyList()", e);
		}
		return list;
	}
	
	public static boolean updateProxyFailTimes(String ip,int port,int failTimes){
		try {
			String sql = "update zgtx.proxy set fail_times = '" + failTimes
					+ "' where ip = '" + StringUtil.encodeSQL(ip) + "' and port = " + port;
			sqlLog.info(sql);
			dbDBEngine.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			errorLog.error("error in updateProxyFailTimes()", e);
		}

		return false;
	}
	
	public static boolean delProxy(String ip,int port){
		try {
			String sql = "delete from zgtx.proxy where ip = '" + StringUtil.encodeSQL(ip) + "' and port = " + port;
			sqlLog.info(sql);
			dbDBEngine.executeUpdate(sql);
			
			return true;
		} catch (Exception e) {
			errorLog.error("error in delProxy()", e);
		}

		return false;
	}
	
	public static int addTop10StockHolder(Top10StockHolderItem top10StockHolderItem){
		int ret = 0;
		try {
			String sql = "insert into zgtx.top10_stock_holder set stock_id = '" + StringUtil.encodeSQL(top10StockHolderItem.getStockId())
					+ "',holder_name_1 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName1()) 
					+ "',quantity_1 = '" + top10StockHolderItem.getQuantity1()
					+ "',percentage_1 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage1())
					+ "',property_1 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty1())
					+ "',holder_name_2 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName2()) 
					+ "',quantity_2 = '" + top10StockHolderItem.getQuantity2()
					+ "',percentage_2 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage2())
					+ "',property_2 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty2())
					+ "',holder_name_3 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName3()) 
					+ "',quantity_3 = '" + top10StockHolderItem.getQuantity3()
					+ "',percentage_3 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage3())
					+ "',property_3 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty3())
					+ "',holder_name_4 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName4()) 
					+ "',quantity_4 = '" + top10StockHolderItem.getQuantity4()
					+ "',percentage_4 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage4())
					+ "',property_4 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty4())
					+ "',holder_name_5 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName5()) 
					+ "',quantity_5 = '" + top10StockHolderItem.getQuantity5()
					+ "',percentage_5 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage5())
					+ "',property_5 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty5())
					+ "',holder_name_6 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName6()) 
					+ "',quantity_6 = '" + top10StockHolderItem.getQuantity6()
					+ "',percentage_6 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage6())
					+ "',property_6 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty6())
					+ "',holder_name_7 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName7()) 
					+ "',quantity_7 = '" + top10StockHolderItem.getQuantity7()
					+ "',percentage_7 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage7())
					+ "',property_7 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty7())
					+ "',holder_name_8 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName8()) 
					+ "',quantity_8 = '" + top10StockHolderItem.getQuantity8()
					+ "',percentage_8 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage8())
					+ "',property_8 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty8())
					+ "',holder_name_9 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName9()) 
					+ "',quantity_9 = '" + top10StockHolderItem.getQuantity9()
					+ "',percentage_9 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage9())
					+ "',property_9 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty9())
					+ "',holder_name_10 = '" + StringUtil.encodeSQL(top10StockHolderItem.getHolderName10()) 
					+ "',quantity_10 = '" + top10StockHolderItem.getQuantity10()
					+ "',percentage_10 = '" + StringUtil.encodeSQL(top10StockHolderItem.getPercentage10())
					+ "',property_10 = '" + StringUtil.encodeSQL(top10StockHolderItem.getProperty10())
					+ "',publish_date = '" + StringUtil.encodeSQL(top10StockHolderItem.getPublishDate())
					+ "';";
			//sqlLog.info(sql);
			dbDBEngine.executeUpdate(sql);
		} catch (Exception e) {
			ret = -1000;
			//errorLog.error("error in addTop10StockHolder()", e);
		}
		
		return ret;
	}
	
	private static Top10StockHolderItem buildTop10StockHolderItem(CachedRowSet rs) {
		if (rs == null) {
			return null;
		}

		try {
			Top10StockHolderItem top10StockHolderItem = new Top10StockHolderItem();
			top10StockHolderItem.setStockId(rs.getString("stock_id"));
			top10StockHolderItem.setPublishDate(rs.getString("publish_date"));
			top10StockHolderItem.setHolderName1(rs.getString("holder_name_1"));
			top10StockHolderItem.setQuantity1(rs.getLong("quantity_1"));
			top10StockHolderItem.setPercentage1(rs.getString("percentage_1"));
			top10StockHolderItem.setProperty1(rs.getString("property_1"));
			top10StockHolderItem.setHolderName2(rs.getString("holder_name_2"));
			top10StockHolderItem.setQuantity2(rs.getLong("quantity_2"));
			top10StockHolderItem.setPercentage2(rs.getString("percentage_2"));
			top10StockHolderItem.setProperty2(rs.getString("property_2"));
			top10StockHolderItem.setHolderName3(rs.getString("holder_name_3"));
			top10StockHolderItem.setQuantity3(rs.getLong("quantity_3"));
			top10StockHolderItem.setPercentage3(rs.getString("percentage_3"));
			top10StockHolderItem.setProperty3(rs.getString("property_3"));
			top10StockHolderItem.setHolderName4(rs.getString("holder_name_4"));
			top10StockHolderItem.setQuantity4(rs.getLong("quantity_4"));
			top10StockHolderItem.setPercentage4(rs.getString("percentage_4"));
			top10StockHolderItem.setProperty4(rs.getString("property_4"));
			top10StockHolderItem.setHolderName5(rs.getString("holder_name_5"));
			top10StockHolderItem.setQuantity5(rs.getLong("quantity_5"));
			top10StockHolderItem.setPercentage5(rs.getString("percentage_5"));
			top10StockHolderItem.setProperty5(rs.getString("property_5"));
			top10StockHolderItem.setHolderName6(rs.getString("holder_name_6"));
			top10StockHolderItem.setQuantity6(rs.getLong("quantity_6"));
			top10StockHolderItem.setPercentage6(rs.getString("percentage_6"));
			top10StockHolderItem.setProperty6(rs.getString("property_6"));
			top10StockHolderItem.setHolderName7(rs.getString("holder_name_7"));
			top10StockHolderItem.setQuantity7(rs.getLong("quantity_7"));
			top10StockHolderItem.setPercentage7(rs.getString("percentage_7"));
			top10StockHolderItem.setProperty7(rs.getString("property_7"));
			top10StockHolderItem.setHolderName8(rs.getString("holder_name_8"));
			top10StockHolderItem.setQuantity8(rs.getLong("quantity_8"));
			top10StockHolderItem.setPercentage8(rs.getString("percentage_8"));
			top10StockHolderItem.setProperty8(rs.getString("property_8"));
			top10StockHolderItem.setHolderName9(rs.getString("holder_name_9"));
			top10StockHolderItem.setQuantity9(rs.getLong("quantity_9"));
			top10StockHolderItem.setPercentage9(rs.getString("percentage_9"));
			top10StockHolderItem.setProperty9(rs.getString("property_9"));
			top10StockHolderItem.setHolderName10(rs.getString("holder_name_10"));
			top10StockHolderItem.setQuantity10(rs.getLong("quantity_10"));
			top10StockHolderItem.setPercentage10(rs.getString("percentage_10"));
			top10StockHolderItem.setProperty10(rs.getString("property_10"));
			
			return top10StockHolderItem;
		} catch (Exception e) {
			errorLog.error("error in buildTop10StockHolderItem()", e);
		}

		return null;
	}

	public static List<Top10StockHolderItem> getTop10StockHolderListByStockId(
			String stockId) {
		List<Top10StockHolderItem> list = new ArrayList<Top10StockHolderItem>();
		try {
			String sql = "select * from zgtx.top10_stock_holder where stock_id = '"
					+ StringUtil.encodeSQL(stockId)
					+ "' order by publish_date desc";

			CachedRowSet rs = dbDBEngine.executeQuery(sql);
			while (rs.next()) {
				list.add(buildTop10StockHolderItem(rs));
			}
		} catch (Exception e) {
			errorLog.debug("error in getTop10StockHolderListByStockId()", e);
		}

		return list;
	}

	public static List<Top10StockHolderItem> getAllTop10StockHolderList() {
		List<Top10StockHolderItem> list = new ArrayList<Top10StockHolderItem>();
		try {
			String sql = "select * from zgtx.top10_stock_holder";

			CachedRowSet rs = dbDBEngine.executeQuery(sql);
			while (rs.next()) {
				list.add(buildTop10StockHolderItem(rs));
			}
		} catch (Exception e) {
			errorLog.debug("error in getAllTop10StockHolderList()", e);
		}

		return list;
	}
	
}
