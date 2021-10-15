import rcr.scribbler2.S2Fluke2;

class TestF2LEDs {
    public static void main( String [] args ) throws Exception {
    	S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.println( "setBrightLed: " );
        robot.setBrightLed( 255 );
        S2Fluke2.pause( 2000 );
        System.out.println( "setBrightLed: " );
        robot.setBrightLed( 128 );
        S2Fluke2.pause( 2000 );
        System.out.println( "setBrightLed: " );
        robot.setBrightLed( 0 );

        robot.close();
    }
}
