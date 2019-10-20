package MainProgram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;

import static MainProgram.Controller.WINSOUND;

/**
 * Project: Mill
 * Package: sample
 * Created by keno on 23.11.16.
 */
public class Controller_winFrame {

    //Alle sich auf dem Fenster befindlichen Objekte mit dem Programmcode Verknuepfen
    @FXML
    private Label statuslabel;

    @FXML
    private ImageView background;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() throws MalformedURLException {
        //Initialisierungen vornehmen -> Nachricht an Spielernamen anpassen und Hintergrundbild laden
        background.setImage(new Image(Controller.MENUE.toURI().toURL().toString()));
        if (Controller.turnsWithoutMill < 50){
            WINSOUND.play();
            statuslabel.setText(Controller.Spieler[Player.getactivePlayer()].getName() + " hat gewonnen!");
        } else
            statuslabel.setText("Unentschieden");
    }

    @FXML
    private void OK_Action(ActionEvent event){
        //Werte uebernehmen, welche zum Neustarten der Gui fuehren
        Controller.Sout("Going back to MainMenue");
        Controller.RESTARTGAME = true;
        Controller.WINWINDOW_ACTIVE = false;
        Controller.stage.close();

    }
}
