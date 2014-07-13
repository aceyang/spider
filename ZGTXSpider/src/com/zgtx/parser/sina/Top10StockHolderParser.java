package com.zgtx.parser.sina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import com.zgtx.common.client.Top10StockHolderItem;

public class Top10StockHolderParser {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static boolean isDateFormat(String content)
	{
		try {
			sdf.parse(content);
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		
		return false;
	}
	
	
	public static List<String> parseSinaTop10StockHolder(String stockId) {
		List<String> list = new ArrayList<String>();
		try {
			String url = "http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CirculateStockHolder/stockid/" + stockId + ".phtml";
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
				if (!id.equalsIgnoreCase("CirculateShareholderTable"))
				{
					continue;
				}
				
				// 取得表中的行集
				TableRow[] rows = table.getRows();
				// 遍历每行
				for (int r = 0; r < rows.length; r++) {
					TableRow tr = rows[r];
					TableColumn[] td = tr.getColumns();
					// 行中的列
					for (int c = 0; c < td.length; c++) {
						String content = td[c].getStringText();
						content = content.replaceAll("<([^>]*)>", "").trim();
						if (content.equals("截止日期"))
						{
							continue;
						}
						if (content.equals("公告日期"))
						{
							break;
						}
						if (content.equals("编号"))
						{
							break;
						}
						if (content.equals("股东名称"))
						{
							break;
						}
						if (content.equals("股本性质"))
						{
							break;
						}
						if (content.indexOf("持股数量") != -1)
						{
							break;
						}
						if (content.indexOf("持股比例") != -1)
						{
							break;
						}
						if (content.equals(""))
						{
							continue;
						}
						
						list.add(content);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	
	public static Top10StockHolderItem buildTop10StockHolderItem(
			String stockId, String publishDate, List<String> list) {
		try {

			if (list.size() == 50) {
				Top10StockHolderItem top10StockHolderItem = new Top10StockHolderItem();
				top10StockHolderItem.setStockId(stockId);
				top10StockHolderItem.setPublishDate(publishDate);
				if (Integer.valueOf(list.get(0)) == 1) {
					top10StockHolderItem.setHolderName1(list.get(1));
					top10StockHolderItem.setQuantity1(Long.valueOf(list
							.get(2)));
					top10StockHolderItem.setPercentage1(list.get(3));
					top10StockHolderItem.setProperty1(list.get(4));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(5)) == 2) {
					top10StockHolderItem.setHolderName2(list.get(6));
					top10StockHolderItem.setQuantity2(Long.valueOf(list
							.get(7)));
					top10StockHolderItem.setPercentage2(list.get(8));
					top10StockHolderItem.setProperty2(list.get(9));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(10)) == 3) {
					top10StockHolderItem.setHolderName3(list.get(11));
					top10StockHolderItem.setQuantity3(Long.valueOf(list
							.get(12)));
					top10StockHolderItem.setPercentage3(list.get(13));
					top10StockHolderItem.setProperty3(list.get(14));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(15)) == 4) {
					top10StockHolderItem.setHolderName4(list.get(16));
					top10StockHolderItem.setQuantity4(Long.valueOf(list
							.get(17)));
					top10StockHolderItem.setPercentage4(list.get(18));
					top10StockHolderItem.setProperty4(list.get(19));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(20)) == 5) {
					top10StockHolderItem.setHolderName5(list.get(21));
					top10StockHolderItem.setQuantity5(Long.valueOf(list
							.get(22)));
					top10StockHolderItem.setPercentage5(list.get(23));
					top10StockHolderItem.setProperty5(list.get(24));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(25)) == 6) {
					top10StockHolderItem.setHolderName6(list.get(26));
					top10StockHolderItem.setQuantity6(Long.valueOf(list
							.get(27)));
					top10StockHolderItem.setPercentage6(list.get(28));
					top10StockHolderItem.setProperty6(list.get(29));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(30)) == 7) {
					top10StockHolderItem.setHolderName7(list.get(31));
					top10StockHolderItem.setQuantity7(Long.valueOf(list
							.get(32)));
					top10StockHolderItem.setPercentage7(list.get(33));
					top10StockHolderItem.setProperty7(list.get(34));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(35)) == 8) {
					top10StockHolderItem.setHolderName8(list.get(36));
					top10StockHolderItem.setQuantity8(Long.valueOf(list
							.get(37)));
					top10StockHolderItem.setPercentage8(list.get(38));
					top10StockHolderItem.setProperty8(list.get(39));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(40)) == 9) {
					top10StockHolderItem.setHolderName9(list.get(41));
					top10StockHolderItem.setQuantity9(Long.valueOf(list
							.get(42)));
					top10StockHolderItem.setPercentage9(list.get(43));
					top10StockHolderItem.setProperty9(list.get(44));
				}
				else
				{
					return null;
				}
				if (Integer.valueOf(list.get(45)) == 10) {
					top10StockHolderItem.setHolderName10(list.get(46));
					top10StockHolderItem.setQuantity10(Long.valueOf(list
							.get(47)));
					top10StockHolderItem.setPercentage10(list.get(48));
					top10StockHolderItem.setProperty10(list.get(49));
				}
				else
				{
					return null;
				}
				
				return top10StockHolderItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static List<Top10StockHolderItem> transformTop10StockHolderItemList(String stockId,List<String> contentList)
	{
		List<Top10StockHolderItem> top10StockHolderList = new ArrayList<Top10StockHolderItem>();
		
		String publishDate = "";
		publishDate = contentList.get(0);
		if (!isDateFormat(publishDate))
		{
			return top10StockHolderList;
		}
		int index = 1;
		List<String> list =  new ArrayList<String>();
		while (index < contentList.size())
		{
			String content = contentList.get(index);
			if (isDateFormat(content))
			{
				Top10StockHolderItem top10StockHolderItem = buildTop10StockHolderItem(stockId,publishDate,list);
				if (top10StockHolderItem != null)
				{
					top10StockHolderList.add(top10StockHolderItem);
				}
				publishDate = content;
				list =  new ArrayList<String>();
			}
			else
			{
				list.add(content);
			}
			index++;
		}
		
		
		
		return top10StockHolderList;
	}
	

	public static void main(String[] args) throws Exception {
	
	
		List<String> contentList = Top10StockHolderParser.parseSinaTop10StockHolder("000829");
		System.out.println("contentList:" + contentList.size());
		List<Top10StockHolderItem> top10StockHolderList = Top10StockHolderParser.transformTop10StockHolderItemList("000829", contentList);
		System.out.println("top10StockHolderList:" + top10StockHolderList.size());
		
		for (Top10StockHolderItem top10StockHolderItem : top10StockHolderList)
		{
			System.out.println(top10StockHolderItem.toString());
		}
		
	
		System.exit(0);
	}
}
