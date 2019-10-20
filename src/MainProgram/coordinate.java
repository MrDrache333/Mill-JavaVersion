package MainProgram;

/**
 * Created by keno on 08.11.16.
 *
 * FUNKTION DIESER KLASSE:
 *
 */
class coordinate {

    //Nur X und Y Koordinate ("noch" ist Z irrelevant)
    private double x;
    private double y;

    //Constuctor <- Neue coordinate erstellen
    coordinate(double X, double Y){
        this.x = X;
        this.y = Y;
    }

//FUNKTIONEN

    //ImageCorrdinate <- Koordinate, welche, wenn angewand, die gegebenen Koordinaten so anpasst, dass das spaeter damit platzierte Bild mittig ueber der Koordinate liegt
    static coordinate makeImageCoordinate(coordinate COORD,double Height, double Width){
        return new coordinate(COORD.getX() - (Width / 2),COORD.getY() - (Height / 2));
    }

//GETTER UND SETTER

    @SuppressWarnings("unused")
    public coordinate getCoordinates() {
        return new coordinate(this.x,this.y);
    }
    @SuppressWarnings("unused")
    void setCoordinates(double X, double Y){
        this.x = X;
        this.y = Y;
    }

    double getX() {
        return this.x;
    }

    @SuppressWarnings("unused")
    public void setX(double x) {
        this.x = x;
    }

    double getY() {
        return this.y;
    }

    @SuppressWarnings("unused")
    public void setY(double y) {
        this.y = y;
    }

}