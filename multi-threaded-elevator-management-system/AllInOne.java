
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} package core;

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

}package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import core.ElevatorManager;
import core.Lift;

public class ElevatorGUI extends JFrame {
  private final JTextArea logArea;
  private final JPanel liftPanel;

  public ElevatorGUI(ElevatorManager manager){
    setTitle("Elevator System");
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    logArea = new JTextArea();
    logArea.setEditable(true); // 😉
    JScrollPane logScrollPane = new JScrollPane(logArea);
    add(logScrollPane, BorderLayout.EAST);

    liftPanel = new JPanel();
    liftPanel.setLayout(new GridLayout(0, 1));
    add(liftPanel, BorderLayout.CENTER);

    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> manager.stop());
    add(stopButton, BorderLayout.SOUTH);

    manager.setGUI(this);
    setVisible(true);
  }

  public void updateLifts(List<Lift> lifts) {
    liftPanel.removeAll();
    for (Lift lift : lifts) {
      JPanel liftInfo = new JPanel();
      liftInfo.setLayout(new BorderLayout());

      JLabel titleLabel = new JLabel("Lift " + lift.getId());
      titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
      liftInfo.add(titleLabel, BorderLayout.NORTH);

      JProgressBar floorIndicator = new JProgressBar(0, 10);
      floorIndicator.setValue(lift.getCurrentFloor());
      floorIndicator.setString("Floor: " + lift.getCurrentFloor());
      floorIndicator.setStringPainted(true);
      liftInfo.add(floorIndicator, BorderLayout.CENTER);

      JLabel loadLabel = new JLabel("Load: " + lift.getCurrentLoad() + "/" + 500);
      loadLabel.setForeground(lift.getCurrentLoad() > 400 ? Color.RED : Color.BLACK);
      liftInfo.add(loadLabel, BorderLayout.SOUTH);

      liftPanel.add(liftInfo);
    }
    liftPanel.revalidate();
    liftPanel.repaint();
  }

  public void updateLog(String log) {
    logArea.setText(log);
  }
}

import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} package core;

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

}package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import core.ElevatorManager;
import core.Lift;

public class ElevatorGUI extends JFrame {
  private final JTextArea logArea;
  private final JPanel liftPanel;

  public ElevatorGUI(ElevatorManager manager){
    setTitle("Elevator System");
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    logArea = new JTextArea();
    logArea.setEditable(true); // 😉
    JScrollPane logScrollPane = new JScrollPane(logArea);
    add(logScrollPane, BorderLayout.EAST);

    liftPanel = new JPanel();
    liftPanel.setLayout(new GridLayout(0, 1));
    add(liftPanel, BorderLayout.CENTER);

    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> manager.stop());
    add(stopButton, BorderLayout.SOUTH);

    manager.setGUI(this);
    setVisible(true);
  }

  public void updateLifts(List<Lift> lifts) {
    liftPanel.removeAll();
    for (Lift lift : lifts) {
      JPanel liftInfo = new JPanel();
      liftInfo.setLayout(new BorderLayout());

      JLabel titleLabel = new JLabel("Lift " + lift.getId());
      titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
      liftInfo.add(titleLabel, BorderLayout.NORTH);

      JProgressBar floorIndicator = new JProgressBar(0, 10);
      floorIndicator.setValue(lift.getCurrentFloor());
      floorIndicator.setString("Floor: " + lift.getCurrentFloor());
      floorIndicator.setStringPainted(true);
      liftInfo.add(floorIndicator, BorderLayout.CENTER);

      JLabel loadLabel = new JLabel("Load: " + lift.getCurrentLoad() + "/" + 500);
      loadLabel.setForeground(lift.getCurrentLoad() > 400 ? Color.RED : Color.BLACK);
      liftInfo.add(loadLabel, BorderLayout.SOUTH);

      liftPanel.add(liftInfo);
    }
    liftPanel.revalidate();
    liftPanel.repaint();
  }

  public void updateLog(String log) {
    logArea.setText(log);
  }
}

import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorMa
import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500, 20); // elevators, lifting capacity, floors
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} package core;

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
  private final int totalFloors;

  // each lift get default capacity value at the beginning
  public ElevatorManager(int liftCount, int liftCapacity, int totalFloors) throws IOException {
    this.totalFloors = totalFloors;
    this.lifts = new ArrayList<>();
    this.log = new StringBuilder();
    String logFileName = "elevators_logs_" + System.currentTimeMillis() + ".txt";
    this.logWriter = new BufferedWriter(new FileWriter(logFileName, true)); // adding mode is true
    this.executor = Executors.newFixedThreadPool(liftCount + 1); // thread pool for lifts and request generation;
  
    for (int i = 0; i < liftCount; i++) {
     lifts.add(new Lift(i+1, liftCapacity, this, totalFloors));   
    }
  }



  public void log(String message){
    log.append(message).append("\n");
    System.out.println(message);
    try {
      logWriter.write(message + "\n");
      // the flush() method clears the buffer by writing its contents to a file.
      logWriter.flush();
    } catch (IOException e) {
      System.err.println("Ошибка при записи логов: " + e.getMessage());
    }

    if (gui != null){
      gui.updateLog(log.toString());
    }
  }

  // to avoid race
  // add a new request and assign to the best lift
  public synchronized void addRequest(Request request){
    Lift bestLift = FindBestLift(request.floor);
    if (bestLift != null) {
      bestLift.addRequest(request);
    } else {
      log("No available lifts for request: " + request);
    }
  }

  // find the closest operational lift to the requested floor
  private Lift FindBestLift(int floor) {
    return lifts.stream()
      .filter(Lift::isOperational)
      .min(Comparator.comparingInt(el -> Math.abs(el.getCurrentFloor() - floor)))
      .orElse(null);
  }


  // set the GUI reference
  public void setGUI(ElevatorGUI gui){
    this.gui = gui;
  }

  // start all lift threads and the request generator
  public void start(RequestGenerator generator){
    for (Lift lift : lifts) {
      executor.execute(lift);
    }
    executor.execute(generator);
  }

  // stop all lifts and shutdown executor
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
      gui.updateLifts(lifts, totalFloors);
    }
  }

  public int getTotalFloors() {
    return totalFloors;
  }
}
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
package core.Requests;
// Realisation of Runnable Interface allows accomplish this task parallel

import java.util.Random;

import core.ElevatorManager;

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
    while (true) { 
        try {
          // imitation of real request system
          Thread.sleep(random.nextInt(3000) + 1000); // request generation
          int floor = random.nextInt(totalFloors);
          int weight = random.nextInt(100) + 50; // weight between 50-149
          manager.addRequest(new Request(floor, weight));
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
}package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import core.ElevatorManager;
import core.Lift;

public class ElevatorGUI extends JFrame {
  private final JTextArea logArea;
  private final JPanel liftPanel;
  private int totalFloors;

  public ElevatorGUI(ElevatorManager manager){
    setTitle("Elevator System");
    setSize(1000, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    logArea = new JTextArea();
    logArea.setEditable(true); // 😉
    JScrollPane logScrollPane = new JScrollPane(logArea);
    add(logScrollPane, BorderLayout.EAST);

    // lift visualization panel
    liftPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFloors(g);
      }

      // draw floor lines and labels
      private void drawFloors(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if (totalFloors > 0) {
          int floorHeight = height / (totalFloors + 1);
          g.setColor(Color.LIGHT_GRAY);
          for (int i = 0; i <= totalFloors; i++) {
            int y = height - (i * floorHeight);
            g.drawLine(0, y, width, y);
            g.drawString("Floor " + i, 10, y - 5);
          }
        }
      }
    };
    liftPanel.setLayout(new GridLayout(0, 1));
    add(liftPanel, BorderLayout.CENTER);

    // stop button
    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> manager.stop());
    add(stopButton, BorderLayout.SOUTH);

    manager.setGUI(this);
    setVisible(true);
  }

   // update lift positions and statuses
   public void updateLifts(List<Lift> lifts, int totalFloors) {
    this.totalFloors = totalFloors;
    liftPanel.removeAll();
    int width = liftPanel.getWidth();
    int height = liftPanel.getHeight();

    if (width == 0 || height == 0) {
      width = 800;
      height = 500; 
    }

    int floorHeight = (totalFloors > 0) ? height / (totalFloors + 1) : 1;
    int liftWidth = 50;
    int spacing = 60;
    int startX = 50;

    for (int i = 0; i < lifts.size(); i++) {
      Lift lift = lifts.get(i);
      int x = startX + i * (liftWidth + spacing);
      int y = height - (lift.getCurrentFloor() * floorHeight) - floorHeight;

      JPanel liftBox = new JPanel();
      liftBox.setBounds(x, y, liftWidth, floorHeight);
      liftBox.setBackground(lift.getCurrentLoad() > 400 ? Color.RED : Color.GREEN);
      liftBox.setToolTipText("Lift: " + lift.getId() + " Floor: " + lift.getCurrentFloor() + " Load: " + lift.getCurrentLoad() + "/" + 500);
      liftPanel.add(liftBox);
    }

    liftPanel.revalidate();
    liftPanel.repaint();
}

   // update with new messages
  public void updateLog(String log) {
    logArea.setText(log);
  }
}
