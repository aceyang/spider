package com.zgtx.parser.sina;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import vc.pe.jutil.string.StringUtil;
import vc.pe.jutil.util.Pair;


public class FinanceInfoParser {

	public static Pair<Long,Long> queryFinanceInfo(String stockId) {
		String id = "";
		if (stockId.startsWith("0")) {
			id = "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			id = "sh" + stockId;
		}else{
			id = "sz" + stockId;
		}
		long annualProfit = 0;
		long recentFourQuarterProfit = 0;
		
		try {
			String url = "http://finance.sina.com.cn/realstock/company/" + id + "/jsvar.js";
			
			String responseContent =  "";
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000);

			GetMethod get = new GetMethod(url);
			get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
			get.addRequestHeader(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB6; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 2.0.50727; InfoPath.1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			try {
				client.executeMethod(get);
				responseContent = new String(get.getResponseBody(), "gb2312");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			get.releaseConnection();
			String [] s = StringUtil.split(responseContent, ";");
			if ((s != null) && (s.length > 0))
			{
				for (int i = 0; i < s.length; i++)
				{
					String v = StringUtil.replaceAll(s[i], " ", "");
					if (v.indexOf("profit=") != -1)
					{
						String [] w = StringUtil.split(v, "profit=");
						annualProfit = (long)(Double.valueOf(w[1]) * 100000000);
					}
					else if (v.indexOf("profit_four=") != -1)
					{
						String [] w = StringUtil.split(v, "profit_four=");
						recentFourQuarterProfit = (long)(Double.valueOf(w[1]) * 100000000);
					}
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Pair.makePair(recentFourQuarterProfit, annualProfit);
	}

	public static void main(String[] args) throws Exception {
		 Pair<Long,Long> p = queryFinanceInfo("002114");
		 System.out.println(p.first + "|" + p.second);
		
		System.exit(0);
	}
}
