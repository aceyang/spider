package stock.service;

import java.util.ArrayList;
import java.util.List;
import stock.db.DealStorage;
import stock.item.DealItem;
import stock.item.QuoteItem;
import stock.item.StockItem;
import stock.parse.SinaSpider;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.jcache.Cache;
import vc.pe.jutil.jcache.CacheFactory;
import vc.pe.jutil.string.StringUtil;

public class SinaDealDaemon extends Thread{

	private static final Logger debugLog = Logger.getLogger("SinaDealDaemon");

	private static Cache<String, String> DealDoneCache = CacheFactory.getCache("DealDoneCache");
	
	private static final int SpiderNum = 5;
	
	private int id = -1;

	public SinaDealDaemon() {
	}

	static {
		String threadName = "";
		for (int i = 1; i <= SpiderNum; i++) {
			threadName = "SinaSpiderDeal#" + i;
			SinaDealDaemon t = new SinaDealDaemon();
			t.setName(threadName);
			SinaDaemonExceptionHandler handler = new SinaDaemonExceptionHandler();
			t.setUncaughtExceptionHandler(handler);
			t.start();
		}
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String name = Thread.currentThread().getName();
		String [] s = StringUtil.split(name, "#");
		if (s.length == 2)
		{
			id = Integer.valueOf(s[1]);
		}
		
		debugLog.debug("Thread " + name + "|" + id + ": run()");

		if ((id >= 1) && (id <= SpiderNum)) {
			spiderDealTask();
		}

		debugLog.debug("Thread " + id + ": exit");
	}

	private void spiderDealTask() {
		List<StockItem> jobList = new ArrayList<StockItem>();
		List<StockItem> stockList = CommonService.getStockPoolList();
		for (StockItem stockItem : stockList) {
			int stockId = Integer.valueOf(stockItem.getStockId());
			int taskId = stockId % SpiderNum + 1;
			if (taskId == id) {
				jobList.add(stockItem);
			}
		}

		while (true) {
			try {
				// 判断交易日
				if (TransactionTimeCheckTask.isVacation) {
					debugLog.debug("Vacation!");
					Thread.sleep(3600 * 1000);
					continue;
				}
				
				if (TransactionTimeCheckTask.isTransactionEnd) {// Begin
					for (StockItem stockItem : jobList) {
						String ua = "";
						String key = TransactionTimeCheckTask.TodayDate + "@All@" + stockItem.getStockId();
						
						if (DealDoneCache.get(key) != null)
						{
							debugLog.debug(key + "|UnDo|" + DealDoneCache.get(key));
							continue;
						}
						
						QuoteItem quoteItem = SinaSpider.queryQuote(stockItem
								.getStockId(), ua);
						if (quoteItem != null)
						{
							if ((quoteItem.getStatus() == 3) || (quoteItem.getStatus() == 2))
							{
								//停牌
								debugLog.debug(key + "|停牌|");
								DealDoneCache.put(key, "停牌");
								continue;
							}
						}
						
						List<DealItem> dealList = DealStorage.getSinaDealList(stockItem.getStockId(), TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day);
						if ((dealList != null) && (dealList.size() > 0))
						{
							DealDoneCache.put(key, String.valueOf(dealList.size()));
							debugLog.debug(key + "|Done|" + dealList.size());
							continue;
						}
						
						dealList = SinaSpider.queryDealListByDate(stockItem.getStockId(), TransactionTimeCheckTask.TodayDate, ua);
						if ((dealList != null) && (dealList.size() > 0))
						{
							DealStorage.insertSinaDealBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, dealList);
							DealDoneCache.put(key, String.valueOf(dealList.size()));
						}
						
						key = TransactionTimeCheckTask.TodayDate + "@Super@" + stockItem.getStockId();
						
						if (DealDoneCache.get(key) != null)
						{
							debugLog.debug(key + "|UnDo|" + DealDoneCache.get(key));
							continue;
						}
						
						quoteItem = SinaSpider.queryQuote(stockItem
								.getStockId(), ua);
						if (quoteItem != null)
						{
							if ((quoteItem.getStatus() == 3) || (quoteItem.getStatus() == 2))
							{
								//停牌
								debugLog.debug(key + "|停牌|");
								DealDoneCache.put(key, "停牌");
								continue;
							}
						}
						
						dealList = DealStorage.getSinaSuperDealList(stockItem.getStockId(), TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day);
						if ((dealList != null) && (dealList.size() > 0))
						{
							DealDoneCache.put(key, String.valueOf(dealList.size()));
							debugLog.debug(key + "|Done|" + dealList.size());
							continue;
						}
						
						dealList = SinaSpider.querySuperDealListByDate(stockItem.getStockId(), TransactionTimeCheckTask.TodayDate, ua);
						if ((dealList != null) && (dealList.size() > 0))
						{
							DealStorage.insertSinaSuperDealBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, dealList);
							DealDoneCache.put(key, String.valueOf(dealList.size()));
						}
					}
				}
				
				Thread.sleep(1000 * 60);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				debugLog.debug("Error in spiderDealTask()", e);
			}
		} // while
	}
}
