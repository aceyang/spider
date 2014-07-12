package stock.service;

import stock.item.StatItem;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.jcache.Cache;
import vc.pe.jutil.jcache.CacheFactory;

public class StatCenter {

	private static Cache<String, StatItem> StatCache = CacheFactory
	.getCache("StatCache");
	
	private static final Logger statLog = Logger.getLogger("stat");
	
	private static final int StatUnit = 100;
	
	public static void report(String stockId, int useTime)
	{
		StatItem stat = StatCache.get(stockId);
		if (stat == null)
		{
			stat = new StatItem();
			stat.setStockId(stockId);
			StatCache.put(stockId, stat);
		}
		
		if (useTime < stat.getMinTime())
		{
			stat.setMinTime(useTime);
		}
		
		if (useTime > stat.getMaxTime())
		{
			stat.setMaxTime(useTime);
		}
		
		stat.setTimes(stat.getTimes() + 1);
		stat.setSum(stat.getSum() + useTime);
		if (stat.getTimes() >= StatUnit)
		{
			statLog.info("|" + stat.getStockId() + "|" + stat.getTimes() + "|" + stat.getMinTime() + "|" + stat.getMaxTime() + "|" + (stat.getSum() / stat.getTimes()));
			
			StatCache.remove(stockId);
		}
		
	}
	
}
