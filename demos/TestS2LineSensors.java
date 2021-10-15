import rcr.scribbler2.S2Serial;

class TestS2LineSensors {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        for(int i=1; i <= 10; i++ ) {
            System.out.printf( "getLineEx 0 : %s\n", robot.getLineEx( 0, 128 ) );
            System.out.printf( "getLineEx 1 : %s\n", robot.getLineEx( 1, 128 ) );
            System.out.printf( "getAllLines : %s\n", robot.getAllLines() );
            System.out.printf( "getLeftLine : %s\n", robot.getLeftLine() );
            System.out.printf( "getRightLine: %s\n", robot.getRightLine() );
        }

        robot.close();
    }
}
