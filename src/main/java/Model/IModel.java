package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.File;

public interface IModel {

    void generateMaze(int rows, int cols);
    Maze getMaze();
    void setMaze(Maze newMaze);
    void updatePlayerLocation(MovementDirection direction);
    void solveMaze();
    int getPlayerRow();
    int getPlayerCol();
    void stop();
    Solution getMazeSolution();

    void saveMaze(String path);

    void loadMaze(File loadFile);
}
