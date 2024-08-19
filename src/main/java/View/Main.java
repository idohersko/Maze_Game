package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;



public class Main extends Application {

    public static MyModel myModel;
    public static Stage stageMain;
    public static Scene scene;
    public static MediaPlayer startMusic;
    public static MyViewController myViewController;
    public static MyViewModel myViewModel;
    public static Parent myViewRoot;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Media media = new Media(new File("resources\\music\\musicplayer.mp3").toURI().toString());
        startMusic = new MediaPlayer(media);
        startMusic.setVolume(0.2);
        startMusic.setAutoPlay(true);
        startMusic.setCycleCount(MediaPlayer.INDEFINITE);

        myModel = new MyModel();
        myViewModel = new MyViewModel(myModel);
        myModel.addObserver(myViewModel);
        //FXMLLoader myViewFXMLLoader = new FXMLLoader();
        //myViewRoot = myViewFXMLLoader.load(getClass().getResource("View/MyView.fxml").openStream());

        FXMLLoader myViewFXMLLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/MyView.fxml"));
        Parent myViewRoot = myViewFXMLLoader.load();

        primaryStage.setTitle("Tom & Jerry : The Maze Game");
        scene = new Scene(myViewRoot, 600, 400);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("View/MyView.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        myViewController = myViewFXMLLoader.getController();
        myViewController.setResizeEvent(scene);
        myViewController.initialize(primaryStage,myViewModel, startMusic);


        try {
            primaryStage.getIcons().add(new Image(new FileInputStream("resources\\images\\pack516.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetStageCloseEvent(primaryStage);
        stageMain = primaryStage;
        primaryStage.show();

    }

    public static void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("You can still stay here... Are you sure that you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    myModel.stop();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void resize(Scene scene)  {

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

            }
        });
    }

    public static void restart() {

        startMusic.setVolume(0.2);
        stageMain.setMaximized(true);
        stageMain.setScene(scene);
        stageMain.setTitle("Tom & Jerry : The Maze Game");
        myViewController.initialize(stageMain,myViewModel, startMusic);
        SetStageCloseEvent(stageMain);
        stageMain.show();
    }
}