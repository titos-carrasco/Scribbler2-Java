package rcr.scribbler2.robot;

import java.io.IOException;

/**
 * Interface de acceso a un dispositivo de manera serial
 *
 * @author Roberto Carrasco
 */
public interface ISerial {
    /**
     * Cierra la conexión con la puerta serial
     */
    public abstract void close();

    /**
     * Envía bytes por la puerta serial
     *
     * @param bytes los bytes a enviar
     * @throws IOException
     */
    public abstract void write( byte[] bytes ) throws IOException;

    /**
     * Obtiene el numero aproximado de bytes disponibles en el stream
     *
     * @throws IOException
     * @return el numero estimado disponibles en el stream
     */
    public abstract int available() throws IOException;

    /**
     * Lee bytes desde la puerta serial
     *
     * @param nbytes el número de bytes a leer
     * @throws IOException
     * @throws SerialTimeoutException
     * @return los bytes leidos desde la puerta serial
     */
    public abstract byte[] read( int nbytes ) throws IOException;

    /**
     * Lee una línea desde la puerta serial. La lectura es realizada caracter por caracter
     *
     * @param maxChars el tamaño máximo de la línea a leer
     * @throws IOException
     * @throws SerialTimeoutException
     * @return la línea leida desde la puerta serial
     */
    public abstract String readLine( int maxChars ) throws IOException;

    /**
     * Durante los ms especificados lee los bytes disponibles descartandolos
     *
     * @param time los ms durante los cuales se leen bytes desde la puerta serial y se ignoran
     */
    public abstract void ignoreInput( int time );

    /**
     * Lee 1 byte desde la puerta serial como un uint8
     *
     * @throws IOException
     * @throws SerialTimeoutException
     * @return el byte leido como un unsigned integer
     */
    public default int readUInt8() throws IOException {
        byte[] b = read(1);
        return b[0] & 0xFF;
    }

    /**
     * Lee 2 bytes desde la puerta serial como un uint16
     *
     * @throws IOException
     * @throws SerialTimeoutException
     * @return los bytes leidos como un unsigned integer
     */
    public default int readUInt16() throws IOException {
        byte[] b = read(2);
        int n = b[0] & 0x000000FF;
        n = ( n<<8 ) | ( b[1] & 0xFF );
        return n;
    }

    /**
     * Lee 4 bytes desde la puerta serial como un uint32
     *
     * @throws IOException
     * @throws SerialTimeoutException
     * @return los bytes leidos como un unsigned long
     */
    public default long readUInt32() throws IOException {
        byte[] b = read(4);
        long n = b[0] & 0x000000FF;
        n = ( n << 8 ) | ( b[1] & 0xFF );
        n = ( n << 8 ) | ( b[2] & 0xFF );
        n = ( n << 8 ) | ( b[3] & 0xFF );
        return n;
    }

    /**
     * Lee 4 bytes desde la puerta serial como un int32
     *
     * @throws IOException
     * @throws SerialTimeoutException
     * @return los bytes leidos como un unsigned long
     */
    public default int readInt32() throws IOException {
        long n = readUInt32();
        return (int) n;
    }

}
