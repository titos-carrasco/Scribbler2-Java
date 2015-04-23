import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2LightSensors;
import rcr.utils.Utils;

class TestS2LightSensors {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2LightSensors s2LightSensors = robot.getS2LightSensors();

        for(int i=1; i <= 10; i++ ) {
            System.out.print( "getLeftLight: " );
            System.out.println( s2LightSensors.getLeftLight() );
            System.out.print( "getCenterLight: " );
            System.out.println( s2LightSensors.getCenterLight() );
            System.out.print( "getRightLed: " );
            System.out.println( s2LightSensors.getRightLed() );
            System.out.print( "getAllLights: " );
            System.out.println( s2LightSensors.getAllLights() );
        }
        robot.close();
    }
}
