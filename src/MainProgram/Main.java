package MainProgram;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;

import static MainProgram.Controller.writeerror;

/**
 * The type Main.
 */
public class Main extends Application {
    /*
    ---FAQ---
    >ALLGEMEIN<

    1. Warum JavaFX und nicht Swing?
    ->  Die umsetzung Grafisch moderner Oberflaechen ist, meiner Meinung nach, mit JavaFX und dem quasi zugehoerigen Programm
        Scene Builder 2.0 verhaeltnissmaessig einfach und bietet ohne viel Code eine Moderne und schnelle Loesung.

    2. Woher kommen die Medien-Dateien?
    ->  Diese wurden zum groessten Teil aus dem Internet geladen und nachtraeglich bearbeitet oder Direkt komplett von Hand
        erstellt -> Dennis Piontek

    3. Wie wurden die .fxml Dateien erstellt und wie kann man diese zum Betrachten oder Bearbeiten oeffnen?
    ->  Die Dateien wurden mit einem externen Editor erstellt. Dieser kann kostenlos aus dem Internet geladen werden und ist
        sowohl fuer macOS als auch fuer Windows verfuegbar.
            Product Version
        JavaFX Scene Builder 2.0
        Version: 2.0-b20, Changeset: 5cac093e5c1f
        Datum: 2014-03-21 10:03

    4. Welcher Editor wurde fuer das Programmieren benutzt?
    ->  Es wurde IntelliJ IDEA 2016.2.5 benutzt. (Eine Kostenlose Community Edition ist auf der Webseite des Herstellers vorhanden)
            Build #IC-162.2228.15, built on October 14, 2016
            JRE: 1.8.0_112-release-287-b2 x86_64
            JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o

    5. Wurden fuer das Programm externe Biblitheken verwendet?
    ->  Es wurden zwei Bibliotheken zur Versendung von E-Mails ueber den E-Mail Anbieter Google-Mail verwendet. Diese befinden sich
        in dem Ordner "library".

    >Programmspezifisch<

    1. Warum werden die Spieler oder auch die Steine gelegendlich ziemlich kompliziert auseinandergehalten
    ->  Dies ist zum jetzigen Zeitpunkt dieses Projektes definitiv nicht notwendig, bekommt aber, sollte dieses Projekt
        noch groesser und komplizierter werden, eine Bedeutende Rolle. Hinzu kommt, dass die Klassen beliebig, ohne groesseren
        Aufwand in andere Projekte integriert werden koennen z.b Player, sound, position und coordinate.

    2. Warum wird der Timer nicht noch schneller aufgerufen?
    ->  Wuerde der Timer noch schneller aufgerufen werden, kaeme es gelegendlich zu einem Overflow in der Aktualisierung
        bestimmter Komponenten in der Stage, sodass diese inhalte nicht mehr Aktualisiert werden. Die Elemente wirken
        eingefroren. Dies kann zu diesem Zeitpunkt auch noch vorkommen, allerdings ist das Risiko minimierter.
        Evtl. wird es noch einen "Spiel speichern"-Button geben, welcher es ermoeglicht, bei besagter Situation das
        Programm einfach neu zu starten und den letzten Spielstand abzurufen

    3. Was passiert wenn Medien aus anderen Themepacks geloescht werden?
    ->  Das Programm ist so Programmiert das es selbststaendig fehlende oder fehlerhafte Medien ueberspringt und diese aus dem Standardpack laed.
        Gleichzeitig liegt in dem gepacktem .jar Archiv eine Kopie des Default-Packs bereit, sodass alle externen aenderungen an der Dateistruktur
        des Programms irrelevant fuer die Funktion des Programms sind. Durch das Laden des Themepacks, bei Spielstart oder anfaenglichem Themepack wechsels,
        in den Arbeitsspeicher stehen die Medien fuer das gesamte Spiel zur Verfuegung auch dann, wenn im Spielverlauf Medien beschaedigt werden.

    >Dateistruktur<

    Allgemein:
        - "config.properties" Speichertort fuer Programmspezifische Einstellungen
        - "mill.log" Als Logdatei fuer Allgemeine Informationen oder Errorberichte <- Nur das Noetigste
        - "mill_detail.log" Als Detailierte Logdatei fuer alle Informationen vom Mausklick bis zum bewegten Stein und allen aenderungen und Fehler

    Themepacks:
        - "Themepackname" als Speicherort fuer alles was zum jewailigen Themepack gehoert
            - "images" als Speicherort fuer alle Verwendeten bzw. Standardbilder ersetzenden Grafiken
            - "sounds" als Speicherort fuer alle Verwendeten bzw. Standardsound ersetzende Soundeffekte und Musikstuecke
            - "Themepackname.properties" als Speicherort fuer alle Spielregeln des verwendeten Packs

    >Inhalt von .properties Dateien<

    "config.properties":
        - "sounds" -> Sind die Programmsounds bzw. Effektsounds aktiviert
        - "music" -> Ist die Hintergrundmusik im Spiel aktiviert
        - "autosave" -> Ist das Automatische Speichern des Spielstandes waehrend des Spiels aktiviert
        - "autoload" -> Ist das Automatische laden eines Spiels, nach vorherigem beenden eines noch nicht abgeschlossenem Spiels, aktiviert
        - "theme" -> Das zuletzt genutze Themepack

    "Themepackname.properties":
        - "wcombs" -> "Win Combinations" -> Signalisiert dem Programm, wieviele Kombinationen von Spielsteinen es gibt um eine Muehle zu bilden
        - "maxwcombs" -> "Max. Win Combinations" -> Signalisiert dem Programm, wieviele Spielsteinpositionen eine Kombination enthaelt
        - "rpoints" -> "Reachable Points" -> Signalisiert dem Programm, wieviele Erreichbare Punkte es fuer einen Spielstein an beliebiger Position gibt und welche es sind
        - "dstj" -> "Death Stones to Jump" -> Signalisiert dem Programm, wieviele Steine eines Spielers "geklaut" sein muessen, damit der jewailige Spieler Springen darf
        - "maxrpoints" -> "Max. Reachable Points" -> Signalisiert dem Programm, wieviele Positionen von einer anderen Position aus erreichbar sind bzw. wieviele es erstellen soll
        - "rp%i%i" -> "Reachable Point von Position + Index der Positionen" -> Enthaelt die Information, welcher Punkt einen anderen Erreichen kann
            - "1. %i" -> Ausgangs Position
            - "1. %i" -> Index, wenn mehrere Vorhanden (Keine Funktion)
            Beispiel: rp1003 = 11 -> Position 10 kann als 3. moeglichkeit an Position 11 gelangen
        - "wc%i%i" -> "Win Combination + Index der Position" -> Enthaelt die Information, welche Positionen durch den Stein eines Spielers belegt sein uessen, damit eine Muehle existiert
            - "1. %i" -> Nummer der Kombination <- Sie dient nur dazu um die Kombinationen auseinander zu halten (Keine Funktion)
            - "1. %i" -> Index, wenn mehrere Vorhanden (Keine Funktion)
            Beispiel: wc1602 = 5 -> Damit die 16. Kombination eine Muehle ergibt, muss einer der Steine (hier der 2.) an Position 5 sein.
        - "%ix" oder "%iy" -> "Coordinate auf X- oder Y-Achse"
            - "%i" -> Position auf dem Spielbrett
            - "x" oder "y" -> Signalisiert dem Programm ob sich die Koordinate auf den Punkt auf der X- oder Y-Achse bezieht
            Beispiel: 15x = 300 -> Die X-Koordinate von Position 15 betraegt 300
     */
    //Funktion zum erstllen des Fensters
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            //FensterLayout aus XML Format laden

            Parent root = FXMLLoader.load(getClass().getResource("mainFrame.fxml"));
            primaryStage.setTitle("Mühle - Das Spiel"); //Titel festlegen
            Scene scene = new Scene(root, 1200, 800);   //Groesse festlegen und Layout uebergeben
            primaryStage.setResizable(false);   //Groessenaenderungen nicht erlauben
            primaryStage.setOnCloseRequest(windowEvent);    //Wenn fenster geschlossen wird


            //Einstellungen uebernehmen und Fenster zeigen
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            //Entstandene Fehler abfangen und in LogDatei schreiben
            writeerror(e);
            JOptionPane.showMessageDialog(null,"!CRITICAL ERROR OCCURRED!\nPlease reinstall the Game!","!CRITICAL ERROR OCCURRED!",JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    //EventHandler <- Wird in diesem Falle aufgerufen, wenn Fenster geschlossen wird
    private EventHandler<WindowEvent> windowEvent = event -> Controller.CLOSEPROGRAM();
}
