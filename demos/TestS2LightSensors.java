import rcr.scribbler2.S2Serial;

class TestS2LightSensors {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        for(int i=1; i <= 10; i++ ) {
            System.out.printf( "getLeftLight  : %s\n", robot.getLeftLight() );
            System.out.printf( "getCenterLight: %s\n", robot.getCenterLight() );
            System.out.printf( "getRightLight : %s\n", robot.getRightLight() );
            System.out.printf( "getAllLights  : %s\n", robot.getAllLights() );
        }

        robot.close();
    }
}
