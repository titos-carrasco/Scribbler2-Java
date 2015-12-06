package rcr.robots.scribbler2;

import java.io.IOException;

/**
 * Clase para interactuar con los servos conectados a la Fluke2 (F2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class F2Servos {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los servos conectados a la F2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected F2Servos( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Posiciona uno de los servos conectados a la F2. SIGNAL en el borde derecho
     *
     * @param id el servo a controlar (0-3). 0=el de más abajo en la fluke
     * @param value el valor del movimiento (0=0° - 255=90°)
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public void setServo(int id, int value) throws IOException {
        // {  12,  3,  0, fluke_servo }
        synchronized( s2 ) {
            byte[] packet = new byte[3];
            packet[0] = 12;
            packet[1] = (byte)(id & 0x03);
            packet[2] = (byte)(value & 0xFF);
            s2.sendF2Command( packet, 100 );
        }
    }

}
