package MainProgram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;

import static MainProgram.Controller.NEWVERSION_ASTEXT;
import static MainProgram.Controller.VERSION_ASTEXT;

/**
 * Project: Mill
 * Package: sample
 * Created by keno on 23.11.16.
 */
public class Controller_update {

    //Alle sich auf dem Fenster befindlichen Objekte mit dem Programmcode Verknuepfen
    @FXML
    private Label statuslabel;

    @FXML
    private ImageView background;


    @FXML
    @SuppressWarnings("unused")
    private void initialize() throws MalformedURLException {
        background.setImage(new Image(Controller.MENUE.toURI().toURL().toString()));
        statuslabel.setText("Eine neue Version ist verfügbar!\r\nAktuelle Version: " + NEWVERSION_ASTEXT + "\r\nIhre Version: " + VERSION_ASTEXT + "\r\nSoll die neue Version jetzt heruntergeladen werden?");
    }

    @FXML
    private void OK_Action(ActionEvent event) throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder(new String[]{"cmd", "/c", "start",
                    "http://oelrichs.garcia.keno.franziskusschule-whv.de/other/Softwareprojekt/Mill_" + NEWVERSION_ASTEXT + ".jar"}).start();
        }else if (System.getProperty("os.name").contains("Mac OS X")){
            new ProcessBuilder(new String[]{"open", "http://oelrichs.garcia.keno.franziskusschule-whv.de/other/Softwareprojekt/Mill_" + NEWVERSION_ASTEXT + ".jar"}).start();
        }
        Controller.UPDATEWINDOW_ACTIVE = false;
        Controller.CLOSEPROGRAM();

    }

    @FXML
    private void CANCEL_Action(ActionEvent event) {
        Controller.UPDATEWINDOW_ACTIVE = false;
        Controller.stage.close();

    }
}
