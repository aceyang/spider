package stock.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vc.pe.jutil.j4log.Logger;

public class TransactionTimeCheckTask extends java.util.TimerTask{

	private static final Logger log = Logger.getLogger("transaction_time");
	
	public static boolean isTransactionTime = false;
	
	public static boolean isTransactionEnd = false;
	
	public static boolean isStatTime = true;
	
	public static boolean isVacation = false;
	
	public static int Month = 0;
	
	public static int Day = 0;
	
	public static String TodayDate = ""; 
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		Month = cal.get(Calendar.MONTH) + 1;
		Day = cal.get(Calendar.DAY_OF_MONTH);
		
		TodayDate = sdf.format(cal.getTime());
		int index = cal.get(Calendar.DAY_OF_WEEK);
		if ((index == Calendar.SATURDAY) || (index == Calendar.SUNDAY)) {
			isVacation = true;
		}
		else
		{
			if (CommonService.isVacation(TodayDate))
			{
				isVacation = true;
			}
			else
			{
				isVacation = false;
			}
		}
		
		isTransactionEnd = false;
		int curTime = hour * 60 + min;
		if ((curTime >= 555) && (curTime <= 700))
		{
			isTransactionTime = true;
			isStatTime = false;
		}
		else if ((curTime >= 770) && (curTime <= 910))
		{
			isTransactionTime = true;
			isStatTime = false;
		}
		else{
			isTransactionTime = false;
			if (curTime >= 1000)
			{
				isTransactionEnd = true;
				isStatTime = true;
			}
		}
				
		log.info("TransactionTimeCheckTask|" + curTime + "|" + isTransactionTime + "|" + isVacation);
	}
}
