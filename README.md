# Tom & Jerry Maze Game

By this project my project partner [`Ido Hersko`](https://github.com/idohersko/) and I implemented a maze game, as we asked to do under the "Advanced Topic Programming" course at BGU.

You are playing "Tom" and you have to catch Jerry. Please, keep yourself from the walls in the way :)
If you are getting into trouble, you can be helped by our solution.

* **Our game supports in playing and going on the board by the keyboard and also by the mouse.**

## The Major Code Designs

| A Design | It's role |
| --- | --- |
| Two Serevers | Each one of them have different strategy. The first server supports on genertaing the maze - by few algorithms. The second server supports on solving the maze be few algorithms, such as BFS/DFS and etc. |
| MVVM Architecture | Model-View-ViewModel, when the model is dealing with the business logic, the view layer with all the visual design - GUI so we implemented JavaFX project, and the ViewModel is the connection part between them. |

* We also created a compression algorithm, which was the most effective in our class!
* For supporting in parallel programming so few clients can contact with the serevers, we also used ThreadPool. It's size can be changed simply by the configuraion file.
* For testing our major classes and methods, we used Unit Testing (JUnit).
* Throughout the project we have maintened the principle of separating the problem from the probelm-solving algorithm, in order to support the principle of Polymorphism.
* By threads, we also soppurted in listening to music during your stay in the app and in the game, and when you will arive the goal a video will start playing.


![This is an image](https://github.com/NoaMagrisso/ATP-Project-MazeGame/blob/main/src/main/resources/ImagesFXML/helperScene.jpg)
