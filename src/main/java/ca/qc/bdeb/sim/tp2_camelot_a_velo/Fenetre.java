package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * Représente une fenêtre dans le jeu.
 * <p>
 * La fenêtre peut être intacte ou brisée (rouge ou verte selon l'abonnement de la maison).
 * Elle gère les collisions avec Camelot et met à jour l'argent du joueur en conséquence.
 */
public class Fenetre extends ObjetDuJeu implements Collisions {
    private final Image FENETRE; //Image de la fenêtre intacte
    private final Image FENETRE_ROUGE;
    private final Image FENETRE_VERTE;
    private Maison refMaison; //maison associé à la fenêtre
    private boolean brisee = false; //Indique si la fenêtre a déjà été brisée
    private Camelot refCamelot; //Référence au joueur Camelot pour ajuster l'argent

    public Fenetre(Point2D velocite, Point2D position){
        super(velocite, position);
        this.FENETRE_ROUGE = new Image ("fenetre-brisee-rouge.png");
        this.FENETRE_VERTE = new Image ("fenetre-brisee-vert.png");
        this.FENETRE = new Image("fenetre.png");
        image = FENETRE;
        this.acceleration = Point2D.ZERO;
    }

    /**
     * Dessine la fenêtre à l'écran si elle est visible.
     *
     * @param context Contexte graphique du Canvas
     * @param camera  Caméra pour convertir la position en coordonnées écran
     */
    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);

        if(coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());
    }

    /**
     * Retourne les bounds de la fenêtre pour la détection de collision.
     *
     * @return Rectangle2D représentant les limites de la fenêtre
     */
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(position.getX(), position.getY(), FENETRE.getWidth(), FENETRE.getHeight());
    }

    /**
     * Détecte si cette fenêtre entre en collision avec un autre objet.
     *
     * @param autreObj Objet avec lequel tester la collision
     * @return true si collision, false sinon
     */
    @Override
    public boolean collision(Collisions autreObj) {
        return Collisions.super.collision(autreObj);
    }

    /**
     * Action exécutée après qu'une collision a été détectée.
     * <p>
     * Met à jour l'image selon le type de maison et ajuste l'argent de Camelot.
     */
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
