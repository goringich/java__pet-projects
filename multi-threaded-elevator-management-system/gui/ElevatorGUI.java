package gui;

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

  public void updateLifts(List<Lift> lifts){
    liftPanel.removeAll();

    for (Lift lift : lifts){
      JPanel liftInfo = new JPanel();
      liftInfo.setLayout(new BorderLayout());
      liftInfo.add(new JLabel("Lift " + lift.getId() + " - Floor: " + lift.getCurrentFloor()), BorderLayout.NORTH);
      liftInfo.add(new JLabel("Load: " + lift.getCurrentLoad() + "/" + 500), BorderLayout.CENTER);
      liftPanel.add(liftInfo);
    }
  }

  public void updateLog(String log) {
    logArea.setText(log);
  }
}
