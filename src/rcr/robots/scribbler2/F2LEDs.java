package rcr.robots.scribbler2;

import java.io.IOException;

/**
 * Clase para interactuar con el LED de la Fluke2 (F2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class F2LEDs {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con el LED de la F2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected F2LEDs( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Asigna energia al LED del F2 apagándolo o encendiéndolo
     *
     * @param pwm el valor de energia a asignar al led (0 a 255)
     * @throws IOException
     */
    public void setBrightLed( int pwm ) throws IOException {
        // { 116,  1,  0, fluke_set_led_on },
        // { 117,  1,  0, fluke_set_led_off },
        // { 126,  2,  0, fluke_set_bright_led }, Controlar aquí el apagado y encendido
        synchronized( s2 ) {
            byte[] packet = new byte[2];
            packet[0] = 126;
            packet[1] = (byte)(pwm & 0xFF);
            s2.sendF2Command( packet, 100 );
        }
    }

}

