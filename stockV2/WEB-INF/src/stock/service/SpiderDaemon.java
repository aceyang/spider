package stock.service;

import java.util.List;
import java.util.Timer;

import stock.db.CommonStorage;
import stock.item.StockItem;
import vc.pe.jutil.j4log.Logger;
import com.zgtx.common.client.Top10StockHolderItem;
import com.zgtx.parser.sina.Top10StockHolderParser;

public class SpiderDaemon extends java.util.TimerTask {

	private static final Logger infoLog = Logger.getLogger("SpiderDaemon");

	public static Timer timer = new Timer();
	
	static {
		timer.schedule(new SpiderDaemon(), 0, 1000 * 3600 * 12);
	}

	@Override
	public void run() {
		try {
			// TODO Auto-generated method stub
			infoLog.info("---SpiderDaemon---Begin");
			if (!TransactionTimeCheckTask.isVacation) {
				infoLog.info("---SpiderDaemon---NoVacation---End");
				return;
			}
			
			List<StockItem> stockList = CommonService.getStockPoolList();
			for (StockItem stockItem : stockList) {
				infoLog.info("--Spider--:" + stockItem.getStockId());
				List<String> contentList = Top10StockHolderParser
						.parseSinaTop10StockHolder(stockItem.getStockId());
				if (contentList != null) {
					infoLog.info("contentList:" + contentList.size());
					List<Top10StockHolderItem> top10StockHolderList = Top10StockHolderParser
							.transformTop10StockHolderItemList(stockItem.getStockId(), contentList);
					infoLog.info("top10StockHolderList:" + top10StockHolderList.size());

					if (top10StockHolderList != null) {
						for (Top10StockHolderItem top10StockHolderItem : top10StockHolderList) {
							CommonStorage.addTop10StockHolder(top10StockHolderItem);
						}
					}
				}
			}
			infoLog.info("---SpiderDaemon---End");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
