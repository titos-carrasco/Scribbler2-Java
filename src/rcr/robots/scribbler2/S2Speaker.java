package rcr.robots.scribbler2;

import java.io.IOException;
import rcr.utils.SerialTimeoutException;

/**
 * Clase para interactuar con el parlante del Scribbler2 (S2)
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class S2Speaker {
    /** El objeto que representa al Scribbler2 */
    private Scribbler2 s2;

    /**
     * Construye el objeto que interactua con el parlante del S2
     *
     * @param s2 el objeto que representa al Scribbler2
     */
    protected S2Speaker( Scribbler2 s2 ) {
        this.s2 = s2;
    }

    /***
     * Apaga el parlante del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setQuiet() throws IOException, SerialTimeoutException {
        // _SET_QUIET = 112
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 112 );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Activa el parlante del S2
     *
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setLoud() throws IOException, SerialTimeoutException {
        // _SET_LOUD = 111
        synchronized( s2 ) {
            byte[] packet = s2.makeS2Packet( 111 );
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Establece el nivel de volumen del parlante del S2
     *
     * @param volume el nivel de volumen del parlante del S2 (0 a 100 - porcentaje)
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setVolume(int volume) throws IOException, SerialTimeoutException {
        // _SET_VOLUME = 160 'Format 160 volume
        synchronized( s2 ) {
            volume = volume & 0xFF;
            if( volume > 100 ) {
                volume= 100;
            }
            byte[] packet = s2.makeS2Packet( 160 );
            packet[1] = (byte)volume;
            s2.sendS2Command( packet, 0 );
            return s2.getS2SensorsResponse();
        }
    }

    /***
     * Emite un sonido a través del parlante del S2. Limitamos a 2500ms la duración
     * del sonido dado que la Fluke2 posee un timeout de 3000ms que intefiere con esta operación
     *
     * @param duration duración del sonido a emitir en ms (no superior a 2500ms)
     * @param freq1 frecuencia en Hz delsonido
     * @param freq2 frecuencia en Hz de un segundo sonido emitido de manera simultanea
     * @return el estado de los sensores mediante un objeto del tipo Sensors
     * @see HS2Sensors
     * @throws IOException
     * @throws SerialTimeoutException
     */
    public HS2Sensors setSpeaker(int duration, int freq1, int freq2) throws IOException, SerialTimeoutException {
        // _SET_SPEAKER   = 113 ' Format: 113 DurationHighByte DurationLowByte FreqHighByte FreqLowByte
        // _SET_SPEAKER_2 = 114 ' Format: 114 DurationHighByte DurationLowByte Freq1HighByte Freq1LowByte Freq2HighByte Freq2LowByte
        //
        // El timeout en la F2 es de 3000ms lo que genera problemas de sync
        // Limitamos a 2500s la duración de cualquier sonido
        synchronized( s2 ) {
            duration = duration & 0xFFFF;
            if( duration > 2500 ) {
                duration = 2500;
            }
            freq1 = freq1 & 0xFFFF;
            freq2 = freq2 & 0xFFFF;
            byte[] packet = s2.makeS2Packet( 114 );
            packet[1] = (byte)((duration >>8) & 0xFF);
            packet[2] = (byte)(duration & 0xFF);
            packet[3] = (byte)((freq1 >> 8) & 0xFF);
            packet[4] = (byte)(freq1 & 0xFF);
            packet[5] = (byte)((freq2 >> 8) & 0xFF);
            packet[6] = (byte)(freq2 & 0xFF);
            s2.sendS2Command( packet, duration );
            return s2.getS2SensorsResponse();
        }
    }

}
