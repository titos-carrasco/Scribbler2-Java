import rcr.scribbler2.S2Serial;

class TestS2Microphone {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        for(int i=1; i <= 50; i++ ) {
            System.out.printf( "getMicEnv: %s\n", robot.getMicEnv() );
        }

        robot.close();
    }
}
