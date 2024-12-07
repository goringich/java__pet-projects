import javax.swing.text.ElementIterator;

import core.ElevatorManager;
import core.Requests.RequestGenerator;
import gui.ElevatorGUI;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args){
    try {
      ElevatorManager manager = new ElevatorManager(3, 500); // 3 elevators, lifting capacity
      RequestGenerator generator = new RequestGenerator(manager, 20); // 20 floors

      manager.start(generator);
      
      SwingUtilities.invokeLater(() => new ElevatorGUI(manager));
    } catch (Exception e) {
      System.err.println("Error initializing the GUI: " + e.getMessage());
    }
  }
}