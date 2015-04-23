package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los LEDs del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2LEDs {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los LEDs del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2LEDs( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Apaga o Enciende el led izquierdo del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setLeftLed( boolean encender ) throws IOException, SerialTimeoutException {
        // _SET_LED_LEFT_ON    = 99
        // _SET_LED_LEFT_OFF   = 100
        synchronized( s2 ) {
            byte cmd = 100;
            if( encender ) {
                cmd = 99;
            }
            byte[] packet = s2.makeS2Packet( cmd );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Apaga o Enciende el led central del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setCenterLed( boolean encender ) throws IOException, SerialTimeoutException {
        // _SET_LED_CENTER_ON  = 101
        // _SET_LED_CENTER_OFF = 102
        synchronized( s2 ) {
            byte cmd = 102;
            if( encender ) {
                cmd = 101;
            }
            byte[] packet = s2.makeS2Packet( cmd );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Apaga o Enciende el led derecho del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setRightLed( boolean encender ) throws IOException, SerialTimeoutException {
        // _SET_LED_RIGHT_ON   = 103
        // _SET_LED_RIGHT_OFF  = 104
        synchronized( s2 ) {
            byte cmd = 104;
            if( encender ) {
                cmd = 103;
            }
            byte[] packet = s2.makeS2Packet( cmd );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Apaga y/o enciende individual y simultaneamene los leds del S2
     *
     * @param left el valor de encendido (1) o apagado (0) del led izquierdo
     * @param center el valor de encendido (1) o apagado (0) del led central
     * @param right el valor de encendido (1) o apagado (0) del led derecho
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setAllLed( int left, int center, int right ) throws IOException, SerialTimeoutException {
        // _SET_LED_ALL_ON     = 105
        // _SET_LED_ALL_OFF    = 106
        // _SET_LED_ALL        = 107 ' Format: 107 LeftLEDstate CenterLEDstate RightLEDstate 0 0 0 0 0
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 107 );
            packet[1] = (byte)(left & 0x01);
            packet[2] = (byte)(center & 0x01);
            packet[3] = (byte)(right & 0x01);
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

}
