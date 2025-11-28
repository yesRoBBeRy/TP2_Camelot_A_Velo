package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fenetre extends ObjetDuJeu implements Collisions {
    private final Image FENETRE;
    private final Image FENETRE_ROUGE;
    private final Image FENETRE_VERTE;
    private Maison refMaison;
    private boolean brisee = false;
    private Camelot refCamelot;

    public Fenetre(Point2D velocite, Point2D position){
        super(velocite, position);
        this.FENETRE_ROUGE = new Image ("fenetre-brisee-rouge.png");
        this.FENETRE_VERTE = new Image ("fenetre-brisee-vert.png");
        this.FENETRE = new Image("fenetre.png");
        image = FENETRE;
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
        return new Rectangle2D(position.getX(), position.getY(), FENETRE.getWidth(), FENETRE.getHeight());
    }

    @Override
    public boolean collision(Collisions autreObj) {
        return Collisions.super.collision(autreObj);
    }

    @Override
    public void actionApresCollision() {
        if(brisee) return;
        boolean estAbonne = refMaison.getEstAbonne();
        if(estAbonne){
            image = FENETRE_ROUGE;
            refCamelot.ajouterArgent(-2);
        } else {
            image = FENETRE_VERTE;
            refCamelot.ajouterArgent(2);
        }
        brisee = true;
    }

    public void setRefMaison(Maison refMaison) {
        this.refMaison = refMaison;
    }
    public void setRefCamelot(Camelot refCamelot) {this.refCamelot = refCamelot;}
}
