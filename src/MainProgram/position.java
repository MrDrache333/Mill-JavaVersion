package MainProgram;

import org.jetbrains.annotations.Contract;

import java.awt.*;

/**
 * Project: Mill
 * Package: ${PACKAGE_NAME}
 * Created by keno on 09.11.16.
 * <p>
 * FUNKTION DIESER KLASSE:
 * Auslagerung von Arrays, welche wichtige Informationen ueber das Spielfeld und die darauf befindenen "Positionen" und Steine, sowie moegliche Zugkombinationen
 * Ausserdem enthaelt Sie wichtige Funktionen, welche fuer das bestimmen von bestimmten Positionen als auch fuer das Auslesen vo Mehrdimensionalen Arrays zustaendig sind
 * <p>
 * <p>
 * Die Positionen auf dem Spielfeld werden ermittelt indem von Oben-Links, Schritt fuer Schritt, nach Unten-Rechts von 0 bis 23 durchnummeriert wird
 */
class position{

    private static final int ACCURACY = 40;    //Genauigkeit eines Punktes auf dem Spielfeld
    private static int hoveringPosition = -1;   //Speichert Position, uber welche zur Zeit "gehovert" wird
    private static int lastCheckedPlayer = -1;

    private static coordinate[] Points;    //Koordinaten der einzelnen Spielfeldpunkte (Index ist die Position)

    private final static coordinate[] defaultPoints = new coordinate[]{    //Koordinaten der einzelnen Spielfeldpunkte (Index ist die Position)
            new coordinate(240,35), //0
            new coordinate(599,35), //1
            new coordinate(965,35), //2
            new coordinate(357,155),//3
            new coordinate(599,155),//4
            new coordinate(843,155),//5
            new coordinate(481,277),//6
            new coordinate(599,277),//7
            new coordinate(724,277),//8
            new coordinate(240,399),//9
            new coordinate(357,399),//10
            new coordinate(481,399),//11
            new coordinate(724,399),//12
            new coordinate(843,399),//13
            new coordinate(965,399),//14
            new coordinate(481,525),//15
            new coordinate(599,525),//16
            new coordinate(724,525),//17
            new coordinate(357,644),//18
            new coordinate(599,644),//19
            new coordinate(843,644),//20
            new coordinate(240,767),//21
            new coordinate(599,767),//22
            new coordinate(965,767) //23
    };

    private static boolean[] PointisFree = {  //Variable, welche Speichert welche Positionen frei sind (Als Index wird die Position angegeben)
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
    };

    private static int REACHABLEPOINTS[][];    //Welcher Punkt kann welchen Punkt erreichen (Index1 ist der StartPunkt
                                                        //der 2.Index wird schleifenartig ueberprueft ob dieser die ZielPosition enthaelt )

    private final static int defaultREACHABLEPOINTS[][] = {    //Welcher Punkt kann welchen Punkt erreichen (Index1 ist der StartPunkt
            //der 2.Index wird schleifenartig ueberprueft ob dieser die ZielPosition enthaelt )
            {1, 1, 9, 21},//0
            {0, 2, 4, 4},//1
            {0, 1, 14, 14},//2
            {4, 5, 10, 10},//3
            {1, 3, 5, 7},//4
            {3, 4, 13, 13},//5
            {7, 8, 11, 11},//6
            {4, 4, 6, 8},//7
            {6, 7, 12, 12},//8
            {0, 10, 10, 21},//9
            {3, 9, 11, 18},//10
            {6, 9, 10, 15},//11
            {8, 13, 13, 17},//12
            {5, 12, 14, 20},//13
            {2, 13, 13, 23},//14
            {11, 11, 16, 16},//15
            {15, 17, 19, 19},//16
            {12, 12, 16, 16},//17
            {10, 10, 19, 19},//18
            {16, 18, 20, 22},//19
            {13, 13, 19, 19},//20
            {9, 9, 22, 22},//21
            {19, 19, 21, 23},//22
            {14, 14, 22, 22} //23
    };
    /*
        Wenn Zwei schritte erlaubt werden, aus Kommentar entfernen!

    private static final int FREEPOINTSTOMOVE[][] = {   //Welcher Punkt frei sein muess um den Spielstein dorthin bewegen zu koennen (wenn direkter weg ohne hindernisse moeglich, dann index des Ziels)
            //Trotzdem, wenn verglichen, muss ZielPosition immer frei sein (hier dient dieser index nur als Platzhalter um Fehler zu vermeiden)
            //Index 1 wieder der Punkt und Index2 wird bestimmt indem von REACHABLEPOINTS der 2te (zuvor ermittelte)Index genutzt wird
            {1, 1, 9, 9},//0
            {0, 2, 4, 4},//1
            {1, 1, 14, 14},//2
            {4, 4, 10, 10},//3
            {1, 3, 5, 7},//4
            {4, 4, 13, 13},//5
            {7, 7, 11, 11},//6
            {4, 4, 6, 8},//7
            {7, 7, 12, 12},//8
            {0, 10, 10, 21},//9
            {3, 9, 11, 18},//10
            {6, 10, 10, 15},//11
            {8, 13, 13, 17},//12
            {5, 12, 14, 20},//13
            {2, 13, 13, 23},//14
            {11, 11, 16, 16},//15
            {15, 17, 19, 19},//16
            {12, 12, 16, 16},//17
            {10, 10, 19, 19},//18
            {16, 18, 20, 20},//19
            {13, 13, 18, 18},//20
            {9, 9, 21, 21},//21
            {19, 19, 21, 23},//22
            {14, 14, 21, 21},//23

    };
    */

    private static int[] StoneonPosition = {    //Steinindex auf Position (Index ist der SteinIndex, Ausgabe ist die aktuelle Position(abgleich mit SteinPosition in der Steinklasse))
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,

    };

    private static int WinCombination[][]; //Kombinationen von gleichfarbigen Steinen, welche eine Muehle ermoeglichen
            //1.Index ist der ArrayIndex der Kombination, um eine gueltige Muehle zu erhalten muessen alle Steine in einer dieser Kombinationen die selbe Farbe haben

    private final static int defaultWinCombination[][] = { //Kombinationen von gleichfarbigen Steinen, welche eine Muehle ermoeglichen
            //1.Index ist der ArrayIndex der Kombination, um eine gueltige Muehle zu erhalten muessen alle Steine in einer dieser Kombinationen die selbe Farbe haben
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {9,10,11},
            {12,13,14},
            {15,16,17},
            {18,19,20},
            {21,22,23},
            {0,9,21},
            {3,10,18},
            {6,11,15},
            {1,4,7},
            {16,19,22},
            {8,12,17},
            {5,13,20},
            {2,14,23},
            {-1,-1,-1}
    };

//FUNKTIONEN

    /**
     * Rem stone position.
     *
     * @param STONEINDEX the stoneindex
     */
//Resetet Daten an angegebener Position (auch bei Spielsteinbewegung aufrufen um alte Position als Frei zu makieren)
    static void remStonePosition(int STONEINDEX){
        PointisFree[StoneonPosition[STONEINDEX]] = true;    //Liesst die Position des Steins aus und setzt die Position auf Frei
        StoneonPosition[STONEINDEX] = -1;   //Setzt Position des Steins auf "nicht vorhanden" -1 stellt hierbei einen Platzhalter da
    }

    /**
     * Disable stone position.
     *
     * @param STONEINDEX the stoneindex
     */
//Resetet Daten an angegebener Position wobei der Stein als nicht mehr verwendbar makiert wird
    static void disableStonePosition(int STONEINDEX){
        PointisFree[StoneonPosition[STONEINDEX]] = true;
        StoneonPosition[STONEINDEX] = -2;
    }

    //sind die Koordinaten von COORD in der Reichweite von dem Punkt, dessen index uebergeben wurde
    private static boolean doesRectPoint(coordinate Point, coordinate COORD){

        if (Point.getX() - ACCURACY < COORD.getX() && Point.getX() + 50 > COORD.getX())    //Wenn X-Koordinate im zulaessigem bereich der X-Achse
            if (Point.getY() - ACCURACY < COORD.getY() && Point.getY() + 50 > COORD.getY())//Wenn Y-Koordinate im zulaessigem bereich der Y-Achse
                return true;
        return false;
    }

    /**
     * Isableto reach point boolean.
     *
     * @param STARTPOINT the startpoint
     * @param STOPPOINT  the stoppoint
     * @return the boolean
     */
//Gibt zurueck ob der Punkt die Zielposition erreichen kann unter beruecksichtigung der freien Positionen und des Weges
    static boolean isabletoReachPoint(int STARTPOINT, int STOPPOINT){

        for (int i = 0; i < REACHABLEPOINTS[STARTPOINT].length; i++) {   //Jeden Wert des Arrays durchlaufen

            if (REACHABLEPOINTS[STARTPOINT][i] == STOPPOINT) {   //Pruefen ob Zielpunkt erreicht werden kann
                //Pruefen weg zum Zielpunkt und der Zielpunkt selbst frei ist
                return isPositionFree(STOPPOINT) /*&& isPositionFree(FREEPOINTSTOMOVE[STARTPOINT][i])*/;
            }
        }
        return false;
    }

    /**
     * Is win combination boolean.
     *
     * @param ActivePlayer the active player
     * @param ActivePoint  the active point
     * @return the boolean
     */
//Ueberpruefen ob Stein des Spielers eine Muehle bildet
    static boolean isWinCombination(int ActivePlayer, int ActivePoint){
        boolean win = true;
        boolean containsActivePoint = false;
        for (int[] MainIndex: WinCombination){  //Fuer jedes Array im Array durchlaufen
            win = true;
            containsActivePoint = false;
            for (int SubIndex:MainIndex){   //Jeden Wert des UnterArrays abrufen
                //Ueberprufen ob Steine des Spielers diesen Punkt enthalten
                if (!isStonescontainPoint(Controller.Spieler[ActivePlayer].getStones(),SubIndex)) win = false;
                if (SubIndex == ActivePoint)containsActivePoint = true; //Prufen ob Wert des UnterArrays mit uebergebnen Punkt uebereinstimmt
            }
            if (win && containsActivePoint)break;   //Schleife verlassen wenn Muehle enthaelt
        }
        return (win && containsActivePoint);
    }

    //Ueberpruefen ob ein Stein im Array auf dem gegebenem Punkt liegt
    private static boolean isStonescontainPoint(stone Stones[], int Point){

        for (stone Stone:Stones)if (Stone.getPosition() == Point)return true;
        return false;
    }

    /**
     * Get player indexfor point int.
     *
     * @param Point the point
     * @return the int
     */
//Gibt SpielerIndex fuer gegebenen Punkt zurueck
    static int getPlayerIndexforPoint(int Point){
        try {
            int Stone = getStoneIndexforPoint(Point);
            if (Stone >= 10) Stone -= 10;
            for (Player Spieler : Controller.Spieler) {
                if (Spieler.getStone(Stone).getPosition() == Point) {
                    if (Spieler.getColor() == Color.white) return 0;
                    else return 1;
                }
            }
        }catch(Exception e){
            return -1;
        }
        return -1;
    }

    /**
     * Reset.
     */
//Setzt Statische Arrays und Werte zurueck
    static void reset(){
        PointisFree = new boolean[Points.length];
        StoneonPosition = new int[Points.length];
        for (int i = 0; i < Points.length; i++){
            PointisFree[i] = true;
            StoneonPosition[i] = -1;
        }
    }

//GETTER UND SETTER

    /**
     * Is position free boolean.
     *
     * @param POSITION the position
     * @return the boolean
     */
//Gibt zurueck ob die Angegebene Position belegt ist oder nicht
    @Contract(pure = true)  //Error unterdruecken
    static boolean isPositionFree(int POSITION){
        boolean free;
        try{
            free = PointisFree[POSITION];
        }catch(Exception e){
            free = false;
        }
        return free;   //Liesst den aktuellen Status der Position aus und gib diese zurueck
    }

    /**
     * Get point int.
     *
     * @param index the index
     * @return the int
     */
//Gibt coordinaten des geforderten Punktes zurueck
    @Contract(pure = true)  //Error unterdruecken
    static int getPoint(int index){
        try {
            return StoneonPosition[index];
        }catch(Exception e){
            return -1;
        }
    }

    /**
     * Get coordinatefor point coordinate.
     *
     * @param Point the point
     * @return the coordinate
     */
//Gibt Coordinaten fuer uebergebenen Punkt zurueck
    @Contract(pure = true)  //Error unterdruecken
    static coordinate getCoordinateforPoint(int Point){
        try {
            return Points[Point];
        }catch(Exception e){
            return new coordinate(-1,-1);
        }
    }

    /**
     * Get reachable points arrayfor point int [ ].
     *
     * @param Point the point
     * @return the int [ ]
     */
//Gibt die erreichbaren Punkte eines Punktes zurueck
    static int[] getReachablePointsArrayforPoint(int Point){
        return REACHABLEPOINTS[Point];
    }

    /**
     * Get win combis int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
//Gibt die noetige Win Kombination
    static int[][] getWinCombis(){
        return WinCombination;
    }

    /**
     * Get stone indexfor point int.
     *
     * @param Point the point
     * @return the int
     */
//Gibt zurueck, welcher Stei auf gegebener Position ist (Wenn leer -> Gibt -1 zurueck)
    @Contract(pure = true)  //Error unterdruecken
    static int getStoneIndexforPoint(int Point){
        for (int i= 0; i <StoneonPosition.length;i++){
            if (StoneonPosition[i] == Point)return i;
        }
        return -1;
    }

    /**
     * Get rected point int.
     *
     * @param COORD the coord
     * @return the int
     */
//Gibt index des Punktes zurueck welcher im bereich der Coordinate liegt, wenn keiner im bereich -> gibt -1 zurueck
    static int getRectedPoint(coordinate COORD){
        for (int i = 0; i < Points.length; i++){   //Jeden Punkt durchlaufen
            if (doesRectPoint(Points[i],COORD))return i;   //Index, wenn gefunden, zurueckgeben
        }
        return -1;  //Wenn nicht gefunden, -1 als vergleichswert zurueckgeben
    }

    /**
     * Set stone position.
     *
     * @param STONEINDEX the stoneindex
     * @param POINTINDEX the pointindex
     */
//Setzt einen Stein an eine Position
    static void setStonePosition(int STONEINDEX, int POINTINDEX){
        StoneonPosition[STONEINDEX] = POINTINDEX;   //Setzt die neue Position des Steins in das Array (Gleicher Index wie Stein)
        PointisFree[POINTINDEX] = false;    //Makiert den uebergebenen SpielPunkt als besetzt
    }

    /**
     * Get hovering position int.
     *
     * @return the int
     */
//Gibt zurueck, ueber welche Position sich der Mauszeiger zuzeit befindet
    static int getHoveringPosition(){
        return hoveringPosition;
    }

    /**
     * Set hovering position.
     *
     * @param Position the position
     */
//Setzt die Position, ueber welche Position zurzeit "gehovert" wird
    static void setHoveringPosition(int Position){
        hoveringPosition = Position;
    }

    /**
     * Gets last checked player.
     *
     * @return the last checked player
     */
//Gibt den letzten ueberprueften SpielerIndex zurueck
    static int getLastCheckedPlayer() {
        return lastCheckedPlayer;
    }

    /**
     * Sets last checked player.
     *
     * @param lastCheckedPlayer the last checked player
     */
//Setzt den zuletzt ueberprueften SpielerIndex
    static void setLastCheckedPlayer(int lastCheckedPlayer) {
        position.lastCheckedPlayer = lastCheckedPlayer;
    }

    /**
     * Get default points coordinate [ ].
     *
     * @return the coordinate [ ]
     */
//Gibt die Standardkoordinaten des Spielfeldes zurueck
    static coordinate[] getDefaultPoints() {
        return defaultPoints;
    }

    /**
     * Get default reachablepoints int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
//Gibt die Standard RachablePoints zurueck
    static int[][] getDefaultREACHABLEPOINTS() {
        return defaultREACHABLEPOINTS;
    }

    /**
     * Get default win combination int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
//Gibt die Standard Wincombinations zurueck
    static int[][] getDefaultWinCombination() {
        return defaultWinCombination;
    }

    /**
     * Sets points.
     *
     * @param points the points
     */
//Setzt die Spielfeldkoordinaten <- Themepack
    static void setPoints(coordinate[] points) {
        Points = points;
    }

    /**
     * Sets reachablepoints.
     *
     * @param REACHABLEPOINTS the reachablepoints
     */
//Setzt die ReachablePoints <- Themepack
    static void setREACHABLEPOINTS(int[][] REACHABLEPOINTS) {
        position.REACHABLEPOINTS = REACHABLEPOINTS;
    }

    /**
     * Sets win combination.
     *
     * @param winCombination the win combination
     */
//Setzt die WinCombinations <- Themepack
    static void setWinCombination(int[][] winCombination) {
        WinCombination = winCombination;
    }
}