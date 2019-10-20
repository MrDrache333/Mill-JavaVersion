package MainProgram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;

import static MainProgram.Controller.VERSION;

/**
 * Project: Mill
 * Package: MainProgram
 * Created by keno on 16.12.16.
 */
public class Controller_About {

    @FXML
    private ImageView background;
    @FXML
    private Label abouttext;

    /**
     * Initialize.
     *
     * @throws MalformedURLException the malformed url exception
     */
//Initialisierung aller benoetigten Komponenten
    @FXML
    @SuppressWarnings("unused")
    public void initialize() throws MalformedURLException {

        background.setImage(new Image(Controller.MENUE.toURI().toURL().toString()));
        String Version = VERSION[0] + " Build " + VERSION[1];


        abouttext.setText("Programm: Mühle - Das Spiel\r\n" +
                "Build Version: " + Version + "\r\n\r\n" +
                "Team:\r\n" +
                "Stefan Kruschewski\r\n" +
                "Keno Oelrichs Garcia\r\n" +
                "Dennis Piontek\r\n" +
                "Leon Doeding\r\n" +
                "\r\n" +
                "Im Auftrag von\r\n" +
                "Matthias Thode\r\n" +
                "BBSoftware\r\n" +
                "BBS Friedenstraße WHV");

    }


    @SuppressWarnings("unused")
    @FXML
    private void backbuttonaction(ActionEvent event){
        Controller.stage.close();
        Controller.ABOUTWINDOW_ACTIVE = false;
    }
}
