import rcr.robots.scribbler2.Scribbler2;
import rcr.robots.scribbler2.S2Inner;
import rcr.utils.Utils;

class TestS2Inner {
    public static void main( String [] args ) throws Exception {
        Scribbler2 robot = new Scribbler2(  "/dev/rfcomm2", 500 );
        S2Inner s2Inner = robot.getS2Inner();

        System.out.print( "getInfo: " );
        System.out.println( s2Inner.getInfo() );
        System.out.print( "getAllSensors: " );
        System.out.println( s2Inner.getAllSensors() );
        System.out.print( "setPass: " );
        System.out.println( s2Inner.setPass( "1234567898765432" ) );
        System.out.print( "getPass: " );
        System.out.println( s2Inner.getPass() );
        System.out.print( "setPass: " );
        System.out.println( s2Inner.setPass( "ABCDEFGHIHGFRDCB" ) );
        System.out.print( "getPass: " );
        System.out.println( s2Inner.getPass() );
        System.out.print( "setName: " );
        System.out.println( s2Inner.setName( "NAME1234" ) );
        System.out.print( "getName: " );
        System.out.println( s2Inner.getName() );
        System.out.print( "setName: " );
        System.out.println( s2Inner.setName( "LilyBot" ) );
        System.out.print( "getName: " );
        System.out.println( s2Inner.getName() );
        System.out.print( "getState: " );
        System.out.println( s2Inner.getState() );
        System.out.print( "setData: " );
        System.out.println( s2Inner.setData( new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 } ) );
        System.out.print( "getData: " );
        System.out.println( Utils.bytesToHex( s2Inner.getData() ) );
        System.out.print( "setData: " );
        System.out.println( s2Inner.setData( new byte[] { 8, 7, 6, 5, 4, 3, 2, 1 } ) );
        System.out.print( "getData: " );
        System.out.println( Utils.bytesToHex( s2Inner.getData() ) );
        System.out.print( "setSingleData: " );
        System.out.println( s2Inner.setSingleData( 4, 44 ) );
        System.out.print( "getData: " );
        System.out.println( Utils.bytesToHex( s2Inner.getData() ) );
        robot.close();
    }
}
