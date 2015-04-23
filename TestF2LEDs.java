import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.F2LEDs;
import rcr.utils.Utils;

class TestF2LEDs {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        F2LEDs f2LEDs = robot.getF2LEDs();

        System.out.println( "setBrightLed: " );
        f2LEDs.setBrightLed( 255 );
        Utils.pause( 2000 );
        System.out.println( "setBrightLed: " );
        f2LEDs.setBrightLed( 128 );
        Utils.pause( 2000 );
        System.out.println( "setBrightLed: " );
        f2LEDs.setBrightLed( 0 );

        robot.close();
    }
}
