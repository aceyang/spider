package stock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import stock.db.CommonStorage;
import vc.pe.jutil.j4log.Logger;
import com.zgtx.common.client.ProxyItem;
import com.zgtx.parser.proxy.CNProxyParser;
import com.zgtx.parser.proxy.Proxy360Parser;


public class ProxyDaemon extends java.util.TimerTask {

	public static Timer timer = new Timer();

	private static final Logger infoLog = Logger.getLogger("ProxyDaemon");
	
	private static List<String> ProxyUrlList = new ArrayList<String>();
		
	static {
		ProxyUrlList.add("http://www.cnproxy.com/proxy1.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy2.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy3.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy4.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy5.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy6.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy7.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy8.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy9.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxy10.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxyedu1.html");
		ProxyUrlList.add("http://www.cnproxy.com/proxyedu2.html");
		
		timer.schedule(new ProxyDaemon(), 0, 1000 * 3600 * 12);
	}

	
	
	
	public static boolean doProxyTest(String proxyHost, int proxyPort) {
		boolean isOK = true;
		String url = "http://www.baidu.com";
		GetMethod get = new GetMethod(url);
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		get.addRequestHeader(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; QQDownload 627; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 2.0.50727; InfoPath.1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; 360SE)");
		get.setRequestHeader("Connection", "close");
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);

		try {
			client.executeMethod(get);
			String response = get.getResponseBodyAsString();
			if (response.indexOf("030173") == -1) {
				isOK = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			isOK = false;
		}

		get.releaseConnection();

		return isOK;
	}
	
	
	public static boolean doProxyTest(String proxyHost, int proxyPort, int times) {
		if (times <=0 )
		{
			times = 1;
		}
		for (int i = 1; i <= times; i++)
		{
			if (doProxyTest(proxyHost,proxyPort))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			infoLog.info("---ProxyDaemon---Begin");
			if (!TransactionTimeCheckTask.isVacation) {
				if (!TransactionTimeCheckTask.isTransactionEnd)
				{
					infoLog.info("---ProxyDaemon---NoTransactionEndTime---End");
					return;
				}
			}
			
			//检查现有代理是否正常
			HashMap<String,String> proxyHash = new HashMap<String,String>();
			List<ProxyItem> newNornalProxyList = new ArrayList<ProxyItem>();
		
			List<ProxyItem> proxyList = CommonService.getProxyList();
			if (proxyList != null)
			{
				for (ProxyItem proxy : proxyList)
				{
					if (doProxyTest(proxy.getIp(),proxy.getPort(),3))
					{
						proxyHash.put(proxy.getIp() + "@" + proxy.getPort(), "");
						newNornalProxyList.add(proxy);
						CommonStorage.updateProxyFailTimes(proxy.getIp(),proxy.getPort(), 0);
						infoLog.info("|OK|" + proxy.getIp() + "|" + proxy.getPort());
					}
					else
					{
						if (proxy.getFailTimes() >= 10)
						{
							infoLog.info("|Delete|" + proxy.getIp() + "|" + proxy.getPort());
							CommonStorage.delProxy(proxy.getIp(),proxy.getPort());
						}
						else
						{
							proxyHash.put(proxy.getIp() + "@" + proxy.getPort(), "");
							infoLog.info("|Fail|" + proxy.getIp() + "|" + proxy.getPort());
							CommonStorage.updateProxyFailTimes(proxy.getIp(),proxy.getPort(), proxy.getFailTimes() + 1);
						}
					}
				}
			}
			
			//获取新增代理
			/*
			for (String url : ProxyUrlList)
			{
				List<ProxyItem> newProxyList = CNProxyParser.parseProxyList(url);
				if (newProxyList != null)
				{
					for (ProxyItem proxy : newProxyList)
					{
						if (proxyHash.get(proxy.getIp() + "@" + proxy.getPort()) != null)
						{
							infoLog.info("|Old|" + proxy.getIp() + "|" + proxy.getPort());
						}
						else
						{
							if (doProxyTest(proxy.getIp(),proxy.getPort(),3))
							{
								infoLog.info("|New|" + proxy.getIp() + "|" + proxy.getPort());
								CommonStorage.addProxy(proxy.getIp(),proxy.getPort(), proxy.getRemark());
							}
							else
							{
								infoLog.info("|Invalid|" + proxy.getIp() + "|" + proxy.getPort());
							}
						}
					}
				}
			}
			*/
			List<ProxyItem> proxy360List = Proxy360Parser.parseProxyList("http://www.proxy360.cn/Proxy");
			if (proxy360List != null)
			{
				for (ProxyItem proxy : proxy360List)
				{
					if (proxyHash.get(proxy.getIp() + "@" + proxy.getPort()) != null)
					{
						infoLog.info("|Old|" + proxy.getIp() + "|" + proxy.getPort());
					}
					else
					{
						if (doProxyTest(proxy.getIp(),proxy.getPort(),3))
						{
							infoLog.info("|New|" + proxy.getIp() + "|" + proxy.getPort());
							CommonStorage.addProxy(proxy.getIp(),proxy.getPort(), proxy.getRemark());
						}
						else
						{
							infoLog.info("|Invalid|" + proxy.getIp() + "|" + proxy.getPort());
						}
					}
				}
			}
			CommonService.reloadProxy();
			infoLog.info("---ProxyDaemon---End");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
