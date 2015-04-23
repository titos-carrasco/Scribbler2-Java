package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los sensores de distancia (IR) de la Fluke2 (F2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class F2IRSensors {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores IR del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected F2IRSensors( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Asigna la potencia al emisor infrarojo de la F2
     *
     * @param pwm la potencia del emisor infrarojo(0 a 255)
     * @throws IOException
     */
    public void setIRPower( int pwm ) throws IOException {
        // { 120,  2,  0, fluke_set_ir_power }, // SetBrightLed modifica el valor pwd registrado
        synchronized( this ) {
            byte[] packet = new byte[2];
            packet[0] = 120;
            packet[1] = (byte)(pwm & 0xFF);
            s2.sendF2Command( packet, 100 );
        }
    }

    /***
     * Obtiene el valor del sensor infrarojo de la F2.
     *
     * @return el valor del sensor infrarojo
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getIR() throws IOException, SerialTimeoutException {
        // {  85,  1,  0, fluke_get_ir_left },
        // {  86,  1,  0, fluke_get_ir_center }, Sólo existe éste
        // {  87,  1,  0, fluke_get_ir_right },
        synchronized( this ) {
            byte[] packet = new byte[1];
            packet[0] = 86;
            s2.sendF2Command( packet, 100 );
            return s2.getUInt16Response();
        }
    }

}
