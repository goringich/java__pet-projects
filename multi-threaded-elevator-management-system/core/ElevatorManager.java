package core;

import core.Requests.Request;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorManager {
  private final List<Lift> lifts;
  private final StringBuilder log;
  private final BufferedWriter logWriter;
  private final ExecutorService executor;
  private ElevatorGUI gui;

  // each lift get default capacity value at the beginning
  public ElevatorManager(int liftCount, int liftCapacity) throws IOException {
    this.lifts = new ArrayList<>();
    this.log = new StringBuilder();
    String logFileName = "elevators_logs_" + System.currentTimeMillis() + ".txt";
    this.logWriter = new BufferedWriter(new FileWriter(logFileName, true)); // adding mode is true
    this.executor = Executors.newFixedThreadPool(liftCount + 1); // thread pool for lifts and request generation;
  
    for (int i = 0; i < liftCount; i++) {
     lifts.add(new Lift(i+1, liftCapacity, this));   
    }
  }



  public void log(String message){
    log.append(message).append("\n");
    System.out.println(message);
    try {
      logWriter.write(message + "\n");
      // The flush() method clears the buffer by writing its contents to a file.
      logWriter.flush();
    } catch (IOException e) {
      System.err.println("Ошибка при записи логов: " + e.getMessage());
    }

    if (gui != null){
      gui.updateLog(log.toString());
    }
  }

  // to avoid rac
  public synchronized void addRequest(Request request){
    Lift bestLift = FindBestLift(request.floor);
    if (bestLift != null) {
      bestLift.addRequest(request);
    } else {
      log("No available lifts for request: " + request);
    }
  }

  private Lift FindBestLift(int floor) {
    return lifts.stream()
      .filter(Lift::isOperational)
      .min(Comparator.comparingInt(el -> Math.abs(el.getCurrentFloor() - floor)))
      .orElse(null);
  }


  // GUI
  public void setGUI(ElevatorGUI gui){
    this.gui = gui;
  }

  public void start(RequestGenerator generator){
    for (Lift lift : lifts) {
      executor.execute(lift);
    }
    executor.execute(generator);
  }

  public void stop(){
    for (Lift lift : lifts) {
      lift.stop();
    }

    // all running tasks end immediately
    executor.shutdownNow();

    try {
      logWriter.close();
    } catch (IOException e) {
      System.err.println("Error closing log writer: " + e.getMessage());
    }
  } 

  public void updateGUI(){
    if (gui != null) {
      gui.updateLifts(lifts);
    }
  }
}
