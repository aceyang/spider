package stock.stat;

public class TimeQuantumItem
{
  private int price = 0;

  private String time = "";

  private int quantum = 0;

  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getQuantum() {
    return this.quantum;
  }

  public void setQuantum(int quantum) {
    this.quantum = quantum;
  }
}