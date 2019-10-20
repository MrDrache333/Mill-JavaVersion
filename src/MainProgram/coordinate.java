package MainProgram;

/**
 * Created by keno on 08.11.16.
 * <p>
 * FUNKTION DIESER KLASSE:
 */
class coordinate {

    //Nur X und Y Koordinate ("noch" ist Z irrelevant)
    private double x;
    private double y;

    /**
     * Instantiates a new Coordinate.
     *
     * @param X the x
     * @param Y the y
     */
//Constuctor <- Neue coordinate erstellen
    coordinate(double X, double Y){
        this.x = X;
        this.y = Y;
    }

//FUNKTIONEN

    /**
     * Make image coordinate coordinate.
     *
     * @param COORD  the coord
     * @param Height the height
     * @param Width  the width
     * @return the coordinate
     */
//ImageCorrdinate <- Koordinate, welche, wenn angewand, die gegebenen Koordinaten so anpasst, dass das spaeter damit platzierte Bild mittig ueber der Koordinate liegt
    static coordinate makeImageCoordinate(coordinate COORD,double Height, double Width){
        return new coordinate(COORD.getX() - (Width / 2),COORD.getY() - (Height / 2));
    }

//GETTER UND SETTER

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    @SuppressWarnings("unused")
    public coordinate getCoordinates() {
        return new coordinate(this.x,this.y);
    }

    /**
     * Set coordinates.
     *
     * @param X the x
     * @param Y the y
     */
    @SuppressWarnings("unused")
    void setCoordinates(double X, double Y){
        this.x = X;
        this.y = Y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    double getX() {
        return this.x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    @SuppressWarnings("unused")
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    double getY() {
        return this.y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    @SuppressWarnings("unused")
    public void setY(double y) {
        this.y = y;
    }

}