# Pac-Man 🕹️

A fully functional clone of the classic arcade game Pac-Man, built from scratch in Java.

This project demonstrates core game development concepts and is structured using a strict Model-View-Controller (MVC) architecture to ensure a clean separation of concerns, making the codebase highly modular, testable, and maintainable.

## 🌟 Features

- **Classic Gameplay:** Navigate the maze, collect pellets, and avoid ghosts. Includes power pellets that trigger 'Frightened Mode,' allowing Pac-Man to eat the ghosts.
- **Advanced Ghost AI:** Each of the four ghosts implements its own distinct targeting personality and logic (Chase, Scatter, and Frightened behaviors).
- **Dynamic Entities:** Fruit spawns based on score thresholds.
- **Persistent Data:** Tracks and saves high scores locally.
- **Smooth Rendering:** Custom 60 FPS game loop for fluid movement and animations.

## 🏗️ Architecture (MVC)

The codebase strictly adheres to the MVC design pattern, featuring a highly modular design with completely decoupled game logic and rendering.

- **Model (`model`):** Contains the pure business logic, state management (Game, World, Entities, and Map), and mathematical rules. It has absolutely zero dependencies on `java.awt` or `javax.swing`.
- **View (`view`):** The presentation layer. Utilizes dedicated Renderers (Player, Ghost, Tiles, Collectibles) to draw the game state onto a `JPanel`. It accesses the model strictly as read-only.
- **Controller (`controller`):** The orchestration layer. Manages the main thread/game loop, handles keyboard inputs, and acts as the bridge updating the Model based on user actions and game rules.

## 💻 Tech Stack

- **Language:** Java (JDK 8+)
- **UI/Rendering:** Java Swing & AWT (Graphics2D)
- **Design Patterns:** MVC, Adapter/Bridge, State encapsulation

## 🚀 How to Run

**Clone the repository:**

```bash
git clone https://github.com/shlomo-famini/pacman-java.git
```

**Navigate to the project directory:**

```bash
cd pacman-java
```

**Compile the project:**

```bash
javac -d out -sourcepath src/main/java src/main/java/Main.java
```

**Run the game:**

```bash
java -cp out Main
```

## 🎮 Controls

- **Up / Down / Left / Right Arrows:** Move Pac-Man / Navigate Menus
- **Enter:** Select menu options
