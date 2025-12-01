package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * Cette classe s'occupe de chacun des objets Journal avec la mise à jour de leur physique et du dessinage
 */
public class Journal extends ObjetDuJeu implements Collisions, Electromagnetique {
    private final Camelot CAMELOT;
    private final double MASSE;
    private final Point2D QT_DE_MOUVEMENT_Y_INIT = new Point2D(900, -900);
    private final Point2D QT_DE_MOUVEMENT_X_INIT = new Point2D(150, -1100);
    private boolean dejaLancer = false;
    private final Image IMG;
    private final List<ParticuleChargee> PARTICULES_CHARGEES;
    private boolean peutCollision = true;

    public Journal(Point2D velocite, Point2D position, double masse, Camelot camelot, List<ParticuleChargee> particules) {
        super(velocite, position);
        this.MASSE = masse;
        this.CAMELOT = camelot;
        this.IMG = new Image("journal.png");
        this.image = IMG;
        this.PARTICULES_CHARGEES = particules;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        if (!dejaLancer || image == null) return;
        var coordoEcran = camera.coordoEcran(position);

        if (coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;

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
        //Module de la velocite maximum
        double max = 1500;
        if (dejaLancer && image != null && velocite.magnitude() > max) {
            velocite = velocite.multiply(max / velocite.magnitude());
        }

    }

    /**
     * Méthode qui ajoute la quantité de mouvement au journal pour le lancé
     * @param zEnfonce si z est enfoncé
     * @param xEnfonce si x est enfoncé
     */
    public void actionLancer(boolean zEnfonce, boolean xEnfonce) {
        image = IMG;
        if (dejaLancer) {
            return;
        }

        Point2D qtDeMouvement;

        //Si z ou x ne sont pas enfoncé on retourne, car aucune action est requise.
        if (zEnfonce) {
            qtDeMouvement = QT_DE_MOUVEMENT_Y_INIT;
        } else if (xEnfonce) {
            qtDeMouvement = QT_DE_MOUVEMENT_X_INIT;
        } else return;

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            qtDeMouvement = qtDeMouvement.multiply(1.5);
        }

        //Formule pour calculer la vitesse initiale avec la quantité de mouvement
        velocite = CAMELOT.getVelocite();
        velocite = velocite.add(qtDeMouvement.multiply(1.0 / MASSE));
        dejaLancer = true;

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

    /**
     * @return L'accélération du champ électromagnétique
     */
    @Override
    public Point2D calculerAccelChampElect() {

        Point2D champTotal = champElectrique(PARTICULES_CHARGEES, position);
        //Calcul du vecteur force électrique
        double CHARGE = 900;
        Point2D forceElectrique = champTotal.multiply(CHARGE);

        //Appliquer la deuxième loi de Newton pour trouver l'accélération produit par le champ
        return forceElectrique.multiply(1 / MASSE);
    }

    /**
     * @param particules Liste de particules
     * @param position position du journal
     * @return Le champ à la position du journal
     */
    public Point2D champElectrique(List<ParticuleChargee> particules, Point2D position) {
        Point2D champTotal = Point2D.ZERO;

        for (ParticuleChargee particule : particules) {
            Point2D positionParticule = particule.getPosition();

            //Calcul champ
            Point2D vecteurDistance = position.subtract(positionParticule);

            double rayon = vecteurDistance.magnitude();
            rayon = Math.max(rayon, 1);

            double CONSTANTE_COULOMB = 90;
            double champElectromagnetique = CONSTANTE_COULOMB * Math.abs(particule.getCHARGE()) / Math.pow(rayon, 2);

            //Calcul du vecteur unitaire
            Point2D vecteurUnitaire = vecteurDistance.normalize();

            Point2D champActuel = vecteurUnitaire.multiply(champElectromagnetique);

            //Somme des champs pour trouver champ total
            champTotal = champTotal.add(champActuel);
        }

        return champTotal;
    }

    /**
     * Montrer les vecteurs du champ
     * @param gc : GraphicsContext
     * @param camera : Camera
     */
    public void deboggageChamp(GraphicsContext gc, Camera camera) {
        for (double x = 0; x < JeuCamelot.largeur + camera.getPositionCamera().getX(); x += 50) {
            double ecranX = x - camera.getPositionCamera().getX();
            if (ecranX < -50 || ecranX > JeuCamelot.largeur) continue;
            for (double y = 0; y < JeuCamelot.hauteur + camera.getPositionCamera().getY(); y += 50) {

                var positionMonde = new Point2D(x, y);
                var positionEcran = camera.coordoEcran(positionMonde);

                Point2D force = champElectrique(PARTICULES_CHARGEES, positionMonde);
                UtilitairesDessins.dessinerVecteurForce(positionEcran, force, gc);
            }
        }

    }

    public Image getIMG() {
        return IMG;
    }

    public boolean getPeutCollision() {
        return peutCollision;
    }
}