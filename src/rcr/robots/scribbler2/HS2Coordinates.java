package rcr.robots.scribbler2;

/**
 * Clase de apoyo para almacenar coordenadas ( x, y ) enviadas por el S2
 * <p>
 * Las coordenadas pueden ser accesadas directamente como <code>obj.x</code>
 * y <code>obj.y</code>
 *
 * @see S2Path
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2Coordinates {
    /** la coordenada x */
    public final int x;
    /** la coordenada y */
    public final int y;

    /**
     * Construye una nueva Coordenada del tipo ( x, y )
     *
     * @param x la coordenada x
     * @param y la coordenada y
     */
    public HS2Coordinates( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retorna un objeto del tipo String representando el valor de la Coordenada
     *
     *  @return un String representando la Coordenada
     */
    public String toString() {
        return "HS2Coordinates(" + x + ", " + y + ")";
    }
}
