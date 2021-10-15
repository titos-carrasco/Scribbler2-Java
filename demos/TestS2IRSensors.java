//import rcr.scribbler2.S2Serial;
import rcr.scribbler2.S2Fluke2;

class TestS2IRSensors {
    public static void main( String [] args ) throws Exception {
        //S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );
        S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        for(int i=1; i<= 30; i++ ) {
            System.out.printf( "getIRLeft : %s\n", robot.getIRLeft() );
            System.out.printf( "getIRRight: %s\n", robot.getIRRight() );
            System.out.printf( "getAllIR  : %s\n", robot.getAllIR() );
            System.out.printf( "getIrEx(0): %s\n", robot.getIrEx( 0, 128 ) );
            System.out.printf( "getIrEx(1): %s\n", robot.getIrEx( 1, 128 ) );
        }

        robot.close();
    }
}
