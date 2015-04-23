import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2Motors;
import rcr.utils.Utils;

class TestS2Motors {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2Motors s2Motors = robot.getS2Motors();

        System.out.print( "getMotorStats: " );
        System.out.println( s2Motors.getMotorStats() );
        System.out.print( "getEncoders: " );
        System.out.println( s2Motors.getEncoders( 1 ) );
        System.out.print( "getStall: " );
        System.out.println( s2Motors.getStall() );
        System.out.print( "setMotors 100, -100 : " );
        System.out.println( s2Motors.setMotors( 100, -100) );
        Utils.pause( 3000 );
        System.out.print( "setMotors -100, 100 : " );
        System.out.println( s2Motors.setMotors( -100, 100) );
        Utils.pause( 3000 );
        System.out.print( "setMotorsOff: " );
        System.out.println( s2Motors.setMotorsOff() );
        robot.close();
    }
}
