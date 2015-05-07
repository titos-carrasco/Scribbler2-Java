package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;
import rcr.utils.Utils;

/**
 * Clase para interactuar con los elementos internos de la Fluke2 (F2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class F2Inner {
    /** el frente del robot es la posición de la fluke2 */
    public static final int FLUKE_FORWARD = 1;
    /** el frente del robot es la posición de los sensores de luz del s2 */
    public static final int SCRIBBLER_FORWARD = 2;
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los elementos internos de la F2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected F2Inner( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene la versión de la tarjeta Fluke2 (F2)
     *
     * @return la versión de la F2
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String getVersion() throws IOException, SerialTimeoutException {
        // { 142,  1,  0, fluke_get_version },
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = (byte)142;
            s2.sendF2Command( packet, 100 );
            return s2.getLineResponse( 128 );
        }
    }

    /***
     * Obtiene la identificación del robot desde la F2. Este comando resetea al S2
     * provocando una pausa de unos 4000ms
     *
     * @return la identificación del robot
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String identifyRobot() throws IOException, SerialTimeoutException {
        // { 156,  1,  0, fluke_identify_robot }, Resetea el Scribbler2
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = (byte)156;
            s2.sendF2Command( packet, 100 );
            String id = s2.getLineResponse( 128 );
            Utils.pause( 4000 );
            return id;
        }
    }

    /***
     * Obtiene el estado de carga de la batería
     *
     * @return el estado de carga de la bateria
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public double getBattery() throws IOException, SerialTimeoutException {
        // {  89,  1,  0, fluke_get_battery },
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = 89;
            s2.sendF2Command( packet, 100 );
            return s2.getUInt16Response() / 20.9813;
        }
    }

    /***
     * Señala a la F2 cual es la cabeza del robot
     *
     * @param forwardness FLUKE_FORWARD o SCRIBBLER_FORWARD
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public void setForwardness( int forwardness ) throws IOException {
        // { 128,  2,  0, fluke_set_forwardness }, Graba en flashdata
        synchronized( s2 ) {
            byte f;
            if( forwardness == FLUKE_FORWARD) {
                f = 0;
            }
            else { // if( forwardness == SCRIBBLER_FORWARD )
                f = 1;
            }
            byte[] packet = new byte[2];
            packet[0] = (byte)128;
            packet[1] = f;
            s2.sendF2Command( packet, 100 );
        }
    }

    /***
     * Obiene el log de errores almacenado en la F2
     *
     * @return el log de errores de la F2
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String getErrors() throws IOException, SerialTimeoutException {
        // {  10,  1,  0, fluke_get_errors },    // read error log - new for fluke2
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = 10;
            s2.sendF2Command( packet, 100 );
            int n = s2.getUInt16Response();
            byte[] log = s2.getBytesResponse( n );
            return new String( log, 0, n );
        }
    }

    /***
     * Resetea el S2.
     *
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public void resetScribbler() throws IOException {
        // { 124,  1,  0, fluke_reset_scribbler },
        // { 154,  1,  0, fluke_reset_scrib2 },
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = 124;
            s2.sendF2Command( packet, 100 );
            Utils.pause( 4000 );
        }
    }

}
