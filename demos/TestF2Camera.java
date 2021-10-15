import rcr.scribbler2.S2Fluke2;
import rcr.scribbler2.HF2Image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

@SuppressWarnings("serial")
class TestF2Camera extends Component {
    BufferedImage img;

    public TestF2Camera( byte[] image ) throws Exception {
        InputStream in = new ByteArrayInputStream( image );
        img = ImageIO.read( in );
    }

    public void paint( Graphics g ) {
        g.drawImage( img, 0, 0, null );
    }

    public Dimension getPreferredSize() {
        return new Dimension( img.getWidth(null), img.getHeight(null) );
    }

    public static void main( String [] args ) throws Exception {
        JFrame jf = new JFrame( "Cámara del Scribbler2" );
        jf.setSize( 640, 480 );
        jf.setLocationRelativeTo(null);

        jf.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.out.println( "Bye..." );
                    System.exit(0);
                }
            });
        jf.setVisible(true);

        S2Fluke2 robot = new S2Fluke2( "/dev/rfcomm2", 38400, 3500 );
        HF2Image image;

        System.out.println( "tamaño imagen" );
        robot.setPicSize( S2Fluke2.IMAGE_SMALL ); // IMAGE_LARGE, IMAGE_SMALL

        Component last = null, img;
        for( int i = 1; i <= 30; i++ ) {
            System.out.println( "pidiendo imagen" );
            image = robot.getImage( S2Fluke2.IMAGE_GRAYJPEG_FAST ); // IMAGE_JPEG_FAST, IMAGE_JPEG, IMAGE_GRAYJPEG
            System.out.print( "mostrando imagen " );
            System.out.println( image );
            img = new TestF2Camera( image.image );
            if( last != null ) {
                jf.remove(last);
            }
            jf.add( img );
            jf.pack();
            last = img;
        }
        System.out.println( "Cerrando conexión..." );
        robot.close();
        System.out.println( "Finalizado!!!" );
    }
}
