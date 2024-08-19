# Tom & Jerry Maze Game

This project is a maze game developed as part of the "Advanced Topics in Programming" course at Ben-Gurion University. In this game, you play as "Tom," and your goal is to catch Jerry while navigating through a maze. Be careful not to hit the walls, and if you get stuck, our game offers solutions to help you out.

## Features

- **Interactive Gameplay**: Play the game using both the keyboard and the mouse.
- **Multiple Maze Generation Algorithms**: The game supports various algorithms for maze generation.
- **Maze Solving Algorithms**: Use different algorithms like BFS, DFS, and more to solve the maze.
- **MVVM Architecture**: The game is built using the Model-View-ViewModel architecture, separating the business logic from the UI.
- **Compression Algorithm**: A custom compression algorithm was developed, which was the most effective in our class.
- **ThreadPool Support**: To handle multiple clients interacting with the servers simultaneously, we implemented a configurable ThreadPool.
- **Unit Testing**: Major classes and methods have been tested using JUnit.
- **Multimedia Support**: Enjoy background music while playing, and a video will play when you reach the goal.

## Code Design

| Design Component       | Description                                                                                                                                 |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **Two Servers**        | The game uses two servers with different strategies: one for generating mazes using various algorithms, and another for solving them.       |
| **MVVM Architecture**  | The Model-View-ViewModel architecture separates the business logic (Model), UI (View), and the connection between them (ViewModel).         |
| **Compression Algorithm** | Developed a custom compression algorithm that outperformed others in our class.                                                          |
| **ThreadPool**         | Supports parallel processing, allowing multiple clients to connect to the servers simultaneously. The pool size can be adjusted via configuration. |
| **Unit Testing**       | JUnit was used to ensure the reliability of the core classes and methods.                                                                   |
| **Multimedia Support** | Music plays in the background during gameplay, and a video plays when the player reaches the goal.                                          |

![Game Screenshot](https://github.com/NoaMagrisso/ATP-Project-MazeGame/blob/main/src/main/resources/ImagesFXML/helperScene.jpg)

## Installation

To run the game locally:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/NoaMagrisso/ATP-Project-MazeGame.git
    ```
2. **Navigate to the project directory**:
    ```bash
    cd ATP-Project-MazeGame
    ```
3. **Build the project using Maven**:
    ```bash
    mvn clean install
    ```
4. **Run the game**:
    - You can use the provided JAR file:
        ```bash
        java -jar ATPProjectJAR.jar
        ```

## Usage

Once the game starts, use the keyboard or mouse to navigate Tom through the maze to catch Jerry. If you need help, the game can solve the maze for you using various algorithms.

## Contribution

Contributions are welcome! Please fork the repository and submit a pull request with your changes. For major changes, please open an issue first to discuss what you would like to change.

## Authors

- **Noa Magrisso** - [NoaMagrisso](https://github.com/NoaMagrisso)
- **Ido Hersko** - [idohersko](https://github.com/idohersko)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
