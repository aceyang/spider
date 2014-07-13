package com.zgtx.parser.sina;

import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TradeHistoryParser {

	 public static int getPriceInt(String price)
	  {
	    int ret = 0;
	    int index = price.indexOf(".");
	    if (index == -1)
	    {
	    	price = price + ".00";
	    	index = price.indexOf(".");
	    }
	    
	    String strNum = price.substring(0, index);
	    int intNum = Integer.valueOf(strNum).intValue();
	    for (int i = 0; i < 2; i++) {
	      intNum *= 10;
	    }

	    String strDec = price.substring(index + 1);
	    int len = strDec.length();
	    while (len < 2) {
	      strDec = strDec + "0";
	      len = strDec.length();
	    }

	    int decNum = Integer.valueOf(strDec).intValue();
	    while (decNum > 100)
	    {
	    	decNum = decNum / 10;
	    }
	    ret = intNum + decNum;

	    return ret;
	  }
	 
	public static List<TradeHistory> parseSinaTradeHistory(String stockId,int year, int quarter) {
		List<TradeHistory> list = new ArrayList<TradeHistory>();
		try {
			String url = "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + stockId + ".phtml?year=" + year + "&jidu=" + quarter;
			Parser p = new Parser(url);
			p.setEncoding("gb2312");
			p.getConnection().setConnectTimeout(5000);
			p.getConnection().setReadTimeout(5000);
			
			NodeList tableList = p.parse(new NodeClassFilter(TableTag.class));
			for (int i = 0; i < tableList.size(); i++) {
				TableTag table = (TableTag) tableList.elementAt(i);
				String id = table.getAttribute("id");
				if (id == null)
				{
					continue;
				}
				if (!id.equalsIgnoreCase("FundHoldSharesTable"))
				{
					continue;
				}

				// 取得表中的行集
				TableRow[] rows = table.getRows();
				// 遍历每行
				for (int r = 0; r < rows.length; r++) {
					TableRow tr = rows[r];
					// 行中的列
					TableColumn[] td = tr.getColumns();
					if (td.length != 7)
					{
						continue;
					}
					String date = td[0].getStringText().replaceAll("<([^>]*)>", "").trim();
					if (date.equals("日期"))
					{
						continue;
					}
					int openPrice = getPriceInt(td[1].getStringText().replaceAll("<([^>]*)>", "").trim());
					int highPrice = getPriceInt(td[2].getStringText().replaceAll("<([^>]*)>", "").trim());
					int closePrice = getPriceInt(td[3].getStringText().replaceAll("<([^>]*)>", "").trim());
					int lowPrice = getPriceInt(td[4].getStringText().replaceAll("<([^>]*)>", "").trim());
					long tradingVolume  = Long.valueOf(td[5].getStringText().replaceAll("<([^>]*)>", "").trim());  //成交量
					long dealVolume  = Long.valueOf(td[6].getStringText().replaceAll("<([^>]*)>", "").trim());	//成交金额 tradeHistory = new TradeHistory();
					
					TradeHistory tradeHistory = new TradeHistory();
					tradeHistory.setClosePrice(closePrice);
					tradeHistory.setDate(date);
					tradeHistory.setDealVolume(dealVolume);
					tradeHistory.setHighPrice(highPrice);
					tradeHistory.setLowPrice(lowPrice);
					tradeHistory.setOpenPrice(openPrice);
					tradeHistory.setStockId(stockId);
					tradeHistory.setTradingVolume(tradingVolume);
					list.add(tradeHistory);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		List<TradeHistory> list = TradeHistoryParser.parseSinaTradeHistory("600824", 2014, 1);
		for (TradeHistory tradeHistory : list)
		{
			System.out.println(tradeHistory.toString());
		}
		
		System.exit(0);
	}
}
