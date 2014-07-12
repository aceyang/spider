package stock.item;

public class QuoteVaryItem {

	private String stockId = "";
	
	private String date = ""; // 日期
	
	private String time = ""; // 时间

	private int price = 0;
	
	private int quantum = 0;
	
	private String bidSell = ""; // 买卖标志
	
	private int totalTop1Times = 0;
	
	private int totalTop1Quantum = 0;
	
	private int totalTop2Times = 0;
	
	private int totalTop2Quantum = 0;
	
	private int totalTop3Times = 0;
	
	private int totalTop3Quantum = 0;
	
	private int bidTop1Times = 0;
	
	private int bidTop1Quantum = 0;
	
	private int bidTop2Times = 0;
	
	private int bidTop2Quantum = 0;
	
	private int bidTop3Times = 0;
	
	private int bidTop3Quantum = 0;

	private int sellTop1Times = 0;
	
	private int sellTop1Quantum = 0;
	
	private int sellTop2Times = 0;
	
	private int sellTop2Quantum = 0;
	
	private int sellTop3Times = 0;
	
	private int sellTop3Quantum = 0;

	public int getTotalTop1Times() {
		return totalTop1Times;
	}

	public void setTotalTop1Times(int totalTop1Times) {
		this.totalTop1Times = totalTop1Times;
	}

	public int getTotalTop1Quantum() {
		return totalTop1Quantum;
	}

	public void setTotalTop1Quantum(int totalTop1Quantum) {
		this.totalTop1Quantum = totalTop1Quantum;
	}

	public int getTotalTop2Times() {
		return totalTop2Times;
	}

	public void setTotalTop2Times(int totalTop2Times) {
		this.totalTop2Times = totalTop2Times;
	}

	public int getTotalTop2Quantum() {
		return totalTop2Quantum;
	}

	public void setTotalTop2Quantum(int totalTop2Quantum) {
		this.totalTop2Quantum = totalTop2Quantum;
	}

	public int getTotalTop3Times() {
		return totalTop3Times;
	}

	public void setTotalTop3Times(int totalTop3Times) {
		this.totalTop3Times = totalTop3Times;
	}

	public int getTotalTop3Quantum() {
		return totalTop3Quantum;
	}

	public void setTotalTop3Quantum(int totalTop3Quantum) {
		this.totalTop3Quantum = totalTop3Quantum;
	}

	public int getBidTop1Times() {
		return bidTop1Times;
	}

	public void setBidTop1Times(int bidTop1Times) {
		this.bidTop1Times = bidTop1Times;
	}

	public int getBidTop1Quantum() {
		return bidTop1Quantum;
	}

	public void setBidTop1Quantum(int bidTop1Quantum) {
		this.bidTop1Quantum = bidTop1Quantum;
	}

	public int getBidTop2Times() {
		return bidTop2Times;
	}

	public void setBidTop2Times(int bidTop2Times) {
		this.bidTop2Times = bidTop2Times;
	}

	public int getBidTop2Quantum() {
		return bidTop2Quantum;
	}

	public void setBidTop2Quantum(int bidTop2Quantum) {
		this.bidTop2Quantum = bidTop2Quantum;
	}

	public int getBidTop3Times() {
		return bidTop3Times;
	}

	public void setBidTop3Times(int bidTop3Times) {
		this.bidTop3Times = bidTop3Times;
	}

	public int getBidTop3Quantum() {
		return bidTop3Quantum;
	}

	public void setBidTop3Quantum(int bidTop3Quantum) {
		this.bidTop3Quantum = bidTop3Quantum;
	}

	public int getSellTop1Times() {
		return sellTop1Times;
	}

	public void setSellTop1Times(int sellTop1Times) {
		this.sellTop1Times = sellTop1Times;
	}

	public int getSellTop1Quantum() {
		return sellTop1Quantum;
	}

	public void setSellTop1Quantum(int sellTop1Quantum) {
		this.sellTop1Quantum = sellTop1Quantum;
	}

	public int getSellTop2Times() {
		return sellTop2Times;
	}

	public void setSellTop2Times(int sellTop2Times) {
		this.sellTop2Times = sellTop2Times;
	}

	public int getSellTop2Quantum() {
		return sellTop2Quantum;
	}

	public void setSellTop2Quantum(int sellTop2Quantum) {
		this.sellTop2Quantum = sellTop2Quantum;
	}

	public int getSellTop3Times() {
		return sellTop3Times;
	}

	public void setSellTop3Times(int sellTop3Times) {
		this.sellTop3Times = sellTop3Times;
	}

	public int getSellTop3Quantum() {
		return sellTop3Quantum;
	}

	public void setSellTop3Quantum(int sellTop3Quantum) {
		this.sellTop3Quantum = sellTop3Quantum;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getBidSell() {
		return bidSell;
	}

	public void setBidSell(String bidSell) {
		this.bidSell = bidSell;
	}
	
	public int getTotalSum()
	{
		return totalTop1Times + totalTop2Times + totalTop3Times;
	}
	
	public int getSellSum()
	{
		return sellTop1Times + sellTop2Times + sellTop3Times;
	}
}
