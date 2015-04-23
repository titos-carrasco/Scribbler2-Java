import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2Speaker;
import rcr.utils.Utils;

class TestS2Speaker {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2Speaker s2Speaker = robot.getS2Speaker();

        System.out.print( "setQuiet: " );
        System.out.println( s2Speaker.setQuiet() );
        System.out.print( "setLoud: " );
        System.out.println( s2Speaker.setLoud() );
        System.out.print( "setVolume: " );
        System.out.println( s2Speaker.setVolume( 50 ) );
        System.out.print( "setSpeaker: " );
        System.out.println( s2Speaker.setSpeaker( 2000, 440, 880 ) );
        System.out.print( "setSpeaker: " );
        System.out.println( s2Speaker.setSpeaker( 2000, 650, 0 ) );
        robot.close();
    }
}
