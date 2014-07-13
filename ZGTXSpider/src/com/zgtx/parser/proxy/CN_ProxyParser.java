package com.zgtx.parser.proxy;

import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.zgtx.common.client.ProxyItem;

public class CN_ProxyParser {

	public static List<ProxyItem> parseProxyList(String url) {
		List<ProxyItem> proxyList = new ArrayList<ProxyItem>();
		try {
			Parser p = new Parser(url);
			p.setEncoding("utf-8");
			p.getConnection().setConnectTimeout(10000);
			p.getConnection().setReadTimeout(10000);

			NodeList tableList = p.parse(new NodeClassFilter(TableTag.class));
			
			if (tableList != null) {
				for (int t = 0; t < tableList.size(); t++) {
					TableTag table = (TableTag) tableList.elementAt(t);
					// 取得表中的行集
					TableRow[] rows = table.getRows();
					// 遍历每行
					for (int r = 0; r < rows.length; r++) {
						TableRow tr = rows[r];
						TableColumn[] td = tr.getColumns();
						if ((td == null) || (td.length < 5))
						{
							continue;
						}
						if (td[1].getStringText().trim().indexOf("端口") != -1)
						{
							continue;
						}
						
						ProxyItem proxy = new ProxyItem();
						proxy.setIp(td[0].getStringText().trim());
						proxy.setPort(Integer.valueOf(td[1].getStringText().trim()));
						proxy.setRemark(td[2].getStringText().trim());
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
		List<ProxyItem> proxyList = parseProxyList("http://cn-proxy.com/");
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
