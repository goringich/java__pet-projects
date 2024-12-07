package core;

import core.Requests.Request;

import java.util.*;
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
  private boolean movingUp = true; // Track direction 
  private int totalDistance;
  private final int totalFloors;

  private final BlockingQueue<Request> requests; // a thread-safe queue
  private final ElevatorManager manager;

  // Constructor with totalFloors parameter
  public Lift(int id, int capacity, ElevatorManager manager, int totalFloors){
    this.id = id;
    this.capacity = capacity;
    this.currentLoad = 0;
    this.currentFloor = 0;
    this.requests = new LinkedBlockingQueue<>();
    this.manager = manager;
    this.running = true;
    this.totalFloors = totalFloors;
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

  // Add a new request to the lift
  public synchronized void addRequest(Request request) {
    synchronized (requests) {
      if (currentLoad + request.weight > capacity) {
        manager.log("Lift " + id + " overloaded! Request rejected: " + request);
      } else {
        requests.add(request);
        currentLoad += request.weight;
        manager.log("Lift: " + id + " accepted request: " + request);

        requests.notify(); // wakes up one of the threads that is waiting on the object
      }
    }
  }

  @Override
  public void run() {
    while (running) {
      try {
        Request request = null;
        synchronized (requests) {
          while (requests.isEmpty() && running) {
            requests.wait();
          }


          if (!running) break;

          // fetch all current requests to process
          List<Request> allReq = new ArrayList<>(requests);
          requests.clear();

          // sort requests based on current direction to minimize distance
          allReq.sort(Comparator.comparingInt(r -> Math.abs(r.floor - currentFloor)));
          for (Request r : allReq) {
              request = r;
              currentLoad -= request.weight;
              moveToFloor(request.floor);
              manager.log("Lift " + id + " delivered passengers to floor " + request.floor);
          }
        }

        // After completing requests, decide direction, Return to ground floor if not there
        if (requests.isEmpty()) {
          if (currentFloor != 0) {
            moveToFloor(0);
            manager.log("Lift " + id + " returned to ground floor");
          }
      }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }

  }

  // Move the lift to the specified floor
  private void moveToFloor(int floor) throws InterruptedException {
    //  requested floor is within bounds
    floor = Math.max(0, Math.min(floor, totalFloors - 1));

    while (currentFloor != floor && running) {
      Thread.sleep(300); // moving emulation
      if (currentFloor < floor) {
        currentFloor++;
        movingUp = true;
      } else {
        currentFloor--;
        movingUp = false;
      }
      totalDistance++;
      manager.updateGUI();
    }
  }

  // Stop the lift thread
  public void stop() {
    running = false;
    synchronized (requests) {
      requests.notifyAll(); // Wake up thread to terminate
    }
   }
}
