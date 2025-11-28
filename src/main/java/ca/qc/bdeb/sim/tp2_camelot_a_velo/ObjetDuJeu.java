package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class ObjetDuJeu implements DeboggageVisuel {
    protected Point2D velocite;
    protected Point2D position;
    protected Point2D acceleration;
    protected Image image;

    public ObjetDuJeu(Point2D velocite, Point2D position) {
        this.velocite = velocite;
        this.position = position;
        this.acceleration = new Point2D(0, 1500);
    }

    public abstract void draw(GraphicsContext context, Camera camera);

    public void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVelocite() {
        return velocite;
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    @Override
    public void deboggage(GraphicsContext gc, Camera camera) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(3);
        //Ligne de déboggage
        if (this instanceof Camelot) {
            var coordoCamelotEcran = camera.coordoEcran(position);
            gc.strokeLine(coordoCamelotEcran.getX(), 0, coordoCamelotEcran.getX(), JeuCamelot.hauteur);
        }
        //boite de collision
        if (image == null) return;
        var coordoEcran = camera.coordoEcran(position);
        gc.strokeRect(coordoEcran.getX(), coordoEcran.getY(), image.getWidth(), image.getHeight());


    }
    /*
    public void dessinChamp(GraphicsContext contexteGraphique, Camera camera, Point2D coordoEcran, Journal journal) {
        for (double x = 0; x < JeuCamelot.largeur; x += 50) {
            for (double y = 0; y < JeuCamelot.hauteur; y += 50) { //TODO changer jeucamlot.hauteur par HAUTEUR_ECRAN
                var positionMonde = new Point2D(x, y);
                var positionEcran = coordoEcran;
                // TODO: Seulement faire ça si la position (x, y) est visible dans l'écran
                if (p)
                Point2D force = journal.calculerChampElectromagnetique();
                UtilitairesDessins.dessinerVecteurForce(
                        positionEcran,
                        force,
                        contexteGraphique
                );
            }
        }

    }

     */


}
