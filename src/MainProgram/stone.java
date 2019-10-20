package MainProgram;

import java.awt.*;
import java.io.File;

/**
 * Created by keno on 08.11.16.
 *
 * Eigenschaften und Funktionen eines Spielsteins
 *
 * FUNKTION DIESER KLASSE:
 *
 */
class stone {

    private static final int W_startX = 17,W_startY = 700;    //Startkoordinaten der Spielsteine
    private static final int B_startX = 1114,B_startY = 700;    //Startkoordinaten der Spielsteine
    private static final int W_DeathX = B_startX - 90, W_DeathY = 700;  //"TODES" Koordinaten
    private static final int B_DeathX = W_startX + 90, B_DeathY = 700;  //"TODES" Koordinaten
    private static final int adderY = -40;   //Verschiebung des Startpunktes der Spielsteine
    private static final File BLACKIMAGE = new File("images/black.png"), WHITEIMAGE = new File("images/white.png");//Pfade der zu verwenden Bilder
    static int IMAGESIZE = 70;    //Groesse des Bildes in Pixel default:70
    private int INDEX;  //Aktueller Index des Steins

    private static int GLOBALINDEX = 0; //Globaler Stand aller erstellten Steine (auch ges. anz. Steine)

    private Color color;    //Steinfarbe
    private coordinate Coordinate;    //Aktuelle Position des Steins
    private coordinate finalCoordinate;   //Position die erreicht werden soll ("Anweisung fuer steinbewegung")
    private File image; //Bilddatei
    @SuppressWarnings("unused")
    private int Position;   //Aktuelle Position des Steins

    //Constuctor <- Neuen Stein generieren
    stone(Color color){
        this.color = color; //Fabe uebernehmen

        //Indizes festlegen
        this.INDEX = GLOBALINDEX;
        GLOBALINDEX++;

        //Coordinate berechnen <- In abhaenigkeit von: StartKoordinate, Addierer fuer Abstand, Farbe fuer Spielfeldseite und Bildgroesse
        if (color == Color.white){
            this.Coordinate = new coordinate(W_startX + (IMAGESIZE / 2), (adderY * 10 - this.INDEX * adderY) + W_startY + IMAGESIZE);
            this.finalCoordinate = this.Coordinate;
        }else
            if (color == Color.black) {
                this.Coordinate = new coordinate(B_startX + (IMAGESIZE / 2), (adderY * 10 - (this.INDEX - 10) * adderY) + B_startY + IMAGESIZE);
                this.finalCoordinate = this.Coordinate;
            }
        //Bilddatei Festlagen
        if (color == Color.WHITE) image = WHITEIMAGE;
            else if (color == Color.BLACK) image = BLACKIMAGE;
        this.Position = -1;
    }

    //FUNKTIONEN

    //Funktion um den naechsten freien Stein des jewailigen Spielers zu bestimmen
    static int getNextUnusedStone(int Player){

        for (int i = 0; i < Controller.Spieler[Player].getStones().length; i++){   //Jeden Stein des Spielers durchlaufen
            //Auslesen der Position <- Bei Fehler beim auslesen (also -> Stein ohne Position) wird -1 zurueckgegeben
            int pos = Controller.Spieler[Player].getStone(i).getPosition();
            if (pos == -1)return i; //Wenn position unbenutzt
        }
        return -1;  //Fehler zurueckgeben
    }

    //Statische Variablen zuruecksetzen
    static void reset(){
        GLOBALINDEX = 0;
    }

    //Stein "Zerstoren"
    void destroystone(){ //Entfernt Spielstein aus dem Spiel
        position.disableStonePosition(this.INDEX);  //Stein als unglueltig makieren
        this.Position = -2; //Stein Intern als ungueltig erklaeren
        //Neue Koordinate festlegen wo der Stein bis Spielende "ruhen" Darf
        //FinalCoordinate berechnen <- In abhaenigkeit von: StartKoordinate, Addierer fuer Abstand, Farbe fuer Spielfeldseite und Bildgroesse
        if (this.color == Color.white){
            this.finalCoordinate = new coordinate(W_DeathX + (IMAGESIZE / 2), (adderY * 10 - this.INDEX * adderY) + W_DeathY + IMAGESIZE);
        }else
        if (this.color == Color.black) {

            this.finalCoordinate = new coordinate(B_DeathX + (IMAGESIZE / 2), (adderY * 10 - (this.INDEX - 10) * adderY) + B_DeathY + IMAGESIZE);
        }

    }

    //GETTER UND SETTER

    //Fabe des Steins wiedergeben
    @SuppressWarnings("unused")
    public Color getColor() {
        return this.color;
    }

    //Farbe des Steins setzen
    @SuppressWarnings("unused")
    public void setColor(Color color) {
        this.color = color;
    }

    //Aktuelle Coordinaten des Steins zurueckgeben
    coordinate getCoordinate() {
        return this.Coordinate;
    }

    //Spielfeldposition des Steins setzen
    void setPosition(int POSITION){
        position.setStonePosition(this.INDEX,POSITION);
        this.Position = POSITION;
    }

    //Spielfeldposition des Steins abrufen
    int getPosition(){
        return position.getPoint(this.INDEX);
    }

    //Spielsteincoordinaten setzen
    void setCoordinate(coordinate COORD) {
        this.Coordinate = COORD;

    }

    //Finale Spielsteincoordinate abrufen
    coordinate getFinalCoordinate() {
        return this.finalCoordinate;
    }

    //Finale Spielsteinkoordinate setzen (um eine Bewegund zum angegebenen Punkt zu ermöglichen)
    void setFinalCoordinate(coordinate finalPosition) {
        this.finalCoordinate = finalPosition;
    }

    @SuppressWarnings("unused")
    public File getImage(){
        return this.image;
    }

    static void setIMAGESIZE(int imagesize){
        IMAGESIZE = imagesize;
    }

}
