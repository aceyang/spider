package stock.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PriceCollection
{
  private List<TimeQuantumItem> bidList = new ArrayList<TimeQuantumItem>();

  private List<TimeQuantumItem> sellList = new ArrayList<TimeQuantumItem>();

  private HashMap<String, Integer> varyHash = new HashMap<String, Integer>();

  public PriceCollection()
  {
    TimeQuantumItem timeQuantumItem = new TimeQuantumItem();
    timeQuantumItem.setQuantum(0);
    timeQuantumItem.setTime("09:15:00");
    this.bidList.add(timeQuantumItem);

    timeQuantumItem = new TimeQuantumItem();
    timeQuantumItem.setQuantum(0);
    timeQuantumItem.setTime("09:15:00");
    this.sellList.add(timeQuantumItem);
  }

  public List<TimeQuantumItem> getBidList()
  {
    return this.bidList;
  }

  public int addBidQuote(TimeQuantumItem timeQuantumItem) {
    int size = this.bidList.size();
    int vary = 0;
    if (size > 0)
    {
      TimeQuantumItem lastItem = (TimeQuantumItem)this.bidList.get(size - 1);
      vary = timeQuantumItem.getQuantum() - lastItem.getQuantum();
    }
    else
    {
      vary = timeQuantumItem.getQuantum();
    }

    this.bidList.add(timeQuantumItem);
    String key = "Bid@" + timeQuantumItem.getTime();
    this.varyHash.put(key, Integer.valueOf(vary));

    return vary;
  }

  public List<TimeQuantumItem> getSellList()
  {
    return this.sellList;
  }

  public int addSellQuote(TimeQuantumItem timeQuantumItem) {
    int vary = 0;
    int size = this.sellList.size();
    if (size > 0)
    {
      TimeQuantumItem lastItem = (TimeQuantumItem)this.sellList.get(size - 1);
      vary = timeQuantumItem.getQuantum() - lastItem.getQuantum();
    }
    else
    {
      vary = timeQuantumItem.getQuantum();
    }

    this.sellList.add(timeQuantumItem);
    String key = "Sell@" + timeQuantumItem.getTime();
    this.varyHash.put(key, Integer.valueOf(vary));

    return vary;
  }

  public int getVary(String time, boolean isBid)
  {
    int vary = 0;
    String key = "";

    if (isBid)
    {
      key = "Bid@" + time;
    }
    else
    {
      key = "Sell@" + time;
    }

    if (this.varyHash.get(key) != null)
    {
      vary = ((Integer)this.varyHash.get(key)).intValue();
    }

    return vary;
  }
}
