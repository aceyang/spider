package stock.db;

import java.util.*;
import javax.sql.rowset.CachedRowSet;

import com.zgtx.parser.sina.TradeHistory;

import stock.item.StockItem;
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
}
