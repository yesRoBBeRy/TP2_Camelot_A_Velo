package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;
import java.util.Random;

public class Maison extends ObjetDuJeu{
    protected int adresse;
    protected boolean estAbonne;
    protected Fenetre[] fenetres;
    protected BoiteAuLettre boiteAuLettre;

    public Maison(Point2D velocite, Point2D position, int adresse) {
        super(velocite, position);
        Random rand = new Random();
        this.estAbonne = rand.nextBoolean();
        this.adresse = adresse;
        this.fenetres = new Fenetre[rand.nextInt(2)];
        for (int i = 0; i < fenetres.length; i++) {
            fenetres[i] = new Fenetre(new Point2D(0, 0), new Point2D(0, 0));
        }

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        position = position.add(camera.getPositionCamera());
    }


}
