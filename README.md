Reverse-engineering applications is a powerful way to master complex system this project proves it.

Puzzle Game (Java/Swing) : 
A sliding puzzle game with move tracking, scoring, and multiple configurations. This project follows the Model-View-Controller (MVC) design pattern to separate logic, UI, and data handling for better maintainability:

Model: Puzzle state and game logic.

View: Swing-based GUI (JFrame, JPanel).

Controller: Handles user input and updates Model/View.



1. Features : 

a) Move Tracking :

  Displays the remaining moves (initialized to a set number).

  Decrements with each player action.

b) Scoring System :
   
  Dynamic Score : Increases with every valid move.

  Best Score : Saved and displayed across sessions.

c) Puzzle Selection :
Choose from preset configurations (0 = empty space):

   073214568 | 124857063 | 204153876  
   624801753 | 670132584 | 781635240 | 280163547  

(Default: 204153876)

d) Game Controls :
   
   Rearrange : Reset to the initial configuration.

   Shuffle : Randomize tiles or pick a random preset.

   Smooth tile animations.

   Timer to track completion speed.

   Victory message on puzzle solve.

2. Technologies : 

     Language : Java

     Framework : Swing (GUI)

     Tools : IntelliJ IDEA (or Eclipse/VS Code)

3. How to Run : 

     Download : Clone or download ZIP.

     Import : Open in IntelliJ/Eclipse as a Java project.

     Option 1: Run from Source (IDE)

     Run the main class: Main.java (or App.java depending on your structure).

     Option 2: Run the JAR File (Standalone)
   
     Download the pre-built 8-puzzle-game.jar from the Releases section.

     Execute it via command line : java -jar 8-puzzle-game.jar
   , (Ensure you have Java 8+ installed)

