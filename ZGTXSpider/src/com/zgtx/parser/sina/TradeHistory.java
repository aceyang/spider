package com.zgtx.parser.sina;

public class TradeHistory {

	private String stockId = "";
	
	private String date = "";
	
	private int openPrice = 0;
	
	private int closePrice = 0;
	
	private int highPrice = 0;
	
	private int lowPrice = 0;
	
	private long tradingVolume  = 0;  //成交量
	
	private long dealVolume  = 0;	//成交金额

	public long getTradingVolume() {
		return tradingVolume;
	}

	public void setTradingVolume(long tradingVolume) {
		this.tradingVolume = tradingVolume;
	}

	public long getDealVolume() {
		return dealVolume;
	}

	public void setDealVolume(long dealVolume) {
		this.dealVolume = dealVolume;
	}

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

	public int getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(int openPrice) {
		this.openPrice = openPrice;
	}

	public int getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(int closePrice) {
		this.closePrice = closePrice;
	}

	public int getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(int highPrice) {
		this.highPrice = highPrice;
	}

	public int getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(int lowPrice) {
		this.lowPrice = lowPrice;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stockId:" + stockId + "\n");
		sb.append("date:" + date + "\n");
		sb.append("openPrice:" + openPrice + "\n");
		sb.append("closePrice:" + closePrice + "\n");
		sb.append("highPrice:" + highPrice + "\n");
		sb.append("lowPrice:" + lowPrice + "\n");
		sb.append("tradingVolume:" + tradingVolume + "\n");
		sb.append("dealVolume:" + dealVolume + "\n");
		
		return sb.toString();
	}
}
