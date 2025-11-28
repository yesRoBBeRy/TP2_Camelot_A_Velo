package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class ParticuleChargee extends ObjetDuJeu{
    private final double RAYON = 10;
    private final Color COULEUR;
    private final double CHARGE = 90;
    public ParticuleChargee(Point2D velocite, Point2D position) {
        super(velocite, position);

        Random rand = new Random();
        double teinte = rand.nextDouble(360);
        COULEUR = Color.hsb(teinte, 1, 1);
        this.velocite = Point2D.ZERO;
        this.acceleration = Point2D.ZERO;
        this.position = new Point2D(rand.nextDouble(Mur.longueurNiveau), rand.nextDouble(JeuCamelot.hauteur));
        System.out.println(this.position);
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);

        if(coordoEcran.getX() + RAYON < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;

        context.setFill(COULEUR);
        context.fillOval(coordoEcran.getX(), coordoEcran.getY(), RAYON * 2, RAYON * 2);
    }


    public double getCHARGE() {
        return CHARGE;
    }
}
