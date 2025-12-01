package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Classe qui s'occupe de montrer les particules chargées
 */
public class ParticuleChargee extends ObjetDuJeu {
    private final Color COULEUR;

    public ParticuleChargee(Point2D velocite, Point2D position) {
        super(velocite, position);

        //Choix aléatoire de la couleur
        Random rand = new Random();
        double teinte = rand.nextDouble(360);
        COULEUR = Color.hsb(teinte, 1, 1);

        this.velocite = Point2D.ZERO;
        this.acceleration = Point2D.ZERO;
        //Choix aléatoire de la couleur
        this.position = new Point2D(rand.nextDouble(Mur.longueurNiveau), rand.nextDouble(JeuCamelot.hauteur));
    }

    /**
     * Dessiner la particule lorsqu'elle est sur l'écran
     * @param context : GraphicsContext
     * @param camera : Camera
     */
    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);

        double RAYON = 10;
        if (coordoEcran.getX() + RAYON < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;

        context.setFill(COULEUR);
        context.fillOval(coordoEcran.getX(), coordoEcran.getY(), RAYON * 2, RAYON * 2);
    }


    public double getCHARGE() {
        return 900;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}
