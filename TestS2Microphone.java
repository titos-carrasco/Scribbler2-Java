import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2Microphone;
import rcr.utils.Utils;

class TestS2Microphone {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2Microphone s2Microphone = robot.getS2Microphone();

        for(int i=1; i <= 50; i++ ) {
            System.out.print( "getMicEnv: " );
            System.out.println( s2Microphone.getMicEnv() );
        }
        robot.close();
    }
}
