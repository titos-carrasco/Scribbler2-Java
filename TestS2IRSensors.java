import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2IRSensors;
import rcr.utils.Utils;

class TestS2IRSensors {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2IRSensors s2IRSensors = robot.getS2IRSensors();

        for(int i=1; i<= 10; i++ ) {
            System.out.print( "getIRLeft: " );
            System.out.println( s2IRSensors.getIRLeft() );
            System.out.print( "getIRRight: " );
            System.out.println( s2IRSensors.getIRRight() );
            System.out.print( "getAllIR: " );
            System.out.println( s2IRSensors.getAllIR() );
            System.out.print( "getIrEx(0): " );
            System.out.println( s2IRSensors.getIrEx( 0, 128 ) );
            System.out.print( "getIrEx(1): " );
            System.out.println( s2IRSensors.getIrEx( 1, 128 ) );
        }
        robot.close();
    }
}
