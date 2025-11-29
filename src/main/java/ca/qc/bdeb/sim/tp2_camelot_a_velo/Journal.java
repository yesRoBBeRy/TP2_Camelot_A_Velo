package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Journal extends ObjetDuJeu implements Collisions, Electromagnetique {
    private final Camelot CAMELOT;
    private final double MASSE;
    private final Point2D QT_DE_MOUVEMENT_Y_INIT = new Point2D(900, -900);
    private final Point2D QT_DE_MOUVEMENT_X_INIT = new Point2D(150, -1100);
    private boolean dejaLancer = false;
    private final Image IMG;
    private final double CHARGE = 900;
    private final double CONSTANTE_COULOMB = 90;
    private final List<ParticuleChargee> PARTICULES_CHARGEES;
    private boolean peutCollision = true;

    public boolean getPeutCollision() {
        return peutCollision;
    }

    public Journal(Point2D velocite, Point2D position, double masse, Camelot camelot, List<ParticuleChargee> particules) {
        super(velocite, position);
        this.MASSE = masse;
        System.out.println(masse);
        this.CAMELOT = camelot;
        this.IMG = new Image("journal.png", false);
        this.image = IMG;
        this.PARTICULES_CHARGEES = particules;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        if (!dejaLancer || image == null) return;
        var coordoEcran = camera.coordoEcran(position);
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());

    }

    @Override
    public void updatePhysique(double deltaTemps) {
        if (!dejaLancer) {
            position = CAMELOT.getPosition();
            return;
        }
        acceleration = acceleration.add(calculerAccelChampElect());

        super.updatePhysique(deltaTemps);
        acceleration = new Point2D(0, 1500);
        //module de la velocite maximum
        double max = 1500;
        if (dejaLancer && image != null && velocite.magnitude() > max) {
            velocite = velocite.multiply(max / velocite.magnitude());
        }

    }

    public void actionLancer(boolean z, boolean x) {
        image = IMG;
        if (dejaLancer) {
            return;
        }

        Point2D qtDeMouvement;

        if (z) {
            qtDeMouvement = QT_DE_MOUVEMENT_Y_INIT;
        } else if (x) {
            qtDeMouvement = QT_DE_MOUVEMENT_X_INIT;
        } else return;

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            qtDeMouvement = qtDeMouvement.multiply(1.5);
        }
        System.out.println(velocite.magnitude());
        //vinitiale = ⃗vcamelot + pinitiale/m
        velocite = CAMELOT.getVelocite();
        velocite = velocite.add(qtDeMouvement.multiply(1.0 / MASSE));
        dejaLancer = true;
        System.out.println(velocite.magnitude());

    }


    public boolean isDejaLancer() {
        return dejaLancer;
    }

    @Override
    public Rectangle2D getBounds() {
        if (image == null) return Rectangle2D.EMPTY;
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public boolean collision(Collisions autreObj) {
        if (image == null) return false;
        if (!dejaLancer) return false;
        return Collisions.super.collision(autreObj);
    }

    @Override
    public void actionApresCollision() {
        velocite = Point2D.ZERO;
        acceleration = Point2D.ZERO;
        image = null;
        peutCollision = false;
    }

    @Override
    public Point2D calculerAccelChampElect() {

        Point2D champTotal = champElectrique(PARTICULES_CHARGEES, position);
        //Calcul du vecteur force électrique
        Point2D forceElectrique = champTotal.multiply(CHARGE);

        //Appliquer la deuxième loi de Newton pour trouver l'accélération produit par le champ
        return forceElectrique.multiply(1 / MASSE);
    }

    public Point2D champElectrique(List<ParticuleChargee> particules, Point2D position) {
        Point2D champTotal = Point2D.ZERO;

        for (ParticuleChargee particule : particules) {
            Point2D positionParticule = particule.getPosition();

            //Calcul champ
            Point2D vecteurDistance = position.subtract(positionParticule);

            double rayon = vecteurDistance.magnitude();
            rayon = Math.max(rayon, 1);

            double champElectromagnetique = CONSTANTE_COULOMB * Math.abs(particule.getCHARGE()) / Math.pow(rayon, 2);

            //Calcul du vecteur unitaire
            Point2D vecteurUnitaire = vecteurDistance.normalize();

            Point2D champActuel = vecteurUnitaire.multiply(champElectromagnetique);

            //Somme des champs pour trouver champ total
            champTotal = champTotal.add(champActuel);
        }

        return champTotal;
    }

    public void deboggageChamp(GraphicsContext gc, Camera camera) {
        for (double x = 0; x < JeuCamelot.largeur + camera.getPositionCamera().getX(); x += 50) {
            double ecranX = x - camera.getPositionCamera().getX();
            if(ecranX < -50 || ecranX > JeuCamelot.largeur) continue;
            for (double y = 0; y < JeuCamelot.hauteur + camera.getPositionCamera().getY(); y += 50) {

                var positionMonde = new Point2D(x, y);
                var positionEcran = camera.coordoEcran(positionMonde);
                /*
                if (estVisibleAEcran(positionEcran)) {
                    Point2D force = champElectrique(PARTICULES_CHARGEES, positionMonde);
                    UtilitairesDessins.dessinerVecteurForce(positionEcran, force, gc);
                }

                 */
                Point2D force = champElectrique(PARTICULES_CHARGEES, positionMonde);
                UtilitairesDessins.dessinerVecteurForce(positionEcran, force, gc);
            }
        }

    }

    private boolean estVisibleAEcran(Point2D positionEcran) {
        return positionEcran.getX() < 0 || positionEcran.getX() > JeuCamelot.largeur;
        /*
        return positionEcran.getX() >= 0 && positionEcran.getX() <= JeuCamelot.largeur
                && positionEcran.getY() >= 0 && positionEcran.getY() <= JeuCamelot.hauteur;


                for(int i = 0; i <= margeNiveau; i += (int) LARGEUR_BRIQUE) {
            double ecranX = (double)i - camera.getPositionCamera().getX();
            if(ecranX < -LARGEUR_BRIQUE || ecranX > LARGEUR_ECRAN) continue;

            for(int j = 0; j <= HAUTEUR_ECRAN; j += (int) HAUTEUR_BRIQUE) {
                context.drawImage(IMAGE, ecranX, j);
            }
        }
         */
    }


    public Image getIMG() {
        return IMG;
    }
}