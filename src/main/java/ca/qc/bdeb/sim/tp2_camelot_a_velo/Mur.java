package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Mur {
    public static final double LARGEUR_ECRAN = JeuCamelot.largeur;
    public static final double HAUTEUR_ECRAN = JeuCamelot.hauteur;

    public static final Image IMAGE = new Image("brique.png");

    public static final double HAUTEUR_BRIQUE = IMAGE.getHeight();
    public static final double LARGEUR_BRIQUE = IMAGE.getWidth();

    public static int longueurNiveau = (int) (15600 + 1.5 * LARGEUR_ECRAN);
    public static int margeNiveau = longueurNiveau + 1000;

    public static void dessinerMur(GraphicsContext context, Camera camera) {
        for(int i = 0; i <= margeNiveau; i += (int) LARGEUR_BRIQUE) {
            double ecranX = (double)i - camera.getPositionCamera().getX();
            if(ecranX < -LARGEUR_BRIQUE || ecranX > LARGEUR_ECRAN) continue;

            for(int j = 0; j <= HAUTEUR_ECRAN; j += (int) HAUTEUR_BRIQUE) {
                context.drawImage(IMAGE, ecranX, j);
            }
        }
    }
}
