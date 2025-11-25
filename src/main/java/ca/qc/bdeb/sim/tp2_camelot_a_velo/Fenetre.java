package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fenetre extends ObjetDuJeu implements Collisions {
    private final Image fenetre;
    private final Image fenetreR;
    private final Image fenetreV;

    public Fenetre(Point2D velocite, Point2D position ){
        super(velocite, position);
        this.fenetreR = new Image ("fenetre-brisee-rouge.png");
        this.fenetreV = new Image ("fenetre-brisee-vert.png");
        this.fenetre = new Image("fenetre.png");
        image = fenetre;
        this.acceleration = Point2D.ZERO;
    }


    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);

        if(coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());
    }


    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(position.getX(), position.getY(), fenetre.getWidth(), fenetre.getHeight());
    }

    @Override
    public boolean collision(Collisions autreObj) {
        return Collisions.super.collision(autreObj);
    }
}
