package stock.service;

import java.util.List;
import stock.item.StockItem;
import vc.pe.jutil.j4log.Logger;

import com.zgtx.common.client.CommonClient;
import com.zgtx.common.client.Top10StockHolderItem;
import com.zgtx.parser.sina.Top10StockHolderParser;

public class SpiderDaemon implements Runnable {

	private static final Logger infoLog = Logger.getLogger("SpiderDaemon");

	static {
		new Thread(new SpiderDaemon(), "SpiderDaemon").start();
	}

	@Override
	public void run() {
		try {
			// TODO Auto-generated method stub
			infoLog.info("---SpiderDaemon---Begin");
			CommonClient commonClient = new CommonClient();
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
							commonClient.addTop10StockHolder(top10StockHolderItem);
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
