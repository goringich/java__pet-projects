package core.Requests;
// Realisation of Runnable Interface allows accomplish this task parallel

import java.util.Random;

import core.ElevatorManager;

public class RequestGenerator implements Runnable {
  private final ElevatorManager manager;
  private final int totalFloors;
  private final Random random;

  public RequestGenerator(ElevatorManager manager, int totalFloors){
    this.manager = manager;
    this.totalFloors = totalFloors;
    this.random = new Random();
  }

  @Override 
  public void run(){
    while (true) { 
        try {
          // imitation of real request system
          Thread.sleep(random.nextInt(3000) + 1000); // request generation
          int floor = random.nextInt(totalFloors);
          int weight = random.nextInt(100) + 50;
          manager.addRequest(new Request(floor, weight));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
    }
  }

}