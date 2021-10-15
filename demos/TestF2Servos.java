import rcr.scribbler2.S2Fluke2;

class TestF2Servos {
    public static void main( String [] args ) throws Exception {
    	S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );

        int id = 0;
        for(int value=0; value<=255; value+=5) {
            System.out.printf("setServo(%d, %d)\n", id, value);
            robot.setServo(id, value);
            S2Fluke2.pause(60);
        }
        robot.close();
    }
}
