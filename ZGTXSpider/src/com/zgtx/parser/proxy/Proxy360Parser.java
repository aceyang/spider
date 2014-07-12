package com.zgtx.parser.proxy;

import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import com.zgtx.common.client.ProxyItem;

public class Proxy360Parser {

	public static List<ProxyItem> parseProxyList(String url) {
		List<ProxyItem> proxyList = new ArrayList<ProxyItem>();
		try {
			Parser p = new Parser(url);
			p.setEncoding("utf-8");
			p.getConnection().setConnectTimeout(10000);
			p.getConnection().setReadTimeout(10000);

			NodeList divList = p.parse(new NodeClassFilter(Div.class));
			if (divList != null) {
				for (int d = 0; d < divList.size(); d++) {
					Div div = (Div) divList.elementAt(d);
					String className = div.getAttribute("class");
					if (className == null) {
						continue;
					}

					if (!className.equals("proxylistitem")){
						continue;
					}

					Parser spanParser = Parser.createParser(div.getStringText(), "utf-8");
					NodeList spanList = spanParser.parse(new NodeClassFilter(Span.class));;
					if ((spanList != null) && (spanList.size() >= 4))
					{
						ProxyItem proxy = new ProxyItem();
						proxy.setIp(((Span)spanList.elementAt(0)).getStringText().trim());
						proxy.setPort(Integer.valueOf(((Span)spanList.elementAt(1)).getStringText().trim()));
						proxy.setRemark(((Span)spanList.elementAt(3)).getStringText().trim());
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
		List<ProxyItem> proxyList = parseProxyList("http://www.proxy360.cn/Proxy");
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
