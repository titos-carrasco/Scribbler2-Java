import rcr.scribbler2.S2Fluke2;

class TestF2Inner {
    public static void main( String [] args ) throws Exception {
    	S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        System.out.print( "getVersion: " );
        System.out.println( robot.getVersion() );
        System.out.print( "identifyRobot: " );
        System.out.println( robot.identifyRobot() );
        System.out.print( "getBattery: " );
        System.out.println( robot.getBattery() );
        System.out.print( "setForwardness: " );
        robot.setForwardness( S2Fluke2.SCRIBBLER_FORWARD );
        System.out.print( "setForwardness: " );
        robot.setForwardness( S2Fluke2.FLUKE_FORWARD );
        System.out.print( "setForwardness: " );
        robot.setForwardness( S2Fluke2.SCRIBBLER_FORWARD );
        System.out.print( "getErrors: " );
        System.out.println( robot.getErrors() );
        robot.resetScribbler();
        robot.close();
    }
}
