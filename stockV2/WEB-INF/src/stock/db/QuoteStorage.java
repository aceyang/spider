package stock.db;

import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import stock.item.QuoteItem;
import stock.item.QuoteVaryItem;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.sql.DBEngine;
import vc.pe.jutil.sql.DBFactory;
import vc.pe.jutil.string.StringUtil;


public class QuoteStorage {

	private static DBEngine dbengine = DBFactory.getDBEngine("STOCK_DB_Pool");

	private static final Logger debugLog = Logger.getLogger("stock_db");

	private static String getSinaQuoteTable(String date) {
		int month = 0;
		int day = 0;
		String [] s = StringUtil.split(date, "-");
		if ((s != null) && (s.length == 3))
		{
			month = Integer.valueOf(s[1]);
			day = Integer.valueOf(s[2]);
		}
		
		String tableName = "data_sina_" + month + ".quote_" + day;
		
		return tableName;
	}
	
	public static List<QuoteItem> getQuoteList(String stockId, String date,
			int site) {

		String tableName = getSinaQuoteTable(date);

		List<QuoteItem> quoteList = new ArrayList<QuoteItem>();

		try {
			String sql = "select distinct bid1Amount,bid1Price,"
					+ "bid2Amount,bid2Price,bid3Amount,bid3Price,bid4Amount,bid4Price,bid5Amount,bid5Price,"
					+ "sell1Amount,sell1Price,sell2Amount,sell2Price,sell3Amount,sell3Price,sell4Amount,sell4Price,"
					+ "sell5Amount,sell5Price,stockId,date,time from "
					+ tableName + " where stockId = '" + stockId
					+ "' and date = '" + date + "' order by time";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				QuoteItem quoteItem = new QuoteItem();
				quoteItem.setBid1Amount(rs.getInt("bid1Amount"));
				quoteItem.setBid1Price(rs.getInt("bid1Price"));

				quoteItem.setBid2Amount(rs.getInt("bid2Amount"));
				quoteItem.setBid2Price(rs.getInt("bid2Price"));

				quoteItem.setBid3Amount(rs.getInt("bid3Amount"));
				quoteItem.setBid3Price(rs.getInt("bid3Price"));

				quoteItem.setBid4Amount(rs.getInt("bid4Amount"));
				quoteItem.setBid4Price(rs.getInt("bid4Price"));

				quoteItem.setBid5Amount(rs.getInt("bid5Amount"));
				quoteItem.setBid5Price(rs.getInt("bid5Price"));

				quoteItem.setDate(date);

				quoteItem.setSell1Amount(rs.getInt("sell1Amount"));
				quoteItem.setSell1Price(rs.getInt("sell1Price"));

				quoteItem.setSell2Amount(rs.getInt("sell2Amount"));
				quoteItem.setSell2Price(rs.getInt("sell2Price"));

				quoteItem.setSell3Amount(rs.getInt("sell3Amount"));
				quoteItem.setSell3Price(rs.getInt("sell3Price"));

				quoteItem.setSell4Amount(rs.getInt("sell4Amount"));
				quoteItem.setSell4Price(rs.getInt("sell4Price"));

				quoteItem.setSell5Amount(rs.getInt("sell5Amount"));
				quoteItem.setSell5Price(rs.getInt("sell5Price"));
				quoteItem.setStockId(stockId);
				quoteItem.setTime(rs.getString("time"));

				quoteList.add(quoteItem);
			}
		} catch (Exception e) {
			debugLog.debug("error in QuoteStorage::getQuoteList()", e);
		}

		return quoteList;
	}
	
	private static String getSinaQuoteTable(int month, int day) {
		String tableName = "data_sina_" + month + ".quote_" + day;
		
		return tableName;
	}
	
	public static boolean insertSinaQuoteBatch(int month, int day,List<QuoteItem> quoteList) {

		String tableName = getSinaQuoteTable(month,day);

		StringBuilder sb = new StringBuilder();
		sb.append("insert into "
				+ tableName
				+ " (bid1Amount,bid1Price,"
				+ "bid2Amount,bid2Price,bid3Amount,bid3Price,bid4Amount,bid4Price,bid5Amount,bid5Price,"
				+ "sell1Amount,sell1Price,sell2Amount,sell2Price,sell3Amount,sell3Price,sell4Amount,sell4Price,"
				+ "sell5Amount,sell5Price,stockId,date,time) values ");
		
	
		int index = 0;
		for (QuoteItem quoteItem : quoteList)
		{
			index++;
			sb.append("('" + quoteItem.getBid1Amount() + "','"
					+ quoteItem.getBid1Price() + "','" + quoteItem.getBid2Amount()
					+ "','" + quoteItem.getBid2Price() + "','"
					+ quoteItem.getBid3Amount() + "','" + quoteItem.getBid3Price()
					+ "','" + quoteItem.getBid4Amount() + "','"
					+ quoteItem.getBid4Price() + "','" + quoteItem.getBid5Amount()
					+ "','" + quoteItem.getBid5Price() + "','"
					+ quoteItem.getSell1Amount() + "','"
					+ quoteItem.getSell1Price() + "','"
					+ quoteItem.getSell2Amount() + "','"
					+ quoteItem.getSell2Price() + "','"
					+ quoteItem.getSell3Amount() + "','"
					+ quoteItem.getSell3Price() + "','"
					+ quoteItem.getSell4Amount() + "','"
					+ quoteItem.getSell4Price() + "','"
					+ quoteItem.getSell5Amount() + "','"
					+ quoteItem.getSell5Price() + "','" 
					+ quoteItem.getStockId() + "','" 
					+ quoteItem.getDate() + "','" + quoteItem.getTime() + "')");
			if (index == quoteList.size())
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
			debugLog.debug("error in QuoteStorage::insertSinaQuoteBatch() ", e);
			return false;
		}

		return true;
	}
	

	public static boolean insertSinaQuoteVary(String stockId, String date,
			String time, int price, int quantum, String bidSell) {

		int stockIdInt = Integer.valueOf(stockId);
		int index = stockIdInt % 100;
		String tableName = "data_sina_v3.quote_vary_" + index;

		String sql = "insert into " + tableName
				+ "(stockId,date,time,price,quantum,bidSell) values ('"
				+ stockId + "','" + date + "','" + time + "','" + price + "','"
				+ quantum + "','" + bidSell + "')";
		try {
			dbengine.executeUpdate(sql);
		} catch (Exception e) {
			debugLog.debug("error in QuoteStorage::insertSinaQuoteVary()", e);

			return false;
		}

		return true;
	}
	
	public static List<QuoteVaryItem> getQuoteVaryByDate(String stockId, String date) {
		List<QuoteVaryItem> quoteVaryList = new ArrayList<QuoteVaryItem>();
		try {
			int stockIdInt = Integer.valueOf(stockId);
			int index = stockIdInt % 100;
			String tableName = "data_sina_v3.quote_vary_" + index;
			String sql = "select * from " + tableName + " where stockId = '" + stockId + "' and date = '" + date + "'";

			CachedRowSet rs = dbengine.executeQuery(sql);
			while (rs.next()) {
				QuoteVaryItem quoteVaryItem = new QuoteVaryItem();
				quoteVaryItem.setBidSell(rs.getString("bidSell"));
				quoteVaryItem.setDate(rs.getString("date"));
				quoteVaryItem.setPrice(rs.getInt("price"));
				quoteVaryItem.setQuantum(rs.getInt("quantum"));
				quoteVaryItem.setStockId(rs.getString("stockId"));
				quoteVaryItem.setTime(rs.getString("time"));

				quoteVaryList.add(quoteVaryItem);
			}
		} catch (Exception e) {
			debugLog.debug("error in QuoteStorage::getQuoteVaryByDate()", e);
		}

		return quoteVaryList;
	}
	
	
	private static String getQQQuoteTable(int month, int day) {
		String tableName = "data_qq_" + month + ".quote_" + day;
		
		return tableName;
	}
	
	public static boolean insertQQQuoteBatch(int month, int day,List<QuoteItem> quoteList) {

		String tableName = getQQQuoteTable(month,day);

		StringBuilder sb = new StringBuilder();
		sb.append("insert into "
				+ tableName
				+ " (bid1Amount,bid1Price,"
				+ "bid2Amount,bid2Price,bid3Amount,bid3Price,bid4Amount,bid4Price,bid5Amount,bid5Price,"
				+ "sell1Amount,sell1Price,sell2Amount,sell2Price,sell3Amount,sell3Price,sell4Amount,sell4Price,"
				+ "sell5Amount,sell5Price,stockId,date,time) values ");
		
	
		int index = 0;
		for (QuoteItem quoteItem : quoteList)
		{
			index++;
			sb.append("('" + quoteItem.getBid1Amount() + "','"
					+ quoteItem.getBid1Price() + "','" + quoteItem.getBid2Amount()
					+ "','" + quoteItem.getBid2Price() + "','"
					+ quoteItem.getBid3Amount() + "','" + quoteItem.getBid3Price()
					+ "','" + quoteItem.getBid4Amount() + "','"
					+ quoteItem.getBid4Price() + "','" + quoteItem.getBid5Amount()
					+ "','" + quoteItem.getBid5Price() + "','"
					+ quoteItem.getSell1Amount() + "','"
					+ quoteItem.getSell1Price() + "','"
					+ quoteItem.getSell2Amount() + "','"
					+ quoteItem.getSell2Price() + "','"
					+ quoteItem.getSell3Amount() + "','"
					+ quoteItem.getSell3Price() + "','"
					+ quoteItem.getSell4Amount() + "','"
					+ quoteItem.getSell4Price() + "','"
					+ quoteItem.getSell5Amount() + "','"
					+ quoteItem.getSell5Price() + "','" 
					+ quoteItem.getStockId() + "','" 
					+ quoteItem.getDate() + "','" + quoteItem.getTime() + "')");
			if (index == quoteList.size())
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
			debugLog.debug("error in QuoteStorage::insertQQQuoteBatch() ", e);
			return false;
		}

		return true;
	}
	
}
