import rcr.scribbler2.S2Serial;

class TestS2Path {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        System.out.printf( "beginPath         : %s\n", robot.beginPath( 15 ) );
        System.out.printf( "setPosn -100, -100: %s\n", robot.setPosn( -100, -100 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "setAngle -90      : %s\n", robot.setAngle( -90 ) );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "setAngle 90       : %s\n", robot.setAngle( 90 ) );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "setPosn 0, 0      : %s\n", robot.setPosn( 0, 0 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );

        System.out.printf( "moveTo 0, 100     : %s\n", robot.moveTo( 0, 100 ) );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "moveTo 0, -100    : %s\n", robot.moveTo( 0, -100) );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "moveTo 0, 100     : %s\n", robot.moveTo( 0, 100 ) );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );

        System.out.printf( "moveTo 0, 200     : %s\n", robot.moveTo( 0, 200 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "moveBy 0, 50      : %s\n", robot.moveBy( 0, 50 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "turnTo 45         : %s\n", robot.turnTo( 45 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "turnBy 45         : %s\n", robot.turnBy( 45 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "arcTo 100, 100, 45: %s\n", robot.arcTo( 100, 100, 45 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );
        System.out.printf( "arcBy 100, 100, 45: %s\n", robot.arcBy( 100, 100, 45 ) );
        System.out.printf( "getPosn           : %s\n", robot.getPosn() );
        System.out.printf( "getAngle          : %s\n", robot.getAngle() );

        System.out.printf( "endPath           : %s\n", robot.endPath() );

        robot.close();
    }
}
