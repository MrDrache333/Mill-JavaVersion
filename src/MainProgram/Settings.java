package MainProgram;

import java.io.*;
import java.util.Properties;

import static MainProgram.Controller.programSettings;

/**
 * Project: Mill
 * Package: sample
 * Created by keno on 14.11.16.
 */
class Settings {

    private Properties props;   //Eigenschaften
    private File Savefile;  //Datei zum speichern und laden

    /**
     * Instantiates a new Settings.
     *
     * @param Saveto the saveto
     */
//Constructor
    Settings(File Saveto){
        this.props = new Properties();
        this.Savefile = Saveto;
    }

    /**
     * Reset properties.
     */
//Einstellungen zuruecksetzen
    void resetProperties(){
        this.props.clear();
    }

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
//Einstellungsparameter zurueckgeben
    String getProperty(String key) {
        return this.props.getProperty(key);
    }

    /**
     * Set property.
     *
     * @param key   the key
     * @param value the value
     */
//Einstellungs Parameter setzen
    void setProperty(String key, String value){
        this.props.setProperty(key,value);
    }

    /**
     * Add property.
     *
     * @param key   the key
     * @param value the value
     */
//Einstellungs Parameter hinzufuegen
    void addProperty(String key, String value) {
        this.props.put(key, value);
    }

    /**
     * Load properties boolean.
     *
     * @return the boolean
     */
//Versuchen, die Einstellungen zu laden und je nach erfolg boolean zurueckgeben
    Boolean loadProperties(){
        Boolean loaded = false;
        try {
            if (this.Savefile.exists()) {   //Wenn die Datei existiert -> Mithilfe von Properties Einstellungen laden
                FileInputStream fis = new FileInputStream(this.Savefile);
                this.props.load(fis);
                fis.close();
                loaded = true;
            }else
                loaded = false;
        }catch(Exception e){
            loaded = false;
        }
        //Debugging
        if (!loaded) Controller.Sout("DEBUG -> ERROR: Faild to Load Settings in " + this.Savefile.getAbsolutePath());
        return loaded;
    }

    /**
     * Save properties boolean.
     *
     * @return the boolean
     */
//Einstellungen speichern
    boolean saveProperties() {
        try {   //Versuchen, einstellungen zu speichern und dementsprechend boolean zurueckgeben
            FileOutputStream fos = new FileOutputStream(this.Savefile);
            this.props.store(fos, this.Savefile.getName());
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get file file.
     *
     * @return the file
     */
//Einstellungen speicherDatei zurueckgeben
    File getFile(){
        return this.Savefile;
    }

//STATIC FUNKTIONS

    /**
     * Checkdefaults boolean.
     *
     * @return the boolean
     */
//Programeinstellungen auf Fehler ueberpruefen
    static boolean checkdefaults(){

        if (!programSettings.loadProperties()){
            programSettings.resetProperties();
            programSettings.addProperty("music", "true");
            programSettings.addProperty("sounds", "true");
            programSettings.addProperty("autosave", "true");
            programSettings.addProperty("autoload", "true");
            programSettings.addProperty("autoerror", "true");
            programSettings.addProperty("theme","default");
            programSettings.saveProperties();
        }else
            return true;

        return false;
    }

    /**
     * Tostring string.
     *
     * @return the string
     */
//Debugfunktion
    String tostring(){
        String string = "";
        try {
            FileReader fr = new FileReader(this.Savefile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                string+= line + "\n";
            }
        }catch (Exception e){
            string = "null";
        }
        return string;
    }
}
