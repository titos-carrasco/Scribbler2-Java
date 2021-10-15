package rcr.scribbler2.robot;

import java.util.Arrays;
import java.util.Formatter;
import java.io.IOException;

/**
 * Clase para interactuar con el robot Scribbler2 (S2). Mayor información en
 * https://bitbucket.org/ipre proyectos "ipre-scribbler2" y "fluke2srv".
 * También revisar en http://www.betterbots.com/ y https://www.parallax.com/product/28136
 *
 * @author Roberto Carrasco
 */
public class Scribbler2 {
    /** total de bytes de datos del paquete a enviar al S2 */
    public static final int DATA_LENGTH = 8;
    /** tamaño total del paquete a enviar al S2 */
    public static final int PACKET_LENGTH = 9;
    /** el objeto de la conexión serial al S2*/
    private ISerial conn = null;

    /**
     * Constructor.
     *
     * Una vez instanciada la clase se debe invocar a SetConn()
     */
    public Scribbler2() {
    }

    /**
     * Establece la conexion a utilizar con el robot
     *
     * @param conexion la conexion al Scribbler2
     */
    public void setConn(ISerial conexion) {
        if( conn == null )
            conn = conexion;
    }

    /***
     * Cierra la conexión con el S2
     */
    public synchronized void close() {
        conn.close();
    }

    /***
     * Obtiene desde el S2 un String descriptivo del robot
     *
     * @return el string informativo del S2
     * @throws IOException
     */
    public synchronized String getInfo() throws IOException{
        // _GET_INFO = 80  ' Response: "information on version robot, etc" 10 80
        // En algunos casos la F2 altera el primer caracter recibido desde el S2
        // cuando se hacen dos invocaciones sucesivas a este método
        // Esperar unos 100ms si se invoca dos veces
        byte[] packet = makeS2Packet( 80 );
        sendS2Command( packet, 0 );
        return getLineResponse( 128 );
    }

    /***
     * Obtiene desde el S2 la clave almacenada
     *
     * @return un String con la clave almacenada en el robot
     * @throws IOException
     */
    public synchronized String getPass() throws IOException{
        // _GET_PASS1 = 50  ' Respone: 16 bytes from flash memory
        // _GET_PASS2 = 51  ' Respone: 16 bytes from flash memory
        String pass1, pass2;
        byte[] packet = makeS2Packet( 50 );
        sendS2Command( packet, 0 );
        pass1 = new String( getBytesResponse( 8 ) );
        packet[0] = (byte)51;
        sendS2Command( packet, 0 );
        pass2 = new String( getBytesResponse( 8 ) );
        return pass1 + pass2;
    }

    /***
     * Obtiene desde el S2 un String con su nombre
     *
     * @return el nombre asignado al robot
     * @throws IOException
     */
    public synchronized String getName() throws IOException{
        // _GET_NAME1 = 78  ' Response: char1 char2 char3 char4 char5 char6 char7 char8 78
        // _GET_NAME2 = 64  ' Response: char9 char10 char11 char12 char13 char14 char15 char16 87
        String name1, name2;
        byte[] packet = makeS2Packet( 78 );
        sendS2Command( packet, 0 );
        name1 = new String( getBytesResponse( 8 ) );
        packet[0] = (byte)64;
        sendS2Command( packet, 0 );
        name2 = new String( getBytesResponse( 8 ) );
        return name1 + name2;
    }

    /***
     * Obtiene desde el S2 el estado almacenado en sus pines
     *
     * @return el estado almacenado en los pines del S2 mediante un objeto del tipo State
     * @see HS2State
     * @throws IOException
     */
    public synchronized HS2State getState() throws IOException{
        // _GET_STATE = 77  ' Response: inPins outPins 77, Notes: inPins is the state of all the input pins (0-7), and outPins is the state of all the output pins (8-15) as defined in the I/O Pin Declarations.
        byte[] packet = makeS2Packet( 77 );
        sendS2Command( packet, 0 );
        return new HS2State( getUInt8Response(), getUInt8Response() );
    }

    /***
     * Obtiene desde el S2 los 8 bytes de data del robot
     *
     * @return los 8 bytes de data del robot
     * @throws IOException
     */
    public synchronized byte[] getData() throws IOException{
        // _GET_DATA = 81  ' Response: data from flash memory 81
        byte[] packet = makeS2Packet( 81 );
        sendS2Command( packet, 0 );
        return getBytesResponse( Scribbler2.DATA_LENGTH );
    }

    /***
     * Asigna una nueva clave al S2 (la clave no tiene uso en el S2)
     *
     * @param pass la clave a asignar (16 caracteres)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setPass(String pass) throws IOException{
        // _SET_PASS1 = 55  ' Format: 55 PASS1 PASS2 ... PASS8
        // _SET_PASS2 = 56  ' Format: 56 PASS9 PASS2 ... PASS16
        byte[] bPass = pass.getBytes();
        int i, j;
        byte[] packet = makeS2Packet( 55 );
        for(i=0; i<bPass.length && i<Scribbler2.DATA_LENGTH; i++) {
            packet[i+1] = bPass[i];
        }
        for(; i<Scribbler2.DATA_LENGTH; i++) {
            packet[i+1] = 32;
        }
        sendS2Command( packet, 0 );
        getS2SensorsResponse();

        packet[0] = (byte)56;
        for(j=0; i<bPass.length && j<Scribbler2.DATA_LENGTH; i++, j++) {
            packet[j+1] = bPass[i];
        }
        for(; j<Scribbler2.DATA_LENGTH; j++) {
            packet[j+1] = 32;
        }
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Asigna un nuevo valor a uno de los bytes de datos del S2
     *
     * @param pos el byte a modifica (0 a 7)
     * @param data el valor a almacenar
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setSingleData(int pos, int data) throws IOException{
        // _SET_SINGLE_DATA = 96  ' Sets a single byte of data in flash memory'
            byte[] packet = makeS2Packet( 96 );
            packet[1] = (byte)(pos & 0x07);
            packet[2] = (byte)(data & 0xFF);
            sendS2Command( packet, 0 );
            return getS2SensorsResponse();
    }

    /***
     * Almacena datos en la zona de datos del S2
     *
     * @param data los datos a almacenar (16 bytes)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setData(byte[] data) throws IOException{
        // _SET_DATA = 97  ' Sets 8 bytes of data in flash memory
            byte[] packet = makeS2Packet( 97 );
            for( int i=0; i<data.length && i<Scribbler2.DATA_LENGTH; i++ ) {
                packet[i+1] = data[i];
            }
            sendS2Command( packet, 0 );
            return getS2SensorsResponse();
    }

    /***
     * Asigna un nuevo nombre al S2
     *
     * @param name el nombre nuevo a asignar al S2 (16 caracteres(
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setName(String name) throws IOException{
        // _SET_NAME1 = 110 ' Format: 110 char1 char2 char3 char4 char5 char6 char7 char8
        // _SET_NAME2 = 119 ' Format: 119 char9 char10 char11 char12 char13 char14 char15 char16
        byte[] bName = name.getBytes();
        int i, j;
        byte[] packet = makeS2Packet( 110 );
        for(i=0; i<bName.length && i<Scribbler2.DATA_LENGTH; i++) {
            packet[i+1] = bName[i];
        }
        for(; i<Scribbler2.DATA_LENGTH; i++) {
            packet[i+1] = 32;
        }
        sendS2Command( packet, 0 );
        getS2SensorsResponse();

        packet[0] = (byte)119;
        for(j=0; i<bName.length && j<Scribbler2.DATA_LENGTH; i++, j++) {
            packet[j+1] = bName[i];
        }
        for(; j<Scribbler2.DATA_LENGTH; j++) {
            packet[j+1] = 32;
        }
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Obtiene desde el S2 todos sus sensores
     *
     * @return los sensores del robot
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors getAllSensors() throws IOException{
        // _GET_ALL          = 65  ' Response: leftIR rightIR LeftLightHighyte LeftLightLowByte CenterLightHighByte CenterLightLowByte RightLightHighByte RightLightLowByte LineLeft LineRight Stall 65
        // _GET_ALL_BINARY   = 66  ' Response: BinaryData 66, Notes: where the individual bits of BinaryData are: 0x000 IRLeft IRRight Stall LineLeft LineRight
        byte[] packet = makeS2Packet( 65 );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Obtiene el valor del sensor infrarojo izquierdo del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @throws IOException
     */
    public synchronized int getIRLeft() throws IOException{
       // _GET_IR_LEFT      = 71  ' Response: leftIR 71, Notes: IR is 1 when there is no obstacle to the left of robot
       byte[] packet = makeS2Packet( 71 );
       sendS2Command( packet, 0 );
       return getUInt8Response();
    }

    /***
     * Obtiene el valor del sensor infrarojo derecho del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @throws IOException
     */
    public synchronized int getIRRight() throws IOException{
        // _GET_IR_RIGHT     = 72  ' Response: rightIR 72, Notes: IR is 1 when there is no obstacle to the left of robot
        byte[] packet = makeS2Packet( 72 );
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Obtiene el valor de los dos sensores infrarojo del S2
     *
     * @return el valor del sensor infrarojo (0-1)
     * @see HS2Infrared
     * @throws IOException
     */
    public synchronized HS2Infrared getAllIR() throws IOException{
        // _GET_IR_ALL       = 73  ' Response: LeftIR RightIR 73, Notes: IR is 1 when there is no obstacle to the left of robot
        byte[] packet = makeS2Packet( 73 );
        sendS2Command( packet, 0 );
        return new HS2Infrared( getUInt8Response(), getUInt8Response() );
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores infrarojos
     *
     * @param side el sensor infrarojo a leer ( 0, 1 )
     * @param thres umbral de corte para indicar el estado 0 o 1 del sensor
     * @return el valor extendido del sensor infrarojo especificado (0-255)
     * @throws IOException
     */
    public synchronized int getIrEx(int side, int thres) throws IOException{
        // _GET_IR_EX = 172 'Format 172 side type thres
        byte[] packet = makeS2Packet( 172 );
        packet[1] = (byte)(side & 0x01);
        packet[2] = 0;
        packet[3] = (byte)(thres & 0xFF);
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores de distancia
     *
     * @param side el sensor de distancia a leer ( 0, 1 )
     * @return el valor extendido del sensor de distancia especificado (0-255)
     * @throws IOException
     */
    public synchronized int getDistanceEx(int side) throws IOException{
        // _DISTANCE_EX = 175 'Format 175 side
        byte[] packet = makeS2Packet( 175 );
        packet[1] = (byte)(side & 0x01);
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Apaga o Enciende el led izquierdo del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setLeftLed( boolean encender ) throws IOException{
        // _SET_LED_LEFT_ON    = 99
        // _SET_LED_LEFT_OFF   = 100
        byte cmd = 100;
        if( encender ) {
            cmd = 99;
        }
        byte[] packet = makeS2Packet( cmd );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Apaga o Enciende el led central del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setCenterLed( boolean encender ) throws IOException{
        // _SET_LED_CENTER_ON  = 101
        // _SET_LED_CENTER_OFF = 102
        byte cmd = 102;
        if( encender ) {
            cmd = 101;
        }
        byte[] packet = makeS2Packet( cmd );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Apaga o Enciende el led derecho del S2
     *
     * @param encender True para encender y False para apagar
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setRightLed( boolean encender ) throws IOException{
        // _SET_LED_RIGHT_ON   = 103
        // _SET_LED_RIGHT_OFF  = 104
        byte cmd = 104;
        if( encender ) {
            cmd = 103;
        }
        byte[] packet = makeS2Packet( cmd );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
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
     */
    public synchronized HS2Sensors setAllLed( int left, int center, int right ) throws IOException{
        // _SET_LED_ALL_ON     = 105
        // _SET_LED_ALL_OFF    = 106
        // _SET_LED_ALL        = 107 ' Format: 107 LeftLEDstate CenterLEDstate RightLEDstate 0 0 0 0 0
        byte[] packet = makeS2Packet( 107 );
        packet[1] = (byte)(left & 0x01);
        packet[2] = (byte)(center & 0x01);
        packet[3] = (byte)(right & 0x01);
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Obtiene el valor del sensor de luz izquierdo del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     */
    public synchronized int getLeftLight() throws IOException{
        // _GET_LIGHT_LEFT   = 67  ' Response: HighByte LowByte 67
        byte[] packet = makeS2Packet( 67 );
        sendS2Command( packet, 0 );
        return getUInt16Response();
    }

    /***
     * Obtiene el valor del sensor de luz del centro del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     */
    public synchronized int getCenterLight() throws IOException{
        // _GET_LIGHT_CENTER = 68  ' Response: HighByte LowByte 68
        byte[] packet = makeS2Packet( 68 );
        sendS2Command( packet, 0 );
        return getUInt16Response();
    }

    /***
     * Obtiene el valor del sensor de luz derecho del S2
     *
     * @return el valor del sensor de luz
     * @throws IOException
     */
    public synchronized int getRightLight() throws IOException{
        // _GET_LIGHT_RIGHT  = 69  ' Response: HighByte LowByte 69
        byte[] packet = makeS2Packet( 69 );
        sendS2Command( packet, 0 );
        return getUInt16Response();
    }

    /***
     * Obtiene el valor de todos los sensores de luz del S2
     *
     * @return el valor del sensor de luz
     * @see HS2Lights
     * @throws IOException
     */
    public synchronized HS2Lights getAllLights() throws IOException{
    	// _GET_LIGHT_ALL    = 70  ' Response: LeftHighyte LeftLowByte CenterHighByte CenterLowByte RightHighByte RightLowByte 70
        byte[] packet = makeS2Packet( 70 );
        sendS2Command( packet, 0 );
        return new HS2Lights( getUInt16Response(), getUInt16Response(), getUInt16Response() );
    }

    /***
     * Obtiene el sensor de línea izquierdo del S2
     *
     * @return el valor del sensor de línea
     * @throws IOException
     */
    public synchronized int getLeftLine() throws IOException{
        // _GET_LINE_LEFT    = 74  ' Response: lineLeft 74
        byte[] packet = makeS2Packet( 74 );
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Obtiene el sensor de línea derecho del S2
     *
     * @return el valor del sensor de línea
     * @throws IOException
     */
    public synchronized int getRightLine() throws IOException{
        // _GET_LINE_RIGHT   = 75  ' Response: lineRight 75
        byte[] packet = makeS2Packet( 75 );
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Obtiene el valor de todos los sensores de línea del S2
     *
     * @return los sensores de línea del robot
     * @see HS2LineSensors
     * @throws IOException
     */
    public synchronized HS2LineSensors getAllLines() throws IOException{
        // _GET_LINE_ALL     = 76  ' Response: LineLeft LineRight 76
        byte[] packet = makeS2Packet( 76 );
        sendS2Command( packet, 0 );
        return new HS2LineSensors( getUInt8Response(), getUInt8Response() );
    }

    /***
     * Obtiene desde el S2 el valor extendido de sus sensores de línea
     *
     * @param side el sensor de línea a leer ( 0, 1 )
     * @param thres umbral de corte para indicar el estado 0 o 1 del sensor
     * @return el valor extendido del sensor de línea especificado
     * @throws IOException
     */
    public synchronized int getLineEx(int side, int thres) throws IOException{
        // _GET_LINE_EX = 173 'Format 173 side type thres
        byte[] packet = makeS2Packet( 173 );
        packet[1] = (byte)(side & 0x01);
        packet[2] = 0;
        packet[3] = (byte)(thres & 0xFF);
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Obtiene desde el S2 el valor de ruido detectado por su micrófono
     *
     * @return el valor de ruido detectado por el micrófono del robot
     * @throws IOException
     */
    public synchronized long getMicEnv() throws IOException{
        // _GET_MIC_ENV = 169 'Format 169
        byte[] packet = makeS2Packet( 169 );
        sendS2Command( packet, 0 );
        return getUInt32Response();
    }

    /***
     * Obtiene desde el S2 el estado de sus motores
     *
     * @return el estado de los motores del robot
     * @see HS2MotorStats
     * @throws IOException
     */
    public synchronized HS2MotorStats getMotorStats() throws IOException{
        // _GET_MOTOR_STATS = 170 'Format 170
        byte[] packet = makeS2Packet( 170 );
        sendS2Command( packet, 0 );
        return new HS2MotorStats( getUInt32Response(), getUInt8Response() );
    }

    /***
     * Obtiene desde el S2 el valor de sus encoders
     *
     * @param preserve indica si se conserva (1) o resetea (0) el valor de los encoders
     * @return el valor de los encoders del S2
     * @see HS2Encoders
     * @throws IOException
     */
    public synchronized HS2Encoders getEncoders( int preserve ) throws IOException{
        // _GET_ENCODERS = 171 'Format 171 type
        byte[] packet = makeS2Packet( 171 );
        packet[1] = (byte)(preserve & 0x01);
        sendS2Command( packet, 0 );
        return new HS2Encoders( getUInt32Response(), getUInt32Response() );
    }

    /***
     * Obtiene el indicador que señala si las ruedas del S2 están atascadas
     *
     * @return el valor que indica si las ruedas están atascadas
     * @throws IOException
     */
    public synchronized int getStall() throws IOException{
        // _GET_STALL = 79  ' Response: stall 79
        byte[] packet = makeS2Packet( 79 );
        sendS2Command( packet, 0 );
        return getUInt8Response();
    }

    /***
     * Apaga los motores del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setMotorsOff() throws IOException{
        // _SET_MOTORS_OFF = 108 ' Format: 108 0 0 0 0 0 0 0 0
        byte[] packet = makeS2Packet( 108 );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Activa los motores del  Los otores permanecerán activos hasta asignar nuevos valores
     *
     * @param left potencia a asignar al motor izquierdo (0 a 100 - porcentaje)
     * @param right potencia a asignar al motor derecho (0 a 100  porcentaje)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setMotors(int left, int right) throws IOException{
        // _SET_MOTORS = 109 ' Format: 109 rightmotor leftmotor 0 0 0 0 0 0, Notes: 0 = full speed backwards, 100 = stop, 200 = full speed forward
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

        byte[] packet = makeS2Packet( 109 );
        packet[1] = (byte)right;
        packet[2] = (byte)left;
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Apaga el parlante del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setQuiet() throws IOException{
        // _SET_QUIET = 112
        byte[] packet = makeS2Packet( 112 );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Activa el parlante del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setLoud() throws IOException{
        // _SET_LOUD = 111
        byte[] packet = makeS2Packet( 111 );
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Establece el nivel de volumen del parlante del S2
     *
     * @param volume el nivel de volumen del parlante del S2 (0 a 100 - porcentaje)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setVolume(int volume) throws IOException{
        // _SET_VOLUME = 160 'Format 160 volume
        volume = volume & 0xFF;
        if( volume > 100 ) {
            volume= 100;
        }
        byte[] packet = makeS2Packet( 160 );
        packet[1] = (byte)volume;
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Emite un sonido a través del parlante del  Limitamos a 2500ms la duración
     * del sonido dado que la Fluke2 posee un timeout de 3000ms que intefiere con esta operación
     *
     * @param duration duración del sonido a emitir en ms (no superior a 2500ms)
     * @param freq1 frecuencia en Hz delsonido
     * @param freq2 frecuencia en Hz de un segundo sonido emitido de manera simultanea
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors setSpeaker(int duration, int freq1, int freq2) throws IOException{
        // _SET_SPEAKER   = 113 ' Format: 113 DurationHighByte DurationLowByte FreqHighByte FreqLowByte
        // _SET_SPEAKER_2 = 114 ' Format: 114 DurationHighByte DurationLowByte Freq1HighByte Freq1LowByte Freq2HighByte Freq2LowByte
        //
        // El timeout en la F2 es de 3000ms lo que genera problemas de sync
        // Limitamos a 2500s la duración de cualquier sonido
        duration = duration & 0xFFFF;
        if( duration > 2500 ) {
            duration = 2500;
        }
        freq1 = freq1 & 0xFFFF;
        freq2 = freq2 & 0xFFFF;
        byte[] packet = makeS2Packet( 114 );
        packet[1] = (byte)((duration >>8) & 0xFF);
        packet[2] = (byte)(duration & 0xFF);
        packet[3] = (byte)((freq1 >> 8) & 0xFF);
        packet[4] = (byte)(freq1 & 0xFF);
        packet[5] = (byte)((freq2 >> 8) & 0xFF);
        packet[6] = (byte)(freq2 & 0xFF);
        sendS2Command( packet, duration );
        return getS2SensorsResponse();
    }

    /***
     * Pone al S2 en modalidad de trazado.
     *
     * @param speed velocidad a asignar a las operaciones de trazado (1 a 15)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors beginPath(int speed) throws IOException{
        // _PATH  = 161 'Format 161 begin_or_end speed
        // _BEGIN = 1
        byte[] packet = makeS2Packet( 161 );
        packet[1] = 1;
        packet[2] = 0;
        packet[3] = (byte)(speed & 0x0F);
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Saca al S2 de la modalidad de trazado. Cualquier operación pendiente es
     * ejecutada por el S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors endPath() throws IOException{
        // _PATH = 161 'Format 161 begin_or_end speed
        // _END  = 0
        byte[] packet = makeS2Packet( 161 );
        packet[1] = 0;
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
    }

    /***
     * Obtiene la posición del S2 en el plano cartesiano de trazado
     *
     * @return las coordenadas de posición (en mm) del S2 mediante un objeto del tipo Coordinates
     * @see HS2Coordinates
     * @throws IOException
     */
    public synchronized HS2Coordinates getPosn() throws IOException{
        // _GET_POSN = 165 'Format 165
        byte[] packet = makeS2Packet( 165 );
        sendS2Command( packet, 0 );
        return new HS2Coordinates( getInt32Response(), getInt32Response() );
    }

    /***
     * Obtiene el ángulo (en grados) del S2 en el plano de trazado
     *
     * @return el ángulo (en grados) del S2 en el plano de trazado
     * @throws IOException
     */
    public synchronized int getAngle() throws IOException{
        // _GET_ANGLE = 167 'Format 167
        byte[] packet = makeS2Packet( 167 );
        sendS2Command( packet, 0 );
        return getInt32Response();
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
     */
    public synchronized HS2Sensors setPosn( int x, int y ) throws IOException{
        // _SET_POSN = 166 'Format 166 x0Byte x1Byte x2Byte x3Byte y0Byte y1Byte y2Byte y3Byte
        byte[] packet = makeS2Packet( 166 );
        packet[1] = (byte)((x >> 24) & 0xFF);
        packet[2] = (byte)((x >> 16) & 0xFF);
        packet[3] = (byte)((x >> 8) & 0xFF);
        packet[4] = (byte)(x & 0xFF);
        packet[5] = (byte)((y >> 24) & 0xFF);
        packet[6] = (byte)((y >> 16) & 0xFF);
        packet[7] = (byte)((y >> 8) & 0xFF);
        packet[8] = (byte)(y & 0xFF);
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
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
     */
    public synchronized HS2Sensors setAngle( int angle ) throws IOException{
        // _SET_ANGLE = 168 'Format 168 angle0Byte angle1Byte angle2Byte angle3Byte
        byte[] packet = makeS2Packet( 168 );
        packet[1] = (byte)((angle >> 24) & 0xFF);
        packet[2] = (byte)((angle >> 16) & 0xFF);
        packet[3] = (byte)((angle >> 8) & 0xFF);
        packet[4] = (byte)(angle & 0xFF);
        sendS2Command( packet, 0 );
        return getS2SensorsResponse();
    }

    /***
     * Ordena al S2 que se mueva a la posición (x, y) en mm dentro del plano de trazado
     *
     * @param x la coordenada x (mm) a la cual debe desplazarse
     * @param y la coordena y (mm) a la cual debe desplazarse
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors moveTo( int x, int y ) throws IOException{
        // _MOVE = 162 'Format 162 type hXByte lXByte hYByte lYByte
        // _TO   = 2
        // _MM   = 1
        byte[] packet = makeS2Packet( 162 );
        packet[1] = 2 + 1;
        packet[2] = (byte)((x >> 8) & 0xFF);
        packet[3] = (byte)(x & 0xFF);
        packet[4] = (byte)((y >> 8) & 0xFF);
        packet[5] = (byte)(y & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
    }

    /***
     * Ordena al S2 que se desplaze de manera relativa a una nueva posición dentro del plano de trazado
     *
     * @param x el desplazamiento (mm) en x a sumar a la posición x actual
     * @param y el desplazamiento (mm) en y a sumar a la posición y actual
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors moveBy( int x, int y ) throws IOException{
        // _MOVE = 162 'Format 162 type hXByte lXByte hYByte lYByte
        // _BY   = 4
        // _MM   = 1
        byte[] packet = makeS2Packet( 162 );
        packet[1] = 4 + 1;
        packet[2] = (byte)((x >> 8) & 0xFF);
        packet[3] = (byte)(x & 0xFF);
        packet[4] = (byte)((y >> 8) & 0xFF);
        packet[5] = (byte)(y & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
    }

    /***
     * Ordena al S2 a que gire al ángulo (grados) especificado dentro del plano de trazado
     *
     * @param angle el ángulo (grados) al cual girar
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors turnTo( int angle ) throws IOException{
        // _TURN = 164 'Format 164 type hAngleByte lAngleByte
        // _TO   = 2
        // _DEG  = 1
        byte[] packet = makeS2Packet( 164 );
        packet[1] = 2 + 1;
        packet[2] = (byte)((angle >> 8) & 0xFF);
        packet[3] = (byte)(angle & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
    }

    /***
     * Ordena al S2 a que gire de manera relativa un número de grados dentro del lano de trazado
     *
     * @param angle los grados a sumar a la orientación actual
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     */
    public synchronized HS2Sensors turnBy( int angle ) throws IOException{
        // _TURN = 164 'Format 164 type hAngleByte lAngleByte
        // _BY   = 4
        // _DEG  = 1
        byte[] packet = makeS2Packet( 164 );
        packet[1] = 4 + 1;
        packet[2] = (byte)((angle >> 8) & 0xFF);
        packet[3] = (byte)(angle & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
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
     */
    public synchronized HS2Sensors arcTo( int x, int y, int radius) throws IOException{
        // _ARC = 163 'Format 163 type hXByte lXByte hYByte lYByte hRadByte lRadByte
        // _TO  = 2
        // _MM  = 1
        byte[] packet = makeS2Packet( 163 );
        packet[1] = 2 + 1;
        packet[2] = (byte)((x >> 8) & 0xFF);
        packet[3] = (byte)(x & 0xFF);
        packet[4] = (byte)((y >> 8) & 0xFF);
        packet[5] = (byte)(y & 0xFF);
        packet[6] = (byte)((radius >> 8) & 0xFF);
        packet[7] = (byte)(radius & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
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
     */
    public synchronized HS2Sensors arcBy( int x, int y, int radius) throws IOException{
        // _ARC = 163 'Format 163 type hXByte lXByte hYByte lYByte hRadByte lRadByte
        // _BY  = 4
        // _MM  = 1
        byte[] packet = makeS2Packet( 163 );
        packet[1] = 4 + 1;
        packet[2] = (byte)((x >> 8) & 0xFF);
        packet[3] = (byte)(x & 0xFF);
        packet[4] = (byte)((y >> 8) & 0xFF);
        packet[5] = (byte)(y & 0xFF);
        packet[6] = (byte)((radius >> 8) & 0xFF);
        packet[7] = (byte)(radius & 0xFF);
        sendS2PathCommand( packet );
        return getS2SensorsResponse();
    }

    /***
     * Crea el paquete requerido por el  S2. El primer byte es el comando
     *
     * @return el paquete requerido por el S2
     ***/
    protected byte[] makeS2Packet( int cmd ) {
        byte[] packet = new byte[ PACKET_LENGTH ];
        packet[0] = (byte)(cmd & 0xFF);
        return packet;
    }

    /***
     * Envía el paquete de comando al S2 y valida el echo si corresponde
     *
     * @param packet el paquete a enviar de tamaño PACKET_LENGTH
     * @param pause tiempo para pausa despues de enviar el comando
     * @return True si la operación es exitosa, False si el echo es inválido
     * @throws IOException
     ***/
    protected boolean sendS2Command( byte[] packet, int pause ) throws IOException{
        conn.write( packet );
        if( pause > 0 ) {
            pause( pause );
        }
        if( packet[0] != 0x50 ){
            byte[] b = conn.read( PACKET_LENGTH );
            if( !Arrays.equals( packet, b ) ) {
                System.out.println( "Packet Mismatch:" );
                return false;
            }
        }
        return true;
    }

    /***
     * Envía el paquete de comando de trazado (PATH) al S2.
     * Si existe un movimiento activo en el S2 el nuevo comando de movimiento
     * esperara a que el anterior finalice
     * Si existe una Fluke2, ésta podría generar problemas de sincronización
     * si es que existe un movimiento en ejecución en el S2, ya que la fluke2
     * tiene un timeout de 3000ms - problemas con movimientos que tarden mas
     * de 3 segundo
     *
     * @param packet el paquete a enviar con el comando de trazado
     * @throws IOException
     ***/
    protected void sendS2PathCommand( byte[] packet ) throws IOException{
    	// envia el comando de desplazamiento
        conn.write( packet );

        // espera por el echo
        byte[] b = conn.read( PACKET_LENGTH );
        if( !Arrays.equals( packet, b ) ) {
            System.out.println( "Packet Mismatch (path):" );
        }

        // espera por la respuesta
        getS2SensorsResponse();

        // espera a que lso motores se detengan
        packet = makeS2Packet( 170 );
        while( true ){
        	sendS2Command( packet, 0 );
            HS2MotorStats stat = new HS2MotorStats( getUInt32Response(), getUInt8Response() );
            boolean moving = (stat.stat & 0x03)!=0;
            if(moving)
            	pause(500);
            else
            	break;
        }
        
        // dejamosla respuesta apropiada para ser leida por quien invoca
        packet = makeS2Packet( 65 );
        sendS2Command( packet, 0 );
    }

    /***
     * Lee el valor de los sensores del S2. Estos valores son reportados
     * como respuesta a la mayoría de los comandos enviados al S2
     *
     * @return los sensores reportados por el S2
     * @throws IOException
     ***/
    protected HS2Sensors getS2SensorsResponse() throws IOException{
        return new HS2Sensors( getUInt8Response(), getUInt8Response(),
                               getUInt16Response(),getUInt16Response(), getUInt16Response(),
                               getUInt8Response(), getUInt8Response(),
                               getUInt8Response() );
    }

    /***
     * Lee una línea de respuesta desde la puerta serial terminada en NL
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param maxChars máximo de caracteres a esperar en la línea antes del NL
     * @return la línea leida con el NL removido
     * @throws IOException
     ***/
    protected String getLineResponse( int maxChars ) throws IOException{
        return conn.readLine( maxChars );
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt8
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt8 leido
     * @throws IOException
     ***/
    protected int getUInt8Response() throws IOException{
        return conn.readUInt8();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt16
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt16 leido
     * @throws IOException
     ***/
    protected int getUInt16Response() throws IOException{
        return conn.readUInt16();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt32
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt32 leido
     * @throws IOException
     ***/
    protected long getUInt32Response() throws IOException{
        return conn.readUInt32();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un Int32
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el Int32 leido
     * @throws IOException
     ***/
    protected int getInt32Response() throws IOException{
        return conn.readInt32();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un arreglo de bytes
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param nbytes el número de bytes a leer como respuesta
     * @return los bytes leidos
     * @throws IOException
     ***/
    protected byte[] getBytesResponse( int nbytes )  throws IOException{
        return conn.read( nbytes );
    }

    /***
     * Establece una pausa dada en ms
     *
     * @param ms la pausa en milisegundos
     ***/
    public static void pause( int ms ) {
        try {
            Thread.sleep( ms );
        } catch( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }

    /***
     * Despliega un arreglo de bytes en hexadecimal
     *
     * @param data el arreglo de bytes a desplegar
     ***/
    public static String bytesToHex(byte[] data) {
        Formatter formatter = new Formatter();
        for (byte b : data)
            formatter.format("%02X", b);
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}


/*
    No implementadas por este módulo
    // SOFT_RESET      = 33  ' Format: 33 0 0 0 0 0 0 0 0, Notes: Performs a software reset of robot.  (turns name broadcast back on)
    // SET_ECHO_MODE   = 98  ' Set the echo mode on or off; if off, no echo is made after command


    {  13,  3,  0, fluke_enable_pan },    // enable bluetooth PAN networking
    {  40,  7,  0, fluke_update_firmware },
    {  41,  1,  0, fluke_save_eeprom },
    {  42,  134, 0, fluke_restore_eeprom },
    {  82,  1,  0, fluke_get_rle },
    {  83,  1,  0, fluke_get_image },
    {  84,  2,  0, fluke_get_image_window },
    {  88,  2,  0, fluke_get_image_window_sum },
    {  90,  5,  0, fluke_get_serial_mem },
    {  91,  2,  0, fluke_get_scrib_program },
    {  92,  2,  0, fluke_get_camera_param },
    {  94,  2,  0, fluke_get_blob_window },
    {  95,  1,  0, fluke_get_blob },
    { 118,  9,  0, fluke_set_rle },
    { 121,  6,  0, fluke_set_serial_mem },
    { 125,  3,  0, fluke_serial_erase },
    { 153,  5,  0, fluke_set_scrib2_program },
    { 155,  3,  0, fluke_set_scrib_program_batch },

    No implementadas en fluke2.c
    {  43,  1,  0, fluke_reset },
    { 122,  0,  0, fluke_set_scrib_program }, // FIX CMDLEN PARAM
    { 123,  4,  0, fluke_set_start_program }, // FIX CMDLEN PARAM
    { 132,  4,  0, fluke_set_uart },
    { 133,  2,  0, fluke_pass_byte },
    { 134,  2,  0, fluke_set_passthrough },
    { 139,  2,  0, fluke_set_pass_n_bytes },
    { 140,  2,  0, fluke_get_pass_n_bytes },
    { 141,  2,  0, fluke_get_pass_bytes_until },
    { 143,  1,  0, fluke_set_passthrough_on },
    { 144,  1,  0, fluke_set_passthrough_off },
    { 150,  1,  0, fluke_get_ir_message },
    { 151,  2,  0, fluke_send_ir_message },
    { 152,  2,  0, fluke_set_ir_emitters },
*/
