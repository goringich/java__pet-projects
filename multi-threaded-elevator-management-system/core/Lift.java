package core;

import core.Requests.Request;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Lift implements Runnable {
  private final int id;
  private final int capacity;
  private int currentFloor;
  private int currentLoad;
  private int totalPassengers;
  private boolean operational = true;
  private  boolean running;
  private boolean movingUp = true;
  private int totalDistance;

  private final BlockingQueue<Request> requests; // a thread-safe queue
  private final ElevatorManager manager;

  
  public Lift(int id, int capacity, ElevatorManager manager){
    this.id = id;
    this.capacity = capacity;
    this.currentLoad = 0;
    this.currentFloor = 0;
    this.requests = new LinkedBlockingQueue<>();
    this.manager = manager;
    this.running = true;
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

  @Override
  public void run() {
    while (running) {
      try {
        synchronized (requests) {
          while (requests.isEmpty() && running) {
            requests.wait();
          }
        }

        if (!running) break;
        
        if (!requests.isEmpty()) {
          Request request;

          synchronized (requests) {
            request = requests.poll();
            currentLoad -= request.weight;
          }

          moveToFloor(request.floor);
          manager.log("Lift " + id + " delivered passengers to floor " + request.floor);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }

  }
  private void moveToFloor(int floor) throws InterruptedException {
    while (currentFloor != floor) {
      Thread.sleep(500); // moving emulation
      movingUp = currentFloor < floor;
      currentFloor += movingUp ? 1 : -1;
      totalDistance++;
      manager.updateGUI();
    }
  }

  public void stop() {
    running = false;
    synchronized (requests) {
      requests.notifyAll();
    }
   }


}
