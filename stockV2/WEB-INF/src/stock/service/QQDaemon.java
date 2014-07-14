package stock.service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import stock.db.DealStorage;
import stock.db.QuoteStorage;
import stock.item.*;
import stock.parse.QQSpider;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.jcache.Cache;
import vc.pe.jutil.jcache.CacheFactory;

public class QQDaemon implements Runnable {

	private static final Logger debugLog = Logger.getLogger("QQDaemon");

	private static final int SpiderNum = 16;

	private static final int BatchNum = 160;
	
	private static LinkedBlockingQueue<QuoteItem> QuoteQueue = new LinkedBlockingQueue<QuoteItem>(); // 盘口竞价

	private static LinkedBlockingQueue<DealItem> DealQueue = new LinkedBlockingQueue<DealItem>(); // 每笔交易

	private static Cache<String, Integer> ExclusiveQuoteCache = CacheFactory
			.getCache("TencentExclusiveQuoteCache");

	private static Cache<String, Integer> ExclusiveDealCache = CacheFactory
			.getCache("TencentExclusiveDealCache");

	private int id = -1;

	QQDaemon(int id) {
		this.id = id;
	}

	public QQDaemon() {
	}

	static {
		String threadName = "";
		for (int i = 1; i <= SpiderNum; i++) {
			threadName = "QQSpider#" + i;
			new Thread(new QQDaemon(i), threadName).start();
		}

		new Thread(new QQDaemon(SpiderNum + 1), "QuoteStorage").start();

		new Thread(new QQDaemon(SpiderNum + 2), "DealStorage").start();
	}

	public void run() {
		debugLog.debug("Thread " + id + ": run()");

		if ((id >= 1) && (id <= SpiderNum)) {
			spiderTask();
		} else if (id == SpiderNum + 1) {
			quoteStorage();
		} else if (id == SpiderNum + 2) {
			dealStorage();
		}

		debugLog.debug("Thread " + id + ": exit");
	}

	private static void addQuoteQueue(QuoteItem quoteItem) {
		if (quoteItem != null) {
			String key = quoteItem.getStockId() + "@" + quoteItem.getDate()
					+ "@" + quoteItem.getTime();
			if (ExclusiveQuoteCache.get(key) == null) {
				ExclusiveQuoteCache.put(key, 0);
				QuoteQueue.offer(quoteItem);
			}
		}
	}

	private static void addDealQueue(List<DealItem> dealList) {
		if (dealList != null) {
			for (DealItem dealItem : dealList) {
				String key = dealItem.getStockId() + "@" + dealItem.getDate()
						+ "@" + dealItem.getTime() + "@" + dealItem.getPrice()
						+ "@" + dealItem.getQuantum();

				if (ExclusiveDealCache.get(key) == null) {
					ExclusiveDealCache.put(key, 0);
					DealQueue.offer(dealItem);
				}
			}
		}
	}

	private void spiderTask() {
		List<StockItem> jobList = new ArrayList<StockItem>();
		List<StockItem> stockList = CommonService.getStockPoolList();
		HashMap<String,Integer> stockHash = new HashMap<String,Integer>();
		for (StockItem stockItem : stockList) {
			stockHash.put(stockItem.getStockId(), 0);
		}
		
		List<StockItem> cnStockPoolList = CommonService.getCNStockPoolList();
		for (StockItem stockItem : cnStockPoolList) {
			if (stockHash.get(stockItem.getStockId()) != null)
			{
				continue;
			}
			int stockId = Integer.valueOf(stockItem.getStockId());
			int taskId = stockId % SpiderNum + 1;
			if (taskId == id) {
				jobList.add(stockItem);
			}
		}
		
		
		while (true) {
			try {				
				if (TransactionTimeCheckTask.isVacation) {
					debugLog.debug("Vacation!");
					try {
						Thread.sleep(3600 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						debugLog.debug("Error in spiderTask()", e);
					}
					continue;
				}
				
				if (TransactionTimeCheckTask.isTransactionTime) { // Begin
					for (StockItem stockItem : jobList) {
						String ua = "";
						QQData qqData = QQSpider.query(
								stockItem.getStockId(), ua);
						if (qqData != null) {
							addQuoteQueue(qqData.getQuoteItem());
							addDealQueue(qqData.getDellList());
						}

						Thread.sleep(1);
					}
				} 
				else {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				debugLog.debug("Error in spiderTask()", e);
			}
		} // while
	}

	private void dealStorage() {
		debugLog.debug("dealStorage");
		List<DealItem> dealList = new ArrayList<DealItem>();
		while (true) {
			try {
				DealItem dealItem = DealQueue.poll();
				if (dealItem != null) {
					dealList.add(dealItem);
					if (dealList.size() >= BatchNum)
					{
						DealStorage.insertQQDealBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, dealList);
						dealList.clear();
					}
				}
				else
				{
					if (dealList.size() > 0)
					{
						DealStorage.insertQQDealBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, dealList);
						dealList.clear();
					}
					Thread.sleep(5000);
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				debugLog.debug("Error in dealStorage()", e);
			}
		}
	}
	
		

	private void quoteStorage() {
		debugLog.debug("quoteStorage");
		List<QuoteItem> quoteList = new ArrayList<QuoteItem>();
		while (true) {
			try {
				 QuoteItem quoteItem = QuoteQueue.poll();
				if (quoteItem != null) {
					quoteList.add(quoteItem);
					if (quoteList.size() >= BatchNum)
					{
						QuoteStorage.insertQQQuoteBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, quoteList);
						quoteList.clear();
					}
				}
				else
				{
					if (quoteList.size() > 0)
					{
						QuoteStorage.insertQQQuoteBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, quoteList);
						quoteList.clear();
					}
					Thread.sleep(5000);
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				debugLog.debug("Error in quoteStorage()", e);
			}
		}
	}

}
