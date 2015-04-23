package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los sensores de luz del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2LightSensors {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores de luz del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2LightSensors( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene el valor del sensor de luz izquierdo del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getLeftLight() throws IOException, SerialTimeoutException {
        // _GET_LIGHT_LEFT   = 67  ' Response: HighByte LowByte 67
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 67 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt16Response();
        }
    }

    /***
     * Obtiene el valor del sensor de luz del centro del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getCenterLight() throws IOException, SerialTimeoutException {
        // _GET_LIGHT_CENTER = 68  ' Response: HighByte LowByte 68
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 68 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt16Response();
        }
    }

    /***
     * Obtiene el valor del sensor de luz derecho del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getRightLed() throws IOException, SerialTimeoutException {
        // _GET_LIGHT_RIGHT  = 69  ' Response: HighByte LowByte 69
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 69 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt16Response();
        }
    }

    /***
     * Obtiene el valor de todos los sensores de luz del S2
     *
     * @return el valor del sensor de luz
     * @see HS2Lights
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Lights getAllLights() throws IOException, SerialTimeoutException {
        // _GET_LIGHT_ALL    = 70  ' Response: LeftHighyte LeftLowByte CenterHighByte CenterLowByte RightHighByte RightLowByte 70
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 70 );
            s2.sendS2Command( packet, 0 );
            return new HS2Lights( s2.getUInt16Response(), s2.getUInt16Response(), s2.getUInt16Response() );
        }
    }

}
