package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los sensores de distancia (IR) del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2IRSensors {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores IR del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2IRSensors( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene el valor del sensor infrarojo izquierdo del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getIRLeft() throws IOException, SerialTimeoutException {
        // _GET_IR_LEFT      = 71  ' Response: leftIR 71, Notes: IR is 1 when there is no obstacle to the left of robot
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 71 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Obtiene el valor del sensor infrarojo derecho del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getIRRight() throws IOException, SerialTimeoutException {
        // _GET_IR_RIGHT     = 72  ' Response: rightIR 72, Notes: IR is 1 when there is no obstacle to the left of robot
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 72 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Obtiene el valor de los dos sensores infrarojo del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @see HS2Infrared
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Infrared getAllIR() throws IOException, SerialTimeoutException {
        // _GET_IR_ALL       = 73  ' Response: LeftIR RightIR 73, Notes: IR is 1 when there is no obstacle to the left of robot
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 73 );
            s2.sendS2Command( packet, 0 );
            return new HS2Infrared( s2.getUInt8Response(), s2.getUInt8Response() );
        }
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores infrarojos
     *
     * @param side el sensor infrarojo a leer ( 0, 1 )
     * @param thres umbral de corte para indicar el estado 0 o 1 del sensor
     * @return el valor extendido del sensor infrarojo especificado (0-255)
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getIrEx(int side, int thres) throws IOException, SerialTimeoutException {
        // _GET_IR_EX = 172 'Format 172 side type thres
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 172 );
            packet[1] = (byte)(side & 0x01);
            packet[2] = 0;
            packet[3] = (byte)(thres & 0xFF);
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores de distancia
     *
     * @param side el sensor de distancia a leer ( 0, 1 )
     * @return el valor extendido del sensor de distancia especificado (0-255)
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getDistanceEx(int side) throws IOException, SerialTimeoutException {
        // _DISTANCE_EX = 175 'Format 175 side
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 175 );
            packet[1] = (byte)(side & 0x01);
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

}
