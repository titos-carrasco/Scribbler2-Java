package rcr.robots.scribbler2;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los sensores de distancia (IR) de la Fluke2 (F2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
 public class F2Camera {
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
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores IR del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected F2Camera( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Configura el tamaño de las imagenes a obtener desde el F2
     *
     * @param size tamaño de la image (IMAGE_SMALL o IMAGE_LARGE)
     * @throws IOException
     */
    public void setPicSize( int size ) throws IOException {
        // {  11,  2,  0, fluke_set_picsize },
        synchronized( s2 ) {
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
            s2.sendF2Command( packet, 100 );
            image_width = w;
            image_height = h;
        }
    }

    /***
     * Obtiene una imagen desde el F2
     *
     * @param mode el tipo de imagen a obtener: IMAGE_GRAYJPEG, IMAGE_GRAYJPEG_FAST, IMAGE_JPEG o IMAGE_JPEG_FAST
     * @return la imagen obtenida desde el F2 mediante un objeto del tipo HF2Image
     * @see HF2Image
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HF2Image getImage( int mode ) throws IOException, SerialTimeoutException {
        // { 135,  1,  0, fluke_get_jpeg_grey_header },
        // { 136,  2,  0, fluke_get_jpeg_grey_scan },
        // { 137,  1,  0, fluke_get_jpeg_color_header },
        // { 138,  2,  0, fluke_get_jpeg_color_scan },
        // Utilizar un timeout de al menos 3000ms
        synchronized( s2 ) {
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
            s2.sendF2Command( packet, 100 );

            int header_len = s2.getUInt8Response() + (s2.getUInt8Response() << 8);
            ByteArrayOutputStream image = new ByteArrayOutputStream();
            image.write( s2.getBytesResponse( header_len ), 0, header_len );

            packet = new byte[2];
            packet[0] = (byte)cmd;
            packet[1] = (byte)reg;
            s2.sendF2Command( packet, 100 );

            int last = 0;
            while( true ) {
                int b = s2.getUInt8Response();
                image.write( b );
                if( b == 0xD9 && last == 0xFF) {
                    s2.getUInt32Response();
                    s2.getUInt32Response();
                    s2.getUInt32Response();
                    break;
                }
                last = b;
            }
            return new HF2Image( image_width, image_height, image.toByteArray() );
        }
    }

     /***
     * @throws IOException
     */
   public void whiteBalanceOn() throws IOException {
        // { 129,  1,  0, fluke_white_balance_on },
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = (byte)129;
            s2.sendF2Command( packet, 100 );
        }
    }

     /***
     * @throws IOException
     */
    public void whiteBalanceOff() throws IOException {
        // { 130,  1,  0, fluke_white_balance_off },
        synchronized( s2 ) {
            byte[] packet = new byte[1];
            packet[0] = (byte)130;
            s2.sendF2Command( packet, 100 );
        }
    }

     /***
     * @throws IOException
     */
    public void setCameraParam( int addr, int value ) throws IOException {
        // { 131,  3,  0, fluke_set_camera_param },
        synchronized( s2 ) {
            byte[] packet = new byte[3];
            packet[0] = (byte)131;
            packet[1] = (byte)(addr & 0xFF);
            packet[2] = (byte)(value & 0xFF);
            s2.sendF2Command( packet, 100 );
        }
    }

     /***
     * @throws IOException
     */
    public void setWindow( int window, int xLow, int yLow, int xHigh, int yHigh, int xStep, int yStep) throws IOException {
        // { 127, 12,  0, fluke_set_window },
        synchronized( s2 ) {
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
            s2.sendF2Command( packet, 100 );
        }
    }

}
