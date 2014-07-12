package stock.common;

import java.util.Comparator;
import vc.pe.jutil.util.Pair;


@SuppressWarnings("rawtypes")
public class PairComparator implements Comparator {

	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {

		Pair<Integer, Integer> p1 = (Pair<Integer, Integer>) o1;
		Pair<Integer, Integer> p2 = (Pair<Integer, Integer>) o2;

		if (p1.second > p2.second) {
			return 1;
		} else {
			if (p1.second == p2.second) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
