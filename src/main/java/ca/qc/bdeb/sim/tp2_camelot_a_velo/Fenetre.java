package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Fenetre extends ObjetDuJeu{
    protected final double largeur = 159;
    protected final double hauteur = 130;

    public Fenetre(Point2D velocite, Point2D position ){
        super(velocite, position);
    }


    @Override
    public void draw(GraphicsContext context) {

    }
}
