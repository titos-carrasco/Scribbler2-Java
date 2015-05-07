package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con los elementos internos del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2Inner {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con los sensores IR del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2Inner( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Obtiene desde el S2 un String descriptivo del robot
     *
     * @return el string que describe al S2
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String getInfo() throws IOException, SerialTimeoutException {
        // _GET_INFO = 80  ' Response: "information on version robot, etc" 10 80
        // En algunos casos la F2 altera el primer caracter recibido desde el S2
        // cuando se hacen dos invocaciones sucesivas a este método
        // Esperar unos 100ms si se invoca dos veces
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 80 );
            s2.sendS2Command( packet, 0 );
            return s2.getLineResponse( 128 );
        }
    }

    /***
     * Obtiene desde el S2 todos sus sensores
     *
     * @return los sensores del robot
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors getAllSensors() throws IOException, SerialTimeoutException {
        // _GET_ALL          = 65  ' Response: leftIR rightIR LeftLightHighyte LeftLightLowByte CenterLightHighByte CenterLightLowByte RightLightHighByte RightLightLowByte LineLeft LineRight Stall 65
        // _GET_ALL_BINARY   = 66  ' Response: BinaryData 66, Notes: where the individual bits of BinaryData are: 0x000 IRLeft IRRight Stall LineLeft LineRight
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 65 );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Obtiene desde el S2 la clave almacenada
     *
     * @return un String con la clave almacenada en el robot
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String getPass() throws IOException, SerialTimeoutException {
        // _GET_PASS1 = 50  ' Respone: 16 bytes from flash memory
        // _GET_PASS2 = 51  ' Respone: 16 bytes from flash memory
        synchronized( s2 ) {
            String pass1, pass2;
            byte[] packet = s2.makeS2Packet( 50 );
            s2.sendS2Command( packet, 0 );
            pass1 = new String( s2.getBytesResponse( 8 ) );
            packet[0] = (byte)51;
            s2.sendS2Command( packet, 0 );
            pass2 = new String( s2.getBytesResponse( 8 ) );
            return pass1 + pass2;
        }
    }

    /***
     * Obtiene desde el S2 un String con su nombre
     *
     * @return el nombre asignado al robot
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public String getName() throws IOException, SerialTimeoutException {
        // _GET_NAME1 = 78  ' Response: char1 char2 char3 char4 char5 char6 char7 char8 78
        // _GET_NAME2 = 64  ' Response: char9 char10 char11 char12 char13 char14 char15 char16 87
        synchronized( s2 ) {
            String name1, name2;
            byte[] packet = s2.makeS2Packet( 78 );
            s2.sendS2Command( packet, 0 );
            name1 = new String( s2.getBytesResponse( 8 ) );
            packet[0] = (byte)64;
            s2.sendS2Command( packet, 0 );
            name2 = new String( s2.getBytesResponse( 8 ) );
            return name1 + name2;
        }
    }

    /***
     * Obtiene desde el S2 el estado almacenado en sus pines
     *
     * @return el estado almacenado en los pines del S2 mediante un objeto del tipo State
     * @see HS2State
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2State getState() throws IOException, SerialTimeoutException {
        // _GET_STATE = 77  ' Response: inPins outPins 77, Notes: inPins is the state of all the input pins (0-7), and outPins is the state of all the output pins (8-15) as defined in the I/O Pin Declarations.
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 77 );
            s2.sendS2Command( packet, 0 );
            return new HS2State( s2.getUInt8Response(), s2.getUInt8Response() );
        }
    }

    /***
     * Obtiene desde el S2 los 8 bytes de data del robot
     *
     * @return los 8 bytes de data del robot
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public byte[] getData() throws IOException, SerialTimeoutException {
        // _GET_DATA = 81  ' Response: data from flash memory 81
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 81 );
            s2.sendS2Command( packet, 0 );
            return s2.getBytesResponse( Scribbler2.DATA_LENGTH );
        }
    }

    /***
     * Asigna una nueva clave al S2 (la clave no tiene uso en el S2)
     *
     * @param pass la clave a asignar (16 caracteres)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setPass(String pass) throws IOException, SerialTimeoutException {
        // _SET_PASS1 = 55  ' Format: 55 PASS1 PASS2 ... PASS8
        // _SET_PASS2 = 56  ' Format: 56 PASS9 PASS2 ... PASS16
        synchronized( s2 ) {
            byte[] bPass = pass.getBytes();
            int i, j;
            byte[] packet = s2.makeS2Packet( 55 );
            for(i=0; i<bPass.length && i<Scribbler2.DATA_LENGTH; i++) {
                packet[i+1] = bPass[i];
            }
            for(; i<Scribbler2.DATA_LENGTH; i++) {
                packet[i+1] = 32;
            }
            s2.sendS2Command( packet, 0 );
            s2.getS2SensorsResponse();

            packet[0] = (byte)56;
            for(j=0; i<bPass.length && j<Scribbler2.DATA_LENGTH; i++, j++) {
                packet[j+1] = bPass[i];
            }
            for(; j<Scribbler2.DATA_LENGTH; j++) {
                packet[j+1] = 32;
            }
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Asigna un nuevo valor a uno de los bytes de datos del S2
     *
     * @param pos el byte a modifica (0 a 7)
     * @param data el valor a almacenar
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setSingleData(int pos, int data) throws IOException, SerialTimeoutException {
        // _SET_SINGLE_DATA = 96  ' Sets a single byte of data in flash memory'
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 96 );
            packet[1] = (byte)(pos & 0x07);
            packet[2] = (byte)(data & 0xFF);
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Almacena datos en la zona de datos del S2
     *
     * @param data los datos a almacenar (16 bytes)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setData(byte[] data) throws IOException, SerialTimeoutException {
        // _SET_DATA = 97  ' Sets 8 bytes of data in flash memory
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 97 );
            for( int i=0; i<data.length && i<Scribbler2.DATA_LENGTH; i++ ) {
                packet[i+1] = data[i];
            }
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Asigna un nuevo nombre al S2
     *
     * @param name el nombre nuevo a asignar al S2 (16 caracteres(
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setName(String name) throws IOException, SerialTimeoutException {
        // _SET_NAME1 = 110 ' Format: 110 char1 char2 char3 char4 char5 char6 char7 char8
        // _SET_NAME2 = 119 ' Format: 119 char9 char10 char11 char12 char13 char14 char15 char16
        synchronized( s2 ) {
            byte[] bName = name.getBytes();
            int i, j;
            byte[] packet = s2.makeS2Packet( 110 );
            for(i=0; i<bName.length && i<Scribbler2.DATA_LENGTH; i++) {
                packet[i+1] = bName[i];
            }
            for(; i<Scribbler2.DATA_LENGTH; i++) {
                packet[i+1] = 32;
            }
            s2.sendS2Command( packet, 0 );
            s2.getS2SensorsResponse();

            packet[0] = (byte)119;
            for(j=0; i<bName.length && j<Scribbler2.DATA_LENGTH; i++, j++) {
                packet[j+1] = bName[i];
            }
            for(; j<Scribbler2.DATA_LENGTH; j++) {
                packet[j+1] = 32;
            }
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

}
