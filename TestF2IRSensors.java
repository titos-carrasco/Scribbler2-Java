import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.F2IRSensors;
import rcr.utils.Utils;

class TestF2IRSensors {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        F2IRSensors f2IRSensors = robot.getF2IRSensors();

        System.out.println( "setIRPower: " );
        f2IRSensors.setIRPower( 255 );

        for(int i = 0; i < 20; i++ ){
        System.out.print( "getIR: " );
        System.out.println( f2IRSensors.getIR() );
        }

        robot.close();
    }
}
