package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Classe utilitaire représentant le mur du niveau.
 * <p>
 * Le mur est construit à partir d'une image de brique répétée pour couvrir
 * toute la hauteur de l'écran et une longueur supérieure à celle du niveau.
 */

public class Mur {
    public static final double LARGEUR_ECRAN = JeuCamelot.largeur;
    public static final double HAUTEUR_ECRAN = JeuCamelot.hauteur;

    public static final Image IMAGE = new Image("brique.png");

    public static final double HAUTEUR_BRIQUE = IMAGE.getHeight();
    public static final double LARGEUR_BRIQUE = IMAGE.getWidth();


    public static int longueurNiveau = (int) (15600 + 1.5 * LARGEUR_ECRAN);
    //Marge utilisée pour dépasser légèrement la fin du niveau
    public static int margeNiveau = longueurNiveau + 1000;

    public static void dessinerMur(GraphicsContext context, Camera camera) {
        // Parcourt horizontalement toute la marge du niveau
        for (int i = 0; i <= margeNiveau; i += (int) LARGEUR_BRIQUE) {
            double ecranX = (double) i - camera.getPositionCamera().getX();
            // Ignore les briques hors écran
            if (ecranX < -LARGEUR_BRIQUE || ecranX > LARGEUR_ECRAN) continue;
            // Parcourt verticalement l'écran pour dessiner la colonne de briques
            for (int j = 0; j <= HAUTEUR_ECRAN; j += (int) HAUTEUR_BRIQUE) {
                context.drawImage(IMAGE, ecranX, j);
            }
        }
    }
}
