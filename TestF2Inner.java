import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.F2Inner;
import rcr.utils.Utils;

class TestF2Inner {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        F2Inner f2Inner = robot.getF2Inner();

        System.out.print( "getVersion: " );
        System.out.println( f2Inner.getVersion() );
        System.out.print( "identifyRobot: " );
        System.out.println( f2Inner.identifyRobot() );
        System.out.print( "getBattery: " );
        System.out.println( f2Inner.getBattery() );
        System.out.print( "setForwardness: " );
        f2Inner.setForwardness( f2Inner.SCRIBBLER_FORWARD );
        System.out.print( "setForwardness: " );
        f2Inner.setForwardness( f2Inner.FLUKE_FORWARD );
        System.out.print( "setForwardness: " );
        f2Inner.setForwardness( f2Inner.SCRIBBLER_FORWARD );
        System.out.print( "getErrors: " );
        System.out.println( f2Inner.getErrors() );
        f2Inner.resetScribbler();
        robot.close();
    }
}
