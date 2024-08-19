package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

public class MyModel extends Observable implements IModel{

    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution mazeSolution;

    private Server mazeGenerateServer;
    private Server solveMazeServer;
    //static final Logger logger = LogManager.getLogger();
    //private final Logger logger = LogManager.getLogger("MyModel");
    private final Logger logger = LogManager.getLogger();

    public MyModel() {
        this.playerRow = 0;
        this.playerCol = 0;
        this.mazeSolution = null;
        this.mazeGenerateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveMazeServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

        this.mazeGenerateServer.start();
        this.solveMazeServer.start();
    }


    public void stop() {
        this.mazeGenerateServer.stop();
        this.solveMazeServer.stop();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {

            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {

                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        int totalBytes = rows * cols + 12;
                        byte[] decompressedMaze = new byte[totalBytes];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        maze.setPositionToZero(getMaze().getGoalPosition());

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Exception " + e);
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("UnKnownHostException "+ e);

        }
        logger.info("server port : 5400");
        logger.info("Maze generated, maze: rows: "+maze.getRows()+" columns: "+maze.getCols() );
        setChanged();
        notifyObservers("maze generated");
        movePlayer(0, 0);
    }


    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public Maze getMaze() {
        return this.maze;
    }

    @Override
    public void setMaze(Maze newMaze) {
        this.maze = newMaze;
    }

    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        mazeSolution = (Solution)fromServer.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Unable connect to the client " + e);
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("UnKnownHostException " + e);
        }
        logger.info("Maze Solution Show");
        setChanged();
        notifyObservers("maze solved");
    }

    public Solution getMazeSolution() {

        return mazeSolution;
    }

    @Override
    public void saveMaze(String path){
        try {
            File newFileToSave = new File(path);
            newFileToSave.createNewFile();
            StringBuilder  builder = new StringBuilder();
            builder.append(playerRow+"\n"+ playerCol + "\n" + maze.getGoalPosition().getRowIndex() + "\n" + maze.getGoalPosition().getColumnIndex()+"\n"+maze.getRows()+"\n"+maze.getCols()+"\n");
            for(int i = 0; i < maze.getRows(); i++) {
                for(int j = 0; j < maze.getCols(); j++) {
                    builder.append(String.valueOf(maze.getMatrix()[i][j]));
                    if(j < maze.getCols() - 1)
                        builder.append(",");
                }
                builder.append("\n");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadMaze(File loadFile) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(loadFile));
            int playerRowIdx = 0;
            int playerColIdx = 0;
            int goalMazeRow = 1;
            int goalMazeCol = 1;
            Position goalMaze = new Position(1,1);
            int rows = 2;
            int cols = 2;

            String line = bufferedReader.readLine();
            int index = 0;
            while (line != null && index <= 5) {

                if (index == 0)
                    playerRowIdx = Integer.parseInt(line);
                else if (index == 1)
                    playerColIdx = Integer.parseInt(line);
                else if (index == 2)
                    goalMazeRow = Integer.parseInt(line);
                else if (index == 3)
                    goalMazeCol = Integer.parseInt(line);
                else if (index == 4)
                    rows = Integer.parseInt(line);
                else
                    cols = Integer.parseInt(line);
                index++;
                line = bufferedReader.readLine();
            }




            goalMaze = new Position(goalMazeRow, goalMazeCol);

            int[][] matrixForMaze = new int[rows][cols];
            line = "";
            int rowIdx = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] colsOfMatrix = line.split(",");
                int colIdx = 0;
                for (String col : colsOfMatrix) {
                    matrixForMaze[rowIdx][colIdx] = Integer.parseInt(col);
                    colIdx++;
                }
                rowIdx++;
            }
            bufferedReader.close();
            this.maze = new Maze(rows, cols);
            this.maze.setMatrix(matrixForMaze);
            this.maze.setEnd(goalMaze);

            this.playerRow = playerRowIdx;
            this.playerCol = playerColIdx;
            setChanged();
            notifyObservers("file loaded");
            System.out.println("Finish Loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if ((playerRow > 0) && ((this.maze.getMatrix()[playerRow - 1][playerCol]) != 1))
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if ((playerRow < maze.getRows() - 1) && ((this.maze.getMatrix()[playerRow + 1][playerCol]) != 1))
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if ((playerCol > 0) && ((this.maze.getMatrix()[playerRow][playerCol - 1]) != 1))
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if ((playerCol < maze.getCols() - 1) && ((this.maze.getMatrix()[playerRow][playerCol + 1]) != 1))
                    movePlayer(playerRow, playerCol + 1);
            }
            case UPLEFT -> {
                if ((playerRow > 0) && (playerCol > 0) && ((this.maze.getMatrix()[playerRow - 1][playerCol - 1]) != 1))
                    if (((this.maze.getMatrix()[playerRow - 1][playerCol]) != 1) || ((this.maze.getMatrix()[playerRow][playerCol - 1]) != 1))
                        movePlayer(playerRow - 1, playerCol - 1);
            }
            case UPRIGHT -> {
                if ((playerRow > 0) && (playerCol < maze.getCols() - 1) && ((this.maze.getMatrix()[playerRow - 1][playerCol + 1]) != 1))
                    if (((this.maze.getMatrix()[playerRow - 1][playerCol]) != 1) || ((this.maze.getMatrix()[playerRow][playerCol + 1]) != 1))
                        movePlayer(playerRow - 1, playerCol + 1);
            }
            case DOWNRIGHT -> {
                if ((playerRow < maze.getRows() - 1) && (playerCol < maze.getCols() - 1) && ((this.maze.getMatrix()[playerRow + 1][playerCol + 1]) != 1))
                    if (((this.maze.getMatrix()[playerRow + 1][playerCol]) != 1) || ((this.maze.getMatrix()[playerRow][playerCol + 1]) != 1))
                        movePlayer(playerRow + 1, playerCol + 1);
            }
            case DOWNLEFT -> {
                if ((playerRow < maze.getRows() - 1) && (playerCol > 0) && ((this.maze.getMatrix()[playerRow + 1][playerCol - 1]) != 1))
                    if (((this.maze.getMatrix()[playerRow + 1][playerCol]) != 1) || ((this.maze.getMatrix()[playerRow][playerCol - 1]) != 1))
                        movePlayer(playerRow + 1, playerCol - 1);
            }
        }
    }

    @Override
    public int getPlayerRow() {
        return this.playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }
}
