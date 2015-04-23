package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los motores del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2Motors {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los motores del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2Motors( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene desde el S2 el estado de sus motores
     *
     * @return el estado de los motores del robot
     * @see HS2MotorStats
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2MotorStats getMotorStats() throws IOException, SerialTimeoutException {
        // _GET_MOTOR_STATS = 170 'Format 170
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 170 );
            s2.sendS2Command( packet, 0 );
            return new HS2MotorStats( s2.getUInt32Response(), s2.getUInt8Response() );
        }
    }

    /***
     * Obtiene desde el S2 el valor de sus encoders
     *
     * @param preserve indica si se conserva (1) o resetea (0) el valor de los encoders
     * @return el valor de los encoders del S2
     * @see HS2Encoders
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Encoders getEncoders( int preserve ) throws IOException, SerialTimeoutException {
        // _GET_ENCODERS = 171 'Format 171 type
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 171 );
            packet[1] = (byte)(preserve & 0x01);
            s2.sendS2Command( packet, 0 );
            return new HS2Encoders( s2.getUInt32Response(), s2.getUInt32Response() );
        }
    }

    /***
     * Obtiene el indicador que señala si las ruedas del S2 están atascadas
     *
     * @return el valor que indica si las ruedas están atascadas
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getStall() throws IOException, SerialTimeoutException {
        // _GET_STALL = 79  ' Response: stall 79
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 79 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt8Response();
        }
    }

    /***
     * Apaga los motores del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setMotorsOff() throws IOException, SerialTimeoutException {
        // _SET_MOTORS_OFF = 108 ' Format: 108 0 0 0 0 0 0 0 0
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 108 );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Activa los motores del S2. Los otores permanecerán activos hasta asignar nuevos valores
     *
     * @param left potencia a asignar al motor izquierdo (0 a 100 - porcentaje)
     * @param right potencia a asignar al motor derecho (0 a 100  porcentaje)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setMotors(int left, int right) throws IOException, SerialTimeoutException {
        // _SET_MOTORS = 109 ' Format: 109 rightmotor leftmotor 0 0 0 0 0 0, Notes: 0 = full speed backwards, 100 = stop, 200 = full speed forward
        synchronized( s2 ) {
            if(left>100) {
                left = 100;
            }
            else if(left<-100) {
                left = -100;
            }
            left = left + 100;

            if(right>100) {
                right = 100;
            }
            else if(right<-100) {
                right = -100;
            }
            right = right + 100;

            byte[] packet = s2.makeS2Packet( 109 );
            packet[1] = (byte)right;
            packet[2] = (byte)left;
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

}
