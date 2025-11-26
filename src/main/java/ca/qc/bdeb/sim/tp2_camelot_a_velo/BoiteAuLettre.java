package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BoiteAuLettre extends ObjetDuJeu implements Collisions{
    protected Image boiteRouge;
    protected Image boiteVerte;
    protected Image boite;

    public BoiteAuLettre(Point2D velocite, Point2D position) {
        super(velocite, position);

        this.boiteRouge = new Image("boite-aux-lettres-rouge.png");
        this.boiteVerte = new Image("/boite-aux-lettres-vert.png");
        this.boite = new Image("/boite-aux-lettres.png");
        image = boite;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);

        if(coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());
    }


    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public boolean collision(Collisions autreObj) {
        return Collisions.super.collision(autreObj);
    }

    @Override
    public void actionApresCollision() {

    }
}
