package rcr.scribbler2;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import rcr.scribbler2.robot.Scribbler2;

/**
 * Acceso a todas las funcionalidades del Scribbler2 (S2) via cable serial
 *
 * @author Roberto Carrasco
 */

 public class S2Serial extends Scribbler2 {
     /**
     * Clase para interactuar con un Scribbler2 via cable serial
     *
     * @param port el nombre de la puerta ("/dev/rfcomm1", "COM1")
     * @param bauds los bauds a operar
     * @param timeout el timeout de cada lectura expresado en ms
     * @param dtr activa linea DTR
     * @throws NoSuchPortException
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public S2Serial( String port, int bauds, int timeout, boolean dtr ) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        Serial serial = new Serial( port, bauds, timeout );
        serial.setDTR( dtr );
        pause( 2000 );
        serial.ignoreInput( 100 );
        setConn( serial );
    }
}
