package stock.parse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import stock.item.*;
import stock.service.StatCenter;
import stock.stat.QuoteCollection;
import vc.pe.jutil.j4log.Logger;
import vc.pe.jutil.string.StringUtil;


public class SinaSpider {

	private static final Logger debugLog = Logger.getLogger("SinaSpider");

	private static HttpClient httpClient;

	static {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
		httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(32);
		httpConnectionManagerParams.setMaxTotalConnections(128);
		httpConnectionManagerParams.setConnectionTimeout(1000);
		connectionManager.setParams(httpConnectionManagerParams);
		httpClient = new HttpClient(connectionManager);
	}

	private static int switch2Shou(String strQuantum) {
		int quantum = Integer.valueOf(strQuantum);
		quantum = quantum / 100;
		return quantum;
	}

	public static QuoteItem queryQuote(String stockId, String ua) {
		String id = "";
		if (stockId.startsWith("0")) {
			id = "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			id = "sh" + stockId;
		}else{
			id = "sz" + stockId;
		}

		String url = "http://hq.sinajs.cn/list=" + id;

		// 使用GET方法,如果服务器需要通过HTTPS连接,那只需要将下面URL中的http换成https.
		HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", ua);

		// 设置超时
		HttpMethodParams httparams = new HttpMethodParams();
		httparams.setSoTimeout(1000);
		method.setParams(httparams);
		String content = "";
		try {
			long begin = System.currentTimeMillis();
			httpClient.executeMethod(method);
			long end = System.currentTimeMillis();
			StatCenter.report(stockId, (int)(end - begin));
			// 记录服务器返回的状态
			//String status = method.getStatusLine().toString();
			//debugLog.debug(url + "|" + status + "|" + (end - begin));
			

			// 解析返回的信息,并存储到数据库.
			content = new String(method.getResponseBody(), "gbk");
			method.releaseConnection();

			int beginIndex = content.indexOf("\"");
			int endIndex = content.lastIndexOf("\"");
			content = content.substring(beginIndex + 1, endIndex);

			String[] values = content.split("[,]");
			if (values.length < 33) {
				return null;
			}

			QuoteItem quoteItem = new QuoteItem();
			quoteItem.setBid1Amount(switch2Shou(values[10].trim()));
			quoteItem.setBid1Price(QuoteCollection.getPriceInt(values[11].trim()));
			quoteItem.setBid2Amount(switch2Shou(values[12].trim()));
			quoteItem.setBid2Price(QuoteCollection.getPriceInt(values[13].trim()));
			quoteItem.setBid3Amount(switch2Shou(values[14].trim()));
			quoteItem.setBid3Price(QuoteCollection.getPriceInt(values[15].trim()));
			quoteItem.setBid4Amount(switch2Shou(values[16].trim()));
			quoteItem.setBid4Price(QuoteCollection.getPriceInt(values[17].trim()));
			quoteItem.setBid5Amount(switch2Shou(values[18].trim()));
			quoteItem.setBid5Price(QuoteCollection.getPriceInt(values[19].trim()));
			quoteItem.setDate(values[30].trim());
			quoteItem.setSell1Amount(switch2Shou(values[20].trim()));
			quoteItem.setSell1Price(QuoteCollection.getPriceInt(values[21].trim()));
			quoteItem.setSell2Amount(switch2Shou(values[22].trim()));
			quoteItem.setSell2Price(QuoteCollection.getPriceInt(values[23].trim()));
			quoteItem.setSell3Amount(switch2Shou(values[24].trim()));
			quoteItem.setSell3Price(QuoteCollection.getPriceInt(values[25].trim()));
			quoteItem.setSell4Amount(switch2Shou(values[26].trim()));
			quoteItem.setSell4Price(QuoteCollection.getPriceInt(values[27].trim()));
			quoteItem.setSell5Amount(switch2Shou(values[28].trim()));
			quoteItem.setSell5Price(QuoteCollection.getPriceInt(values[29].trim()));
			quoteItem.setStockId(stockId);

			quoteItem.setTime(values[31]);
			
			int status = 0;
			try{
				status = Integer.valueOf(values[32]);
			}catch (Exception e) {
				status = 0;
			}
			quoteItem.setStatus(status);

			return quoteItem;
		} catch (Exception e) {
			method.releaseConnection();
			debugLog.debug("Exception in queryQuote()," + stockId + ":"
					+ content, e);
		}

		return null;
	}

	public static List<DealItem> queryDeal(String stockId, String ua) {
		List<DealItem> dealList = new ArrayList<DealItem>();
		String id = "";
		String content = "";
		if (stockId.startsWith("0")) {
			id = "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			id = "sh" + stockId;
		}
		else{
			id = "sz" + stockId;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String date = sdf.format(cal.getTime());
		stockId = stockId.toLowerCase();
		String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/CN_TransList.php?symbol="
				+ id;

		// 使用GET方法,如果服务器需要通过HTTPS连接,那只需要将下面URL中的http换成https.
		HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", ua);

		// 设置超时
		HttpMethodParams httparams = new HttpMethodParams();
		httparams.setSoTimeout(3000);
		method.setParams(httparams);

		try {
			httpClient.executeMethod(method);

			// 记录服务器返回的状态
			String status = method.getStatusLine().toString();
			debugLog.debug(url + "|" + status);
			content = method.getResponseBodyAsString();
			// 释放连接
			method.releaseConnection();

			String[] lines = content.split("[;]");
			for (int i = 1; i < lines.length - 1; i++) {
				int beginIndex = lines[i].indexOf("(");
				int endIndex = lines[i].indexOf(")");
				String data = lines[i].substring(beginIndex + 1, endIndex);
				String[] values = data.split("[,]");
				DealItem dealItem = new DealItem();

				dealItem.setDate(date);
				String price = values[2];
				price = price.trim();
				int len = price.length();
				price = price.substring(1, len - 1);
				price = price.trim();

				// 过滤价格为空
				if (price.equals("")) {
					continue;
				}

				dealItem.setPrice(QuoteCollection.getPriceInt(price));

				String quantum = values[1];
				quantum = quantum.trim();
				len = quantum.length();
				quantum = quantum.substring(1, len - 1);
				quantum = quantum.trim();

				// 过滤成交量为空
				if (quantum.equals("")) {
					continue;
				}
				dealItem.setQuantum(switch2Shou(quantum));

				dealItem.setStockId(stockId);

				String time = values[0];
				time = time.trim();
				len = time.length();
				time = time.substring(1, len - 1);
				time = time.trim();

				// 过滤时间为空
				if (time.equals("")) {
					continue;
				}

				dealItem.setTime(time);

				dealList.add(dealItem);
			}

		} catch (Exception e) {
			method.releaseConnection();
			debugLog.debug("Exception in queryDeal()," + stockId + ":"
					+ content, e);
		}

		return dealList;
	}

	
	
	
	public static List<DealItem> queryDealListByDate(String stockId, String date,String ua) {
		List<DealItem> dealList = new ArrayList<DealItem>();

		String id = "";
		if (stockId.startsWith("0")) {
			id = "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			id = "sh" + stockId;
		} else {
			id = "sz" + stockId;
		}
	
		String url = "http://market.finance.sina.com.cn/downxls.php?date="
				+ date + "&symbol=" + id;
		HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", ua);
		// 设置超时
		HttpMethodParams httparams = new HttpMethodParams();
		httparams.setSoTimeout(3000);
		method.setParams(httparams);
		long begin = System.currentTimeMillis();
		try {
			httpClient.executeMethod(method);
			long end = System.currentTimeMillis();
			
			String status = method.getStatusLine().toString();
			debugLog.debug(url + "|" + status + "|" + (end - begin));
			String content = new String(method.getResponseBody(), "gbk");
			// 释放连接
			method.releaseConnection();
			String [] lines = StringUtil.split(content, "\n");
			if ((lines != null) && (lines.length > 0))
			{
				for (int i = 0; i < lines.length; i++)
				{
					if (i >= 1)
					{
						String [] s = StringUtil.split(lines[i], "\t");
						if ((s != null) && (s.length >= 4))
						{
							DealItem deal = new DealItem();
							deal.setStockId(stockId);
							deal.setDate(date);
							deal.setTime(s[0]);
							deal.setPrice(QuoteCollection.getPriceInt(s[1].trim()));
							deal.setQuantum(Integer.valueOf(s[3].trim()));
							if (deal.getQuantum() > 0)
							{
								dealList.add(deal);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			method.releaseConnection();
			debugLog.debug("Exception in queryDealListByDate()", e);
		}

		return dealList;
	}
	
	
	public static List<DealItem> querySuperDealListByDate(String stockId, String date,String ua) {
		List<DealItem> dealList = new ArrayList<DealItem>();

		String id = "";
		if (stockId.startsWith("0")) {
			id = "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			id = "sh" + stockId;
		} else {
			id = "sz" + stockId;
		}
		String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/cn_bill_download.php?symbol="
				+ id + "&day=" + date;
		HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", ua);
		// 设置超时
		HttpMethodParams httparams = new HttpMethodParams();
		httparams.setSoTimeout(3000);
		method.setParams(httparams);
		long begin = System.currentTimeMillis();
		try {
			httpClient.executeMethod(method);
			long end = System.currentTimeMillis();
			
			String status = method.getStatusLine().toString();
			debugLog.debug(url + "|" + status + "|" + (end - begin));
			String content = new String(method.getResponseBody(), "gbk");
			// 释放连接
			method.releaseConnection();
			String [] lines = StringUtil.split(content, "\n");
			if ((lines != null) && (lines.length > 0))
			{
				for (int i = 0; i < lines.length; i++)
				{
					if (i >= 1)
					{
						String [] s = StringUtil.split(lines[i], ",");
						if ((s != null) && (s.length >= 5))
						{
							DealItem deal = new DealItem();
							deal.setStockId(stockId);
							deal.setDate(date);
							deal.setTime(s[2]);
							deal.setPrice(QuoteCollection.getPriceInt(s[3]));
							deal.setQuantum(switch2Shou(s[4]));
							if (deal.getQuantum() > 0)
							{
								dealList.add(deal);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			method.releaseConnection();
			debugLog.debug("Exception in querySuperDealListByDate()", e);
		}

		return dealList;
	}
	
	public static void main(String[] args) {
		/*
		QuoteItem quoteItem = SinaSpider.queryQuote("600824", "");
		if (quoteItem != null) {
			System.out.println(quoteItem.toString());
		}
		List<DealItem> dellList = SinaSpider.queryDeal("600824", "");
		if (dellList != null) {
			for (DealItem dealItem : dellList) {
				System.out.println(dealItem.toString());
				System.out.println("------------------------------");
			}
		}
		*/
	
		List<DealItem> dealList = queryDealListByDate("600824","2014-07-11","");
		if ((dealList != null) && (dealList.size() > 0))
		{
			for (DealItem deal : dealList)
			{
				System.out.println(deal.toString());
			}
		}
		
		System.exit(0);
	}
}
