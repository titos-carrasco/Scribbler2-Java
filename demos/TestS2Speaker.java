//import rcr.scribbler2.S2Serial;
import rcr.scribbler2.S2Fluke2;

class TestS2Speaker {
    public static void main( String [] args ) throws Exception {
        //S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );
        S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.printf( "setQuiet  : %s\n", robot.setQuiet() );
        System.out.printf( "setLoud   : %s\n", robot.setLoud() );
        System.out.printf( "setVolume : %s\n", robot.setVolume( 50 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 370,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 330,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 0,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 500, 523,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 370,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 330,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 0,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 500, 370,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 370,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 330,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 440,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 523,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 587,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 622,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 125, 659,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 63, 622,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 63, 659,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 63, 622,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 63, 659,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 63, 622,0 ) );
        System.out.printf( "setSpeaker: %s\n", robot.setSpeaker( 500, 659,0 ) );

        robot.close();
    }
}
