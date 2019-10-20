package MainProgram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Objects;

import static MainProgram.Controller.*;

/**
 * Created by keno on 11.11.16.
 */
public class Controller_Settings {

    //Alle sich auf dem Fenster befindlichen Objekte mit dem Programmcode Verknuepfen
    @FXML
    private CheckBox check_sounds, check_music, check_autosave, check_autoload,check_autoerror;
    @FXML
    private ChoiceBox<String> themeBox;
    @FXML
    private ImageView background;

    //Zwischenspeicher fuer das aktuelle Themepack
    private String Theme;

    /**
     * Initialize.
     *
     * @throws MalformedURLException the malformed url exception
     */
    @FXML
    @SuppressWarnings("unused")
    public void initialize() throws MalformedURLException {
        //Initialisierungen vornehmen
        //Prufen ob Einstellungen geladen werden koennen -!> Standardeinstellungen anlegen
        Theme = programSettings.getProperty("theme");
        Controller.ChoosenThemePack = Theme;
        if (GAMESTARTED)themeBox.setDisable(true);
        background.setImage(new Image(Controller.MENUE.toURI().toURL().toString()));
        if (!programSettings.loadProperties()){
            programSettings.resetProperties();
            programSettings.addProperty("music", "true");
            programSettings.addProperty("sounds", "true");
            programSettings.addProperty("autosave", "true");
            programSettings.addProperty("autoload", "true");
            programSettings.addProperty("theme", "default");
            programSettings.addProperty("autoerror","true");
            programSettings.saveProperties();
            programSettings.loadProperties();
        }

        //Geladene Einstellungen anwenden und diverse CheckBoxen dementsprechend abaendern
        if (programSettings.getProperty("music").equals("true"))check_music.setSelected(true); else check_music.setSelected(false);
        if (programSettings.getProperty("sounds").equals("true"))check_sounds.setSelected(true); else check_sounds.setSelected(false);
        if (programSettings.getProperty("autosave").equals("true"))check_autosave.setSelected(true); else check_autosave.setSelected(false);
        if (programSettings.getProperty("autoload").equals("true"))check_autoload.setSelected(true); else check_autoload.setSelected(false);
        if (programSettings.getProperty("autoerror").equals("true"))check_autoerror.setSelected(true); else check_autoerror.setSelected(false);

        //Auslesen der Verfuegbaren Themes und uebergeben an die ChoiseBox
        ArrayList<String> Themes = ThemePack.getThemes();   //Verfuegebare Themes auslesen
        themeBox.getItems().addAll(Themes); //Themes als auswahl hinzufuegen
        try{
            themeBox.getSelectionModel().select(programSettings.getProperty("theme"));  //Versuchen das zuletzt gespeicherte Theme bzw. das aktuelle Theme auszuwaehlen
        }catch(Exception e){
            themeBox.getSelectionModel().selectFirst();
        }
        if (Themes.size() == 1)themeBox.setDisable(true);

        //ChangeListener hinzufuegen um auf evtl. aenderugen zu reagieren
        themeBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Controller.ChoosenThemePack = themeBox.getItems().get(newValue.intValue()) + "";
            Controller.Sout("Choose Themepack: " + Controller.ChoosenThemePack);
        });


    }

    @SuppressWarnings("unused")
    @FXML
    private void handlecheck(ActionEvent event){
        //Aenderungen an CheckBoxen verarbeiten
        //Soundeinstellungen ueberpruefen
        if (programSettings.getProperty("sounds").equals("false") && check_sounds.isSelected()){
            programSettings.setProperty("sounds","true");
            Controller.BUTTONHOVER.play();
        }else
            if (programSettings.getProperty("sounds").equals("true") && !check_sounds.isSelected())programSettings.setProperty("sounds","false");

        //Musikeinstellungen ueberpruefen
        if (BACKGROUNDMUSIC.isPlaying()){
            if (!check_music.isSelected())BACKGROUNDMUSIC.stop();
        }else
        if (check_music.isSelected()){
            programSettings.setProperty("music",check_music.isSelected() + "");
            BACKGROUNDMUSIC.play();
        }

        //Neue Einstellungen uebernehmen
        programSettings.setProperty("music",check_music.isSelected() + "");
        programSettings.setProperty("sounds",check_sounds.isSelected() + "");
        programSettings.setProperty("autosave",check_autosave.isSelected() + "");
        programSettings.setProperty("autoload",check_autoload.isSelected() + "");
        programSettings.setProperty("autoerror",check_autoerror.isSelected() + "");

    }

    @SuppressWarnings("unused")
    @FXML
    private void backbuttonaction(ActionEvent event){
        //Druecken des ZurueckButtons
        //Versuchen aenderungen des ThemePacks zu uebernehmen -> Themepack versuchen zu laden und ggf. auf das Standardpack ausweichen
        Controller.SETTINGWINDOW_ACTIVE = false;
        if (!Objects.equals(Theme, Controller.ChoosenThemePack)){   //Wenn aenderungen vorgenommen wurden
            ProgramThemePack = new ThemePack(Controller.ChoosenThemePack);
            if (!ProgramThemePack.loadTheme()){ //Wenn Fehler beim Laden des Packs entstehen -> Defaultpack laden
                ThemePack.makedefaultpack();
                ProgramThemePack = new ThemePack("default");
                ProgramThemePack.loadTheme();
            }
            programSettings.setProperty("theme",ProgramThemePack.getName());
            Controller.UpdateImages = true;
        }

        //Debugging
        if (!programSettings.saveProperties()){
            Controller.Sout("DEBUG -> ERROR: Faild to Store Settings");
        }else
            Controller.Sout("DEBUG -> INFO: Settings stored in " + programSettings.getFile().getAbsolutePath());

        Controller.stage.close();
    }

}
