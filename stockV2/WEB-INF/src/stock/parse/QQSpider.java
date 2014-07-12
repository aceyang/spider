package stock.parse;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import stock.item.*;
import stock.stat.QuoteCollection;
import vc.pe.jutil.j4log.Logger;

public class QQSpider {

	private static final Logger debugLog = Logger.getLogger("QQSpider");

	private static HttpClient httpClient;

	static {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
		httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(32);
		httpConnectionManagerParams.setMaxTotalConnections(128);
		connectionManager.setParams(httpConnectionManagerParams);
		httpClient = new HttpClient(connectionManager);
	}

	public static QQData query(String stockId, String ua) {
		QQData qqData = new QQData();
		String content = "";
		stockId = stockId.toLowerCase();
		String prefix = "";
		if (stockId.startsWith("0")) {
			prefix = "sz";
		} else if (stockId.startsWith("6")) {
			prefix = "sh";
		}

		String url = "http://qt.gtimg.cn/q=" + prefix + stockId;
		// 使用GET方法,如果服务器需要通过HTTPS连接,那只需要将下面URL中的http换成https.
		HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", ua);

		// 设置超时
		HttpMethodParams httparams = new HttpMethodParams();
		httparams.setSoTimeout(2000);
		method.setParams(httparams);

		try {
			httpClient.executeMethod(method);

			// 记录服务器返回的状态
			String status = method.getStatusLine().toString();
			debugLog.debug(url + "|" + status);

			// 解析返回的信息,并存储到数据库.
			content = new String(method.getResponseBody(), "gbk");
			// 释放连接
			method.releaseConnection();
			int beginIndex = content.indexOf("\"");
			int endIndex = content.lastIndexOf("\"");

			if ((beginIndex == -1) || (endIndex == -1)) {
				return null;
			}

			if (beginIndex == endIndex) {
				content = new String(content.substring(beginIndex + 1));
			} else {
				content = new String(
						content.substring(beginIndex + 1, endIndex));
			}

			content = content.trim();
			if (content.equals("")) {
				return null;
			}

			String[] values = content.split("[~]");

			if (values.length <= 40) {
				return null;
			}

			String year = values[30].substring(0, 4);
			String month = values[30].substring(4, 6);
			String day = values[30].substring(6, 8);
			String hour = values[30].substring(8, 10);
			String min = values[30].substring(10, 12);
			String second = values[30].substring(12, 14);

			String date = year + "-" + month + "-" + day;
			String time = hour + ":" + min + ":" + second;

			// 判断是否为当天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String curDate = sdf.format(cal.getTime());

			if (!date.equals(curDate)) {
				return null;
			}

			QuoteItem quoteItem = new QuoteItem();
			quoteItem.setBid1Amount(Integer.valueOf(values[10].trim())); // 买一数量
			quoteItem.setBid1Price(QuoteCollection.getPriceInt(values[9].trim())); // 买一价格
			quoteItem.setBid2Amount(Integer.valueOf(values[12].trim())); // 买二数量
			quoteItem.setBid2Price(QuoteCollection.getPriceInt(values[11].trim())); // 买二价格
			quoteItem.setBid3Amount(Integer.valueOf(values[14].trim())); // 买三数量
			quoteItem.setBid3Price(QuoteCollection.getPriceInt(values[13].trim())); // 买三价格
			quoteItem.setBid4Amount(Integer.valueOf(values[16].trim())); // 买四数量
			quoteItem.setBid4Price(QuoteCollection.getPriceInt(values[15].trim())); // 买四价格
			quoteItem.setBid5Amount(Integer.valueOf(values[18].trim())); // 买五数量
			quoteItem.setBid5Price(QuoteCollection.getPriceInt(values[17].trim())); // 买五价格
			quoteItem.setDate(date); // 日期
			quoteItem.setSell1Amount(Integer.valueOf(values[20].trim())); // 卖一数量
			quoteItem.setSell1Price(QuoteCollection.getPriceInt(values[19].trim())); // 卖一价格
			quoteItem.setSell2Amount(Integer.valueOf(values[22].trim())); // 卖二数量
			quoteItem.setSell2Price(QuoteCollection.getPriceInt(values[21].trim())); // 卖二价格
			quoteItem.setSell3Amount(Integer.valueOf(values[24].trim())); // 卖三数量
			quoteItem.setSell3Price(QuoteCollection.getPriceInt(values[23].trim())); // 卖三价格
			quoteItem.setSell4Amount(Integer.valueOf(values[26].trim())); // 卖四数量
			quoteItem.setSell4Price(QuoteCollection.getPriceInt(values[25].trim())); // 卖四价格
			quoteItem.setSell5Amount(Integer.valueOf(values[28].trim())); // 卖五数量
			quoteItem.setSell5Price(QuoteCollection.getPriceInt(values[27].trim())); // 卖五价格
			quoteItem.setStockId(values[2].trim()); // 股票代码
			quoteItem.setTime(time); // 时间

			List<DealItem> dellList = new ArrayList<DealItem>();
			String deals = values[29].trim();

			if (!deals.equals("")) {
				String[] parts = deals.split("[|]"); // 分离每笔交易信息
				for (int j = 0; j < parts.length; j++) {
					String[] v = parts[j].trim().split("[/]"); // 获取每笔交易详细信息
					DealItem dealItem = new DealItem();
					dealItem.setDate(date);
					dealItem.setPrice(QuoteCollection.getPriceInt(v[1].trim()));
					dealItem.setQuantum(Integer.valueOf(v[2]));
					dealItem.setStockId(stockId);
					dealItem.setTime(v[0]);
					dellList.add(dealItem);
				}
			}

			qqData.setDellList(dellList);
			qqData.setQuoteItem(quoteItem);

		} catch (Throwable e) {
			debugLog.debug("Exception in query()," + stockId + ":" + content, e);
		}

		return qqData;
	}

	public static void main(String[] args) {
		QQData date = QQSpider.query("600824", "");
		if (date != null) {
			System.out.println(date.getQuoteItem().toString());
			List<DealItem> dellList = date.getDellList();
			if (dellList != null) {
				for (DealItem dealItem : dellList) {
					System.out.println(dealItem.toString());
					System.out.println("------------------------------");
				}
			}
		}

		System.exit(0);
	}
}
