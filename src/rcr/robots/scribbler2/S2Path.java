package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para operar el Scribbler2 (S2) en un plano de trazado.
 * No se deben mezclar las operaciones de dibujo con otras operaciones de desplazamiento,
 * en particular las del controlador de motores S2Motors
 * El movimiento se realiza sobre un hipotético plano cartesiano en mm
 * Si la S2 opera con una Fluke2, ésta última genera problemas de sincronismo
 * debido al timeout de 3000ms que posee.
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2Path {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que opera al S2 en un plano de trazado
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2Path( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Pone al S2 en modalidad de trazado.
     *
     * @param speed velocidad a asignar a las operaciones de trazado (1 a 15)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors beginPath( int speed ) throws IOException, SerialTimeoutException {
        // _PATH  = 161 'Format 161 begin_or_end speed
        // _BEGIN = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 161 );
            packet[1] = 1;
            packet[2] = 0;
            packet[3] = (byte)(speed & 0x0F);
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Saca al S2 de la modalidad de trazado. Cualquier operación pendiente es
     * ejecutada por el S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors endPath() throws IOException, SerialTimeoutException {
        // _PATH = 161 'Format 161 begin_or_end speed
        // _END  = 0
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 161 );
            packet[1] = 0;
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Obtiene la posición del S2 en el plano cartesiano de trazado
     *
     * @return las coordenadas de posición (en mm) del S2 mediante un objeto del tipo Coordinates
     * @see HS2Coordinates
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Coordinates getPosn() throws IOException, SerialTimeoutException {
        // _GET_POSN = 165 'Format 165
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 165 );
            s2.sendS2Command( packet, 0 );
            return new HS2Coordinates( s2.getInt32Response(), s2.getInt32Response() );
        }
    }

    /***
     * Obtiene el ángulo (en grados) del S2 en el plano de trazado
     *
     * @return el ángulo (en grados) del S2 en el plano de trazado
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public int getAngle() throws IOException, SerialTimeoutException {
        // _GET_ANGLE = 167 'Format 167
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 167 );
            s2.sendS2Command( packet, 0 );
            return s2.getInt32Response();
        }
    }

    /***
     * Señala al S2 su posición en el plano de trazado. Siempre después de BeginPath()
     * debe señalarse la posición de partida del S2 por ejemplo ( 0, 0)
     *
     * @param x la posición en el eje x (mm) del S2
     * @param y la posición en el eje y (mm) del S2
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setPosn( int x, int y ) throws IOException, SerialTimeoutException {
        // _SET_POSN = 166 'Format 166 x0Byte x1Byte x2Byte x3Byte y0Byte y1Byte y2Byte y3Byte
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 166 );
            packet[1] = (byte)((x >> 24) & 0xFF);
            packet[2] = (byte)((x >> 16) & 0xFF);
            packet[3] = (byte)((x >> 8) & 0xFF);
            packet[4] = (byte)(x & 0xFF);
            packet[5] = (byte)((y >> 24) & 0xFF);
            packet[6] = (byte)((y >> 16) & 0xFF);
            packet[7] = (byte)((y >> 8) & 0xFF);
            packet[8] = (byte)(y & 0xFF);
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Señala al S2 el ángulo (en grado) en el cual se encuentra dentro del plano de trazado.
     * Siempre después de BeginPath() debe señalarse el ángulo inicial en el cual
     * se encuentra el S2,por ejemplo (90)
     *
     * @param angle el ángulo (grados) en el cual se encuentra el S2
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setAngle( int angle ) throws IOException, SerialTimeoutException {
        // _SET_ANGLE = 168 'Format 168 angle0Byte angle1Byte angle2Byte angle3Byte
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 168 );
            packet[1] = (byte)((angle >> 24) & 0xFF);
            packet[2] = (byte)((angle >> 16) & 0xFF);
            packet[3] = (byte)((angle >> 8) & 0xFF);
            packet[4] = (byte)(angle & 0xFF);
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 que se mueva a la posición (x, y) en mm dentro del plano de trazado
     *
     * @param x la coordenada x (mm) a la cual debe desplazarse
     * @param y la coordena y (mm) a la cual debe desplazarse
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors moveTo( int x, int y ) throws IOException, SerialTimeoutException {
        // _MOVE = 162 'Format 162 type hXByte lXByte hYByte lYByte
        // _TO   = 2
        // _MM   = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 162 );
            packet[1] = 2 + 1;
            packet[2] = (byte)((x >> 8) & 0xFF);
            packet[3] = (byte)(x & 0xFF);
            packet[4] = (byte)((y >> 8) & 0xFF);
            packet[5] = (byte)(y & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 que se desplaze de manera relativa a una nueva posición dentro del plano de trazado
     *
     * @param x el desplazamiento (mm) en x a sumar a la posición x actual
     * @param y el desplazamiento (mm) en y a sumar a la posición y actual
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors moveBy( int x, int y ) throws IOException, SerialTimeoutException {
        // _MOVE = 162 'Format 162 type hXByte lXByte hYByte lYByte
        // _BY   = 4
        // _MM   = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 162 );
            packet[1] = 4 + 1;
            packet[2] = (byte)((x >> 8) & 0xFF);
            packet[3] = (byte)(x & 0xFF);
            packet[4] = (byte)((y >> 8) & 0xFF);
            packet[5] = (byte)(y & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 a que gire al ángulo (grados) especificado dentro del plano de trazado
     *
     * @param angle el ángulo (grados) al cual girar
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors turnTo( int angle ) throws IOException, SerialTimeoutException {
        // _TURN = 164 'Format 164 type hAngleByte lAngleByte
        // _TO   = 2
        // _DEG  = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 164 );
            packet[1] = 2 + 1;
            packet[2] = (byte)((angle >> 8) & 0xFF);
            packet[3] = (byte)(angle & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 a que gire de manera relativa un número de grados dentro del lano de trazado
     *
     * @param angle los grados a sumar a la orientación actual
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors turnBy( int angle ) throws IOException, SerialTimeoutException {
        // _TURN = 164 'Format 164 type hAngleByte lAngleByte
        // _BY   = 4
        // _DEG  = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 164 );
            packet[1] = 4 + 1;
            packet[2] = (byte)((angle >> 8) & 0xFF);
            packet[3] = (byte)(angle & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 que trace un arco dentro del plano de trazado
     *
     * @param x la coordenada x (mm) a la cual desplazarse
     * @param y la coordenada y (mm) a la cual desplazarse
     * @param radius la distancia (mm) del arco medidadesde el centro de la línea que une la posición actual con la nueva posición
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors arcTo( int x, int y, int radius) throws IOException, SerialTimeoutException {
        // _ARC = 163 'Format 163 type hXByte lXByte hYByte lYByte hRadByte lRadByte
        // _TO  = 2
        // _MM  = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 163 );
            packet[1] = 2 + 1;
            packet[2] = (byte)((x >> 8) & 0xFF);
            packet[3] = (byte)(x & 0xFF);
            packet[4] = (byte)((y >> 8) & 0xFF);
            packet[5] = (byte)(y & 0xFF);
            packet[6] = (byte)((radius >> 8) & 0xFF);
            packet[7] = (byte)(radius & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Ordena al S2 que trace un arco de manera relativa dentro del plano de trazado
     *
     * @param x el desplazamiento en x (mm) a sumar al x actual
     * @param y el desplazamiento en y (mm) a sumar al y actual
     * @param radius la distancia (mm) del arco medidadesde el centro de la línea que une la posición actual con la nueva posición
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors arcBy( int x, int y, int radius) throws IOException, SerialTimeoutException {
        // _ARC = 163 'Format 163 type hXByte lXByte hYByte lYByte hRadByte lRadByte
        // _BY  = 4
        // _MM  = 1
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 163 );
            packet[1] = 4 + 1;
            packet[2] = (byte)((x >> 8) & 0xFF);
            packet[3] = (byte)(x & 0xFF);
            packet[4] = (byte)((y >> 8) & 0xFF);
            packet[5] = (byte)(y & 0xFF);
            packet[6] = (byte)((radius >> 8) & 0xFF);
            packet[7] = (byte)(radius & 0xFF);
            s2.sendS2PathCommand( packet );
            return s2.getS2SensorsResponse();
        }
    }

}
