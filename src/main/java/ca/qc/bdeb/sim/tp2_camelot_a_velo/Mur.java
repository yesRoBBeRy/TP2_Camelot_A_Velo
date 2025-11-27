package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Mur {
    public static final double largeurEcran = JeuCamelot.largeur;
    public static final double hauteurEcran = JeuCamelot.hauteur;

    public static final Image image = new Image("brique.png");

    public static final double hauteurBrique = image.getHeight();
    public static final double largeurBrique = image.getWidth();

    public static int longueurNiveau = (int) (15600 + 1.5 * largeurEcran);

    public static void dessinerMur(GraphicsContext context, Camera camera) {
        for(int i = 0; i <= longueurNiveau; i += (int) largeurBrique) {
            double ecranX = (double)i - camera.getPositionCamera().getX();
            if(ecranX < -largeurBrique || ecranX > largeurEcran) continue;

            for(int j = 0; j <= hauteurEcran; j += (int) hauteurBrique) {
                context.drawImage(image, ecranX, j);
            }
        }
    }
}
