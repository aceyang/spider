package stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;
import stock.db.QuoteStorage;
import stock.item.QuoteItem;
import stock.item.StockItem;
import stock.parse.SinaSpider;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.jcache.Cache;
import vc.pe.jutil.jcache.CacheFactory;
import vc.pe.jutil.string.StringUtil;


public class SinaQuoteDaemon extends Thread{

	private static final Logger debugLog = Logger.getLogger("SinaQuoteDaemon");

	private static final int SpiderNum = 10;
	
	private static final int BatchNum = 160;
	
	private static LinkedBlockingQueue<QuoteItem> QuoteQueue = new LinkedBlockingQueue<QuoteItem>(); // 盘口竞价

	private static Cache<String, Integer> ExclusiveQuoteCache = CacheFactory
			.getCache("SinaExclusiveQuoteCache");

	public static  Timer timer = new Timer();
	
	public static  Timer timer2 = new Timer();
	
	private int id = -1;

	public SinaQuoteDaemon() {
	}

	static {
		timer.schedule(new TransactionTimeCheckTask(), 0, 1000 * 60);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer2.schedule(new SinaStatTaskDispatcher(), 0, 1000 * 60 * 60);
		String threadName = "";
		for (int i = 1; i <= SpiderNum; i++) {
			threadName = "SinaSpiderQuote#" + i;
			SinaQuoteDaemon t = new SinaQuoteDaemon();
			t.setName(threadName);
			SinaDaemonExceptionHandler handler = new SinaDaemonExceptionHandler();
			t.setUncaughtExceptionHandler(handler);
			t.start();
		}
		
		SinaQuoteDaemon t = new SinaQuoteDaemon();
		t.setName("QuoteStorage#88");
		SinaDaemonExceptionHandler handler = new SinaDaemonExceptionHandler();
		t.setUncaughtExceptionHandler(handler);
		t.start();
	}

	public void run() {
		String name = Thread.currentThread().getName();
		String [] s = StringUtil.split(name, "#");
		if (s.length == 2)
		{
			id = Integer.valueOf(s[1]);
		}
		
		debugLog.debug("Thread " + name + "|" + id + ": run()");

		if ((id >= 1) && (id <= SpiderNum)) {
			spiderQuoteTask();
		} 
		else {
			quoteStorage();
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

	private void spiderQuoteTask() {
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

				if (TransactionTimeCheckTask.isTransactionTime) { // Begin
					for (StockItem stockItem : jobList) {
						String ua = "";
						QuoteItem quoteItem = SinaSpider.queryQuote(stockItem
								.getStockId(), ua);
						if (quoteItem != null) {
							if ((quoteItem.getStatus() == 3) || (quoteItem.getStatus() == 2))
							{
								debugLog.debug(stockItem
								.getStockId() + "|停牌");
							}
							addQuoteQueue(quoteItem);
						}

						Thread.sleep(1);
					}
				} 
				else {
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				debugLog.debug("Error in spiderQuoteTask()", e);
			}
		} // while
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
						QuoteStorage.insertSinaQuoteBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, quoteList);
						quoteList.clear();
					}
				}
				else
				{
					if (quoteList.size() > 0)
					{
						QuoteStorage.insertSinaQuoteBatch(TransactionTimeCheckTask.Month, TransactionTimeCheckTask.Day, quoteList);
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
