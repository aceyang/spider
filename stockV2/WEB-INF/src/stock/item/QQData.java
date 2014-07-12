package stock.item;

import java.util.ArrayList;
import java.util.List;

public class QQData {

	private QuoteItem quoteItem = new QuoteItem();

	private List<DealItem> dellList = new ArrayList<DealItem>();

	public QuoteItem getQuoteItem() {
		return quoteItem;
	}

	public void setQuoteItem(QuoteItem quoteItem) {
		this.quoteItem = quoteItem;
	}

	public List<DealItem> getDellList() {
		return dellList;
	}

	public void setDellList(List<DealItem> dellList) {
		this.dellList = dellList;
	}

}
