package MainProgram;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

public class Controller {

    //Logdateien fuer Debuginformationen
    private static File logfile = new File("mill.log");
    private static File detaillog = new File("mill_detail.log");
    //Version
    //Hauptversion, Unterversion
    static final String[] VERSION = {"01","14"};
    static final String VERSION_ASTEXT = VERSION[0] + "." + VERSION[1];
    static final long VERSION_ASNUMBER = Long.parseLong(VERSION[0] + "" + VERSION[1]);
    static long NEWVERSION = VERSION_ASNUMBER;
    static String NEWVERSION_ASTEXT = VERSION_ASTEXT;

    //Standardspeicherort der Sounds
    static sound BACKGROUNDMUSIC = new sound(new File("sounds/backgroundmusic.wav"),"music");  //Hintergrundmusik sound
    static sound BUTTONKLICK = new sound(new File("sounds/buttonsound.wav"), "sound");  //Klicksound eines Buttons
    static sound BUTTONHOVER = new sound(new File("sounds/buttonhover.wav"), "sound");  //Wenn Mauszeiger ueber Button hovert
    static sound WINSOUND = new sound(new File("sounds/winsound.wav"), "sound");    //Wenn Spieler gewinnt

    //Standardspeicherort der Bilder
    private static File BLACKSTONE = new File("images/black.png");
    private static File WHITESTONE = new File("images/white.png");
    private static File GAME_RAND = new File("images/game_rand.png");
    private static File GAMEFIELD = new File("images/gamefield.png");
    static File MENUE = new File("images/menu.png");
    private static File DENY = new File("images/deny.png");

    //Mail
    static final String username = "safetyguardinfo@gmail.com";
    static final String password = "11223440";
    static final String SupportMailAdress = "kenoog@freenet.de";

    //Themepack Einstellungen
    static ThemePack ProgramThemePack;
    static boolean UpdateImages = false;
    static String ChoosenThemePack = "default";
    static int DeathStonestoJump = 7;

    //Weitere Spielvariablen
    static int turnsWithoutMill = 0;
    private static final int Points_adder = 100;
    private boolean menuBar_ACTIVE = false;
    static long ANIMATIONSPEED = 50;

    //Variablen zum Speichern von Zustaenden bestimmter Fenster und steuern des Spielverlaufs
    static boolean GAMESTARTED = false;    //Speicher ob Spiel gestartet ist
    static boolean PROGRAMMSTARTED = false;
    private static boolean turnFinish = true;
    static boolean SETTINGWINDOW_ACTIVE = false;
    static boolean WINWINDOW_ACTIVE = false;
    static boolean FEEDBACKWINDOW_ACTIVE = false;
    static boolean ABOUTWINDOW_ACTIVE = false;
    static boolean UPDATEWINDOW_ACTIVE = false;
    static boolean RESTARTGAME = false;
    private double MENUOPACITY = 1.0;   //Sichtbarkeit des Menues
    private double PlayerStatus1Opacity = 1.0;  //Sichtbarkeit des Playerstatus
    private boolean playerchange = true;    //Variable zum speichern ob die Sichtbarkeit des Playerstatus geaendert werden muss
    static Stage stage;
    private static boolean HOVER_STARTBUTTON = true, HOVER_SETTINGSBUTTON = true, HOVER_EXITBUTTON = true,HOVER_FEEDBACKBUTTON = true,HOVER_ABOUTBUTTON = true;
    private static coordinate Mouse = new coordinate(0,0);

    //MessageLabel einstellungen
    private final static double DEFAULTMessageSpeed = ANIMATIONSPEED / 10;
    private static double MessageSpeed = DEFAULTMessageSpeed;
    private static Boolean MessageLabelActive = false;
    private static long MessageLabelVisibleUntil = 0;
    private static ArrayList<String> Messages = new ArrayList<>(10);
    private static ArrayList<Long> MessagesTime = new ArrayList<>(10);

    //Variablen zum Speichern von vielen Daten
    static final Settings programSettings = new Settings(new File("config.properties"));
    static Player Spieler[];
    private static ImageView Spielsteine[] = new ImageView[20];

    //Alle sich auf dem Fenster befindlichen Objekte mit dem Programmcode Verknuepfen
    @FXML
    private Button menu_startgame,menu_settings,menu_exitgame,menu_feedback,menu_about;
    @FXML
    private ImageView menu_background,game_playground,game_rand,game_rand1,positionhover;
    @FXML
    private Label menu_title,game_title,pointstext1,pointstext2,pointlabel_player1,pointlabel_player2,playerstatus_player1,playerstatus_player2,MessageLabel,player1timelabel,player2timelabel,player1timetext,player2timetext;
    @FXML
    private TextField namefield_player1,namefield_player2;
    @FXML
    private ImageView
            stone0,stone1,stone2,stone3,stone4,stone5,stone6,stone7,stone8,stone9,stone10,stone11,stone12,stone13,stone14,stone15,stone16,stone17,stone18,stone19;
    @FXML
    private MenuBar menubar;

    //Initialsierung der Stage bzw. der Komponenten
    @SuppressWarnings("unused")//<- Warnung fuer "ungenutze" Methode unterdruecken
    public void initialize() {
        try {
            writetolog("Program started!");

            //MessageLabel hochsetzen
            Platform.runLater(() -> MessageLabel.setLayoutY(-MessageLabel.getHeight()));

            Settings.checkdefaults();   //Standardeinstellungen laden
            //Prufen ob das StandardThemepack existiert -!> Standardpack erstellen und laden
            if (!new File("Themes/default/default.properties").exists()) ThemePack.makedefaultpack();

            //Wenn anderes Themepack gespeichert wurde, prufen ob dies geladen werden kann -> Theme laden
            if (!programSettings.getProperty("theme").equals("default"))
                ProgramThemePack = new ThemePack(programSettings.getProperty("theme"));
            else {
                ProgramThemePack = new ThemePack("default");
            }
            if (!ProgramThemePack.loadTheme()) {
                ThemePack.makedefaultpack();
                ProgramThemePack = new ThemePack("default");
                ProgramThemePack.loadTheme();
            }
            //Einstellungen uebernehmen und aenderungen anzeigen
            programSettings.setProperty("theme", ProgramThemePack.getName());
            displayMessage("Thema geladen: " + ProgramThemePack.getName().toUpperCase(), 7000);

            //Themepack Resourcen laden und uebernehmen + hinufuegen der EventListener
            menu_background.setImage(new Image(MENUE.toURI().toURL().toString()));
            menu_background.setOnMouseMoved(hoverevent);
            menu_background.setOnMouseClicked(clickevent);
            game_playground.setImage(new Image(GAMEFIELD.toURI().toURL().toString()));
            game_title.setOnMouseMoved(hoverevent);
            positionhover.setImage(new Image(DENY.toURI().toURL().toString()));

            game_rand.setImage(new Image(GAME_RAND.toURI().toURL().toString()));
            game_rand1.setImage(new Image(GAME_RAND.toURI().toURL().toString()));

            menu_exitgame.setOnMouseMoved(hoverevent);
            menu_feedback.setOnMouseMoved(hoverevent);
            menu_about.setOnMouseMoved(hoverevent);
            menu_about.setText("Über");
            menu_settings.setOnMouseMoved(hoverevent);
            menu_startgame.setOnMouseMoved(hoverevent);
            menu_title.setOnMouseMoved(hoverevent);
            menu_title.setOnMouseClicked(clickevent);

            BACKGROUNDMUSIC.play(); //Plays the Backgroundmusic

            loadHomeGui();  // -> Funktion
            initstones();   // -> Funktion

            //Neuen automatischen Task erstellen, welcher Dynamische Funktionen uebernimmt
            Timer timer = new Timer();
            timer.schedule(task, 3000, ANIMATIONSPEED);


        }catch(Exception e){
            writeerror(e);
            JOptionPane.showMessageDialog(null,"!CRITICAL ERROR OCCURRED!\nPlease reinstall the Game!","!CRITICAL ERROR OCCURRED!",JOptionPane.ERROR_MESSAGE);
        }

    }

    //Uebernehmen von Standardeinstellungen bei Programmstart
    private void loadHomeGui(){

        Platform.runLater(() ->{
            positionhover.setVisible(false);

            menu_background.setVisible(true);
            menu_startgame.setVisible(true);
            menu_settings.setVisible(true);
            menu_exitgame.setVisible(true);
            menu_title.setVisible(true);
            menu_feedback.setVisible(true);
            menu_about.setVisible(true);

            game_playground.setVisible(false);
            game_title.setVisible(false);
            game_rand.setVisible(false);
            game_rand1.setVisible(false);
            pointstext1.setVisible(false);
            pointstext2.setVisible(false);
            pointlabel_player1.setVisible(false);
            pointlabel_player2.setVisible(false);
            playerstatus_player1.setVisible(false);
            playerstatus_player2.setVisible(false);
            namefield_player1.setVisible(false);
            namefield_player2.setVisible(false);
            player1timetext.setVisible(false);
            player1timelabel.setVisible(false);
            player2timetext.setVisible(false);
            player2timelabel.setVisible(false);

            setStonesVisible(false);

            //Sichtbar der Spieloberflaeche erhoehen
            game_playground.setOpacity(0);
            game_title.setOpacity(0);
            game_rand1.setOpacity(0);
            game_rand.setOpacity(0);

            pointstext1.setOpacity(0);
            pointstext2.setOpacity(0);
            pointlabel_player1.setOpacity(0);
            pointlabel_player2.setOpacity(0);
            namefield_player1.setOpacity(0);
            namefield_player2.setOpacity(0);
            player1timetext.setOpacity(0);
            player1timelabel.setOpacity(0);
            player2timetext.setOpacity(0);
            player2timelabel.setOpacity(0);

            setStonesOpacity(0);

            //Sichtbarkeit des Menues verringern
            menu_background.setOpacity(1.0);
            menu_startgame.setOpacity(1.0);
            menu_settings.setOpacity(1.0);
            menu_exitgame.setOpacity(1.0);
            menu_title.setOpacity(1.0);
            menu_feedback.setOpacity(1.0);
            menu_about.setOpacity(1.0);

            menu_feedback.setDisable(false);
            menu_exitgame.setDisable(false);
            menu_settings.setDisable(false);
            menu_startgame.setDisable(false);
            menu_title.setDisable(false);
            menu_about.setDisable(false);

            MENUOPACITY = 1.0;
            PlayerStatus1Opacity = 1.0;

            GAMESTARTED = false;
            menuBar_ACTIVE = false;

        });
    }

    //Button Spiel starten wurde gedrueckt
    @FXML
    @SuppressWarnings("unused")
    private void startgame(ActionEvent event){
        Sout("StartGame");
        if (!SETTINGWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE) {
            //ButtonSOund abspielen
            BUTTONKLICK.play();

            //Sichtbarkeit von Spielelementen auf 0 setzen damit dieser Dynamisch erhoeht werden kann
            game_playground.setOpacity(0);
            game_title.setOpacity(0);
            game_rand1.setOpacity(0);
            game_rand.setOpacity(0);
            pointstext1.setOpacity(0);
            pointstext2.setOpacity(0);
            pointlabel_player1.setOpacity(0);
            pointlabel_player2.setOpacity(0);
            playerstatus_player1.setOpacity(0);
            playerstatus_player2.setOpacity(0);
            namefield_player1.setOpacity(0);
            namefield_player2.setOpacity(0);
            setStonesOpacity(0);

            //Spielelemente Sichtbar schalten (Da Tranzparenz auf 100 Prozent keine Sichtbarkeit)
            game_playground.setVisible(true);
            game_title.setVisible(true);
            game_rand.setVisible(true);
            game_rand1.setVisible(true);
            pointstext1.setVisible(true);
            pointstext2.setVisible(true);
            pointlabel_player1.setVisible(true);
            pointlabel_player2.setVisible(true);
            playerstatus_player1.setVisible(true);
            playerstatus_player2.setVisible(true);
            namefield_player1.setVisible(true);
            namefield_player2.setVisible(true);

            player1timetext.setVisible(true);
            player1timelabel.setVisible(true);
            player2timetext.setVisible(true);
            player2timelabel.setVisible(true);

            setStonesVisible(true);

            //Menuelemente Deaktivieren
            menu_exitgame.setDisable(true);
            menu_settings.setDisable(true);
            menu_startgame.setDisable(true);
            menu_title.setDisable(true);
            menu_feedback.setDisable(true);
            menu_about.setDisable(true);

            //Evtle Daten aus vorherigen Spielen ueberschreiben <- Statische Zaehler
            Player.reset();
            stone.reset();
            position.reset();
            Spieler = null;

            //2 neue Spieler erstellen und Farben zuweisen
            Spieler = new Player[2];
            Spieler[0] = new Player(Color.WHITE);
            Spieler[0].setActive(true);
            Spieler[1] = new Player(Color.BLACK);

            //Da bei Spielererstellung neue Spielsteine generiert wurden werden die Einstellungen an den Steinen erstmalig uebernommen
            applyStones();
            //Spiel wurde gestartet -> Variable wird fuer andere Funktionen auf "true" gesetzt
            GAMESTARTED = true;
        }
    }

    //Startinitialisierung der Spielsteine bzw. der ImageViews -> Auslesen der mainFrame.fxml, Bider aus anderer Resource laden und Clickevents hinzufuegen
    private void initstones(){
        //Neue Bilddateien festlegen
        try {
            stone0.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone1.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone2.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone3.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone4.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone5.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone6.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone7.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone8.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone9.setImage(new Image(WHITESTONE.toURI().toURL().toString()));
            stone10.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone11.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone12.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone13.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone14.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone15.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone16.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone17.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone18.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
            stone19.setImage(new Image(BLACKSTONE.toURI().toURL().toString()));
        }catch(Exception ignored){}


        //ImageView Array mit neuen Daten beschreiben
        Spielsteine[0] = stone0;
        Spielsteine[1] = stone1;
        Spielsteine[2] = stone2;
        Spielsteine[3] = stone3;
        Spielsteine[4] = stone4;
        Spielsteine[5] = stone5;
        Spielsteine[6] = stone6;
        Spielsteine[7] = stone7;
        Spielsteine[8] = stone8;
        Spielsteine[9] = stone9;
        Spielsteine[10] = stone10;
        Spielsteine[11] = stone11;
        Spielsteine[12] = stone12;
        Spielsteine[13] = stone13;
        Spielsteine[14] = stone14;
        Spielsteine[15] = stone15;
        Spielsteine[16] = stone16;
        Spielsteine[17] = stone17;
        Spielsteine[18] = stone18;
        Spielsteine[19] = stone19;

        //Allen Spielsteinen ein Clickevent hinzufuegen, damit diese ausgewaehlt werden koennen
        for (ImageView Spielstein: Spielsteine){
            Spielstein.setOnMouseClicked(clickevent);
            Spielstein.setOnMouseMoved(hoverevent);
            Spielstein.setFitHeight(stone.IMAGESIZE);
            Spielstein.setFitWidth(stone.IMAGESIZE);
        }

    }

    //Aenderungen an den Spielsteinen uebernehmen z.B. Aenderungen an den Koordinaten
    private void applyStones(){

        //Automatisches Anpassen aller Spielsteine fuer das mittige ausrichten auf der gegebenen Position
        for (int i = 0; i < 20; i++) {   //
            if (i < Spieler[0].getStones().length) {    //Spielsteine gehen von 0-9 und von 10-19 fuer die Spieler 0 und 1
                //Spieler 1
                Spielsteine[i].setLayoutX(coordinate.makeImageCoordinate(Spieler[0].getStone(i).getCoordinate(),stone.IMAGESIZE,stone.IMAGESIZE).getX());
                Spielsteine[i].setLayoutY(coordinate.makeImageCoordinate(Spieler[0].getStone(i).getCoordinate(),stone.IMAGESIZE,stone.IMAGESIZE).getY());
                Spielsteine[i].setVisible(true);
            } else {
                //Spieler 2
                Spielsteine[i].setLayoutX(coordinate.makeImageCoordinate(Spieler[1].getStone(i-10).getCoordinate(),stone.IMAGESIZE,stone.IMAGESIZE).getX());
                Spielsteine[i].setLayoutY(coordinate.makeImageCoordinate(Spieler[1].getStone(i-10).getCoordinate(),stone.IMAGESIZE,stone.IMAGESIZE).getY());
                Spielsteine[i].setVisible(true);
            }
        }

        //Daten aus dem SpielsteinArray uebernehmen
        stone0 = Spielsteine[0];
        stone1 = Spielsteine[1];
        stone2 = Spielsteine[2];
        stone3 = Spielsteine[3];
        stone4 = Spielsteine[4];
        stone5 = Spielsteine[5];
        stone6 = Spielsteine[6];
        stone7 = Spielsteine[7];
        stone8 = Spielsteine[8];
        stone9 = Spielsteine[9];
        stone10 = Spielsteine[10];
        stone11 = Spielsteine[11];
        stone12 = Spielsteine[12];
        stone13 = Spielsteine[13];
        stone14 = Spielsteine[14];
        stone15 = Spielsteine[15];
        stone16 = Spielsteine[16];
        stone17 = Spielsteine[17];
        stone18 = Spielsteine[18];
        stone19 = Spielsteine[19];
    }

    //Alle Steine auf einmal sichtbar bzw. unsichtbar setzen z.B. bei Spielstart
    private void setStonesVisible(boolean Visible){
        //Sollte sich von selbst erklaeren
        if (Visible){
            stone0.setVisible(true);
            stone1.setVisible(true);
            stone2.setVisible(true);
            stone3.setVisible(true);
            stone4.setVisible(true);
            stone5.setVisible(true);
            stone6.setVisible(true);
            stone7.setVisible(true);
            stone8.setVisible(true);
            stone9.setVisible(true);
            stone10.setVisible(true);
            stone11.setVisible(true);
            stone12.setVisible(true);
            stone13.setVisible(true);
            stone14.setVisible(true);
            stone15.setVisible(true);
            stone16.setVisible(true);
            stone17.setVisible(true);
            stone18.setVisible(true);
            stone19.setVisible(true);
        }else{
            stone0.setVisible(false);
            stone1.setVisible(false);
            stone2.setVisible(false);
            stone3.setVisible(false);
            stone4.setVisible(false);
            stone5.setVisible(false);
            stone6.setVisible(false);
            stone7.setVisible(false);
            stone8.setVisible(false);
            stone9.setVisible(false);
            stone10.setVisible(false);
            stone11.setVisible(false);
            stone12.setVisible(false);
            stone13.setVisible(false);
            stone14.setVisible(false);
            stone15.setVisible(false);
            stone16.setVisible(false);
            stone17.setVisible(false);
            stone18.setVisible(false);
            stone19.setVisible(false);

        }
    }

    //Allen Spielsteine eine Tranzparanz hinzufuegen z.B. bei Dynamischem wechsel zwischen Menu und Spiel
    private void setStonesOpacity(double Opacity) {
        //Sollte sich selbst erklaeren
        stone0.setOpacity(Opacity);
        stone1.setOpacity(Opacity);
        stone2.setOpacity(Opacity);
        stone3.setOpacity(Opacity);
        stone4.setOpacity(Opacity);
        stone5.setOpacity(Opacity);
        stone6.setOpacity(Opacity);
        stone7.setOpacity(Opacity);
        stone8.setOpacity(Opacity);
        stone9.setOpacity(Opacity);
        stone10.setOpacity(Opacity);
        stone11.setOpacity(Opacity);
        stone12.setOpacity(Opacity);
        stone13.setOpacity(Opacity);
        stone14.setOpacity(Opacity);
        stone15.setOpacity(Opacity);
        stone16.setOpacity(Opacity);
        stone17.setOpacity(Opacity);
        stone18.setOpacity(Opacity);
        stone19.setOpacity(Opacity);
    }

    //FXML
    //Settings Button wurde gedrueckt
    @FXML
    @SuppressWarnings("unused")
    private void settings(ActionEvent event) throws IOException {
        if (!ABOUTWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !SETTINGWINDOW_ACTIVE) {
            try {
                //Clicksound spielen
                BUTTONKLICK.play();
                SETTINGWINDOW_ACTIVE = true;
                String Theme = programSettings.getProperty("theme");
                //Neues Fenster generieren und anzeigen <- Verarbeitung uebernimmt die Klasse Controller_settings (in fxml Datei festgelegt)
                stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("settings.fxml"));
                stage.setTitle("Einstellungen");
                Scene scene = new Scene(root, 360, 400);
                stage.setResizable(false);
                //EventHandler hinzufuegen, welcher aufgerufen wird wenn das Fenster geschlossen wird
                stage.setOnCloseRequest(event1 -> {
                    //Themepack auswahl auf aenderungen pruefen und ggf. versuchen dieses zu laden -!> Default Theme Laden
                    SETTINGWINDOW_ACTIVE = false;
                    if (!Objects.equals(Theme, Controller.ChoosenThemePack)) {
                        ProgramThemePack = new ThemePack(Controller.ChoosenThemePack);
                        if (!ProgramThemePack.loadTheme()) {
                            ThemePack.makedefaultpack();
                            ProgramThemePack = new ThemePack("default");
                            ProgramThemePack.loadTheme();
                        }
                        programSettings.setProperty("theme", ProgramThemePack.getName());
                        Controller.UpdateImages = true;
                    }

                    //Log schreiben
                    if (!programSettings.saveProperties()) {
                        Controller.Sout("DEBUG -> ERROR: Faild to Store Settings");
                    } else
                        Controller.Sout("DEBUG -> INFO: Settings stored in " + programSettings.getFile().getAbsolutePath());

                    stage.close();
                });
                stage.initModality(Modality.APPLICATION_MODAL); //Fenser im Focus einfangen
                stage.setScene(scene);
                stage.show();
            }catch (Exception e){
                writeerror(e);
            }
        }
    }

    //Feedback Button wurde gedrueckt
    @FXML
    @SuppressWarnings("unused")
    private void Feedback(ActionEvent event) throws IOException {
        if (!ABOUTWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !GAMESTARTED && !SETTINGWINDOW_ACTIVE) {
            try {
                //Clicksound spielen
                BUTTONKLICK.play();
                FEEDBACKWINDOW_ACTIVE = true;
                //Neues Fenster generieren und anzeigen <- Verarbeitung uebernimmt die Klasse Controller_Feedback (in fxml Datei festgelegt)
                stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("feedback.fxml"));
                stage.setTitle("Feedback Assistent");

                Scene scene = new Scene(root, 680, 400);
                stage.setResizable(false);
                //EventHandler hinzufuegen, welcher aufgerufen wird wenn das Fenster geschlossen wird
                stage.setOnCloseRequest(event1 -> {
                    //Themepack auswahl auf aenderungen pruefen und ggf. versuchen dieses zu laden -!> Default Theme Laden
                    FEEDBACKWINDOW_ACTIVE = false;
                    Controller_Feedback.timer.cancel();
                    stage.close();
                });
                //Fenser im Focus einfangen
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            }catch (Exception e){
                writeerror(e);
            }
        }
    }

    //About Button wurde gedrueckt
    @FXML
    @SuppressWarnings("unused")
    private void About(ActionEvent event) throws IOException {
        if (!ABOUTWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !GAMESTARTED && !SETTINGWINDOW_ACTIVE) {
            try {
                //Clicksound spielen
                BUTTONKLICK.play();
                ABOUTWINDOW_ACTIVE = true;
                //Neues Fenster generieren und anzeigen <- Verarbeitung uebernimmt die Klasse Controller_Feedback (in fxml Datei festgelegt)
                stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("about.fxml"));
                stage.setTitle("Über uns");

                Scene scene = new Scene(root, 360, 400);
                stage.setResizable(false);
                //EventHandler hinzufuegen, welcher aufgerufen wird wenn das Fenster geschlossen wird
                stage.setOnCloseRequest(event1 -> {
                    //Themepack auswahl auf aenderungen pruefen und ggf. versuchen dieses zu laden -!> Default Theme Laden
                    ABOUTWINDOW_ACTIVE = false;
                    stage.close();
                });
                stage.initModality(Modality.APPLICATION_MODAL); //Fenser im Focus einfangen
                stage.setScene(scene);
                stage.show();
            }catch (Exception e){
                writeerror(e);
            }
        }
    }

    //Gewonnen/Unentschieden Fenster anzeigen
    private void showWinFrame(int ActivePlayer){
        if (!WINWINDOW_ACTIVE) {
            GAMESTARTED = false;
            if (turnsWithoutMill >= 50) Sout("Its a Draw!");
            else Sout(Spieler[ActivePlayer].getName() + " won the Game!");
            //Clicksound spielen
            BUTTONKLICK.play();
            WINWINDOW_ACTIVE = true;
            //Neues Fenster generieren und anzeigen <- Verarbeitung uebernimmt die Klasse Controller_winFrame (in fxml Datei festgelegt)
            stage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("winFrame.fxml"));
            } catch (IOException e) {
                writeerror(e);
            }
            if (turnsWithoutMill >= 50) stage.setTitle("Unentschieden");
            else stage.setTitle(Spieler[ActivePlayer].getName() + " gewinnt!");
            assert root != null;    // -> ERROR wenn nicht vorhanden
            Scene scene = new Scene(root, 500, 150);
            stage.setResizable(false);
            //EventHandler hinzufuegen, welcher aufgerufen wird wenn das Fenster geschlossen wird
            stage.setOnCloseRequest(event1 -> {
                Sout("Going back to MainMenue");
                RESTARTGAME = true;
                WINWINDOW_ACTIVE = false;
                stage.close();
            });
            stage.initModality(Modality.APPLICATION_MODAL); //Fenser im Focus einfangen
            stage.setScene(scene);
            stage.show();
        }
    }

    //Spiel Beenden Button wurde gedrueckt
    @FXML
    @SuppressWarnings("unused")
    private void exitgame(ActionEvent event){
        //Log schreiben und Programm beenden
        if (!ABOUTWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !SETTINGWINDOW_ACTIVE) {
            //Clicksound spielen
            BUTTONKLICK.play();
            //Programm beenden
            writetolog("Program finished!\n");
            System.exit(0);
        }
    }


    //EventHandler, welcher aufgerufen wird, wenn die Maus ueber einen Gegenstand, welcher mit einem MouseMoved Listener ausgestattet wurde, drueber bewegt
    private EventHandler<MouseEvent> hoverevent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Mouse.setCoordinates(event.getSceneX(),event.getSceneY());

            checkforeffects();

            //Neue Position des HoverImages uebernehmen
            positionhover.setLayoutX(coordinate.makeImageCoordinate(Mouse,positionhover.getFitHeight(),positionhover.getFitWidth()).getX());
            positionhover.setLayoutY(coordinate.makeImageCoordinate(Mouse,positionhover.getFitHeight(),positionhover.getFitWidth()).getY());

            //Wenn Spiel noch nicht gestartet ist, soll, wenn die Maus ueber die Buttons "schwebt", ein sound abgespielt werden (macht das ganze Interaktiver und lebendiger)
            //Die Variablen mit HOVER_* dienen als Speicher ob der Sound, solange der Mauszeiger noch auf der Schaltflaeche ist, schonmal abgespielt wurde
            if (!GAMESTARTED && !SETTINGWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !ABOUTWINDOW_ACTIVE && !UPDATEWINDOW_ACTIVE){
                if (event.getSource().toString().contains("id=menu_startgame")){  //StartButton Hover
                    if (HOVER_STARTBUTTON) {
                        BUTTONHOVER.play();
                        HOVER_STARTBUTTON = false;
                        Sout("EVENT -> ButtonHover: Start");
                    }
                }else
                    HOVER_STARTBUTTON = true;

                if (event.getSource().toString().contains("id=menu_settings")) {  //SettingsButton Hover
                    if (HOVER_SETTINGSBUTTON) {
                        BUTTONHOVER.play();
                        HOVER_SETTINGSBUTTON = false;
                        Sout("EVENT -> ButtonHover: Settings");
                    }
                }else
                    HOVER_SETTINGSBUTTON = true;

                if (event.getSource().toString().contains("id=menu_exitgame")) {  //ExitButton Hover
                    if (HOVER_EXITBUTTON) {
                        BUTTONHOVER.play();
                        HOVER_EXITBUTTON = false;
                        Sout("EVENT -> ButtonHover: Exit");
                    }
                }else
                    HOVER_EXITBUTTON = true;

                if (event.getSource().toString().contains("id=menu_feedback")) {  //FeedbackButton Hover
                    if (HOVER_FEEDBACKBUTTON) {
                        BUTTONHOVER.play();
                        HOVER_FEEDBACKBUTTON = false;
                        Sout("EVENT -> ButtonHover: Feedback");
                    }
                }else
                    HOVER_FEEDBACKBUTTON = true;

                if (event.getSource().toString().contains("id=menu_about")) {  //FeedbackButton Hover
                    if (HOVER_ABOUTBUTTON) {
                        BUTTONHOVER.play();
                        HOVER_ABOUTBUTTON = false;
                        Sout("EVENT -> ButtonHover: About");
                    }
                }else
                    HOVER_ABOUTBUTTON = true;
            }
        }
    };

    //Pruefen ob effekte eingeblendet werden sollen
    private void checkforeffects(){
        //HoverImage abarbeiten
        if (GAMESTARTED && !WINWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !ABOUTWINDOW_ACTIVE && !UPDATEWINDOW_ACTIVE) {
            int Point = position.getRectedPoint(Mouse); //Aktuellen HoverPoint auslesen
            int ActivePlayer = Player.getactivePlayer();

            if (Point != -1) {
                //Wenn Position oder Spieler sich veraendert hat
                if ((Point != position.getHoveringPosition() || position.getLastCheckedPlayer() != ActivePlayer)) {

                    //Wenn Spieler zuvor noch keinen Stein ausgewaehlt hat bzw. keinen auswaehlen konnte
                    if (Spieler[ActivePlayer].getChoosenStone() == -1) {

                        //Wenn der aktive Spieler keine Steine Klauen darf
                        if (!Spieler[ActivePlayer].CanTakeStone()) {
                            position.setLastCheckedPlayer(Player.getactivePlayer());
                            position.setHoveringPosition(Point);
                            //Wenn die Mauskoordinaten einen neuen Punkt beruehren

                            //Wenn die Position Frei ist
                            if (!position.isPositionFree(Point)) {
                                if (stone.getNextUnusedStone(ActivePlayer) == -1){
                                    if (position.getPlayerIndexforPoint(Point) != ActivePlayer)positionhover.setVisible(true); else positionhover.setVisible(false);
                                }else
                                    positionhover.setVisible(true);
                            }
                        } else {    //Wenn dieser Steine klauen darf

                            //Wenn HoverStein -> Eigener Stein oder in aktiver Muehle
                            int StonePlayer = position.getPlayerIndexforPoint(Point);
                            if (StonePlayer != -1)
                                if (StonePlayer == ActivePlayer || (Mill(StonePlayer, Point) && Spieler[StonePlayer].getDeathStones() < DeathStonestoJump && !Spieler[StonePlayer].getHasOnlyMills())) {
                                    positionhover.setVisible(true);
                                } else positionhover.setVisible(false);
                        }
                    } else {    //Wenn Spieler zuvor einen Stein ausgewaehlt hat

                        int ChoosenStone = Spieler[ActivePlayer].getChoosenStone();
                        if (ChoosenStone >= 10) ChoosenStone -= 10;
                        //Pruefen ob Zielposition frei ist
                        if (position.isPositionFree(Point)) {
                            if (!((position.isabletoReachPoint(Spieler[ActivePlayer].getStone(ChoosenStone).getPosition(), Point)) || Spieler[ActivePlayer].getDeathStones() == 7)) {
                                positionhover.setVisible(true);
                            }else
                                positionhover.setVisible(false);
                        }else{
                            if (Spieler[ActivePlayer].getChoosenStone() != position.getStoneIndexforPoint(Point))positionhover.setVisible(true); else positionhover.setVisible(false);
                        }

                    }
                }else
                if (Spieler[ActivePlayer].CanTakeStone()) positionhover.setVisible(true);
            }else{//Wenn kein Punkt unter Maus
                position.setHoveringPosition(Point);
                positionhover.setVisible(false);
            }

            //MenuBar ein und ausfahren
            menuBar_ACTIVE = Mouse.getY() < 30;

        }
    }

    //EventHandler, welcher aufgerufen wird, wenn auf einen Gegenstand geklickt wurde, welcher mit einem MouseClicked Listener ausgestattet wurde
    private EventHandler<MouseEvent> clickevent = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            //Mausposition Speichern
            Mouse.setCoordinates(event.getSceneX(),event.getSceneY());
            Sout("EVENT -> Clickevent: On X: " + Mouse.getX() + " Y: " + Mouse.getY());

            //Wenn das Spiel "Im Gange" ist und der zuletzt ausgefuehrte Zug abgeschlossen ist <- Feststellung in CheckStones()
            if (GAMESTARTED && turnFinish && !WINWINDOW_ACTIVE && !FEEDBACKWINDOW_ACTIVE && !ABOUTWINDOW_ACTIVE && !UPDATEWINDOW_ACTIVE) {
                turnFinish = false;

                boolean SwitchPlayer = true;    //Soll der Spieler gewechselt werden (Wichtig fuer fehlerhafte Auswahl oder abwahl von Steinen)

                int Point = position.getRectedPoint(new coordinate(Mouse.getX(), Mouse.getY()));    //Pruefen ob angeklickte Position eine gueltige Spielfeldposition ist und Position zurueckgeben

                if (Point != -1) {  //Wenn angeklickte Position mit einer Spielfeldposition uebereinstimmt

                    int ActivePlayer = Player.getactivePlayer();    //Aktuellen Spieler abfragen

                    if (Spieler[ActivePlayer].getChoosenStone() == -1) {    //Wenn kein Spielstein vorher ausgewaehlt wurde

                        int StoneINDEX = stone.getNextUnusedStone(ActivePlayer);    //Naechsten unbenutzen Spielstein finden
                        boolean allStonesPlaced = (StoneINDEX == -1);   //Pruefen ob Stein gueltig ist
                        int ChoosenStone = position.getStoneIndexforPoint(Point);
                        //Wenn Spieler stein klauen darf
                        if (Spieler[ActivePlayer].CanTakeStone()){
                            //Wenn ausgewaelter Stein vom Gegner ist
                            if (ChoosenStone != -1 && ((Spieler[ActivePlayer].getColor() == Color.WHITE && ChoosenStone >= 10) || (Spieler[ActivePlayer].getColor() == Color.BLACK && ChoosenStone < 10)) && (!Mill(Player.getnextPlayer(),Point) || Spieler[Player.getnextPlayer()].getDeathStones() >= DeathStonestoJump || Spieler[Player.getnextPlayer()].getHasOnlyMills())) {
                                Sout("DEBUG -> INFO: Destroying Stone " + ChoosenStone + " from Player " + ActivePlayer + " on Position " + Point);
                                turnsWithoutMill = 0;
                                if (ChoosenStone >= 10)ChoosenStone -= 10;  //Steinindex auf Spieler rechnen
                                Spieler[Player.getnextPlayer()].getStone(ChoosenStone).destroystone();  //Stein zerstoeren
                                Spieler[Player.getnextPlayer()].addDeathStones();   //Unbenutzbare steine hinzufuegen
                                Spieler[ActivePlayer].setPoints(Spieler[ActivePlayer].getPoints() + Points_adder);  //Punkte zaehlen
                                Spieler[ActivePlayer].setCanTakeStone(false);   //Spieler darf keinen Stein mehr klauen
                                if (Spieler[Player.getnextPlayer()].getDeathStones() == DeathStonestoJump){
                                    displayMessage(Spieler[Player.getnextPlayer()].getName() + ", du kannst Springen!",3000);
                                }

                                //Wenn ein Spieler gewinnt wird unten bei Spielerwechsel bearbeitet

                            }else
                                SwitchPlayer = false;
                        }else {
                            //Wenn bereits alle Spielsteine gelegt wurden
                            if (allStonesPlaced) {
                                //Pruefen ob gewaehlter Stein gueltig und Stein gehoert Aktuellem Spieler
                                if (ChoosenStone != -1 && ((Spieler[ActivePlayer].getColor() == Color.WHITE && ChoosenStone < 10) || (Spieler[ActivePlayer].getColor() == Color.BLACK && ChoosenStone >= 10))) {
                                    //Stein auswaehlen und speichern wenn dieser auch einem gehoert
                                    Sout("DEBUG -> INFO: Saved Choosen Stone");
                                    //Neuen ausgewaehlten Stein speichern
                                    Spieler[ActivePlayer].setChoosenStone(ChoosenStone);
                                    Spielsteine[ChoosenStone].setOpacity(0.5);

                                }
                                SwitchPlayer = false;
                            }else{
                                //Wenn gewaehlte Position frei ist
                                if (position.isPositionFree(Point)) {
                                    Sout("DEBUG -> INFO: Player + " + ActivePlayer + " placed Stone " + StoneINDEX + " on Position " + Point);
                                    Spieler[ActivePlayer].getStone(StoneINDEX).setFinalCoordinate(new coordinate(position.getCoordinateforPoint(Point).getX(), position.getCoordinateforPoint(Point).getY()));
                                    Spieler[ActivePlayer].getStone(StoneINDEX).setPosition(Point);
                                    if (Mill(ActivePlayer,Point)){
                                        displayMessage(Spieler[ActivePlayer].getName() + " kann einen Stein klauen",4000);
                                        Spieler[ActivePlayer].setCanTakeStone(true);    //Variable setzen: Spieler kann als naechsten Zug einen Stein vom gegener Klauen
                                        SwitchPlayer = false;
                                    }
                                }else
                                    SwitchPlayer = false;
                            }
                        }


                    }else {//ChoosenStone
                        //Wenn vorher ein stein ausgewaehlt wurde
                        Sout("DEBUG -> INFO: Choosen Stone " + Spieler[ActivePlayer].getChoosenStone() + " Point: " + Point);
                        int ChoosenStone = Spieler[ActivePlayer].getChoosenStone();
                        if (ChoosenStone != position.getStoneIndexforPoint(Point)) {//Wenn Spieler Stein abwaehlen will
                            if (ChoosenStone >= 10) ChoosenStone -= 10;
                            //Pruefen ob Zielposition frei ist
                            if (position.isPositionFree(Point)) {
                                Sout("DEBUG -> INFO: Position is Free");
                                //Pruefen ob ausgewaelter Stein die ausgewaelte Zielposition erreichen kann
                                // (einmal ueber die anliegenden Positionen und wenn Spieler springen kann(bei 3 uebrigen Steinen))
                                //Ausgewaelten Stein abrufen
                                if ((position.isabletoReachPoint(Spieler[ActivePlayer].getStone(ChoosenStone).getPosition(), Point)) || Spieler[ActivePlayer].getDeathStones() == DeathStonestoJump) {
                                    Sout("DEBUG -> INFO: Stone can Reach Point");

                                    //Neue Zielcoordinaten festlegen
                                    Spieler[ActivePlayer].getStone(ChoosenStone).setFinalCoordinate(new coordinate(position.getCoordinateforPoint(Point).getX(), position.getCoordinateforPoint(Point).getY()));
                                    //Spielstein von alter Position entfernen
                                    position.remStonePosition(Spieler[ActivePlayer].getChoosenStone());
                                    //Spielstein neuer Position hinzufuegen
                                    Spieler[ActivePlayer].getStone(ChoosenStone).setPosition(Point);
                                    //Ausgewaehlten Stein wieder Voll sichtbar machen
                                    Spielsteine[Spieler[ActivePlayer].getChoosenStone()].setOpacity(1);
                                    //Auswahl zuruecksetzen
                                    Spieler[ActivePlayer].setChoosenStone(-1);
                                    //Pruefen ob letzter Zug zu einer Muehle gefuert hat
                                    if (Mill(ActivePlayer, Point)){
                                        displayMessage(Spieler[ActivePlayer].getName() + " kann einen Stein klauen",4000);
                                        Spieler[ActivePlayer].setCanTakeStone(true);    //Variable setzen: Spieler kann als naechsten Zug einen Stein vom gegener Klauen
                                        SwitchPlayer = false;
                                    }


                                } else
                                    SwitchPlayer = false;


                            } else
                                SwitchPlayer = false;
                        }else {//Wenn abwahl des Steins
                            Spielsteine[Spieler[ActivePlayer].getChoosenStone()].setOpacity(1);
                            //Ausgewaehlten Stein aus speicher entfernen
                            Spieler[ActivePlayer].setChoosenStone(-1);
                            SwitchPlayer = false;
                        }
                    }
                    if (SwitchPlayer) {  //Wenn Spieler gewechselt werden soll

                        boolean Moveable = !(!Spieler[Player.getnextPlayer()].isMoveable() && stone.getNextUnusedStone(Player.getnextPlayer()) == -1);

                        if (!Moveable)displayMessage(Spieler[Player.getnextPlayer()].getName() + ", keine Züge mehr möglich!",5000);

                        //Wenn ein Spieler gewinnt oder das Spiel unentschieden gespielt wird
                        if (turnsWithoutMill >= 50 || !Moveable || Spieler[Player.getnextPlayer()].getDeathStones() > DeathStonestoJump) {
                            showWinFrame(ActivePlayer);
                        }
                        //Spieler wechhseln und Spielerwechsel aktivieren
                        Spieler[ActivePlayer].hasonlymills();
                        Player.nextPlayer();
                        playerchange = true;
                    }
                    checkforeffects();

                }//allowedPick
                else Sout("DEBUG -> INFO: No avariable Point!");
            }//gamestarted
        }

    };

    //Funktion zum ueberpruefen ob ein Spieler eine Muehle gelegt hat (In eigener Funktion, da diese 2 mal aufgerufen werden muss)
    private static boolean Mill(int ActivePlayer, int Point){
        //Wenn Muehle
        return position.isWinCombination(ActivePlayer, Point);
    }

    //Timer, welcher automatisch alle 10ms aufgerufen wird (Zum anzeigen von Dynamischem Content wie z.B. Bewegende Spielsteine oder Tranzparenzaenderungen)
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //Wenn Spiel gestartet wurde
            if (GAMESTARTED){
                //Wenn Menu noch sichtbar ist
                if (MENUOPACITY > 0.0){
                    //Speicher der Sichtbarkeit runter zaehlen
                    MENUOPACITY -= ((double) ANIMATIONSPEED / 1000);

                    //Sichtbar der Spieloberflaeche erhoehen
                    game_playground.setOpacity(1.0 - MENUOPACITY);
                    game_title.setOpacity(1.0 - MENUOPACITY);
                    game_rand1.setOpacity(1.0 - MENUOPACITY);
                    game_rand.setOpacity(1.0 - MENUOPACITY);

                    pointstext1.setOpacity(1.0 - MENUOPACITY);
                    pointstext2.setOpacity(1.0 - MENUOPACITY);
                    pointlabel_player1.setOpacity(1.0 - MENUOPACITY);
                    pointlabel_player2.setOpacity(1.0 - MENUOPACITY);
                    namefield_player1.setOpacity(1.0 - MENUOPACITY);
                    namefield_player2.setOpacity(1.0 - MENUOPACITY);

                    player1timetext.setOpacity(1.0 - MENUOPACITY);
                    player1timelabel.setOpacity(1.0 - MENUOPACITY);
                    player2timetext.setOpacity(1.0 - MENUOPACITY);
                    player2timelabel.setOpacity(1.0 - MENUOPACITY);

                    setStonesOpacity(1.0 - MENUOPACITY);

                    //Sichtbarkeit des Menues verringern
                    menu_background.setOpacity(MENUOPACITY);
                    menu_startgame.setOpacity(MENUOPACITY);
                    menu_settings.setOpacity(MENUOPACITY);
                    menu_exitgame.setOpacity(MENUOPACITY);
                    menu_title.setOpacity(MENUOPACITY);
                    menu_feedback.setOpacity(MENUOPACITY);
                    menu_about.setOpacity(MENUOPACITY);


                }else if (MENUOPACITY == 0){    //Wenn Menu unsichtbar ,sichtbarkeit komplett Deaktivieren und Festlegen das dies nicht mehr geaendert werden darf
                    menu_background.setVisible(false);
                    menu_startgame.setVisible(false);
                    menu_settings.setVisible(false);
                    menu_exitgame.setVisible(false);
                    menu_feedback.setVisible(false);
                    menu_title.setVisible(false);
                    menu_about.setVisible(false);
                    MENUOPACITY = -2;
                }else{

                    //Dynamischer uebergang zwischen Spielerstatus wenn Menu nichtmehr sichtbar ist
                    if (playerchange && turnFinish){  //Findet ein Spielerwechsel statt

                        //Pruefen ob Sichtbarkeit hoch oder runtergezaehlt werden muss (bei erreichen des Ziels Spielerwechselbeenden)
                        if (Player.getactivePlayer() == 0){
                            if (PlayerStatus1Opacity < 1.00)PlayerStatus1Opacity+= ((double)ANIMATIONSPEED / 200); else playerchange = false;

                        }else{
                            if (PlayerStatus1Opacity > 0.00)PlayerStatus1Opacity-= ((double)ANIMATIONSPEED / 200); else playerchange = false;

                        }

                        //Veraenderte werte uebernehmen
                        playerstatus_player1.setOpacity(PlayerStatus1Opacity);
                        playerstatus_player2.setOpacity(1.0 - PlayerStatus1Opacity);
                    }

                }

                //Wenn aenderungen an Labels oder TextFeldern (Als eigene Instanz starten da sonst Arrayueberlaufe passieren koennten - Ausserdem Zeitkritisch -> Grafische Elemente werden geaendert)
                if (!Objects.equals(Spieler[0].getPoints() + "", pointlabel_player1.getText()) || !Objects.equals(Spieler[1].getPoints() + "", pointlabel_player2.getText()) || !Objects.equals(Spieler[0].getName(), namefield_player1.getText()) || !Objects.equals(Spieler[1].getName(), namefield_player2.getText())){

                    //Versuchen als eigene Instanz zu starten und ohne "Zeitdruck" zu aendern
                    try {
                        Platform.runLater(() -> {
                            //Punkte bei aenderung auf Label anpassen
                            if (!Objects.equals(Spieler[0].getPoints() + "", pointlabel_player1.getText()))pointlabel_player1.setText(Spieler[0].getPoints() +"");
                            if (!Objects.equals(Spieler[1].getPoints() + "", pointlabel_player2.getText()))pointlabel_player2.setText(Spieler[1].getPoints() +"");

                            //Spielername bei aenderung auf Label anpassen
                            if (!Objects.equals(Spieler[0].getName(), namefield_player1.getText()))Spieler[0].setName(namefield_player1.getText());
                            if (!Objects.equals(Spieler[1].getName(), namefield_player2.getText()))Spieler[1].setName(namefield_player2.getText());
                        });
                    }catch(Exception e){
                        System.err.println("DEBUG -> ERROR: Aenderung der Punkte bzw. der Spielernamen");
                    }
                }
                //Spielsteine auf aenderungen Pruefen und ggf. anpassen
                CheckStones();

                Spieler[Player.getactivePlayer()].setTurntime(new Date().getTime() - Spieler[Player.getactivePlayer()].getTurnstarttime());
                if (Player.getactivePlayer() == 0)Platform.runLater(() -> player1timelabel.setText(new SimpleDateFormat("sss").format(Spieler[0].getTurntime()) + " Sek."));
                if (Player.getactivePlayer() == 1)Platform.runLater(() -> player2timelabel.setText(new SimpleDateFormat("sss").format(Spieler[1].getTurntime()) + " Sek."));


            }else//GAMESTARTED
                if (!PROGRAMMSTARTED){
                    PROGRAMMSTARTED = true;
                    Platform.runLater(()->checkforprogramupdate());
                }
            //Prufen ob MessageLabel aktiv ist -> Position und anzeigezeit ueberpruefen und ggf. Positionsaenderungen
            //an der Y-Achse vornehmen (je nachdem ob Label ein oder ausgeblendet wird <- Mit Dynamischer Geschwindigkeit)
            if (MessageLabelActive){
                if (MessageLabelVisibleUntil == 0 || MessageLabelVisibleUntil > new Date().getTime()){
                    if (MessageLabel.getLayoutY() < 20){
                        Platform.runLater(() -> MessageLabel.setLayoutY(MessageLabel.getLayoutY() + MessageSpeed)); //Position nach unten veraendern
                        if (MessageSpeed < DEFAULTMessageSpeed)MessageSpeed += DEFAULTMessageSpeed / 10;
                    }else
                    MessageSpeed = DEFAULTMessageSpeed;
                }else {
                    Messages.remove(0);
                    MessagesTime.remove(0);
                    MessageLabelActive = false;
                }
            }else{
                if (MessageLabel.getLayoutY() + MessageLabel.getHeight() > 0){
                    Platform.runLater(() -> MessageLabel.setLayoutY(MessageLabel.getLayoutY() - MessageSpeed)); //Position nach oben veraendern
                    if (MessageSpeed > DEFAULTMessageSpeed)MessageSpeed -= DEFAULTMessageSpeed;
                }else {
                    if (MessageSpeed != DEFAULTMessageSpeed)MessageSpeed = DEFAULTMessageSpeed;

                    if (Messages.size() > 0) {
                        MessageLabelActive = true;
                        try {
                            Platform.runLater(() -> MessageLabel.setText(Messages.get(0)));
                        }catch (Exception ignored){}
                        MessageLabelVisibleUntil = MessagesTime.get(0);
                    }

                }
            }

            //Wenn ein neues Themepack gewaehlt wurde werden hier die Resourcen "live" uebernommen
            if (UpdateImages){
                Sout("Updating Themepack Images");
                Platform.runLater(() -> {
                    try {
                        menu_background.setImage(new Image(MENUE.toURI().toURL().toString()));

                            game_playground.setImage(new Image(GAMEFIELD.toURI().toURL().toString()));
                            positionhover.setImage(new Image(Controller.DENY.toURI().toURL().toString()));

                            initstones();

                            game_rand.setImage(new Image(GAME_RAND.toURI().toURL().toString()));
                            game_rand1.setImage(game_rand.getImage());
                            displayMessage("Thema geladen: " + ProgramThemePack.getName().toUpperCase(), 3000);  //Aenderungen anzeigen
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                        });
                UpdateImages = false;
            }

            //Wenn Spiel abgeschlossen -> Gui reseten und neu laden
            if (RESTARTGAME) {
                RESTARTGAME = false;
                loadHomeGui();
            }

            if (menuBar_ACTIVE && menubar.getLayoutY() < -1){
                Platform.runLater(() -> menubar.setLayoutY(menubar.getLayoutY() + ((double)ANIMATIONSPEED / 20)));
            }else
                if (!menuBar_ACTIVE && menubar.getLayoutY() > -29){
                    Platform.runLater(() -> menubar.setLayoutY(menubar.getLayoutY() - ((double)ANIMATIONSPEED / 20)));
                }
        }
    };

    //Funktion, welche die Spielsteinkoordinaten auf aenderungen Prueft und gegebenfalls anpasst
    private void CheckStones() {
        boolean changes = false;    //Speichert ob aenderungen an den Spielsteinen vorgenommen wurden -> Wichtig fuer Variable "turnFinish"
        for (Player player : Spieler) {   //Jeden Spieler durchlaufen
            for (stone Stone : player.getStones()) {  //Jeden Spielstein durchlaufen

                if (Stone.getCoordinate() != Stone.getFinalCoordinate()) {  //Nur wenn Zielkoordinaten von jetzigen Koordinaten abweichen aenderungen vornehmen -> Performance

                    //Differrenz berechnen
                    double speedX = Stone.getCoordinate().getX() - Stone.getFinalCoordinate().getX();
                    double speedY = Stone.getCoordinate().getY() - Stone.getFinalCoordinate().getY();

                    //Wenn Differenz negativ -> Umdrehen fuer weitere Rechnungen
                    if (speedY < 0) speedY *= -1.0;
                    if (speedX < 0) speedX *= -1.0;
                    double teiler;

                    //Teiler bestimmen <- Groesseren Differenzwert bestimmen
                    if (speedX >= speedY) {
                        teiler = speedX;
                    } else {
                        teiler = speedY;
                    }
                    //Geschwindigkeit anteilig und in abhaengigkeit zur Maximalgeschwindigkeit auf der X- und Y-Achse ausrechenen
                    speedX = (speedX / teiler) * ANIMATIONSPEED;
                    speedY = (speedY / teiler) * ANIMATIONSPEED;

                    //Pruefen in welche Richtung sich der Stein bewegen soll und dementsprechende Anpassungen vornehmen
                    if (Stone.getCoordinate().getX() - Stone.getFinalCoordinate().getX() < -ANIMATIONSPEED) {  //Wenn Stein von LINKS nach RECHTS
                        changes = true;
                        Stone.setCoordinate(new coordinate(Stone.getCoordinate().getX() + speedX, Stone.getCoordinate().getY()));
                    } else if (Stone.getCoordinate().getX() - Stone.getFinalCoordinate().getX() > ANIMATIONSPEED) {   //Wenn Stein von RECHTS nach LINKS
                        changes = true;
                        Stone.setCoordinate(new coordinate(Stone.getCoordinate().getX() - speedX, Stone.getCoordinate().getY()));
                    } else   //Sonst Koordinaten gleichsetzen um unnoetige folgende ueberpruefungen zu umgehen
                        if (Stone.getCoordinate().getX() != Stone.getFinalCoordinate().getX())
                            Stone.setCoordinate(new coordinate(Stone.getFinalCoordinate().getX(), Stone.getCoordinate().getY()));

                    if (Stone.getCoordinate().getY() - Stone.getFinalCoordinate().getY() < -ANIMATIONSPEED) {  //Wenn Stein von OBEN nach UNTEN (ACHTUNG Y = 0 -> OBEN)
                        changes = true;
                        Stone.setCoordinate(new coordinate(Stone.getCoordinate().getX(), Stone.getCoordinate().getY() + speedY));
                    } else if (Stone.getCoordinate().getY() - Stone.getFinalCoordinate().getY() > ANIMATIONSPEED) {   //Wenn Stein von UNTEN nach OBEN (ACHTUNG Y = 0 -> OBEN)
                        changes = true;
                        Stone.setCoordinate(new coordinate(Stone.getCoordinate().getX(), Stone.getCoordinate().getY() - speedY));
                    } else   //Sonst Koordinaten gleichsetzen um unnoetige folgende ueberpruefungen zu umgehen
                        if (Stone.getCoordinate().getY() != Stone.getFinalCoordinate().getY())
                            Stone.setCoordinate(new coordinate(Stone.getCoordinate().getX(), Stone.getFinalCoordinate().getY()));
                }
            }
        }
        //Wenn keine aenderungen vorgenommen wurden ist der Zug zuende -> Speichern
        turnFinish = !changes;

        //Anderungen an Spielsteinen uebernehmen
        applyStones();
    }

    //Pruefen ob es eine neue Version gibt
    private void checkforprogramupdate(){
        boolean UPDATE = false;
        try {
            URL url = new URL("http://oelrichs.garcia.keno.franziskusschule-whv.de/other/Softwareprojekt/version.txt");
            URLConnection conn =  url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            try{
                UPDATE = (NEWVERSION = Long.parseLong((NEWVERSION_ASTEXT = in.readLine()))) > VERSION_ASNUMBER;  //Update = true wenn aktuelle Version kleiner
                NEWVERSION_ASTEXT = NEWVERSION_ASTEXT.substring(0,2) + "." + NEWVERSION_ASTEXT.substring(2,4);
            }catch (Exception ignored){}
            in.close();
        }catch (Exception e){
            UPDATE = false;
        }

        if (UPDATE){
            if (!UPDATEWINDOW_ACTIVE) {
                //Clicksound spielen
                UPDATEWINDOW_ACTIVE = true;
                //Neues Fenster generieren und anzeigen <- Verarbeitung uebernimmt die Klasse Controller_update (in fxml Datei festgelegt)
                stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("update.fxml"));

                    stage.setTitle("Neues Update verfügbar!");
                    assert root != null;
                    Scene scene = new Scene(root, 500, 150);
                    stage.setResizable(false);
                    //EventHandler hinzufuegen, welcher aufgerufen wird wenn das Fenster geschlossen wird
                    stage.setOnCloseRequest(event1 -> {
                        Sout("Going back to MainMenue");
                        UPDATEWINDOW_ACTIVE = false;
                        stage.close();
                    });
                    stage.initModality(Modality.APPLICATION_MODAL); //Fenser im Focus einfangen
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                writeerror(e);
            }
            }

        }
    }

    //Schreibt eine Logdatei, welche Informationen zu evtl. auftretenden Fehlern enthaelt
    private static boolean writetolog(String output){
        Sout(output);   //Consolenausgabe
        boolean success = true;
        //Versuchen die Informationen in die Logdatei zu schreiben + Datum und Uhrzeit
        try {
            FileWriter fw = new FileWriter(logfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(new SimpleDateFormat("YYYY-MM-dd_HH:mm:ss - ").format(new Date().getTime()) + output + "\n");
            bw.close();
        }catch(Exception ignored){
            success = false;
        }
        return success;
    }

    //Fehlermeldungen in Log schreiben und ggf. Report Senden
    static void writeerror(Exception e){
        //Entstandene Fehler abfangen und in LogDatei schreiben
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        writetolog(sw.toString());
        if (programSettings.getProperty("autoerror").equals("true"))senderrorreport(sw.toString());

    }

    //ErrorReport als E-Mail an Entwickler senden
    private static void senderrorreport(String errorreport) {

        try {
            //Einstellungen fuer die Spaetere uebertragung anlegen
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            //Username und Passwort in Variable speichern
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            //Das eigentliche zu uebertragene "Paket" erstellen
            Message message = new MimeMessage(session);

            //Zieladressen hinzufuegen, Betreff und Nachricht
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SupportMailAdress));
            message.setFrom(new InternetAddress("GameMill@errorreports.de"));
            message.setSubject("Mill - Feedback Assistent");
            Controller.Sout("Try to send Feedback");

            //Alle Spielervariablen und Werte uebersihtlich in String schreiben
            String Variables = "";
            try {
                for (Player player : Spieler) {
                    Variables += "Player: " + player.getColor() + " DS: " + player.getDeathStones() + " CS:" + player.getChoosenStone() + "\n";
                    for (stone Stone : player.getStones()) {
                        Variables += "Stein: " + Stone.getPosition() + " COORDS: X:" + Stone.getCoordinate().getX() + " Y:" + Stone.getCoordinate().getY() + " FCOORDS: X:" + Stone.getFinalCoordinate().getX() + " Y:" + Stone.getFinalCoordinate().getY() + "\n";
                    }

                }
            }catch (Exception e){
                Variables += "Failed to read Players, Stones\n";
            }
            //Spieleinstellungen und Version des Spiels in String schreiben
            Variables += "\nSettings:\n" + programSettings.tostring();
            //Alle Informationen in die Nachricht "schieben"
            message.setText("-AUTOMATIC ERROR REPORT-\nName: Anonymous\nERRORREPORT:\n" + errorreport + "\n\nSystem Information:\nOS: " + System.getProperty("os.name") + "\nArchitektur: " + System.getProperty("os.arch") + "\nVersion: " + System.getProperty("os.version") + "\nProgrammVersion: " + VERSION_ASTEXT + "\n\nVariables:\n" + Variables);
            //E-Mail senden
            Transport.send(message);
            Sout("Automatic Errorreport send!");
        }catch(Exception e){
            //Wenn Fehler -> Ausgeben
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Sout(sw.toString());
            Sout("Failed to send automatic Report!");


        }

    }

    //Gibt Informationen in der Console aus und schreibt diese zusaetzlich in einen Detaillierten Log -> Fuer Offline Debugging
    static boolean Sout(String output){
        System.out.println(output);
        boolean success = true;
        try {
            FileWriter fw = new FileWriter(detaillog , true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(new SimpleDateFormat("YYYY-MM-dd_HH:mm:ss - ").format(new Date().getTime()) + output + "\n");
            bw.close();
        }catch(Exception ignored){
            success = false;
        }
        return success;
    }

    //Nachrichten auf Dynamischem Label anzeigen
    private void displayMessage(String Message, long DisplayTime){
        //Parameter festlegen und dem TimerTask die aenderungen machen lassen
        Sout("Displaying Message: \"" + Message + "\"");
        //Einfaches Hinzufuegen von Nachrichten durch "adden" von Daten zu den ArrayLists
        MessagesTime.add(new Date().getTime() + DisplayTime);
        Messages.add(Message);
    }

    //Option zum Aufgeben waerend des Spiels
    @FXML
    private void giveup(){
        Player.nextPlayer();
        showWinFrame(Player.getactivePlayer());
    }

    //SETTER UND GETTER
    //Fuer aenderungen an Resourcen <- Bei aenderung des ThemePacks
    static void setBACKGROUNDMUSIC(File file){
        BACKGROUNDMUSIC.setSoundfile(file);
    }
    static void setBUTTONKLICK(File file){
        BUTTONKLICK.setSoundfile(file);
    }
    static void setWINSOUND(File file){
        WINSOUND.setSoundfile(file);
    }
    static void setBUTTONHOVER(File file){
        BUTTONHOVER.setSoundfile(file);
    }
    static void setBLACKSTONE(File file){
        BLACKSTONE = file;
    }
    static void setWHITESTONE(File file){
        WHITESTONE = file;
    }
    static void setGameRand(File file){
        GAME_RAND = file;
    }
    static void setGAMEFIELD(File file){
        GAMEFIELD = file;
    }
    static void setDENY(File file){
        DENY = file;
    }
    static void setMENUE(File file){
        MENUE = file;
    }

    //Option zum beenden des Programms waerend des Spiels
    @FXML
    private void closeprogram(){
        CLOSEPROGRAM();
    }

    //Programm beenden
    static void CLOSEPROGRAM(){
        if (!SETTINGWINDOW_ACTIVE) {
            //Clicksound spielen
            BUTTONKLICK.play();
            //Programm beenden
            writetolog("Program finished!\n");
            System.exit(0);
        }
    }
}
