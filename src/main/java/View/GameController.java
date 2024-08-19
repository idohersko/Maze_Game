package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class GameController extends AController implements Observer, IView {


    public Menu Options;
    public Menu Properties;
    public MenuItem threadPool;
    public RadioMenuItem menu_Options_Mute;
    private int rows;
    private int cols;
    private double zoomVal;

    @FXML
    public javafx.scene.control.CheckMenuItem BestFirstSearch;
    public javafx.scene.control.CheckMenuItem BreathFirstSearch;
    public javafx.scene.control.CheckMenuItem DepthFirstSearch;
    public javafx.scene.control.CheckMenuItem EmptyMazeGenerator;
    public javafx.scene.control.CheckMenuItem SimpleMazeGenerator;
    public javafx.scene.control.CheckMenuItem MyMazeGenerator;
    @FXML
    private MazeDisplayer mazeDisplayer;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();



    public static int numSaveMaze = 1;

    public void initialize(Stage stage, MyViewModel myViewModel, int rows, int cols, MediaPlayer startMusic) {
        this.stage = stage;
        this.myViewModel = myViewModel;
        this.rows = rows;
        this.cols = cols;
        this.startMusic = startMusic;

        myViewModel.addObserver(this);
        myViewModel.generateMaze(this.rows, this.cols);
        mazeDisplayer.drawMaze(myViewModel.getMaze());

    }

    public void Home(ActionEvent actionEvent) {

        try {
            FXMLLoader myViewFXMLLoader = new FXMLLoader();
            Parent myView = myViewFXMLLoader.load(getClass().getResource("MyView.fxml").openStream());
            Scene myViewScene = new Scene(myView);
            stage.setScene(myViewScene);
            MyViewController myViewController = myViewFXMLLoader.getController();
            myViewController.initialize(this.stage, this.myViewModel, startMusic);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void Reset(ActionEvent actionEvent) {

        setPlayerPosition(0,0);
        mazeDisplayer.setSolution(null);


    }

    public void ShowSolution(ActionEvent actionEvent) {
        myViewModel.solveMaze();
    }

    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change) {
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "file loaded" -> fileLoaded();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void fileLoaded() {
        mazeDisplayer.setNullSolution();
        mazeDisplayer.setPlayerPositionOnly(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
        mazeDisplayer.drawMaze(myViewModel.getMaze());
    }

    public void mazeSolved() {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }


    public void KeyPressed(KeyEvent keyEvent) {
        myViewModel.updatePlayerLocation(keyEvent.getCode());
        keyEvent.consume();
    }

    private void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }


    private void mazeGenerated() {
        mazeDisplayer.drawMaze(myViewModel.getMaze());
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        if (this.mazeDisplayer.getGoal().getRowIndex() == row && this.mazeDisplayer.getGoal().getColumnIndex() == col)
            arriveTheGoal();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void New() throws IOException {
        this.Home(null);
    }

    public void Save() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File newFile = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(newFile);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files", "*.maze")
        );
        fileChooser.setInitialFileName("MyMaze" + numSaveMaze);
        File saveFile = fileChooser.showSaveDialog(this.stage);
        if (saveFile != null) {
            myViewModel.SaveGame(saveFile);
        }
        numSaveMaze++;
    }

    public void Load() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        File newFile = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(newFile);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files", "*.maze")
        );
        File loadFile = fileChooser.showOpenDialog(this.stage);
        if (loadFile != null) {
            myViewModel.loadGame(loadFile);
        }
    }


    public void solverDepthFirstSearch(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeSearchingAlgorithm("DepthFirstSearch");
    }

    public void solverBreadthFirstSearch(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeSearchingAlgorithm("BreadthFirstSearch");
    }

    public void solverBestFirstSearch(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeSearchingAlgorithm("BestFirstSearch");
    }

    public void generatorEmptyMazeGenerator(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeGeneratingAlgorithm("EmptyMazeGenerator");
    }

    public void generatorSimpleMazeGenerator(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeGeneratingAlgorithm("SimpleMazeGenerator");
    }

    public void generatorMyMazeGenerator(ActionEvent actionEvent) {
        Configurations.getConfigurations().setMazeGeneratingAlgorithm("MyMazeGenerator");
    }

    public void ThreadPool(ActionEvent actionEvent) {
        TextField text = new TextField();
        text.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Configurations.getConfigurations().setThreadPoolSize(text.getText());
            }
        });
        threadPool.setGraphic(text);
    }

    public void Exit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you afraid? Do you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            myViewModel.stop();
            stage.close();
        } else {
            actionEvent.consume();
        }
    }

    public void Help(ActionEvent actionEvent) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Help");
            //FXMLLoader myViewFXMLLoader = new FXMLLoader();
            //Parent myViewRoot = myViewFXMLLoader.load(getClass().getResource("View/Help.fxml").openStream());

            FXMLLoader myViewFXMLLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/Help.fxml"));
            Parent myViewRoot = myViewFXMLLoader.load();

            Scene scene = new Scene(myViewRoot, 550, 400);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);


            newStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if(myViewModel.getMaze() != null) {
            int rows = myViewModel.getMaze().getMatrix().length;
            int cols = myViewModel.getMaze().getMatrix()[0].length;


            double changePositionOnLineX = helper(mazeDisplayer.getHeight(), Math.max(rows, cols), rows, mouseEvent.getX(),mazeDisplayer.getWidth() / Math.max(rows, cols));
            double changePositionOnLineY = helper(mazeDisplayer.getWidth(), Math.max(rows, cols),  cols, mouseEvent.getY(),mazeDisplayer.getHeight() / Math.max(rows, cols));

            if (changePositionOnLineX < 0 || (changePositionOnLineX > cols - 1) || changePositionOnLineY < 0 || (changePositionOnLineY > rows - 1))
                return;

            if (this.mazeDisplayer.getGoal().getRowIndex() == changePositionOnLineY && this.mazeDisplayer.getGoal().getColumnIndex() == changePositionOnLineX)
                arriveTheGoal();

            if (changePositionOnLineX == myViewModel.getPlayerCol() && changePositionOnLineY < myViewModel.getPlayerRow())
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD8);
            else if (changePositionOnLineX == myViewModel.getPlayerCol() && changePositionOnLineY > myViewModel.getPlayerRow())
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD2);
            else if (changePositionOnLineY == myViewModel.getPlayerRow() && changePositionOnLineX < myViewModel.getPlayerCol())
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD4);
            else if (changePositionOnLineY == myViewModel.getPlayerRow() && changePositionOnLineX > myViewModel.getPlayerCol())
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD6);
            else if (changePositionOnLineY > myViewModel.getPlayerRow() && changePositionOnLineX < myViewModel.getPlayerCol() && (myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow() + 1][myViewModel.getPlayerCol()] == 0 || myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow()][myViewModel.getPlayerCol() - 1] == 0))
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD1);
            else if (changePositionOnLineY > myViewModel.getPlayerRow() && changePositionOnLineX > myViewModel.getPlayerCol() && (myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow() + 1][myViewModel.getPlayerCol()] == 0 || myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow()][myViewModel.getPlayerCol() + 1] == 0))
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD3);
            else if (changePositionOnLineY < myViewModel.getPlayerRow() && changePositionOnLineX < myViewModel.getPlayerCol() && (myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow() - 1][myViewModel.getPlayerCol()] == 0 || myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow()][myViewModel.getPlayerCol() - 1] == 0))
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD7);
            else if (changePositionOnLineY < myViewModel.getPlayerRow() && changePositionOnLineX > myViewModel.getPlayerCol() && (myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow() - 1][myViewModel.getPlayerCol()] == 0 || myViewModel.getMaze().getMatrix()[myViewModel.getPlayerRow()][myViewModel.getPlayerCol() + 1] == 0))
                myViewModel.updatePlayerLocation(KeyCode.NUMPAD9);

        }
    }


    public void arriveTheGoal() {

        Main.startMusic.setVolume(0);
        Media media = new Media(new File("resources\\video\\finalVideo.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        Group root = new Group();
        root.getChildren().add(mediaView);
        Scene scene2 = new Scene(root, 1200,800);
        this.stage.setScene(scene2);
        this.stage.setTitle("Playing video");
        this.stage.show();
        this.stage.setMaximized(true);

        javafx.scene.media.MediaPlayer player = new javafx.scene.media.MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {

            @Override
            public void run() {
                mediaPlayer.stop();
                Main.restart();
            }
        });
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    private double helper(double mazedisplayerSize, int maxsize,  int mazeSize,double mouseEvent,double number){
        double result = (int) ((mouseEvent) / (number) - ((mazedisplayerSize / 2 - ((mazedisplayerSize/maxsize) * mazeSize / 2)) / (mazedisplayerSize/maxsize)));
        return result;
    }


    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            if (scroll.getDeltaY() >= 0)
                zoomVal = 1.05;
            else
                zoomVal = 0.95;

            settingScale(zoomVal, scroll);
            scroll.consume();
        }
    }

    private void settingScale(double zoomVal, ScrollEvent scroll) {
        Scale myScale = new Scale();
        myScale.setPivotX(scroll.getX());
        myScale.setPivotY(scroll.getY());
        myScale.setX(mazeDisplayer.getScaleX() * zoomVal);
        myScale.setY(mazeDisplayer.getScaleY() * zoomVal);
        mazeDisplayer.helperScroll(myScale);
    }

    public void setResizeEvent(Scene scene) {

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.setWidth (scene.getWidth());
            mazeDisplayer.draw();

        });


        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.setHeight(scene.getHeight());
            mazeDisplayer.draw();

        });

    }

}