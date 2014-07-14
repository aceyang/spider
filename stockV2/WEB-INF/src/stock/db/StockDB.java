package stock.db;

import java.util.*;
import javax.sql.rowset.CachedRowSet;
import com.zgtx.parser.sina.TradeHistory;
import stock.item.StockItem;
import stock.item.TradeHistoryStat;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.sql.DBEngine;
import vc.pe.jutil.sql.DBFactory;
import vc.pe.jutil.string.StringUtil;

public class StockDB {

	private static DBEngine dbengine = DBFactory.getDBEngine("STOCK_DB_Pool");

	private static final Logger sqlLog = Logger.getLogger("stock_sql");
	
	private static final Logger debugLog = Logger.getLogger("stock_db");

	public static List<StockItem> getStockPoolList() {
		List<StockItem> stockList = new ArrayList<StockItem>();
		try {
			String sql = "select * from stock_pool";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				String stockId = rs.getString("stockId");
				String stockName = rs.getString("stockName");

				StockItem stockItem = new StockItem();
				stockItem.setStockName(stockName);
				stockItem.setStockId(stockId);

				stockList.add(stockItem);
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::getStockPoolList()", e);
		}

		return stockList;
	}
	

	public static List<StockItem> getCNStockPoolList() {
		List<StockItem> stockList = new ArrayList<StockItem>();
		try {
			String sql = "select * from stock_pool_cn_a";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				String stockId = rs.getString("stockId");
				String stockName = rs.getString("stockName");

				StockItem stockItem = new StockItem();
				stockItem.setStockName(stockName);
				stockItem.setStockId(stockId);

				stockList.add(stockItem);
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::getCNStockPoolList()", e);
		}

		return stockList;
	}
	
	
	
	public static boolean isSinaQuoteStatDone(int month,String stockId, String date) {
		try {
			String sql = "select * from sina_quote_stat_log_" + month + " where stockId = '" + StringUtil.encodeSQL(stockId) + "' and date = '" + StringUtil.encodeSQL(date)
					+ "'";
			CachedRowSet rs = dbengine.executeQuery(sql);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::isSinaQuoteStatDone()", e);
		}

		return false;
	}
	
	public static boolean logSinaQuoteStat(int month,String stockId, String date) {
		String sql = "insert into sina_quote_stat_log_" + month + " (stockId,date) values ('" + StringUtil.encodeSQL(stockId) + "','" + StringUtil.encodeSQL(date) + "')";
		try {
			dbengine.executeUpdate(sql);
		} catch (Exception e) {
			debugLog.debug("error in StockDB::logSinaQuoteStat()", e);

			return false;
		}

		return true;
	}
	
	public static int addTradeHistory(TradeHistory tradeHistory){
		int ret = 0;
		String tableName = "data_sina_v3.trade_history_" + (Integer.valueOf(tradeHistory.getStockId()) % 100);
		try {
			String sql = "insert into " + tableName + " set stockId = '" + StringUtil.encodeSQL(tradeHistory.getStockId())
					+ "',date = '" + StringUtil.encodeSQL(tradeHistory.getDate()) 
					+ "',openPrice = '" + tradeHistory.getOpenPrice()
					+ "',closePrice = '" + tradeHistory.getClosePrice()
					+ "',highPrice = '" + tradeHistory.getHighPrice()
					+ "',lowPrice = '" + tradeHistory.getLowPrice()
					+ "',tradingVolume = '" + tradeHistory.getTradingVolume()
					+ "',dealVolume = '" + tradeHistory.getDealVolume()
					+ "';";
					
			sqlLog.info(sql);
			dbengine.executeUpdate(sql);
		} catch (Exception e) {
			ret = -1000;
		}
		
		return ret;
	}
	
	
	private static TradeHistory buildTradeHistory(CachedRowSet rs) {
		if (rs == null) {
			return null;
		}

		try {
			TradeHistory history = new TradeHistory();
			history.setClosePrice(rs.getInt("closePrice"));
			history.setDate(rs.getString("date"));
			history.setDealVolume(rs.getLong("dealVolume"));
			history.setHighPrice(rs.getInt("highPrice"));
			history.setLowPrice(rs.getInt("lowPrice"));
			history.setOpenPrice(rs.getInt("openPrice"));
			history.setStockId(rs.getString("stockId"));
			history.setTradingVolume(rs.getLong("tradingVolume"));
			


			return history;
		} catch (Exception e) {
			debugLog.error("error in buildTradeHistory()", e);
		}

		return null;
	}

	
	public static List<TradeHistory> getStockTradeHistoryList(String stockId) {
		List<TradeHistory> tradeHistoryList = new ArrayList<TradeHistory>();
		try {
			String tableName = "data_sina_v3.trade_history_" + (Integer.valueOf(stockId) % 100);
			String sql = "select * from " + tableName + " where stockId = '" + StringUtil.encodeSQL(stockId) + "' order by date desc";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				tradeHistoryList.add(buildTradeHistory(rs));
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::getStockTradeHistoryList()", e);
		}

		return tradeHistoryList;
	}
	
	public static boolean isStockTradeHistoryExisted(String stockId, String date) {
		try {
			String tableName = "data_sina_v3.trade_history_" + (Integer.valueOf(stockId) % 100);
			String sql = "select * from " + tableName + " where stockId = '" + StringUtil.encodeSQL(stockId) + "' and date = '" + StringUtil.encodeSQL(date) + "'";

			CachedRowSet rs = dbengine.executeQuery(sql);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::getStockTradeHistoryList()", e);
		}

		return false;
	}
	
	public static int addTradeHistoryStat(TradeHistoryStat tradeHistoryStat){
		int ret = 0;
		String tableName = "data_sina_v3.trade_history_stat_" + (Integer.valueOf(tradeHistoryStat.getStockId()) % 100);
		try {
			String sql = "replace into " + tableName + " set stockId = '" + StringUtil.encodeSQL(tradeHistoryStat.getStockId())
					+ "',date = '" + StringUtil.encodeSQL(tradeHistoryStat.getDate()) 
					+ "',averageDealPrice5 = '" + tradeHistoryStat.getAverageDealPrice5()
					+ "',averageDealVolume5 = '" + tradeHistoryStat.getAverageDealVolume5()
					+ "',averageDealPrice20 = '" + tradeHistoryStat.getAverageDealPrice20()
					+ "',averageDealVolume20 = '" + tradeHistoryStat.getAverageDealVolume20()
					+ "',averageDealPrice60 = '" + tradeHistoryStat.getAverageDealPrice60()
					+ "',averageDealVolume60 = '" + tradeHistoryStat.getAverageDealVolume60()
					+ "';";
					
			sqlLog.info(sql);
			dbengine.executeUpdate(sql);
		} catch (Exception e) {
			ret = -1000;
		}
		
		return ret;
	}
	
	private static TradeHistoryStat buildTradeHistoryStat(CachedRowSet rs) {
		if (rs == null) {
			return null;
		}

		try {
			TradeHistoryStat historyStat = new TradeHistoryStat();
			historyStat.setAverageDealPrice20(rs.getInt("averageDealPrice20"));
			historyStat.setAverageDealPrice5(rs.getInt("averageDealPrice5"));
			historyStat.setAverageDealPrice60(rs.getInt("averageDealPrice60"));
			historyStat.setAverageDealVolume20(rs.getLong("averageDealVolume20"));
			historyStat.setAverageDealVolume5(rs.getLong("averageDealVolume5"));
			historyStat.setAverageDealVolume60(rs.getLong("averageDealVolume60"));
			historyStat.setDate(rs.getString("date"));
			historyStat.setStockId(rs.getString("stockId"));
			
			return historyStat;
		} catch (Exception e) {
			debugLog.error("error in buildTradeHistoryStat()", e);
		}

		return null;
	}

	public static List<TradeHistoryStat> getStockTradeHistoryStatList(String stockId) {
		List<TradeHistoryStat> tradeHistoryStatList = new ArrayList<TradeHistoryStat>();
		try {
			String tableName = "data_sina_v3.trade_history_stat_" + (Integer.valueOf(stockId) % 100);
			String sql = "select * from " + tableName + " where stockId = '" + StringUtil.encodeSQL(stockId) + "' order by date desc";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				tradeHistoryStatList.add(buildTradeHistoryStat(rs));
			}
		} catch (Exception e) {
			debugLog.error("error in StockDB::getStockTradeHistoryStatList()", e);
		}

		return tradeHistoryStatList;
	}
}
