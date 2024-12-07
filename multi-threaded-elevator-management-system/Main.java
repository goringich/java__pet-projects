
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
} 