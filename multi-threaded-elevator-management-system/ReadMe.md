# Elevator System Simulation

## Overview

This project is a **Java-based simulation** of a multi-elevator management system designed for a building. It efficiently handles multiple elevators, processes real-time requests, and visualizes their operation in a **graphical user interface (GUI)**.

---

## Features

- **Multi-elevator management:** Simulates multiple elevators operating concurrently.
- **Real-time request handling:** Automatically generates and processes random requests.
- **Graphical visualization:** Displays elevator movements, load status, and activity logs.
- **Thread-safe design:** Utilizes Java's multithreading features for concurrent operations.
- **Logging:** Tracks all actions in a log for monitoring and debugging.

---

## Prerequisites

- **Java Development Kit (JDK):** Version 8 or higher (tested on JDK 22).
- **GSON Library:** For JSON serialization and deserialization.
- **IDE (optional):** IntelliJ IDEA, Eclipse, or any Java-compatible IDE.

---

## Directory Structure

```
elevator-system-simulation/
├── src/
│   ├── core/                # Core functionality for managing elevators
│   ├── core/requests/       # Request generation and processing logic
│   ├── gui/                 # Graphical interface implementation
│   └── Main.java            # Application entry point
└── README.md                # Project documentation
```

### Key Components

- **Core:** Contains the logic for elevator movement, state management, and request allocation.
- **Requests:** Handles the creation and processing of requests from simulated users.
- **GUI:** Provides a real-time graphical display of elevator operations.

---

## Setup and Execution

### 1. Compile and Run via Command Line

#### Developer Environment (Windows-specific):
```bash
javac -d bin -cp ".;C:\Program Files\Java\jdk-22\lib\gson-2.11.0.jar" Main.java core/*.java core/requests/*.java gui/*.java
cd "c:\Users\user\Desktop\study\java\multi-threaded-elevator-management-system\" && javac -cp ".;C:\Program Files\Java\jdk-22\lib\gson-2.11.0.jar" Main.java && java -cp ".;C:\Program Files\Java\jdk-22\lib\gson-2.11.0.jar" Main
```

#### Generic Instructions:
1. Compile:
   ```bash
   javac core/*.java core/requests/*.java gui/*.java Main.java
   ```
2. Run:
   ```bash
   java Main
   ```

### 2. Using an IDE

1. Import the project into your IDE.
2. Build the project.
3. Run the `Main` class as the entry point.

---

## How It Works

1. **Initialization:** 
   - The application initializes the GUI and the elevator system.
   - Elevators are assigned to threads for concurrent operation.
2. **Request Handling:** 
   - Requests are generated randomly and queued for processing.
   - The system assigns requests to the nearest or most appropriate elevator.
3. **Visualization:**
   - The GUI updates in real-time to display the current state of elevators.
   - A log area shows activities such as requests handled, elevator positions, and loading status.


```

### GUI Elements
- **Elevator positions:** Displays the current floor of each elevator.
- **Load status:** Shows the current capacity of each elevator.
- **Log area:** Tracks actions, warnings, and errors in real-time.

---

## Notes

- Ensure your Java environment is set up correctly.
- Validate that the GSON library is available in your `classpath`.
- Match package names with the directory structure to avoid compilation errors.

---

## Potential Enhancements

- **Custom Request Generation:** Allow users to input custom requests through the GUI.
- **Optimization:** Implement smarter algorithms for request allocation (e.g., AI-based heuristics).
- **Scalability:** Extend to handle a larger number of floors and elevators. 

--- 

This simulation serves as a starting point for understanding **multi-threaded systems**, **concurrent programming**, and **interactive GUI development** in Java.
