package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Maison extends ObjetDuJeu{
    protected int adresse;
    protected boolean estAbonne;
    protected Fenetre fenetre;
    protected BoiteAuLettre boiteAuLettre;

    public Maison(Point2D velocite, Point2D position) {
        super(velocite, position);
    }

    @Override
    public void draw(GraphicsContext context) {

    }

    public boolean estAbonne() {
        Random rand = new Random();
        estAbonne = rand.nextBoolean();
        return estAbonne;
    }
}
