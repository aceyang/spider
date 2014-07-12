package stock.common;

import java.util.Comparator;
import stock.item.QuoteVaryItem;

@SuppressWarnings("rawtypes")
public class QuoteVaryComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		QuoteVaryItem qv1 = (QuoteVaryItem) o1;
		QuoteVaryItem qv2 = (QuoteVaryItem) o2;

		if (qv1.getSellSum() > qv2.getSellSum()) {
			return 1;
		} else {
			if (qv1.getSellSum() == qv2.getSellSum()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
