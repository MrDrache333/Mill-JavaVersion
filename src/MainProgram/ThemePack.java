package MainProgram;

import java.io.File;
import java.util.ArrayList;

import static MainProgram.Controller.Sout;
import static MainProgram.Controller.writeerror;

/**
 * Project: Mill
 * Package: sample
 * Created by keno on 15.11.16.
 */
class ThemePack {

    //Standardresourcen
    private final static File BACKGROUNDMUSIC = new File("sounds/backgroundmusic.wav");
    private final static File BUTTONHOVER = new File("sounds/buttonhover.wav");
    private final static File BUTTONCLICK = new File("sounds/buttonsound.wav");
    private final static File WINSOUND = new File("sounds/winsound.wav");

    private final static File BLACKSTONE = new File("images/black.png");
    private final static File WHITESTONE = new File("images/white.png");
    private final static File GAME_RAND = new File("images/game_rand.png");
    private final static File GAMEFIELD = new File("images/gamefield.png");
    private final static File MENUE = new File("images/menu.png");
    private final static File DENY = new File("images/deny.png");

    private String Name;
    private File Path;
    private File Themeroot;

    /**
     * Instantiates a new Theme pack.
     *
     * @param name the name
     */
//Constructor
    ThemePack(String name){
        this.Name = name;
        this.Themeroot = new File("Themes/" + name + "/");  //Rootverzeichnis des Themepacks bestimmen
        if (!this.Themeroot.exists())//noinspection ResultOfMethodCallIgnored
            this.Themeroot.mkdirs();
        this.Path = new File("Themes/" + name + "/" + name + ".properties");    //Speicherdatei festlegen
    }

    /**
     * Gets name.
     *
     * @return the name
     */
//Themepack Name zurueckgeben
    String getName() {
        return Name;
    }

    /**
     * Gets themeroot.
     *
     * @return the themeroot
     */
//Themeroot Verzeichnis zurueckgeben
    File getThemeroot() {
        return Themeroot;
    }

    /**
     * Load theme boolean.
     *
     * @return the boolean
     */
//Themepack laden
    boolean loadTheme(){
        Sout("Trying to Load Themepack: " + this.Name);
        try{
            Settings Theme = new Settings(this.Path);
            Theme.loadProperties();

            //Wichtige anhaltspunkte aus Themepack laden
            int anzcoords = Integer.parseInt(Theme.getProperty("coords"));
            int anzwcombs = Integer.parseInt(Theme.getProperty("wcombs"));
            int anzrpoints = Integer.parseInt(Theme.getProperty("rpoints"));
            int maxrpoints = Integer.parseInt(Theme.getProperty("maxrpoints"));
            int maxwcombs = Integer.parseInt(Theme.getProperty("maxwcombs"));
            int randader = Integer.parseInt(Theme.getProperty("randadder"));
            Controller.DeathStonestoJump = Integer.parseInt(Theme.getProperty("dstj"));
            int imagesize;
            try {
                imagesize = Integer.parseInt(Theme.getProperty("imagesize"));
            }catch (Exception e){
                imagesize = 70;
            }
            stone.setIMAGESIZE(imagesize);

            coordinate COORDS[] = new coordinate[anzcoords];
            int ReachablePoints[][] = new int[anzrpoints][maxrpoints];
            int WinCombinations[][] = new int[anzwcombs][maxwcombs];

            //X und Y-Koordinaten der Punkte laden
            for (int i = 0; i < anzcoords; i++){
                COORDS[i] = new coordinate(Double.parseDouble(Theme.getProperty(i + "x") + randader),Double.parseDouble(Theme.getProperty(i + "y")));
            }

            //Punkte, die von bestimmten anderen Punkten erreicht werden koennen, laden
            for (int i = 0; i < anzrpoints; i++){
                String Property;
                for (int z = 0; z < maxrpoints; z++) {
                    if ((Property = Theme.getProperty("rp" + i + z)) != null) {
                        ReachablePoints[i][z] = Integer.parseInt(Property);
                    }else{
                        ReachablePoints[i][z] = -1;
                    }
                }
            }

            //WinCombinations laden
            for (int i = 0; i < anzwcombs; i++){
                String Property;
                int z = 0;
                while ((Property = Theme.getProperty("wc" + i + z)) != null){
                    WinCombinations[i][z] = Integer.parseInt(Property);
                    z++;
                }
            }

            //WENN ALLES BIS HIER FUNKTIONIERT HAT IST DAS THEMEPACK FUERS ERSTE GUELTIG UND DIE DATEN KOENNEN UEBERNOMMEN UND GGF. UEBERPRUEFT WERDEN

            //Koordinaten und Kunkte uebernehmen
            position.setPoints(COORDS);
            position.setREACHABLEPOINTS(ReachablePoints);
            position.setWinCombination(WinCombinations);

            String PATH = "";

            //Evtl. nicht vorhandene Resourcen durch Standardresourcen ersetzen um die vollstaendigkeit zu gewaehrleisten und um Fehler zu verhindern
            if (new File(PATH + "Themes/" + this.Name + "/sounds/" + BACKGROUNDMUSIC.getName()).exists())Controller.setBACKGROUNDMUSIC(new File(PATH + "Themes/" + this.Name + "/sounds/" + BACKGROUNDMUSIC.getName())); else
                Controller.setBACKGROUNDMUSIC(new File(PATH + "Themes/default/sounds/" + BACKGROUNDMUSIC.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/sounds/" + BUTTONCLICK.getName()).exists())Controller.setBUTTONKLICK(new File(PATH + "Themes/" + this.Name + "/sounds/" + BUTTONCLICK.getName())); else
                Controller.setBUTTONKLICK(new File(PATH + "Themes/default/sounds/" + BUTTONCLICK.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/sounds/" + BUTTONHOVER.getName()).exists())Controller.setBUTTONHOVER(new File(PATH + "Themes/" + this.Name + "/sounds/" + BUTTONHOVER.getName())); else
                Controller.setBUTTONHOVER(new File(PATH + "Themes/default/sounds/" + BUTTONHOVER.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/sounds/" + WINSOUND.getName()).exists())Controller.setWINSOUND(new File(PATH + "Themes/" + this.Name + "/sounds/" + WINSOUND.getName())); else
                Controller.setWINSOUND(new File(PATH + "Themes/default/sounds/" + WINSOUND.getName()));


            if (new File(PATH + "Themes/" + this.Name + "/images/" + BLACKSTONE.getName()).exists())Controller.setBLACKSTONE(new File(PATH + "Themes/" + this.Name + "/images/" + BLACKSTONE.getName())); else
                Controller.setBLACKSTONE(new File(PATH + "Themes/default/images/" + BLACKSTONE.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/images/" + WHITESTONE.getName()).exists())Controller.setWHITESTONE(new File(PATH + "Themes/" + this.Name + "/images/" + WHITESTONE.getName())); else
                Controller.setWHITESTONE(new File(PATH + "Themes/default/images/" + WHITESTONE.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/images/" + GAME_RAND.getName()).exists())Controller.setGameRand(new File(PATH + "Themes/" + this.Name + "/images/" + GAME_RAND.getName())); else
                Controller.setGameRand(new File(PATH + "Themes/default/images/" + GAME_RAND.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/images/" + GAMEFIELD.getName()).exists())Controller.setGAMEFIELD(new File(PATH + "Themes/" + this.Name + "/images/" + GAMEFIELD.getName())); else
                Controller.setGAMEFIELD(new File(PATH + "Themes/default/images/" + GAMEFIELD.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/images/" + MENUE.getName()).exists())Controller.setMENUE(new File(PATH + "Themes/" + this.Name + "/images/" + MENUE.getName())); else
                Controller.setMENUE(new File(PATH + "Themes/default/images/" + MENUE.getName()));

            if (new File(PATH + "Themes/" + this.Name + "/images/" + DENY.getName()).exists())Controller.setDENY(new File(PATH + "Themes/" + this.Name + "/images/" + DENY.getName())); else
                Controller.setDENY(new File(PATH + "Themes/default/images/" + DENY.getName()));

            if (Controller.BACKGROUNDMUSIC.isPlaying()){
                Controller.BACKGROUNDMUSIC.stop();
                Controller.BACKGROUNDMUSIC.play();
            }


        }catch(Exception e){
            Sout("Faild to Load Themepack: " + this.Name);
            writeerror(e);
            return false;
        }
        Sout("Loaded Themepack: " + this.Name);
        return true;
    }

    /**
     * Get themes array list.
     *
     * @return the array list
     */
//Vorhandene Themepacks als Stringarray zurueckgeben
    static ArrayList<String> getThemes(){

        //Im Themeroot nach Ordnern suchen dessen Inhalt den selben Namen als EinstellungsDatei hat
        ArrayList<String> Themes = new ArrayList<>();

        File[] fileArr = new File("Themes/").listFiles();   //Alle Dateien im Themeroot auflisten
        for (File f : fileArr) {
            if (f.isDirectory()){   //Prufen ob Ordner
                if (new File(f.getPath() + "/" + f.getName() + ".properties").exists()){    //Prufen ob Einstellungen vorhanden
                    Themes.add(f.getName());    //Theme in Array speichern
                }
            }
        }
        return Themes;
    }

    /**
     * Makedefaultpack.
     */
//Erstellt das Standard Theme Pack als "BackUp"
    static void makedefaultpack(){

        Settings DefaultTheme = new Settings(new File("Themes/default/default.properties"));
        int index = 0;
        coordinate COORD[] = position.getDefaultPoints();
        int maxrpointslength = 0;

        int ReachablePoints[][] = position.getDefaultREACHABLEPOINTS();

        //Standardkoordinaten zu Standardeinstellungen hinzufuegen
        for(int z = 0; z < position.getDefaultPoints().length;z++){    //Solange werte richtig uebergeben werden
            //Koordinaten der Punkte speichern
            DefaultTheme.addProperty(index + "x",COORD[index].getX() + "");
            DefaultTheme.addProperty(index + "y",COORD[index].getY() + "");

            //Welcher Punkt zu welchem kann -> zu Einstellungen  hinzufuegen
            int[] Points = ReachablePoints[index];
            for (int i = 0; i < Points.length; i++){
                if (Points.length > maxrpointslength)maxrpointslength = Points.length;
                DefaultTheme.addProperty("rp" + index + i,Points[i] + "");
            }
            index++;
        }

        int[][] Wins = position.getDefaultWinCombination();
        int winmaxlength = 0;

        //Standardkombinationen in Einstellungen schreiben
        for (int i = 0; i < Wins.length; i++){
            for (int s = 0; s < Wins[i].length; s++){
                if (Wins[i].length > winmaxlength)winmaxlength = Wins[i].length;
                DefaultTheme.addProperty("wc" + i + s,Wins[i][s] + "");

            }
        }

        //Extra Parameter zum leichteren Laden der Packs hinzufuegen und Einstellungen speichern
        DefaultTheme.addProperty("maxrpoints",maxrpointslength + "");
        DefaultTheme.addProperty("maxwcombs",winmaxlength + "");

        DefaultTheme.addProperty("coords",position.getDefaultPoints().length + "");
        DefaultTheme.addProperty("rpoints",position.getDefaultREACHABLEPOINTS().length + "");
        DefaultTheme.addProperty("wcombs",position.getDefaultWinCombination().length + "");
        DefaultTheme.addProperty("dstj","7");
        DefaultTheme.addProperty("randadder","0");
        DefaultTheme.addProperty("imagesize","70");

        DefaultTheme.saveProperties();

    }

}