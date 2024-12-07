package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import core.ElevatorManager;
import core.Lift;

public class ElevatorGUI extends JFrame {
  private final JTextArea logArea;
  private final JPanel liftPanel;
  private int totalFloors;

  public ElevatorGUI(ElevatorManager manager) {
    setTitle("Elevator System");
    setSize(1200, 800);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    logArea = new JTextArea();
    logArea.setEditable(true);
    logArea.setLineWrap(true);
    logArea.setWrapStyleWord(true);

    JScrollPane logScrollPane = new JScrollPane(logArea);
    add(logScrollPane, BorderLayout.EAST);

    // lift visualization panel
    liftPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        if (totalFloors > 0) {
          int floorHeight = height / (totalFloors + 1);
          g.setColor(Color.LIGHT_GRAY);

          for (int i = 0; i <= totalFloors; i++) {
            int y = height - (i * floorHeight);
            g.drawLine(0, y, width, y); // Draw horizontal line for each floor
            g.drawString("Floor " + i, 10, y - 5); // Label each floor
          }
        }
      }
    };
    liftPanel.setLayout(null); // Allow absolute positioning
    add(liftPanel, BorderLayout.CENTER);

    // Add resize listener to update lift positions dynamically
    liftPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateLifts(manager.getLifts(), totalFloors); // Redraw lifts on resize
      }
    });

    // stop button
    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> manager.stop());
    add(stopButton, BorderLayout.SOUTH);

    manager.setGUI(this);
    setVisible(true);
  }

  // update lift positions and statuses
  public void updateLifts(List<Lift> lifts, int totalFloors) {
    liftPanel.removeAll(); // Clear previous lifts
    int width = liftPanel.getWidth();
    int height = liftPanel.getHeight();

    if (width == 0 || height == 0) {
      width = 800; 
      height = 600; 
    }

    int floorHeight = (totalFloors > 0) ? height / (totalFloors + 1) : 1;
    int liftWidth = 50;
    int spacing = 60;
    int startX = 100;

    for (int i = 0; i < lifts.size(); i++) {
      Lift lift = lifts.get(i);
      int x = startX + i * (liftWidth + spacing);
      int y = height - (lift.getCurrentFloor() * floorHeight) - floorHeight;

      // Print lift position
      // System.out.println("Lift " + lift.getId() + " X: " + x + " Y: " + y);

      JPanel liftBox = new JPanel();
      liftBox.setBounds(x, Math.max(y, 0), liftWidth, floorHeight - 10);
      liftBox.setBackground(lift.getCurrentLoad() > 400 ? Color.RED : Color.GREEN);
      liftBox.setToolTipText("Lift: " + lift.getId() + " Floor: " + lift.getCurrentFloor() +
              " Load: " + lift.getCurrentLoad() + "/" + lift.getCapacity());
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
