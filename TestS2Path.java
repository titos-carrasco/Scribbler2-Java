import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2Path;
import rcr.utils.Utils;

class TestS2Path {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2Path s2Path = robot.getS2Path();

        System.out.print( "beginPath: " );
        System.out.println( s2Path.beginPath( 15 ) );
        System.out.print( "setPosn -100, -200: " );
        System.out.println( s2Path.setPosn( -100, -200 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "setPosn 0, 0: " );
        System.out.println( s2Path.setPosn( 0, 0 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "setAngle -90: " );
        System.out.println( s2Path.setAngle( -90 ) );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "setAngle 90: " );
        System.out.println( s2Path.setAngle( 90 ) );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );

        System.out.print( "moveTo 0, 100: " );
        System.out.println( s2Path.moveTo( 0, 100 ) );
        System.out.print( "moveTo 0, -1000: " );
        System.out.println( s2Path.moveTo( 0, -1000) );
        System.out.print( "moveTo 0, 1000: " );
        System.out.println( s2Path.moveTo( 0, 1000 ) );

        System.out.print( "moveTo 0, 2000: " );
        System.out.println( s2Path.moveTo( 0, 2000 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "moveBy 0, 50: " );
        System.out.println( s2Path.moveBy( 0, 50 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "turnTo 45: " );
        System.out.println( s2Path.turnTo( 45 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "turnBy 45: " );
        System.out.println( s2Path.turnBy( 45 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "arcTo 100, 100, 45: " );
        System.out.println( s2Path.arcTo( 100, 100, 45 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );
        System.out.print( "arcBy 100 : " );
        System.out.println( s2Path.arcBy( 100, 100, 45 ) );
        System.out.print( "getPosn: " );
        System.out.println( s2Path.getPosn() );
        System.out.print( "getAngle: " );
        System.out.println( s2Path.getAngle() );

        System.out.print( "endPath: " );
        System.out.println( s2Path.endPath() );

        robot.close();
    }
}
