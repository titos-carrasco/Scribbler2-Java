import rcr.scribbler2.S2Fluke2;

class TestF2IRSensors {
    public static void main( String [] args ) throws Exception {
    	S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.println( "setIRPower: " );
        robot.setIRPower( 255 );

        for(int i = 0; i < 20; i++ ){
        System.out.print( "getIR: " );
        System.out.println( robot.getIR() );
        }

        robot.close();
    }
}
