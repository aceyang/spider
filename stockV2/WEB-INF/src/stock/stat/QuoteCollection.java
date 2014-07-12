package stock.stat;

import java.util.HashMap;
import java.util.List;

import stock.item.QuoteItem;

public class QuoteCollection
{
  private int quoteHighPrice = 0;

  private int quoteLowPrice = 0;

  private HashMap<Integer, PriceCollection> quotePriceHash = new HashMap<Integer, PriceCollection>();

  public static String priceToString(int price) {
    String strPrice = "";
    int intNum = price / 100;
    int decNum = price % 100;
    String strDecNum = String.valueOf(decNum);
    while (strDecNum.length() < 2) {
      strDecNum = "0" + strDecNum;
    }

    strPrice = intNum + "." + strDecNum;

    return strPrice;
  }

  private PriceCollection getPriceCollection(int price) {
    PriceCollection priceCollection = (PriceCollection)this.quotePriceHash.get(Integer.valueOf(price));
    if (priceCollection == null) {
      priceCollection = new PriceCollection();
      this.quotePriceHash.put(Integer.valueOf(price), priceCollection);
    }

    return priceCollection;
  }

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

  public void addQuote(QuoteItem quoteItem) {
    int sell5Price = quoteItem.getSell5Price();
    if (sell5Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = sell5Price;
      }
      else if (sell5Price > this.quoteHighPrice) {
        this.quoteHighPrice = sell5Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = sell5Price;
      }
      else if (sell5Price < this.quoteLowPrice) {
        this.quoteLowPrice = sell5Price;
      }

    }

    int sell4Price = quoteItem.getSell4Price();
    if (sell4Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = sell4Price;
      }
      else if (sell4Price > this.quoteHighPrice) {
        this.quoteHighPrice = sell4Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = sell4Price;
      }
      else if (sell4Price < this.quoteLowPrice) {
        this.quoteLowPrice = sell4Price;
      }

    }

    int sell3Price = quoteItem.getSell3Price();
    if (sell3Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = sell3Price;
      }
      else if (sell3Price > this.quoteHighPrice) {
        this.quoteHighPrice = sell3Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = sell3Price;
      }
      else if (sell3Price < this.quoteLowPrice) {
        this.quoteLowPrice = sell3Price;
      }

    }

    int sell2Price = quoteItem.getSell2Price();
    if (sell2Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = sell2Price;
      }
      else if (sell2Price > this.quoteHighPrice) {
        this.quoteHighPrice = sell2Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = sell2Price;
      }
      else if (sell2Price < this.quoteLowPrice) {
        this.quoteLowPrice = sell2Price;
      }

    }

    int sell1Price = quoteItem.getSell1Price();
    if (sell1Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = sell1Price;
      }
      else if (sell1Price > this.quoteHighPrice) {
        this.quoteHighPrice = sell1Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = sell1Price;
      }
      else if (sell1Price < this.quoteLowPrice) {
        this.quoteLowPrice = sell1Price;
      }

    }

    int bid1Price = quoteItem.getBid1Price();
    if (bid1Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = bid1Price;
      }
      else if (bid1Price > this.quoteHighPrice) {
        this.quoteHighPrice = bid1Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = bid1Price;
      }
      else if (bid1Price < this.quoteLowPrice) {
        this.quoteLowPrice = bid1Price;
      }

    }

    int bid2Price = quoteItem.getBid2Price();
    if (bid2Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = bid2Price;
      }
      else if (bid2Price > this.quoteHighPrice) {
        this.quoteHighPrice = bid2Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = bid2Price;
      }
      else if (bid2Price < this.quoteLowPrice) {
        this.quoteLowPrice = bid2Price;
      }

    }

    int bid3Price = quoteItem.getBid3Price();
    if (bid3Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = bid3Price;
      }
      else if (bid3Price > this.quoteHighPrice) {
        this.quoteHighPrice = bid3Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = bid3Price;
      }
      else if (bid3Price < this.quoteLowPrice) {
        this.quoteLowPrice = bid3Price;
      }

    }

    int bid4Price = quoteItem.getBid4Price();
    if (bid4Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = bid4Price;
      }
      else if (bid4Price > this.quoteHighPrice) {
        this.quoteHighPrice = bid4Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = bid4Price;
      }
      else if (bid4Price < this.quoteLowPrice) {
        this.quoteLowPrice = bid4Price;
      }

    }

    int bid5Price = quoteItem.getBid5Price();
    if (bid5Price > 0)
    {
      if (this.quoteHighPrice == 0) {
        this.quoteHighPrice = bid5Price;
      }
      else if (bid5Price > this.quoteHighPrice) {
        this.quoteHighPrice = bid5Price;
      }

      if (this.quoteLowPrice == 0) {
        this.quoteLowPrice = bid5Price;
      }
      else if (bid5Price < this.quoteLowPrice) {
        this.quoteLowPrice = bid5Price;
      }

    }

    if (sell1Price == 0) {
      if ((this.quoteLowPrice > 0) && (this.quoteHighPrice > 0))
        for (int i = this.quoteLowPrice; i <= this.quoteHighPrice; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setPrice(i);
          timeQuantumItem.setTime(quoteItem.getTime());
          priceCollection.addSellQuote(timeQuantumItem);
        }
    }
    else
    {
      if (this.quoteLowPrice > 0)
      {
        int begin = this.quoteLowPrice;
        int end = sell1Price;
        for (int i = begin; i < end; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setPrice(i);
          timeQuantumItem.setTime(quoteItem.getTime());
          priceCollection.addSellQuote(timeQuantumItem);
        }
      }

      int begin = sell1Price;
      int end = sell5Price;
      if (end == 0) {
        end = sell4Price;
      }
      if (end == 0) {
        end = sell3Price;
      }
      if (end == 0) {
        end = sell2Price;
      }
      if (end == 0) {
        end = sell1Price;
      }

      for (int i = begin; i <= end; i++) {
        PriceCollection priceCollection = getPriceCollection(i);
        int quantum = 0;
        if (i == sell1Price)
          quantum = Integer.valueOf(quoteItem.getSell1Amount()).intValue();
        else if (i == sell2Price)
          quantum = Integer.valueOf(quoteItem.getSell2Amount()).intValue();
        else if (i == sell3Price)
          quantum = Integer.valueOf(quoteItem.getSell3Amount()).intValue();
        else if (i == sell4Price)
          quantum = Integer.valueOf(quoteItem.getSell4Amount()).intValue();
        else if (i == sell5Price) {
          quantum = Integer.valueOf(quoteItem.getSell5Amount()).intValue();
        }

        TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
        timeQuantumItem.setQuantum(quantum);
        timeQuantumItem.setTime(quoteItem.getTime());
        priceCollection.addSellQuote(timeQuantumItem);
      }

      if (sell5Price == 0) {
        begin = end + 1;
        end = this.quoteHighPrice;

        for (int i = begin; i <= end; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setPrice(i);
          timeQuantumItem.setTime(quoteItem.getTime());
          priceCollection.addSellQuote(timeQuantumItem);
        }
      }

    }

    if (bid1Price == 0) {
      if ((this.quoteLowPrice > 0) && (this.quoteHighPrice > 0))
        for (int i = this.quoteLowPrice; i <= this.quoteHighPrice; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setTime(quoteItem.getTime());
          timeQuantumItem.setPrice(i);
          priceCollection.addBidQuote(timeQuantumItem);
        }
    }
    else
    {
      if (this.quoteHighPrice > 0)
      {
        int begin = bid1Price + 1;
        int end = this.quoteHighPrice;
        for (int i = begin; i <= end; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setTime(quoteItem.getTime());
          timeQuantumItem.setPrice(i);
          priceCollection.addBidQuote(timeQuantumItem);
        }
      }

      int end = bid1Price;
      int begin = bid5Price;
      if (begin == 0) {
        begin = bid4Price;
      }
      if (begin == 0) {
        begin = bid3Price;
      }
      if (begin == 0) {
        begin = bid2Price;
      }
      if (begin == 0) {
        begin = bid1Price;
      }

      for (int i = begin; i <= end; i++) {
        PriceCollection priceCollection = getPriceCollection(i);

        int quantum = 0;
        if (i == bid1Price)
          quantum = Integer.valueOf(quoteItem.getBid1Amount()).intValue();
        else if (i == bid2Price)
          quantum = Integer.valueOf(quoteItem.getBid2Amount()).intValue();
        else if (i == bid3Price)
          quantum = Integer.valueOf(quoteItem.getBid3Amount()).intValue();
        else if (i == bid4Price)
          quantum = Integer.valueOf(quoteItem.getBid4Amount()).intValue();
        else if (i == bid5Price) {
          quantum = Integer.valueOf(quoteItem.getBid5Amount()).intValue();
        }

        TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
        timeQuantumItem.setQuantum(quantum);
        timeQuantumItem.setTime(quoteItem.getTime());
        timeQuantumItem.setPrice(i);
        priceCollection.addBidQuote(timeQuantumItem);
      }

      if (bid5Price == 0) {
        end = begin - 1;
        begin = this.quoteLowPrice;

        for (int i = begin; i <= end; i++) {
          PriceCollection priceCollection = getPriceCollection(i);
          TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
          timeQuantumItem.setQuantum(0);
          timeQuantumItem.setTime(quoteItem.getTime());
          timeQuantumItem.setPrice(i);
          priceCollection.addBidQuote(timeQuantumItem);
        }
      }
    }
  }

  public List<TimeQuantumItem> getBidList(int price)
  {
    PriceCollection priceCollection = getPriceCollection(price);

    return priceCollection.getBidList();
  }

  public List<TimeQuantumItem> getSellList(int price)
  {
    PriceCollection priceCollection = getPriceCollection(price);

    return priceCollection.getSellList();
  }

  public int getQuoteHighPrice()
  {
    return this.quoteHighPrice;
  }

  public int getQuoteLowPrice() {
    return this.quoteLowPrice;
  }

  public int getVary(int price, String time, boolean isBid) {
    PriceCollection priceCollection = getPriceCollection(price);

    return priceCollection.getVary(time, isBid);
  }
}
