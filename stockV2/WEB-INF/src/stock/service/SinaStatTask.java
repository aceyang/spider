package stock.service;

import java.util.List;
import stock.db.QuoteStorage;
import stock.db.StockDB;
import stock.item.QuoteItem;
import stock.stat.QuoteCollection;
import stock.stat.TimeQuantumItem;
import vc.pe.jutil.j4log.Logger;

public class SinaStatTask extends Thread{

	private static final Logger quoteAnalyseLog = Logger.getLogger("QuoteAnalyse");
	
	private static final int QuantumVaryLimit = 500;
	
	private int month = 0;
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	private String stockId = "";
	
	private String date = "";
	
	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public SinaStatTask() {
	}
	
	public SinaStatTask(String stockId,String date,int month) {
		this.stockId = stockId;
		this.date = date;
		this.month = month;
	}
	
	private void doQuoteAnalyse(String stockId, String date) {
		quoteAnalyseLog.info("|doQuoteAnalyse|stockId:" + stockId + "|date:" + date);
		QuoteCollection quoteCollection = new QuoteCollection();

		List<QuoteItem> quoteList = QuoteStorage.getQuoteList(stockId, date, 0);
		for (QuoteItem quoteItem : quoteList) {
			if (stockId.startsWith("0")) {
				if (((quoteItem.getTime().compareTo("09:30:") >= 0) && (quoteItem
						.getTime().compareTo("11:30:") <= 0))
						|| ((quoteItem.getTime().compareTo("13:00:") >= 0) && (quoteItem
								.getTime().compareTo("14:57:") < 0))) {
					quoteCollection.addQuote(quoteItem);
				}
			} else if (stockId.startsWith("3")) {
				if (((quoteItem.getTime().compareTo("09:30:") >= 0) && (quoteItem
						.getTime().compareTo("11:30:") <= 0))
						|| ((quoteItem.getTime().compareTo("13:00:") >= 0) && (quoteItem
								.getTime().compareTo("14:57:") < 0))) {
					quoteCollection.addQuote(quoteItem);
				}
			} else {
				if (((quoteItem.getTime().compareTo("09:30:") >= 0) && (quoteItem
						.getTime().compareTo("11:30:") <= 0))
						|| ((quoteItem.getTime().compareTo("13:00:") >= 0) && (quoteItem
								.getTime().compareTo("15:00:") < 0))) {
					quoteCollection.addQuote(quoteItem);
				}
			}
		}

		int highPrice = quoteCollection.getQuoteHighPrice();
		int lowPrice = quoteCollection.getQuoteLowPrice();
		if ((lowPrice > 0) && (highPrice > 0)) {
			for (int price = lowPrice; price <= highPrice; price++) {
				List<TimeQuantumItem> bidList = quoteCollection
						.getBidList(price);
				if ((bidList != null) && (bidList.size() > 2)) {
					for (int i = 1; i <= bidList.size() - 1; i++) {
						int quantumvary = bidList.get(i).getQuantum()
								- bidList.get(i - 1).getQuantum();
						if (Math.abs(quantumvary) >= QuantumVaryLimit) {
							quoteAnalyseLog.info("B|" + date + "|"
									+ stockId + "|" + price + "|"
									+ bidList.get(i).getTime() + "|"
									+ quantumvary);
							QuoteStorage.insertSinaQuoteVary(stockId,
									date, bidList.get(i).getTime(), price,
									quantumvary, "B");
						}
					}
				}

				List<TimeQuantumItem> sellList = quoteCollection
						.getSellList(price);
				if ((sellList != null) && (sellList.size() > 2)) {
					for (int i = 1; i <= sellList.size() - 1; i++) {
						int quantumvary = sellList.get(i).getQuantum()
								- sellList.get(i - 1).getQuantum();
						if (Math.abs(quantumvary) >= QuantumVaryLimit) {
							quoteAnalyseLog.info("S|" + date + "|"
									+ stockId + "|" + price + "|"
									+ sellList.get(i).getTime() + "|"
									+ quantumvary);
							QuoteStorage.insertSinaQuoteVary(stockId,
									date, sellList.get(i).getTime(), price,
									quantumvary, "S");
						}
					}
				}
			}
		}

	}
	
	public void run() {
		// TODO Auto-generated method stub
		if (!TransactionTimeCheckTask.isVacation) {
			if (!TransactionTimeCheckTask.isStatTime)
			{
				return;
			}
		}
		
		try {
			if (!StockDB.isSinaQuoteStatDone(month, stockId, date))
			{
				doQuoteAnalyse(stockId,date);
				StockDB.logSinaQuoteStat(month, stockId, date);
			}
		}
		catch(Throwable t){
			
		}
	}
}
