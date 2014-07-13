package stock.service;

import java.util.List;
import java.util.Timer;

import com.zgtx.parser.sina.TradeHistory;
import com.zgtx.parser.sina.TradeHistoryParser;

import stock.db.StockDB;
import stock.item.StockItem;
import vc.pe.jutil.j4log.Logger;


public class TradeHistoryDaemon extends java.util.TimerTask{

	private static final Logger infoLog = Logger.getLogger("TradeHistoryDaemon");

	public static Timer timer = new Timer();
	
	static {
		timer.schedule(new TradeHistoryDaemon(), 0, 1000 * 3600);
	}

	@Override
	public void run() {
		try {
			// TODO Auto-generated method stub
			infoLog.info("---TradeHistoryDaemon---Begin");
			/*
			if (!TransactionTimeCheckTask.isTransactionEnd) {
				infoLog.info("---TradeHistoryDaemon---NoTransactionEnd---End");
				return;
			}
			*/
			List<StockItem> stockList = CommonService.getStockPoolList();
			for (StockItem stockItem : stockList) {
				infoLog.info("--TradeHistory--:" + stockItem.getStockId());
				for (int year = 2007; year <= 2014; year++)
				{
					for (int quarter = 1; quarter <= 4; quarter++)
					{
						infoLog.info("--TradeHistory--:" + stockItem.getStockId() + "|" + year + "|" + quarter);
						List<TradeHistory> tradeHistoryList = TradeHistoryParser.parseSinaTradeHistory(stockItem.getStockId(), year, quarter);
						if (tradeHistoryList != null)
						{
							for (TradeHistory tradeHistory : tradeHistoryList)
							{
								StockDB.addTradeHistory(tradeHistory);
							}
						}
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
