package core.Requests;
public class Request {
  public final int floor;
  public final int weight;

  public Request(int floor, int weight){
    this.floor = floor;
    this.weight = weight;
  }

  // override is necessary because it will correctly display string in println commands
  @Override
  public String toString() {
    return "Floor: " + floor + ",  Weight: " + weight; 
  }
}
