package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los sensores de línea del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2LineSensors {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores de línea del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2LineSensors( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene el sensor de línea izquierdo del S2
     *
     * @return el valor del sensor de línea
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getLeftLine() throws IOException, SerialTimeoutException {
        // _GET_LINE_LEFT    = 74  ' Response: lineLeft 74
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 74 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Obtiene el sensor de línea derecho del S2
     *
     * @return el valor del sensor de línea
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getRightLine() throws IOException, SerialTimeoutException {
        // _GET_LINE_RIGHT   = 75  ' Response: lineRight 75
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 75 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Obtiene el valor de todos los sensores de línea del S2
     *
     * @return los sensores de línea del robor
     * @see HS2LineSensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2LineSensors getAllLines() throws IOException, SerialTimeoutException {
        // _GET_LINE_ALL     = 76  ' Response: LineLeft LineRight 76
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 76 );
            s2.sendS2Command( packet, 0 );
            return new HS2LineSensors( s2.getUInt8Response(), s2.getUInt8Response() );
        }
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores de línea
     *
     * @param side el sensor de línea a leer ( 0, 1 )
     * @param thres umbral de corte para indicar el estado 0 o 1 del sensor
     * @return el valor extendido del sensor de línea especificado
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getLineEx(int side, int thres) throws IOException, SerialTimeoutException {
        // _GET_LINE_EX = 173 'Format 173 side type thres
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 173 );
            packet[1] = (byte)(side & 0x01);
            packet[2] = 0;
            packet[3] = (byte)(thres & 0xFF);
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

}
