package stock.service;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import com.zgtx.parser.sina.TradeHistory;
import com.zgtx.parser.sina.TradeHistoryParser;

import stock.db.StockDB;
import stock.item.StockItem;
import stock.item.TradeHistoryStat;
import vc.pe.jutil.j4log.Logger;


public class TradeHistoryDaemon extends java.util.TimerTask{

	private static final Logger infoLog = Logger.getLogger("TradeHistoryDaemon");

	public static Timer timer = new Timer();
	
	static {
		timer.schedule(new TradeHistoryDaemon(), 0, 2000 * 3600);
	}

	private static int Year = 2014;
	
	private static int Quarter = 3;
	
	@Override
	public void run() {
		try {
			// TODO Auto-generated method stub
			infoLog.info("---TradeHistoryDaemon---Begin");
			/*
			if (TransactionTimeCheckTask.isVacation)
			{
				infoLog.info("---TradeHistoryDaemon---Vacation---End");
				return;
			}
			
			if (!TransactionTimeCheckTask.isTransactionEnd) {
				infoLog.info("---TradeHistoryDaemon---NoTransactionEnd---End");
				return;
			}
			*/
			
			List<StockItem> stockList = CommonService.getStockPoolList();
			/*
			for (StockItem stockItem : stockList) {
				if (StockDB.isStockTradeHistoryExisted(stockItem.getStockId(), TransactionTimeCheckTask.TodayDate))
				{
					continue;
				}
				infoLog.info("--TradeHistorySync--:" + stockItem.getStockId() + "|" + Year + "|" + Quarter);
				List<TradeHistory> tradeHistoryList = TradeHistoryParser.parseSinaTradeHistory(stockItem.getStockId(), Year, Quarter);
				if (tradeHistoryList != null)
				{
					for (TradeHistory tradeHistory : tradeHistoryList)
					{
						StockDB.addTradeHistory(tradeHistory);
					}
				}
			}
			*/
			//统计
			for (StockItem stockItem : stockList) {
				infoLog.info("--TradeHistoryStat--:" + stockItem.getStockId());
				List<TradeHistoryStat> tradeHistoryStatList = StockDB.getStockTradeHistoryStatList(stockItem.getStockId());
				HashMap<String,Integer> tradeHistoryStatHash = new HashMap<String,Integer>();
				for (TradeHistoryStat tradeHistoryStat : tradeHistoryStatList)
				{
					tradeHistoryStatHash.put(tradeHistoryStat.getDate(), 0);
				}
				List<TradeHistory> tradeHistoryList = StockDB.getStockTradeHistoryList(stockItem.getStockId());
				for (int i = 0; i < tradeHistoryList.size(); i++)
				{
					TradeHistory tradeHistory = tradeHistoryList.get(i);
					if (tradeHistoryStatHash.get(tradeHistory.getDate()) != null)
					{
						continue;
					}
					
					TradeHistoryStat tradeHistoryStat = new TradeHistoryStat();
					tradeHistoryStat.setDate(tradeHistory.getDate());
					tradeHistoryStat.setStockId(stockItem.getStockId());
					int from = i;
					int to = i + 4;
					if (to > tradeHistoryList.size() - 1)
					{
						to = tradeHistoryList.size() - 1;
					}
					
					long  sumTradingVolume5 = 0;
					long  sumDealVolume5 = 0;
					for (int t = from ; t <= to; t++)
					{
						sumTradingVolume5 += tradeHistoryList.get(t).getTradingVolume();
						sumDealVolume5 += tradeHistoryList.get(t).getDealVolume();
					}
					
					int days = to - from + 1;
					tradeHistoryStat.setAverageDealPrice5((int)(sumDealVolume5 * 100 / sumTradingVolume5));
					tradeHistoryStat.setAverageDealVolume5(sumTradingVolume5 / days);
			
					from = i;
					to = i + 19;
					if (to > tradeHistoryList.size() - 1)
					{
						to = tradeHistoryList.size() - 1;
					}
					
					long  sumTradingVolume20 = 0;
					long  sumDealVolume20 = 0;
					for (int t = from ; t <= to; t++)
					{
						sumTradingVolume20 += tradeHistoryList.get(t).getTradingVolume();
						sumDealVolume20 += tradeHistoryList.get(t).getDealVolume();
					}
					
					days = to - from + 1;
					tradeHistoryStat.setAverageDealPrice20((int)(sumDealVolume20 * 100 / sumTradingVolume20));
					tradeHistoryStat.setAverageDealVolume20(sumTradingVolume20 / days);
			
					from = i;
					to = i + 59;
					if (to > tradeHistoryList.size() - 1)
					{
						to = tradeHistoryList.size() - 1;
					}
					
					long  sumTradingVolume60 = 0;
					long  sumDealVolume60 = 0;
					for (int t = from ; t <= to; t++)
					{
						sumTradingVolume60 += tradeHistoryList.get(t).getTradingVolume();
						sumDealVolume60 += tradeHistoryList.get(t).getDealVolume();
					}
					
					days = to - from + 1;
					tradeHistoryStat.setAverageDealPrice60((int)(sumDealVolume60 * 100 / sumTradingVolume60));
					tradeHistoryStat.setAverageDealVolume60(sumTradingVolume60 / days);
					
					StockDB.addTradeHistoryStat(tradeHistoryStat);
					tradeHistoryStatList.add(tradeHistoryStat);
				}
				
				for (TradeHistoryStat tradeHistoryStat : tradeHistoryStatList)
				{
					if ((tradeHistoryStat.getAverageDealVolume5() / tradeHistoryStat.getAverageDealVolume60() > 2)
						&& (tradeHistoryStat.getDate().compareTo("2014") > 0))
					{
						infoLog.info("|Hit|" + tradeHistoryStat.getStockId() + "|" + tradeHistoryStat.getDate() + "|" + tradeHistoryStat.getAverageDealVolume5() + "|" + tradeHistoryStat.getAverageDealVolume60());
					}
				}
					
			}
			
			infoLog.info("---TradeHistoryDaemon---End");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
