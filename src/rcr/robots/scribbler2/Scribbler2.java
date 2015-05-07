package rcr.robots.scribbler2;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rcr.utils.Serial;
import rcr.utils.SerialTimeoutException;
import rcr.utils.Utils;

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
    /** el objeto de la conexión serial */
    private Serial serial;
    /** el controlador interno del S2 */
    private S2Inner s2Inner;
    /** el controlador de los sensores IR del S2 */
    private S2IRSensors s2IRSensors;
    /** el controlador de los LEDs del S2 */
    private S2LEDs s2LEDs;
    /** el controlador de los sensores de luz del S2 */
    private S2LightSensors s2LightSensors;
    /** el controlador de los sensores de línea del S2 */
    private S2LineSensors s2LineSensors;
    /** el controlador del micrófono del S2 */
    private S2Microphone s2Microphone;
    /** el controlador de los motores del S2 */
    private S2Motors s2Motors;
    /** el controlador del parlante del S2 */
    private S2Speaker s2Speaker;
    /** el controlador de trazados del S2 */
    private S2Path s2Path;
    /** el controlador de la cámara de la F2 */
    private F2Camera f2Camera;
    /** el controlador interno de la F2 */
    private F2Inner f2Inner;
    /** el controlador de los sensores IR de la F2 */
    private F2IRSensors f2IRSensors;
    /** el controlador de los LEDs de la F2 */
    private F2LEDs f2LEDs;

    /**
     * Construye el objeto para manipular el S2 a través de un canal serial
     * Todas las operaciones sobre el robot deben realizarse con un mutex
     * implementado como un lock sobre este objeto. Esto requiere que cada
     * clase que implementa las componentes del S2 se adhiera a este contrato
     *
     * @param port la puerta serial a la cual conectarse ("/dev/rfcomm1", "COM1")
     * @param timeout tiempo de espera en ms para recibir los datos desde el S2
     * @throws NoSuchPortException
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public Scribbler2( String port, int timeout ) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        serial = new Serial( port, 9600, timeout );
        Utils.pause( 1000 );
        serial.flushRead( 1000 );
        s2Inner = new S2Inner( this );
        s2IRSensors = new S2IRSensors( this );
        s2LEDs = new S2LEDs( this );
        s2LightSensors = new S2LightSensors( this );
        s2LineSensors = new S2LineSensors( this );
        s2Microphone = new S2Microphone( this );
        s2Motors = new S2Motors( this );
        s2Speaker = new S2Speaker( this );
        s2Path = new S2Path( this );
        f2Camera = new F2Camera( this );
        f2Inner = new F2Inner( this );
        f2IRSensors = new F2IRSensors( this );
        f2LEDs = new F2LEDs( this );
    }

    /***
     * Cierra la conexión con el S2
     * No requiere el lock sobre este objeto pues implementa por sola el mutex
     */
    public void close() {
        synchronized( this ) {
            serial.close();
        }
    }

    /***
     * Otorga acceso al controlador interno del S2
     *
     * @return el controlador interno del S2
     * @see S2Inner
     ***/
    public S2Inner getS2Inner() {
        return s2Inner;
    }

    /***
     * Otorga acceso al controlador de los sensores IR
     *
     * @return el controlador de los sensores IR
     * @see S2IRSensors
     ***/
    public S2IRSensors getS2IRSensors() {
        return s2IRSensors;
    }

    /***
     * Otorga acceso al controlador de los LEDs
     *
     * @return el controlador de los LEDs
     * @see S2LEDs
     ***/
    public S2LEDs getS2LEDs() {
        return s2LEDs;
    }

    /***
     * Otorga acceso al controlador de los sensores de luz
     *
     * @return el controlador de los sensores de luz
     * @see S2LightSensors
     ***/
    public S2LightSensors getS2LightSensors() {
        return s2LightSensors;
    }

    /***
     * Otorga acceso al controlador de los sensores de línea
     *
     * @return el controlador de los sensores de línea
     * @see S2LineSensors
     ***/
    public S2LineSensors getS2LineSensors() {
        return s2LineSensors;
    }

    /***
     * Otorga acceso al controlador del micrófono
     *
     * @return el controlador del micrófono
     * @see S2Microphone
     ***/
    public S2Microphone getS2Microphone() {
        return s2Microphone;
    }

    /***
     * Otorga acceso al controlador de los motores
     *
     * @return el controlador de los motores
     * @see S2Motors
     ***/
    public S2Motors getS2Motors() {
        return s2Motors;
    }

    /***
     * Otorga acceso al controlador del parlante
     *
     * @return el controlador del parlante
     * @see S2Speaker
     ***/
    public S2Speaker getS2Speaker() {
        return s2Speaker;
    }

    /***
     * Otorga acceso al controlador de motores modalidad trazado
     *
     * @return el controlador de motores modalidad trazado
     * @see S2Speaker
     ***/
    public S2Path getS2Path() {
        return s2Path;
    }

    /***
     * Otorga acceso al controlador de la cámara de la F2
     *
     * @return el controlador de la cámara de la F2
     * @see S2Speaker
     ***/
    public F2Camera getF2Camera() {
        return f2Camera;
    }

    /***
     * Otorga acceso al controlador inerno de la F2
     *
     * @return el controlador interno de la F2
     * @see S2Speaker
     ***/
    public F2Inner getF2Inner() {
        return f2Inner;
    }

    /***
     * Otorga acceso al controlador de los sensores IR de la F2
     *
     * @return el controlador de los sensores IR de la F2
     * @see S2Speaker
     ***/
    public F2IRSensors getF2IRSensors() {
        return f2IRSensors;
    }

    /***
     * Otorga acceso al controlador dde los LEDs de la F2
     *
     * @return el controlador de los LEDs de la F2
     * @see S2Speaker
     ***/
    public F2LEDs getF2LEDs() {
        return f2LEDs;
    }

    /***
     * Crea el paquete requerido por el S2. El primer byte es el comando
     *
     * @return el paquete requerido por el S2
     ***/
    protected byte[] makeS2Packet( int cmd ) {
        byte[] packet = new byte[ PACKET_LENGTH ];
        //Arrays.fill( packet, (byte)0x00 );
        packet[0] = (byte)(cmd & 0xFF);
        return packet;
    }

    /***
     * Envía el paquete de comando al S2 y valida el echo si corresponde
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param packet el paquete a enviar de tamaño PACKET_LENGTH
     * @param pause tiempo para pausa despues de enviar el comando
     * @return True si la operación es exitosa, False si el echo es inválido
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected boolean sendS2Command( byte[] packet, int pause ) throws IOException, SerialTimeoutException {
        serial.write( packet );
        if( pause > 0 ) {
            Utils.pause( pause );
        }
        if( packet[0] != 0x50 ){
            byte[] b = serial.read( PACKET_LENGTH );
            if( !Arrays.equals( packet, b ) ) {
                Utils.debug( "Packet Mismatch:" );
                Utils.debug( packet );
                Utils.debug( b );
                return false;
            }
        }
        return true;
    }

    /***
     * Envía el paquete de comando de trazado (PATH) al S2.
     * Si existe una Fluke2, ésta genera problemas de sincronización
     * debido al delay interno de 3000ms que posee al redirigir los comandos a la S2
     * Se ha de considerar este hecho y el que el S2 encola este tipo de comandos
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param packet el paquete a enviar con el comando de trazado
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected void sendS2PathCommand( byte[] packet ) throws IOException, SerialTimeoutException {
        serial.write( packet );
        long t = System.currentTimeMillis();
        while( (int)( System.currentTimeMillis() - t) < 3500 ) {
            try {
                byte[] b = serial.read( PACKET_LENGTH );
                if( !Arrays.equals( packet, b ) ) {
                    Utils.debug( "Packet Mismatch (path):" );
                    Utils.debug( packet );
                    Utils.debug( b );
                    throw new IOException();
                }
                return;
            } catch( SerialTimeoutException e ) {
            }
        }

        // necesitamos sincronizar las respuestas, lo hacemos con GetAll()
        packet = makeS2Packet( 65 );
        while( true ) {
            serial.write( packet );
            try {
                serial.read( 11 ); // estos bytes quedaron sin ser recibidos
                serial.read( PACKET_LENGTH );
                break;
            } catch( SerialTimeoutException e ) {
            }
        }

        // solicitamos el estado de los sensores
        sendS2Command( packet, 0 );
    }

    /***
     * Envía el paquete de comando a la Fluke2. El timeout intern0 de la F2 es de 3000ms
     * La F2 no envía ningún elemento de regreso para efectos de sincronismo
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param packet el paquete a enviar con el comando a la F2
     * @param pause tiempo para pausa despues de enviar el comando
     * @throws IOException
     ***/
    protected void sendF2Command( byte[] packet, int pause ) throws IOException {
        serial.write( packet );
        if( pause > 0 ) {
            Utils.pause( pause );
        }
    }

    /***
     * Lee el valor de los sensores del S2. Estos valores son reportados
     * como respuesta a la mayoría de los comandos enviados al S2
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return los sensores reportados por el S2
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected HS2Sensors getS2SensorsResponse() throws IOException, SerialTimeoutException {
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
     * @throws SerialTimeoutException
     ***/
    protected String getLineResponse( int maxChars ) throws IOException, SerialTimeoutException {
        return serial.readLine( maxChars );
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt8
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt8 leido
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected int getUInt8Response() throws IOException, SerialTimeoutException {
        return serial.readUInt8();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt16
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt16 leido
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected int getUInt16Response() throws IOException, SerialTimeoutException {
        return serial.readUInt16();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un UInt32
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el UInt32 leido
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected long getUInt32Response() throws IOException, SerialTimeoutException {
        return serial.readUInt32();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un Int32
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @return el Int32 leido
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected int getInt32Response() throws IOException, SerialTimeoutException {
        return serial.readInt32();
    }

    /***
     * Lee una respuesta desde la puerta serial correspondiente a un arreglo de bytes
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param nbytes el número de bytes a leer como respuesta
     * @return los bytes leidos
     * @throws IOException
     * @throws SerialTimeoutException
     ***/
    protected byte[] getBytesResponse( int nbytes )  throws IOException, SerialTimeoutException {
        return serial.read( nbytes );
    }

}


/*
    No implementadas por este módulo
    // SOFT_RESET      = 33  ' Format: 33 0 0 0 0 0 0 0 0, Notes: Performs a software reset of robot.  (turns name broadcast back on)
    // SET_ECHO_MODE   = 98  ' Set the echo mode on or off; if off, no echo is made after command


    {  12,  3,  0, fluke_servo },
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

