package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public abstract class AController {

    protected Stage stage;
    protected Rectangle2D rectangleSizes = Screen.getPrimary().getBounds();
    protected MyViewModel myViewModel;
    protected MediaPlayer startMusic;

    public void Home(ActionEvent actionEvent) {
        try {
            FXMLLoader myViewFXMLLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent myView = myViewFXMLLoader.load();
            Scene myViewScene = new Scene(myView);

            stage.setScene(myViewScene);

            MyViewController myViewController = myViewFXMLLoader.getController();
            myViewController.initialize(this.stage, this.myViewModel, startMusic);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void SetMute(ActionEvent actionEvent) {
        if (this.startMusic.getVolume() != 0)
            this.startMusic.setVolume(0);
        else
            this.startMusic.setVolume(2);
    }

}
