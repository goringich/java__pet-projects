package core.Requests;
// Realisation of Runnable Interface allows accomplish this task parallel

import java.util.List;
import java.util.Random;

import core.ElevatorManager;
import core.Lift;

public class RequestGenerator implements Runnable {
  private final ElevatorManager manager;
  private final int totalFloors;
  private final Random random;
  private boolean running = true;

  public RequestGenerator(ElevatorManager manager, int totalFloors){
    this.manager = manager;
    this.totalFloors = totalFloors;
    this.random = new Random();
  }

  @Override 
  public void run(){
    while (running) { 
        try {
          // imitation of real request system
          Thread.sleep(random.nextInt(1000) + 500); // request generation
          int numberOfRequests = random.nextInt(3) + 1;

          for (int i = 0; i < numberOfRequests; i++) {
            int floor = random.nextInt(totalFloors);
            int weight = random.nextInt(100) + 50; // weight between 50-149
            manager.addRequest(new Request(floor, weight));
         }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
    }
  }

  // stop generating requests
  public void stop() {
    running = false;
  }
}