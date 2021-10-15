package rcr.scribbler2;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.util.Formatter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import rcr.scribbler2.robot.ISerial;

/**
 * Clase para realizar operaciones sobre una puerta serial
 *
 * @author Roberto Carrasco
 */
public class Serial implements ISerial {
    /** la puerta serial en operación */
    private SerialPort serialPort = null;
    /** el objeto para lecturas */
    private InputStream in = null;
    /** el objeto para escrituras */
    private OutputStream out = null;

    /**
     * Construye un nuevo objeto para interactuar con una puerta serial y abre la conexión
     *
     * @param port el nombre de la puerta ("/dev/rfcomm1", "COM1")
     * @param bauds los bauds a operar ("2400", "9600")
     * @param timeout el timeout de cada lectura expresado en ms
     * @throws NoSuchPortException
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public Serial( String port, int bauds, int timeout ) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        super();
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier( port );
            serialPort = (SerialPort)portId.open( this.getClass().getName() + " " + port, 2000 );
            serialPort.setSerialPortParams( bauds, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE );
            serialPort.setFlowControlMode( SerialPort.FLOWCONTROL_NONE );
            serialPort.enableReceiveTimeout( timeout );
            //serialPort.enableReceiveThreshold(1);
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();

        } catch ( NoSuchPortException e ) {
            throw e;
        } catch ( PortInUseException e ) {
            throw e;
        } catch ( UnsupportedCommOperationException e ) {
            serialPort.close();
            throw e;
        }
        catch ( IOException e ) {
            close();
            throw e;
        }
    }

    /**
     * Cierra la conexión con la puerta serial
     */
    public void close() {
        if( in != null ) {
            try {
                in.close();
            } catch( IOException e ) {
            }
            in = null;
        }

        if( out != null ) {
            try {
                out.close();
            } catch( IOException e ) {
            }
            out = null;
        }

        if( serialPort != null ) {
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * Envía bytes por la puerta serial
     *
     * @param bytes los bytes a enviar
     * @throws IOException
     */
    public void write( byte[] bytes ) throws IOException {
        out.write( bytes );
        out.flush();
    }

    /**
     * Obtiene en numero aproximado de bytes disponibles en el stream
     *
     * @throws IOException
     * @return el numero estimado disponibles en el stream
     */
    public int available() throws IOException {
        return in.available();
    }

    /**
     * Lee bytes desde la puerta serial
     *
     * @param nbytes el número de bytes a leer
     * @throws IOException
     * @return los bytes leidos desde la puerta serial
     */
    public byte[] read( int nbytes ) throws IOException {
        byte bytes[] = new byte[nbytes];
        int pos = 0;
        while( pos < nbytes ){
            int b = in.read();
            if( b < 0 )
                throw new IOException();
            bytes[ pos++ ] = (byte)b;
        }
        return bytes;
    }

    /**
     * Lee una línea desde la puerta serial. La lectura es realizada caracter por caracter
     *
     * @param maxChars el tamaño máximo de la línea a leer
     * @throws IOException
     * @return la línea leida desde la puerta serial
     */
    public String readLine( int maxChars ) throws IOException {
        byte bytes[] = new byte[maxChars + 1];
        int pos = 0;
        while( pos < maxChars + 1 ){
            int b = in.read();
            if( b < 0 )
                throw new IOException();
            if( b == 10 )
                return new String( bytes, 0, pos );
            bytes[ pos++ ] = (byte)b;
        }
        throw new IOException();
    }

    /**
     * Durante los ms especificados lee los bytes disponibles descartandolos
     *
     * @param time los ms durante los cuales se leen bytes desde la puerta serial y se ignoran
     */
    public void ignoreInput( int time ) {
        int t = serialPort.getReceiveTimeout();
        serialPort.disableReceiveTimeout();
        long t2, t1 = System.currentTimeMillis();
        do {
            try {
            	if( available() > 0 )
            		in.read();
            	else
            		pause( 10 );
            } catch( Exception e ) {
            }
            t2 = System.currentTimeMillis();
        } while( (int)( t2 - t1 ) <= time );
        try {
        	serialPort.enableReceiveTimeout(t);
        } catch( Exception e ) {
        }
    }

    /**
     * Establece el estado de la linea DTR
     *
     * @param value especifica si se activa o no la linea DTR
     */
    public void setDTR( boolean value ) {
       serialPort.setDTR( value );
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
