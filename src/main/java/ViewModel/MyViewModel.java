package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{

    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Maze getMaze(){
        return this.model.getMaze();
    }

    public int getPlayerRow(){
        return this.model.getPlayerRow();
    }

    public int getPlayerCol(){
        return this.model.getPlayerCol();
    }

    public void generateMaze(int rows, int cols){
        this.model.generateMaze(rows, cols);
    }

    public void stop() {
        model.stop();
    }

    public void updatePlayerLocation(KeyCode keyCode) {

        MovementDirection direction;
        switch (keyCode){
            case NUMPAD8 -> direction = MovementDirection.UP;
            case DIGIT8 -> direction = MovementDirection.UP;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case DIGIT2 -> direction = MovementDirection.DOWN;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case DIGIT4 -> direction = MovementDirection.LEFT;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case DIGIT6 -> direction = MovementDirection.RIGHT;
            case NUMPAD7 -> direction = MovementDirection.UPLEFT;
            case DIGIT7 -> direction = MovementDirection.UPLEFT;
            case NUMPAD9 -> direction = MovementDirection.UPRIGHT;
            case DIGIT9 -> direction = MovementDirection.UPRIGHT;
            case NUMPAD3 -> direction = MovementDirection.DOWNRIGHT;
            case DIGIT3 -> direction = MovementDirection.DOWNRIGHT;
            case NUMPAD1 -> direction = MovementDirection.DOWNLEFT;
            case DIGIT1 -> direction = MovementDirection.DOWNLEFT;

            default -> {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public Solution getSolution(){
        return model.getMazeSolution();
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void SaveGame(File saveFile) throws IOException {
        model.saveMaze(saveFile.getPath());
    }

    public void loadGame(File loadFile) throws IOException, ClassNotFoundException {
        model.loadMaze(loadFile);
    }
}
