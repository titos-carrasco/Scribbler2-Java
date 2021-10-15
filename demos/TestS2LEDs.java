//import rcr.scribbler2.S2Serial;
import rcr.scribbler2.S2Fluke2;

class TestS2LEDs {
    public static void main( String [] args ) throws Exception {
        //S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );
        S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.printf( "setLeftLed  : %s\n", robot.setLeftLed( true ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setLeftLed  : %s\n", robot.setLeftLed( false ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setCenterLed: %s\n", robot.setCenterLed( true ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setCenterLed: %s\n", robot.setCenterLed( false ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setRightLed : %s\n", robot.setRightLed( true ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setRightLed : %s\n", robot.setRightLed( false ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setAllLed   : %s\n", robot.setAllLed( 1, 1, 1 ) );
        S2Fluke2.pause( 2000 );
        System.out.printf( "setAllLed   : %s\n", robot.setAllLed( 0, 0, 0 ) );
        S2Fluke2.pause( 2000 );
        
        robot.close();
    }
}
