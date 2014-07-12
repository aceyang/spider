package stock.item;

public class QuoteItem {

	private String stockId = ""; // 股票代码

	private int bid1Amount = 0; // 买一数量（单位：手）

	private int bid1Price = 0; // 买一价格（单位：分）

	private int bid2Amount = 0; // 买二数量

	private int bid2Price = 0; // 买二价格

	private int bid3Amount = 0; // 买三数量

	private int bid3Price = 0; // 买三价格

	private int bid4Amount = 0; // 买四数量

	private int bid4Price = 0; // 买四价格

	private int bid5Amount = 0; // 买五数量

	private int bid5Price = 0; // 买五价格

	private int sell1Amount = 0; // 卖一数量

	private int sell1Price = 0; // 卖一价格

	private int sell2Amount = 0; // 卖二数量

	private int sell2Price = 0; // 卖二价格

	private int sell3Amount = 0; // 卖三数量

	private int sell3Price = 0; // 卖三价格

	private int sell4Amount = 0; // 卖四数量

	private int sell4Price = 0; // 卖四价格

	private int sell5Amount = 0; // 卖五数量

	private int sell5Price = 0; // 卖五价格

	private String time = ""; // 日期

	private String date = ""; // 时间
	
	private Integer status = 0;  //新浪 0:正常  3：停牌

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public int getBid1Amount() {
		return bid1Amount;
	}

	public void setBid1Amount(int bid1Amount) {
		this.bid1Amount = bid1Amount;
	}

	public int getBid1Price() {
		return bid1Price;
	}

	public void setBid1Price(int bid1Price) {
		this.bid1Price = bid1Price;
	}

	public int getBid2Amount() {
		return bid2Amount;
	}

	public void setBid2Amount(int bid2Amount) {
		this.bid2Amount = bid2Amount;
	}

	public int getBid2Price() {
		return bid2Price;
	}

	public void setBid2Price(int bid2Price) {
		this.bid2Price = bid2Price;
	}

	public int getBid3Amount() {
		return bid3Amount;
	}

	public void setBid3Amount(int bid3Amount) {
		this.bid3Amount = bid3Amount;
	}

	public int getBid3Price() {
		return bid3Price;
	}

	public void setBid3Price(int bid3Price) {
		this.bid3Price = bid3Price;
	}

	public int getBid4Amount() {
		return bid4Amount;
	}

	public void setBid4Amount(int bid4Amount) {
		this.bid4Amount = bid4Amount;
	}

	public int getBid4Price() {
		return bid4Price;
	}

	public void setBid4Price(int bid4Price) {
		this.bid4Price = bid4Price;
	}

	public int getBid5Amount() {
		return bid5Amount;
	}

	public void setBid5Amount(int bid5Amount) {
		this.bid5Amount = bid5Amount;
	}

	public int getBid5Price() {
		return bid5Price;
	}

	public void setBid5Price(int bid5Price) {
		this.bid5Price = bid5Price;
	}

	public int getSell1Amount() {
		return sell1Amount;
	}

	public void setSell1Amount(int sell1Amount) {
		this.sell1Amount = sell1Amount;
	}

	public int getSell1Price() {
		return sell1Price;
	}

	public void setSell1Price(int sell1Price) {
		this.sell1Price = sell1Price;
	}

	public int getSell2Amount() {
		return sell2Amount;
	}

	public void setSell2Amount(int sell2Amount) {
		this.sell2Amount = sell2Amount;
	}

	public int getSell2Price() {
		return sell2Price;
	}

	public void setSell2Price(int sell2Price) {
		this.sell2Price = sell2Price;
	}

	public int getSell3Amount() {
		return sell3Amount;
	}

	public void setSell3Amount(int sell3Amount) {
		this.sell3Amount = sell3Amount;
	}

	public int getSell3Price() {
		return sell3Price;
	}

	public void setSell3Price(int sell3Price) {
		this.sell3Price = sell3Price;
	}

	public int getSell4Amount() {
		return sell4Amount;
	}

	public void setSell4Amount(int sell4Amount) {
		this.sell4Amount = sell4Amount;
	}

	public int getSell4Price() {
		return sell4Price;
	}

	public void setSell4Price(int sell4Price) {
		this.sell4Price = sell4Price;
	}

	public int getSell5Amount() {
		return sell5Amount;
	}

	public void setSell5Amount(int sell5Amount) {
		this.sell5Amount = sell5Amount;
	}

	public int getSell5Price() {
		return sell5Price;
	}

	public void setSell5Price(int sell5Price) {
		this.sell5Price = sell5Price;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stockId:" + stockId + "\n");
		sb.append("sell5Price:" + sell5Price + "\n");
		sb.append("sell5Amount:" + sell5Amount + "\n");
		sb.append("sell4Price:" + sell4Price + "\n");
		sb.append("sell4Amount:" + sell4Amount + "\n");
		sb.append("sell3Price:" + sell3Price + "\n");
		sb.append("sell3Amount:" + sell3Amount + "\n");
		sb.append("sell2Price:" + sell2Price + "\n");
		sb.append("sell2Amount:" + sell2Amount + "\n");
		sb.append("sell1Price:" + sell1Price + "\n");
		sb.append("sell1Amount:" + sell1Amount + "\n");
		sb.append("bid1Price:" + bid1Price + "\n");
		sb.append("bid1Amount:" + bid1Amount + "\n");
		sb.append("bid2Price:" + bid2Price + "\n");
		sb.append("bid2Amount:" + bid2Amount + "\n");
		sb.append("bid3Price:" + bid3Price + "\n");
		sb.append("bid3Amount:" + bid3Amount + "\n");
		sb.append("bid4Price:" + bid4Price + "\n");
		sb.append("bid4Amount:" + bid4Amount + "\n");
		sb.append("bid5Price:" + bid5Price + "\n");
		sb.append("bid5Amount:" + bid5Amount + "\n");
		sb.append("date:" + date + "\n");
		sb.append("time:" + time + "\n");

		return sb.toString();
	}

}
