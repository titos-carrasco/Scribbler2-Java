package rcr.robots.scribbler2;

/**
 * Clase de apoyo para manejar las imagenes tomadas desde la cámara de la F2
 * <p>
 * Los elementos pueden ser accesadas directamente como <code>obj.width</code>,
 * <code>obj.height</code> y <code>obj.image</code>
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HF2Image {
    /** ancho en pixeles de la imagen */
    public final int width;
    /** alto en pixeles de la imagen */
    public final int height;
    /** la imagen como uno arreglo de bytes */
    public final byte[] image;

    /**
     * Construye un nuevo objeto para almacenar una imagen obtenida desde la F2
     *
     * @param width el ancho de la imagen
     * @param height el alto de la imagen
     * @param image la imagen representada como un arreglo de bytes
     */
    public HF2Image( int width, int height, byte[] image ) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Retorna un objeto del tipo String representando datos de la imagen
     *
     *  @return un String representando datos de la imagen
     */
    public String toString() {
        return "HF2Image(" + width + ", " + height + ", " + image.length + ")";
    }
}
