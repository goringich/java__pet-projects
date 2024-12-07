package core;

import core.Requests.Request;
import java.util.concurrent.BlockingQueue;

public class Lift implements Runnable {
  private final int id;
  private final int capacity;
  private int currentFloor;
  private int currentLoad;
  private int totalPassengers;
  private boolean operational = true;
  private  boolean running;
  private final BlockingQueue<Request> requests; // a thread-safe queue
  
  private final ElevatorManager manager;

  
  public Lift(int id, int capacity, ElevatorManager manager){
    this.id = id;
    this.capacity = capacity;
    this.currentFloor = 0;
  }

  public int getId() {
    return id;
  }

  public int getCurrentFloor(){
    return currentFloor;
  }

  public int getCurrentLoad(){
    return currentLoad;
  }

  public boolean isOperational(){
    return operational;
  }

  public synchronized void addRequest(Request request) {
    synchronized (requests) {
      if (currentLoad + request.weight > capacity) {
        manager.log("Lift " + id + " overloaded! Request rejected: " + request);
      } else {
        requests.add(request);
        currentLoad += request.weight;
        totalPassengers++;
        manager.log("Lift: " + id + " accepted request: " + request);
        
        requests.notify(); // wakes up one of the threads that is waiting on the object
      }
    }
  }

  public void stop() {
    running = false;
    synchronized (requests) {
      requests.notifyAll();
    }
   }


}
