package com.zgtx.parser.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import com.zgtx.common.client.ProxyItem;
import vc.pe.jutil.string.StringUtil;

public class CNProxyParser {

	public static List<ProxyItem> parseProxyList(String url) {
		List<ProxyItem> proxyList = new ArrayList<ProxyItem>();
		try {
			HashMap<String,String> transferHash = new HashMap<String,String>();
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

			Parser p = new Parser(responseContent);
			p.setEncoding("gb2312");
			NodeList scriptList = p.parse(new NodeClassFilter(ScriptTag.class));
			for (int i = 0; i < scriptList.size(); i++) {
				ScriptTag script = (ScriptTag) scriptList.elementAt(i);
				String content = script.getChildrenHTML();
				String [] s = StringUtil.split(content, ";");
				if ((s != null) && (s.length == 10))
				{
					for (int t = 0; t < s.length; t++)
					{
						String c = s[t].replaceAll("\"", "").trim();
						String [] v = StringUtil.split(c, "=");
						if ((v != null) && (v.length == 2))
						{
							transferHash.put(v[0], v[1]);
						}
					}
				}
			}
			
			p = new Parser(responseContent);
			p.setEncoding("gb2312");
			NodeList tableList = p.parse(new NodeClassFilter(TableTag.class));
			for (int t = 0; t < tableList.size(); t++) {
				TableTag table = (TableTag) tableList.elementAt(t);
				if (table.getChildrenHTML().indexOf("IP:Port") != -1)
				{
				
					// 取得表中的行集
					TableRow[] rows = table.getRows();
					// 遍历每行
					for (int r = 0; r < rows.length; r++) {
						TableRow tr = rows[r];
						TableColumn[] td = tr.getColumns();
						if ((td == null) || (td.length < 4))
						{
							continue;
						}
						
						String ip_port = td[0].getStringText().trim();
						String type = td[1].getStringText().trim();
						String remark = td[3].getStringText().trim();
						if (ip_port.indexOf("SCRIPT") == -1)
						{
							continue;
						}
						if (!type.equalsIgnoreCase("HTTP"))
						{
							continue;
						}
						
						int index = ip_port.indexOf("<");
						String ip = ip_port.substring(0,index);
						int form = ip_port.indexOf("(");
						int to = ip_port.indexOf(")");
						String port = ip_port.substring(form + 1,to);
						String [] s = StringUtil.split(port, "+");
						port = "";
						if (s != null)
						{
							for (int i = 0; i < s.length; i++)
							{
								if (transferHash.get(s[i]) != null)
								{
									port = port + transferHash.get(s[i]);
								}
							}
						}
						
						ProxyItem proxy = new ProxyItem();
						proxy.setIp(ip);
						proxy.setPort(Integer.valueOf(port));
						proxy.setRemark(remark);
						proxyList.add(proxy);
						
					}
				}
			}

		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return proxyList;
	}

	public static void main(String[] args) throws Exception {
		List<ProxyItem> proxyList = parseProxyList("http://www.cnproxy.com/proxy1.html");
		if (proxyList != null)
		{
			for (ProxyItem proxy : proxyList)
			{
				System.out.println(proxy.getIp() + "|" + proxy.getPort() + "|" + proxy.getRemark());
			}
		}
		
		System.exit(0);
	}
}
