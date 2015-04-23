package rcr.robots.scribbler2;

/**
 * Clase de apoyo para almacenar el estado de los motores del S2
 * <p>
 * El estado de los motores puede ser accesado directamente como <code>obj.stat</code>
 * y <code>obj.moveReady</code>
 *
 * @see S2Motors
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2MotorStats {
    /** el estado de varios componentes de los motores (bitwise) */
    public final long stat;
    /** el stado de movimiento de las ruedas ( moveReady & 0x03 == 0 )*/
    public final int moveReady;

    /**
     * Construye un nuevo objeto para contener el estado de los motores
     *
     * @param stat el estado de varios componentes de los motores (bitwise)
     * @param moveReady el stado de movimiento de las ruedas ( moveReady & 0x03 == 0 )
     */
    public HS2MotorStats( long stat, int moveReady ) {
        this.stat = stat;
        this.moveReady = moveReady;
    }

    /**
     * Retorna un objeto del tipo String representando el estado de los motores del S2
     *
     *  @return un String representando el estado de los motores
     */
    public String toString() {
        return "HS2MotorStats(" + stat + ", " + moveReady + ")";
    }
}
