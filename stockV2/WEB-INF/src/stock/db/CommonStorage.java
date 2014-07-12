package stock.db;

import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.sql.DBEngine;
import vc.pe.jutil.sql.DBFactory;
import vc.pe.jutil.string.StringUtil;
import com.zgtx.common.client.ProxyItem;

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
}
