package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con el micrófono del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2Microphone {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con el micrófono del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2Microphone( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene desde el S2 el valor de ruido detectado por su micrófono
     *
     * @return el valor de ruido detectado por el micrófono del robot
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public long getMicEnv() throws IOException, SerialTimeoutException {
        // _GET_MIC_ENV = 169 'Format 169
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 169 );
            s2.sendS2Command( packet, 0 );
            return s2.getUInt32Response();
        }
    }

}
