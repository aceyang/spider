package stock.item;

public class DealItem {

	private String stockId = ""; // 股票代码

	private int price = 0; // 价格(单位:分)

	private int quantum = 0; // 成交量

	private String time = ""; // 日期

	private String date = ""; // 时间

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stockId:" + stockId + "\n");
		sb.append("date:" + date + "\n");
		sb.append("time:" + time + "\n");
		sb.append("quantum:" + quantum + "\n");
		sb.append("price:" + price + "\n");

		return sb.toString();
	}
}
