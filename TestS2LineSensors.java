import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2LineSensors;
import rcr.utils.Utils;

class TestS2LineSensors {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2LineSensors s2LineSensors = robot.getS2LineSensors();

        for(int i=1; i <= 10; i++ ) {
            System.out.print( "getLineEx 0: " );
            System.out.println( s2LineSensors.getLineEx( 0, 128 ) );
            System.out.print( "getLineEx 1: " );
            System.out.println( s2LineSensors.getLineEx( 1, 128 ) );
            System.out.print( "getAllLines: " );
            System.out.println( s2LineSensors.getAllLines() );
            System.out.print( "getLeftLine: " );
            System.out.println( s2LineSensors.getLeftLine() );
            System.out.print( "getRightLine: " );
            System.out.println( s2LineSensors.getRightLine() );
        }
        robot.close();
    }
}
