package stock.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import stock.db.StockDB;
import stock.item.StockItem;
import vc.pe.jutil.j4log.Logger;

public class SinaStatTaskDispatcher extends java.util.TimerTask{

	private static final Logger quoteAnalyseLog = Logger.getLogger("QuoteAnalyse");
	
	private static ThreadPoolExecutor StatTaskExecutor = new ThreadPoolExecutor(
            2, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
                    2000000));
	
	private static HashMap<String, String> VacationHash = new HashMap<String, String>();
	
	private static HashMap<String, String> ExclusiveHash = new HashMap<String, String>();

	static{
	}
	
	private static void addSinaStatTask(String stockId,String date,int month)
	{
		quoteAnalyseLog.info("|addSinaStatTask|stockId:" + stockId + "|date:" + date);
		SinaStatTask task = new SinaStatTask(stockId,date,month);
		StatTaskExecutor.execute(task);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<StockItem> stockList = CommonService.getStockPoolList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (!TransactionTimeCheckTask.isVacation) {
			if (!TransactionTimeCheckTask.isTransactionEnd)
			{
				return;
			}
		}
		
		String date = sdf.format(cal.getTime());
		while (date.compareTo("2014-02-24") >= 0)
		{
			int index = cal.get(Calendar.DAY_OF_WEEK);
			if ((index != Calendar.SATURDAY) && (index != Calendar.SUNDAY)) {
				if (VacationHash.get(date) == null)
				{
					int month = cal.get(Calendar.MONTH) + 1;
					for (StockItem stock : stockList)
					{
						if (!StockDB.isSinaQuoteStatDone(month, stock.getStockId(), date))
						{
							String key = stock.getStockId() + "@" + date;
							if (ExclusiveHash.get(key) == null)
							{
								addSinaStatTask(stock.getStockId(),date,month);
								ExclusiveHash.put(key, "");
							}
						}
					}
				}
			}
			
			cal.add(Calendar.DAY_OF_YEAR, -1);
			date = sdf.format(cal.getTime());
		}
	}
}
