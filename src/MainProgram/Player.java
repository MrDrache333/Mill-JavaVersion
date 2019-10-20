package MainProgram;

import java.awt.*;
import java.util.Date;

/**
 * Created by keno on 08.11.16.
 * <p>
 * FUNKTION DIESER KLASSE:
 * Sie ist verantwortlich fuer das uebersichtliche anlegen eines Spielerprofil's
 */
class Player {

    private static int anzPlayer = 0;   //Anzahl Spieler

    private boolean Active; //Speichert ob aktueller Spieler am Zug ist
    private int Points; //Speicher fuer aktuelle Punktzahl
    private stone[] stones; //Speicher fuer Spielsteine des Spielers
    private Color color;    //Speicher fuer Farbe des Spielers
    private int DeathStones = 0;    //Speicher fuer Zerstoerte Spielsteine
    private int ChoosenStone;   //Aktuell ausgewaehlter Stein
    private boolean canTakeStone;   //Speicher ob Aktueller Spieler einen Stein "Zerstoeren bzw. klauen" darf
    private String Name;    //Spielername
    private long turntime;  //Zeit in Ms wie lange Spieler am Zug
    private long totaltime; //Gesamt Spielzeit des Spielers
    private long turnstarttime; //Zeit in ms, wann der Spielzug begonnen hat
    private boolean hasOnlyMills = false;


    /**
     * Instantiates a new Player.
     *
     * @param color the color
     */
//Constructor <- Erstellt neuen Spieler
    Player(Color color){
        this.color = color;
        this.Active = false;            //If Player is still Active
        this.Points = 0;                //Set Startpoints
        this.stones = new stone[10];    //Generate Playerstones
        for (int i = 0; i < this.stones.length;i++)this.stones[i] = new stone(this.color);  //Neue Spielsteine erstellen
        this.ChoosenStone = -1;
        this.canTakeStone = false;
        this.Name = "Spieler " + anzPlayer; //Standard Spielernahmen generieren
        anzPlayer++;
        this.totaltime = 0;

    }

//FUNKTIONEN

    /**
     * Gets player.
     *
     * @return the player
     */
//Gibt Index des Aktuellen Spielers zurueck
    static int getactivePlayer() {
        try {

            for (int i = 0; i < Controller.Spieler.length; i++) {
                if (Controller.Spieler[i].isActive()) return i;
            }

        }catch(Exception e){
            return -1;
        }
        return -1;
    }

    /**
     * Next player.
     */
//Diese Funktion bewirkt einen Spielerwechsel
    static void nextPlayer(){
        //Der Code ist so gut, dass dieser sogar einen Fehler von getActivePlayer beruecksichtigt XD Man siehts nur nicht auf dem ersten Blick
        int ActivePlayer = getactivePlayer();
        if (ActivePlayer + 1 < Controller.Spieler.length){
            Controller.Spieler[ActivePlayer + 1].setActive(true);
            Controller.Spieler[ActivePlayer].setActive(false);
        }else{
            Controller.Spieler[ActivePlayer].setActive(false);
            Controller.Spieler[0].setActive(true);
        }
        Controller.turnsWithoutMill++;
    }

    /**
     * Getnext player int.
     *
     * @return the int
     */
//Gibt Index des naechsten Spielers zurueck
    static int getnextPlayer(){
        int ActivePlayer = getactivePlayer();
        if (ActivePlayer + 1 < Controller.Spieler.length){
            return (ActivePlayer + 1);
        }else{
            return 0;
        }
    }

    /**
     * Add death stones.
     */
//Den "Zerstoerten" Steinen 1 Hinzufuegen
    void addDeathStones(){
        this.DeathStones++;
    }

    /**
     * Reset.
     */
//Statische Variablen zuruecksetzen
    static void reset(){
        anzPlayer = 0;
    }

    /**
     * Is moveable boolean.
     *
     * @return the boolean
     */
//Kann Spieler gueltige Zuege machen
    boolean isMoveable(){

        if (this.DeathStones == 7)return true;  //Wenn Spieler springen kann ist immer eine Position frei
        for (stone Stone: this.stones){ //Fuer jeden Stein
            if (Stone.getPosition() >= 0){  //Wenn sich dieser auf dem SPielfeld befindet
                int Reachable[] = position.getReachablePointsArrayforPoint(Stone.getPosition());    //Erreichbare Positionen des Steins holen
                for (int index: Reachable){
                    if (position.isPositionFree(index))return true; //Wenn erreichbare Position frei ist kann der Spieler "laufen"
                }
            }
        }
        return false;
    }

    /**
     * Hasonlymills boolean.
     *
     * @return the boolean
     */
//Funktion zum pruefen ob der Spieler nur Steine in Mühlen hat
    boolean hasonlymills(){
        for (stone Stone:this.stones){
            if (Stone.getPosition() != -1){
                if (Stone.getPosition() != -2){
                    if (!position.isWinCombination(getactivePlayer(),Stone.getPosition())){
                        hasOnlyMills = false;
                        return false;
                    }
                }
            }else{
                hasOnlyMills = false;
                return false;
            }
        }
        hasOnlyMills = true;
        return true;
    }


//GETTER UND SETTER (Selbsterklaerend)

    private boolean isActive() {
        return this.Active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    void setActive(boolean active) {
        if (this.Active && !active){    //Wenn Zug vorbei
            turntime = new Date().getTime() - turnstarttime;
            totaltime += turntime;
        }else
            if (!this.Active && active) { //Wenn Zug begonnen
                turnstarttime = new Date().getTime();
            }
        this.Active = active;
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    int getPoints() {
        return this.Points;
    }

    /**
     * Sets points.
     *
     * @param points the points
     */
    void setPoints(int points) {
        this.Points = points;
    }

    /**
     * Get stones stone [ ].
     *
     * @return the stone [ ]
     */
    stone[] getStones() {
        return this.stones;
    }

    /**
     * Get stone stone.
     *
     * @param INDEX the index
     * @return the stone
     */
    stone getStone(int INDEX){
        return this.stones[INDEX];
    }

    /**
     * Get death stones int.
     *
     * @return the int
     */
    @SuppressWarnings("unused")
    public int getDeathStones(){
        return this.DeathStones;
    }

    /**
     * Gets choosen stone.
     *
     * @return the choosen stone
     */
    int getChoosenStone() {
        return ChoosenStone;
    }

    /**
     * Sets choosen stone.
     *
     * @param choosenStone the choosen stone
     */
    void setChoosenStone(int choosenStone) {
        ChoosenStone = choosenStone;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    @SuppressWarnings("unused")
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Can take stone boolean.
     *
     * @return the boolean
     */
    boolean CanTakeStone() {
        return canTakeStone;
    }

    /**
     * Sets can take stone.
     *
     * @param canTakeStone the can take stone
     */
    void setCanTakeStone(boolean canTakeStone) {
        this.canTakeStone = canTakeStone;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName() {
        return Name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    void setName(String name) {
        Name = name;
    }

    /**
     * Gets turntime.
     *
     * @return the turntime
     */
    public long getTurntime() {
        return turntime;
    }

    /**
     * Gets totaltime.
     *
     * @return the totaltime
     */
    public long getTotaltime() {
        return totaltime;
    }

    /**
     * Sets anz player.
     *
     * @param anzPlayer the anz player
     */
    public static void setAnzPlayer(int anzPlayer) {
        Player.anzPlayer = anzPlayer;
    }

    /**
     * Sets turntime.
     *
     * @param turntime the turntime
     */
    public void setTurntime(long turntime) {
        this.turntime = turntime;
    }

    /**
     * Sets totaltime.
     *
     * @param totaltime the totaltime
     */
    public void setTotaltime(long totaltime) {
        this.totaltime = totaltime;
    }

    /**
     * Gets turnstarttime.
     *
     * @return the turnstarttime
     */
    public long getTurnstarttime() {
        return turnstarttime;
    }

    /**
     * Get has only mills boolean.
     *
     * @return the boolean
     */
    boolean getHasOnlyMills(){
        return hasOnlyMills;
    }
}
