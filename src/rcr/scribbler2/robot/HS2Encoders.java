package rcr.scribbler2.robot;

/**
 * Clase de apoyo para almacenar los datos de los encoders del S2
 * <p>
 * El valor de los encoders puede ser accesado directamente como <code>obj.left</code>
 * y <code>obj.right</code>
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
 public class HS2Encoders {
    /** el encoder izquierdo */
    public final long left;
    /** el encoder derecho */
    public final long right;

    /**
     * Construye un nuevo objeto para almacenar los encoders del S2
     *
     * @param left el encoder izquierdo
     * @param right el encoder derecho
     */
    public HS2Encoders( long left, long right ) {
        this.left = left;
        this.right = right;
    }

    /**
     * Retorna un objeto del tipo String representando el valor de los encoders
     *
     *  @return un String representando los encoders
     */
    public String toString() {
        return "HS2Encoders(" + left + ", " + right + ")";
    }
}
