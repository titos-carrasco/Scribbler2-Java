//import rcr.scribbler2.S2Serial;
import rcr.scribbler2.S2Fluke2;

class TestS2Motors {
    public static void main( String [] args ) throws Exception {
        //S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );
        S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.printf( "getMotorStats       : %s\n", robot.getMotorStats() );
        System.out.printf( "getEncoders         : %s\n", robot.getEncoders( 1 ) );
        System.out.printf( "getStall            : %s\n", robot.getStall() );
        System.out.printf( "setMotors 100, -100 : %s\n", robot.setMotors( 100, -100) );
        S2Fluke2.pause( 3000 );
        System.out.printf( "setMotors -100, 100 : %s\n", robot.setMotors( -100, 100) );
        S2Fluke2.pause( 3000 );
        System.out.printf( "setMotorsOff        : %s\n", robot.setMotorsOff() );

        robot.close();
    }
}
