package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BoiteAuLettre extends ObjetDuJeu{
    protected Image boiteRouge;
    protected Image boiteVerte;
    protected Image boite;

    public BoiteAuLettre(Point2D velocite, Point2D position) {
        super(velocite, position);
        this.boiteRouge = new Image("boite-aux-lettres-rouge.png");
        this.boiteVerte = new Image("boite-aux-lettres-verte.png");
        this.boite = new Image("boite-aux-lettres.png");
        image.setImage(boite);
    }

    @Override
    public void draw(GraphicsContext context) {

    }
}
