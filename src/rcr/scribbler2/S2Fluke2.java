package rcr.scribbler2;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rcr.scribbler2.robot.Scribbler2;

/**
 * Acceso a todas las funcionalidades del Scribbler2 y de la Fluke2 via BT.
 *
 * la Fluke2 tieme un timeout interno de 300ms para que los comandos redireccionados
 * al S2 envien su respuesta
 *
 * @author Roberto Carrasco
 */

public class S2Fluke2 extends Scribbler2 {
    /** el frente del robot es la posición de la fluke2 */
    public static final int FLUKE_FORWARD = 1;
    /** el frente del robot es la posición de los sensores de luz del s2 */
    public static final int SCRIBBLER_FORWARD = 2;
    /** foto grande */
    public static final int IMAGE_LARGE = 1;
    /** foto pequeña */
    public static final int IMAGE_SMALL = 2;
    /** foto jpg gris */
    public static final int IMAGE_GRAYJPEG = 1;
    /** foto gris jpg rápida */
    public static final int IMAGE_GRAYJPEG_FAST = 2;
    /** foto color jpg */
    public static final int IMAGE_JPEG  = 3;
    /** foto color jpg rápida */
    public static final int IMAGE_JPEG_FAST = 4;
    /** ancho de la imagen en pixeles */
    private int image_width  = 0;
    /** alto de la imagen en pixeles */
    private int image_height = 0;
    /** la conexion a la fluke2 */
    private Serial serial;
	
	/**
     * Clase para interactuar con un Scribbler2 via uan Fluke2
     *
     * @param port el nombre de la puerta ("/dev/rfcomm1", "COM1")
     * @param bauds los bauds a operar
     * @param timeout el timeout de cada lectura expresado en ms
     * @throws NoSuchPortException
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public S2Fluke2( String port, int bauds, int timeout ) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        serial = new Serial( port, bauds, timeout );
        pause( 2000 );
        serial.ignoreInput( 100 );
        setConn( serial );
    }

    /***
     * Configura el tamaño de las imagenes a obtener desde el F2
     *
     * @param size tamaño de la image (IMAGE_SMALL o IMAGE_LARGE)
     * @throws IOException
     */
    public synchronized void setPicSize( int size ) throws IOException {
        // {  11,  2,  0, fluke_set_picsize },
        int s, w, h;
        if( size == IMAGE_LARGE ) {
            s = 213;
            w = 1280;
            h = 800;
        }
        else { // if( size == IMAGE_SMALL )
            s = 71;
            w = 427;
            h = 266;
        }
        byte[] packet = new byte[2];
        packet[0] = 11;
        packet[1] = (byte)s;
        sendF2Command( packet, 100 );
        image_width = w;
        image_height = h;
    }

    /***
     * Obtiene una imagen desde el F2
     *
     * @param mode el tipo de imagen a obtener: IMAGE_GRAYJPEG, IMAGE_GRAYJPEG_FAST, IMAGE_JPEG o IMAGE_JPEG_FAST
     * @return la imagen obtenida desde el F2 mediante un objeto del tipo HF2Image
     * @see HF2Image
     * @throws IOException
     */
    public synchronized HF2Image getImage( int mode ) throws IOException {
        // { 135,  1,  0, fluke_get_jpeg_grey_header },
        // { 136,  2,  0, fluke_get_jpeg_grey_scan },
        // { 137,  1,  0, fluke_get_jpeg_color_header },
        // { 138,  2,  0, fluke_get_jpeg_color_scan },
        // Utilizar un timeout de al menos 3000ms
        int reg, cmd_header, cmd;
        if( mode == IMAGE_GRAYJPEG ) {
            reg = 1;
            cmd_header = 135;
            cmd = 136;
        }
        else if( mode == IMAGE_GRAYJPEG_FAST ) {
            reg = 0;
            cmd_header = 135;
            cmd = 136;
        }
        else if( mode == IMAGE_JPEG ) {
            reg = 1;
            cmd_header = 137;
            cmd = 138;
        }
        else { // if( mode == IMAGE_JPEG_FAST )
            reg = 0;
            cmd_header = 137;
            cmd = 138;
        }
        byte[] packet = new byte[1];
        packet[0] = (byte)cmd_header;
        sendF2Command( packet, 100 );

        int header_len = getUInt8Response() + (getUInt8Response() << 8);
        ByteArrayOutputStream image = new ByteArrayOutputStream();
        image.write( getBytesResponse( header_len ), 0, header_len );

        packet = new byte[2];
        packet[0] = (byte)cmd;
        packet[1] = (byte)reg;
        sendF2Command( packet, 100 );

        int last = 0;
        while( true ) {
            int b = getUInt8Response();
            image.write( b );
            if( b == 0xD9 && last == 0xFF) {
                getUInt32Response();
                getUInt32Response();
                getUInt32Response();
                break;
            }
            last = b;
        }
        return new HF2Image( image_width, image_height, image.toByteArray() );
    }

     /***
     * @throws IOException
     */
   public synchronized void whiteBalanceOn() throws IOException {
        // { 129,  1,  0, fluke_white_balance_on },
        byte[] packet = new byte[1];
        packet[0] = (byte)129;
        sendF2Command( packet, 100 );
    }

     /***
     * @throws IOException
     */
    public synchronized void whiteBalanceOff() throws IOException {
        // { 130,  1,  0, fluke_white_balance_off },
        byte[] packet = new byte[1];
        packet[0] = (byte)130;
        sendF2Command( packet, 100 );
    }

     /***
     * @throws IOException
     */
    public synchronized void setCameraParam( int addr, int value ) throws IOException {
        // { 131,  3,  0, fluke_set_camera_param },
        byte[] packet = new byte[3];
        packet[0] = (byte)131;
        packet[1] = (byte)(addr & 0xFF);
        packet[2] = (byte)(value & 0xFF);
        sendF2Command( packet, 100 );
    }

     /***
     * @throws IOException
     */
    public synchronized void setWindow( int window, int xLow, int yLow, int xHigh, int yHigh, int xStep, int yStep) throws IOException {
        // { 127, 12,  0, fluke_set_window },
        byte[] packet = new byte[12];
        packet[0] = (byte)127;
        packet[1] = (byte)(window & 0xFF);
        packet[2] = (byte)((xLow >> 8) & 0xFF);
        packet[3] = (byte)(xLow & 0xFF);
        packet[4] = (byte)((yLow >> 8) & 0xFF);
        packet[5] = (byte)(yLow & 0xFF);
        packet[6] = (byte)((xHigh >> 8) & 0xFF);
        packet[7] = (byte)(xHigh & 0xFF);
        packet[8] = (byte)((yHigh >> 8) & 0xFF);
        packet[9] = (byte)(yHigh & 0xFF);
        packet[10] = (byte)(xStep & 0xFF);
        packet[11] = (byte)(yStep & 0xFF);
        sendF2Command( packet, 100 );
    }

    /***
     * Obtiene la versión de la tarjeta Fluke2 (F2)
     *
     * @return la versión de la F2
     * @throws IOException
     */
    public synchronized String getVersion() throws IOException {
        // { 142,  1,  0, fluke_get_version },
        byte[] packet = new byte[1];
        packet[0] = (byte)142;
        sendF2Command( packet, 100 );
        return getLineResponse( 128 );
    }

    /***
     * Obtiene la identificación del robot desde la F2. Este comando resetea al S2
     * provocando una pausa de unos 4000ms
     *
     * @return la identificación del robot
     * @throws IOException
     */
    public synchronized String identifyRobot() throws IOException {
        // { 156,  1,  0, fluke_identify_robot }, Resetea el Scribbler2
        byte[] packet = new byte[1];
        packet[0] = (byte)156;
        sendF2Command( packet, 100 );
        String id = getLineResponse( 128 );
        pause( 4000 );
        return id;
    }

    /***
     * Obtiene el estado de carga de la batería
     *
     * @return el estado de carga de la bateria
     * @throws IOException
     */
    public synchronized double getBattery() throws IOException {
        // {  89,  1,  0, fluke_get_battery },
        byte[] packet = new byte[1];
        packet[0] = 89;
        sendF2Command( packet, 100 );
        return getUInt16Response() / 20.9813;
    }

    /***
     * Señala a la F2 cual es el frente del robot
     *
     * @param forwardness FLUKE_FORWARD o SCRIBBLER_FORWARD
     * @throws IOException
     */
    public synchronized void setForwardness( int forwardness ) throws IOException {
        // { 128,  2,  0, fluke_set_forwardness }, Graba en flashdata
        byte f;
        if( forwardness == FLUKE_FORWARD) {
            f = 0;
        }
        else { // if( forwardness == SCRIBBLER_FORWARD )
            f = 1;
        }
        byte[] packet = new byte[2];
        packet[0] = (byte)128;
        packet[1] = f;
        sendF2Command( packet, 100 );
    }

    /***
     * Obiene el log de errores almacenado en la F2
     *
     * @return el log de errores de la F2
     * @throws IOException
     */
    public synchronized String getErrors() throws IOException {
        // {  10,  1,  0, fluke_get_errors },    // read error log - new for fluke2
        byte[] packet = new byte[1];
        packet[0] = 10;
        sendF2Command( packet, 100 );
        int n = getUInt16Response();
        byte[] log = getBytesResponse( n );
        return new String( log, 0, n );
    }

    /***
     * Resetea el S2.
     *
     * @throws IOException
     */
    public synchronized void resetScribbler() throws IOException {
        // { 124,  1,  0, fluke_reset_scribbler },
        // { 154,  1,  0, fluke_reset_scrib2 },
        byte[] packet = new byte[1];
        packet[0] = 124;
        sendF2Command( packet, 100 );
        pause( 4000 );
    }

    /***
     * Asigna la potencia al emisor infrarojo de la F2
     *
     * @param pwm la potencia del emisor infrarojo(0 a 255)
     * @throws IOException
     */
    public synchronized void setIRPower( int pwm ) throws IOException {
        // { 120,  2,  0, fluke_set_ir_power }, // SetBrightLed modifica el valor pwd registrado
        byte[] packet = new byte[2];
        packet[0] = 120;
        packet[1] = (byte)(pwm & 0xFF);
        sendF2Command( packet, 100 );
    }

    /***
     * Obtiene el valor del sensor infrarojo de la F2.
     *
     * @return el valor del sensor infrarojo
     * @throws IOException
     */
    public synchronized int getIR() throws IOException {
        // {  85,  1,  0, fluke_get_ir_left },
        // {  86,  1,  0, fluke_get_ir_center }, Sólo existe éste
        // {  87,  1,  0, fluke_get_ir_right },
        byte[] packet = new byte[1];
        packet[0] = 86;
        sendF2Command( packet, 100 );
        return getUInt16Response();
    }

    /***
     * Asigna energia al LED del F2 apagándolo o encendiéndolo
     *
     * @param pwm el valor de energia a asignar al led (0 a 255)
     * @throws IOException
     */
    public synchronized void setBrightLed( int pwm ) throws IOException {
        // { 116,  1,  0, fluke_set_led_on },
        // { 117,  1,  0, fluke_set_led_off },
        // { 126,  2,  0, fluke_set_bright_led }, Controlar aquí el apagado y encendido
        byte[] packet = new byte[2];
        packet[0] = 126;
        packet[1] = (byte)(pwm & 0xFF);
        sendF2Command( packet, 100 );
    }

    /***
     * Posiciona uno de los servos conectados a la F2. SIGNAL en el borde derecho
     *
     * @param id el servo a controlar (0-3). 0=el de más abajo en la fluke
     * @param value el valor del movimiento (0=0° - 255=90°)
     * @throws IOException
     */
    public synchronized void setServo(int id, int value) throws IOException {
        // {  12,  3,  0, fluke_servo }
        byte[] packet = new byte[3];
        packet[0] = 12;
        packet[1] = (byte)(id & 0x03);
        packet[2] = (byte)(value & 0xFF);
        sendF2Command( packet, 100 );
    }


    /***
     * Envía el paquete de comando a la Fluke2. El timeout intern0 de la F2 es de 3000ms
     * La F2 no envía ningún elemento de regreso para efectos de sincronismo
     * Debe invocarse dentro de un mutex - lock sobre este objeto
     *
     * @param packet el paquete a enviar con el comando a la F2
     * @param pause tiempo para pausa despues de enviar el comando
     * @throws IOException
     */
    public void sendF2Command( byte[] packet, int pause ) throws IOException {
    	serial.write( packet );
        if( pause > 0 ) {
            pause( pause );
        }
    }
    
}

