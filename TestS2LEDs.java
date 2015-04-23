import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2LEDs;
import rcr.utils.Utils;

class TestS2LEDs {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2LEDs s2LEDs = robot.getS2LEDs();

        System.out.print( "setLeftLed: " );
        System.out.println( s2LEDs.setLeftLed( true ) );
        Utils.pause( 2000 );
        System.out.print( "setLeftLed: " );
        System.out.println( s2LEDs.setLeftLed( false ) );
        Utils.pause( 2000 );
        System.out.print( "setCenterLed: " );
        System.out.println( s2LEDs.setCenterLed( true ) );
        Utils.pause( 2000 );
        System.out.print( "setCenterLed: " );
        System.out.println( s2LEDs.setCenterLed( false ) );
        Utils.pause( 2000 );
        System.out.print( "setRightLed: " );
        System.out.println( s2LEDs.setRightLed( true ) );
        Utils.pause( 2000 );
        System.out.print( "setRightLed: " );
        System.out.println( s2LEDs.setRightLed( false ) );
        Utils.pause( 2000 );
        System.out.print( "setAllLed: " );
        System.out.println( s2LEDs.setAllLed( 1, 1, 1 ) );
        Utils.pause( 2000 );
        System.out.print( "setAllLed: " );
        System.out.println( s2LEDs.setAllLed( 0, 0, 0 ) );
        Utils.pause( 2000 );
        robot.close();
    }
}
