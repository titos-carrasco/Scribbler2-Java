import rcr.scribbler2.S2Serial;

class TestS2Inner {
    public static void main( String [] args ) throws Exception {
        S2Serial robot = new S2Serial( "/dev/ttyUSB0", 38400, 3500, false );

        System.out.printf( "getInfo      : %s\n", robot.getInfo() );
        System.out.printf( "getAllSensors: %s\n", robot.getAllSensors() );
        System.out.printf( "setPass      : %s\n", robot.setPass( "1234567898765432" ) );
        System.out.printf( "getPass      : %s\n", robot.getPass() );
        System.out.printf( "setPass      : %s\n", robot.setPass( "ABCDEFGHIJKLMNOP" ) );
        System.out.printf( "getPass      : %s\n", robot.getPass() );
        System.out.printf( "setName      : %s\n", robot.setName( "NAME1234" ) );
        System.out.printf( "getName      : %s\n", robot.getName() );
        System.out.printf( "setName      : %s\n", robot.setName( "TitosBot" ) );
        System.out.printf( "getName      : %s\n", robot.getName() );
        System.out.printf( "getState     : %s\n", robot.getState() );
        System.out.printf( "setData      : %s\n", robot.setData( new byte[] { 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01 } ) );
        System.out.printf( "getData      : %s\n", S2Serial.bytesToHex( robot.getData() ) );
        System.out.printf( "setData      : %s\n", robot.setData( new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 } ) );
        System.out.printf( "getData      : %s\n", robot.setSingleData( 4, 0xFF ) );
        System.out.printf( "getData      : %s\n", S2Serial.bytesToHex( robot.getData() ) );

        robot.close();
    }
}
