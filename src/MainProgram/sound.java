package MainProgram;

import javafx.application.Platform;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;

import static MainProgram.Controller.ProgramThemePack;
import static MainProgram.Controller.writeerror;

/**
 *
 * FUNKTION DIESER KLASSE:
 * Diese Klasse ist fuer das Verarbeiten von Sounddateien verantwortlich
 *
 */

class sound {

    private File soundfile;
    private Clip clip;
    private boolean started = false;
    private static File tempfile;
    private String Type;

    //Constuctor <- Neue Sounddatei kreieren
	sound(File Soundfile,String type){
        this.soundfile = Soundfile;
        this.Type = type;
    }

//FUNKTIONEN

    //Sounddatei abspielen
    void play() {
        tempfile = this.soundfile;

        //Wenn Abspielen des Typs erlaubt durch Einstellungen
        if ((this.Type.equals("music") && Controller.programSettings.getProperty("music").equals("true")) || (this.Type.equals("sound") && Controller.programSettings.getProperty("sounds").equals("true"))) {

            Platform.runLater(() -> {
                try {
                    //Pfad ggf. anpassen
                    if (!tempfile.exists()) if (new File(ProgramThemePack.getThemeroot() +  "/" + tempfile.getPath()).exists())
                        tempfile = new File(ProgramThemePack.getThemeroot() +  "/" + tempfile.getPath());

                    //Sound laden
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tempfile);  //Sound als Stream oeffnen
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);    //Stream Buffer erstellen
                    AudioFormat af = audioInputStream.getFormat();  //AudioFormat laden
                    int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());   //Sounddatenlaenge bestimmen
                    byte[] audio = new byte[size];  //Variable zum speichern der Sounddatei erstellen
                    DataLine.Info info = new DataLine.Info(Clip.class, af, size);   //Soundinformationen zwischenspeichern
                    bufferedInputStream.read(audio, 0, size);   //Sounddatei gebuffert einlesen
                    clip = (Clip) AudioSystem.getLine(info);    //Eingelesene Sounddatei als "Clip" speichern
                    clip.open(af, audio, 0, size);  //Clip oeffnen mit gegebenen Informationen

                    //Lautstaerke anpassen bei bestimmten Dateien
                    if (tempfile == Controller.BACKGROUNDMUSIC.getSoundfile()) {
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-20.0f);
                    } else if (tempfile == Controller.BUTTONHOVER.getSoundfile()) {
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-5.0f);
                    }
                    //Sounddatei abspielen
                    started = true;
                    if (this.Type.equals("music"))clip.loop(-1);    //Wenn Hintergrundmusic -> Unendlich Loopen
                    clip.start();
                    Controller.Sout("DEBUG -> INFO: Start playing " + tempfile.getName());
                } catch (Exception e) {
                    //Aus Error Reagieren
                    writeerror(e);
                    Controller.Sout("Warning: Soundfile " + tempfile.getPath() + " cant be played!");
                }
            });
        }
    }

    //Abspielen Stoppen
    @SuppressWarnings("unused")
    void stop(){
        //Wenn Datei abgespielt wird, stoppen
        if (this.started) {
            Controller.Sout("DEBUG -> INFO: Stoped playing " + this.soundfile.getName());
            this.clip.stop();
            this.started = false;
        }
    }

//GETTER UND SETTER (Selbsterklaerend)

    //SoundDatei zurueckgeben
    private File getSoundfile() {
        return this.soundfile;
    }

    //Zurueckgeben ob Sounddatei nach abgespielt wird
    @SuppressWarnings("unused")
    boolean isPlaying(){
        if (this.started) return this.clip.isActive(); else return false;
    }

    //Sounddatei setzen bzw. aendern
    @SuppressWarnings("unused")
    void setSoundfile(File Soundfile) {
        this.soundfile = Soundfile;
    }
}