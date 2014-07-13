package stock.item;

public class TradeHistoryStat {

	private String stockId = "";
	
	private String date = "";
	
	private int averageDealPrice5 = 0;
	
	private long averageDealVolume5 = 0;
	
	private int averageDealPrice20 = 0;
	
	private long averageDealVolume20 = 0;
	
	private int averageDealPrice60 = 0;
	
	private long averageDealVolume60 = 0;

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

	public int getAverageDealPrice5() {
		return averageDealPrice5;
	}

	public void setAverageDealPrice5(int averageDealPrice5) {
		this.averageDealPrice5 = averageDealPrice5;
	}

	public long getAverageDealVolume5() {
		return averageDealVolume5;
	}

	public void setAverageDealVolume5(long averageDealVolume5) {
		this.averageDealVolume5 = averageDealVolume5;
	}

	public int getAverageDealPrice20() {
		return averageDealPrice20;
	}

	public void setAverageDealPrice20(int averageDealPrice20) {
		this.averageDealPrice20 = averageDealPrice20;
	}

	public long getAverageDealVolume20() {
		return averageDealVolume20;
	}

	public void setAverageDealVolume20(long averageDealVolume20) {
		this.averageDealVolume20 = averageDealVolume20;
	}

	public int getAverageDealPrice60() {
		return averageDealPrice60;
	}

	public void setAverageDealPrice60(int averageDealPrice60) {
		this.averageDealPrice60 = averageDealPrice60;
	}

	public long getAverageDealVolume60() {
		return averageDealVolume60;
	}

	public void setAverageDealVolume60(long averageDealVolume60) {
		this.averageDealVolume60 = averageDealVolume60;
	}
	
	
}
