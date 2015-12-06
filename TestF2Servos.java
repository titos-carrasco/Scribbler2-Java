import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.F2Servos;
import rcr.utils.Utils;

class TestF2Servos {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        F2Servos f2Servos = robot.getF2Servos();

        int id = 0;
        for(int value=0; value<=255; value+=5) {
            System.out.printf("setServo(%d, %d)\n", id, value);
            f2Servos.setServo(id, value);
            Utils.pause(60);
        }
        robot.close();
    }
}
