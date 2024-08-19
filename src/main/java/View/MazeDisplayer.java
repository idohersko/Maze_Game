package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import java.io.FileInputStream;

public class MazeDisplayer extends Canvas {

    private int[][] mazeMatrix;
    private Solution solution;
    private int playerRow = 0;
    private int playerCol = 0;
    private Position goal;
    int rowSolution_i;
    int colSolution_i;

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setPlayerPositionOnly(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    public void drawMaze(Maze maze) {
        this.goal = maze.getGoalPosition();
        this.mazeMatrix = maze.getMatrix();
        draw();
    }

    public void draw() {

        if (mazeMatrix != null) {
            try {
                Image wallImage = new Image(new FileInputStream("resources\\images\\iceWall.jpg"));
                Image goalImage = new Image(new FileInputStream("resources\\images\\goalPicJerry.JPG"));
                Image startImage = new Image(new FileInputStream("resources\\images\\tomPic.JPG"));
                Image wayImage = new Image(new FileInputStream("resources\\images\\wayPic.png"));
                Image solutionImage = new Image(new FileInputStream("resources\\images\\solutionPic.jpg"));
                double canvasHeight = getHeight();
                double canvasWidth = getWidth();
                int rows = mazeMatrix.length;
                int cols = mazeMatrix[0].length;

                double cellHeight = canvasHeight / rows;
                double cellWidth = canvasWidth / cols;
                GraphicsContext graphicsContext = getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
                //drawSolution(graphicsContext,cellHeight,cellWidth);
                drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols, wallImage, goalImage, startImage, wayImage);
                if (solution != null) {
                    drawSolution(solutionImage ,graphicsContext, cellHeight, cellWidth, solution);
                }
                drawPlayer(graphicsContext, cellHeight, cellWidth, startImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void drawSolution(Image solutionImage, GraphicsContext graphicsContext, double cellHeight, double cellWidth, Solution sol) {

        for (int i = 0; i < sol.getSolutionPath().size()-1; i++) {
            AState a = sol.getSolutionPath().get(i);
            MazeState m = (MazeState)a;
            rowSolution_i = m.getPosState().getRowIndex();
            colSolution_i = m.getPosState().getColumnIndex();

            double x = colSolution_i * cellWidth;
            double y = rowSolution_i * cellHeight;
            graphicsContext.drawImage(solutionImage, x, y, cellWidth, cellHeight);
        }
    }

    public void setNullSolution() {
        this.solution = null;
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols, Image wallImage, Image goalImage, Image startImage, Image wayImage) {


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == this.goal.getRowIndex() && j == this.goal.getColumnIndex()) {
                    graphicsContext.drawImage(goalImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                }
                else if (mazeMatrix[i][j] == 1 && (i != this.goal.getRowIndex() || j != this.goal.getColumnIndex())) {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
                else {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    graphicsContext.drawImage(wayImage, x, y, cellWidth, cellHeight);
                }

            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth, Image player) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.drawImage(player, x, y, cellWidth, cellHeight);
    }

    public Position getGoal() {
        return this.goal;
    }

    public void helperScroll(Scale myScale){
        this.getTransforms().add(myScale);
    }

}
