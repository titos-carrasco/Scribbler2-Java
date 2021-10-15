import rcr.scribbler2.S2Serial;

class TestS2LEDs {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        System.out.printf( "setLeftLed  : %s\n", robot.setLeftLed( true ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setLeftLed  : %s\n", robot.setLeftLed( false ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setCenterLed: %s\n", robot.setCenterLed( true ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setCenterLed: %s\n", robot.setCenterLed( false ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setRightLed : %s\n", robot.setRightLed( true ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setRightLed : %s\n", robot.setRightLed( false ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setAllLed   : %s\n", robot.setAllLed( 1, 1, 1 ) );
        S2Serial.pause( 2000 );
        System.out.printf( "setAllLed   : %s\n", robot.setAllLed( 0, 0, 0 ) );
        S2Serial.pause( 2000 );
        
        robot.close();
    }
}
