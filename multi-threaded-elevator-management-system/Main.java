
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
      RequestGenerator generator1 = new RequestGenerator(manager, 30);
      RequestGenerator generator2 = new RequestGenerator(manager, 30);
      RequestGenerator generator3 = new RequestGenerator(manager, 30);
      manager.start(generator1, generator2, generator3);

      SwingUtilities.invokeLater(() -> new ElevatorGUI(manager));
    } catch (IOException e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
} 