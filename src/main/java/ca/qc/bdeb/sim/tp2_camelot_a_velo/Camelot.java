package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente le joueur "Camelot" dans le jeu.
 * Cette classe gère la physique, le dessin à l'écran, la gestion des journaux transportés,
 * ainsi que les interactions clavier pour le déplacement et le lancer de journaux.
 * Elle hérite de {@link ObjetDuJeu} pour bénéficier des mises à jour de position et de vélocité.
 */

public class Camelot extends ObjetDuJeu {
    private final List<Journal> journaux = new ArrayList<>();//Liste des journaux que Camelot transporte actuellement
    private final Image camelot1;
    private final Image camelot2;
    private int dernierTemps = 0;
    protected boolean toucheLeSol = true;
    private double dtDernierLancer = 0;//Temps écoulé depuis le dernier lancer de journal
    private int argentTotal;

    /**
     * Constructeur du joueur Camelot.
     *
     * @param velocite    Vélocité initiale du personnage
     * @param position    Position initiale du personnage
     * @param argentTotal Argent de départ du joueur
     */
    public Camelot(Point2D velocite, Point2D position, int argentTotal) {
        super(velocite, position);
        // Empêche d'aller plus vite que 200 ou 600
        this.velocite = velocite;
        this.camelot1 = new Image("camelot1.png");
        this.camelot2 = new Image("camelot2.png");
        image = camelot1;
        this.argentTotal = argentTotal;
    }

    /**
     * Dessine Camelot à l'écran en fonction de sa position et de la caméra.
     * Alterne entre deux images pour créer une animation simple.
     *
     * @param context Contexte graphique du Canvas
     * @param camera  Caméra pour convertir la position monde en coordonnées écran
     */
    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());
        // Animation pour alterner entre les deux images toutes les 0,25 secondes
        int temps = (int) (Math.floor(JeuCamelot.tempsTotal * 4) % 2);
        if (temps != dernierTemps) {
            dernierTemps = temps;
            if (temps == 0) {
                image = camelot1;
            } else {
                image = camelot2;
            }
        }
    }
    /**
     * Met à jour la physique de Camelot selon le temps écoulé et les entrées clavier.
     * Gère le mouvement horizontal, le saut, la gravité, la limitation de vitesse,
     * le double saut et le confinement dans la fenêtre de jeu.
     *
     * @param deltaTemps Temps écoulé depuis la dernière mise à jour (en secondes)
     */

    @Override
    public void updatePhysique(double deltaTemps) {
        //Déplacement horizontale
        gererDeplacementHorizontal();
        //Déplacement verticale (saut)
        gererDeplacementVertical(deltaTemps);
        //MAJ
        super.updatePhysique(deltaTemps);
        appliquerContrainteDeJeu();
        //Vérifie si un jouranl peut être lancé
        checkLancer(deltaTemps);
    }

    private void gererDeplacementHorizontal(){
        if (Input.isKeyPressed(KeyCode.LEFT)) {
            acceleration = new Point2D(-300, acceleration.getY()); //gauche
        } else if (Input.isKeyPressed(KeyCode.RIGHT)) {
            acceleration = new Point2D(300, acceleration.getY()); //droite
        } else {

            int vitesseDeBase = 400;
            if (Math.abs(velocite.getX()) > vitesseDeBase && !Input.isKeyPressed(KeyCode.LEFT) && !Input.isKeyPressed(KeyCode.RIGHT)) {

                // Si la vitesse en X est un minimum élevée, on décélère
                // (pas besoin de faire ça si le personnage est déja la vitesste initial)
                int signe = velocite.getX() > vitesseDeBase ? -1 : +1;
                acceleration = new Point2D(signe * 300, acceleration.getY());

            } else {
                // Assure que la vitesse soit exactement revenu à la base éventuellement
                velocite = new Point2D(vitesseDeBase, velocite.getY());
                acceleration = new Point2D(0, acceleration.getY());
            }

        }
    }

    private void gererDeplacementVertical(double deltaTemps){
        if ((Input.isKeyPressed(KeyCode.SPACE) || Input.isKeyPressed(KeyCode.UP)) && toucheLeSol) {
            velocite = new Point2D(velocite.getX(), -500); //sauter
            toucheLeSol = false;
        }
        //Gravite
        if (!toucheLeSol) {
            velocite = new Point2D(velocite.getX(), velocite.getY() + acceleration.getY() * deltaTemps);
        }
    }

    private void appliquerContrainteDeJeu(){
        final double VITESSE_MIN_X = 200;
        final double VITESSE_MAX_X = 600;
        //limite de la vitesse en x pour éviter de s'arrêter ou reculer
        if (velocite.getX() < VITESSE_MIN_X|| velocite.getX() > VITESSE_MAX_X) {
            velocite = new Point2D(Math.clamp(velocite.getX(), VITESSE_MIN_X, VITESSE_MAX_X), velocite.getY());
        }
        //Gestion du double saut
        if (position.getY() + camelot1.getHeight() >= JeuCamelot.hauteur) {
            toucheLeSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }
        //Contrainte de rester dans la fenêtre du jeu
        position = new Point2D(
                Math.max(0, position.getX()),
                Math.clamp(position.getY(), 0, JeuCamelot.hauteur - camelot1.getHeight())
        );
    }

    /**
     * Ajoute un journal à la liste des journaux transportés.
     *
     * @param journal Journal à ajouter
     */
    public void addJournaux(Journal journal) {
        journaux.add(journal);
    }

    /**
     * Retire et retourne le premier journal de la liste, ou null si aucun journal.
     *
     * @return Journal retiré ou null
     */
    public Journal removeJournal() {
        if (!journaux.isEmpty()) {
            return journaux.removeFirst();
        }

        return null;
    }

    /**
     * Vérifie si un lancer de journal peut être effectué selon les touches appuyées et
     * le temps écoulé depuis le dernier lancer.
     *
     * @param deltaTemps Temps écoulé depuis la dernière mise à jour
     */
    public void checkLancer(double deltaTemps) {
        dtDernierLancer += deltaTemps;
        double intervalleLancer = 0.5;

        boolean xEnfonce = false;
        boolean zEnfonce = false;
        if (Input.isKeyPressed(KeyCode.X)) {
            xEnfonce = true;
        } else if (Input.isKeyPressed(KeyCode.Z)) {
            zEnfonce = true;
        }
        if((xEnfonce || zEnfonce) && !journaux.isEmpty() && dtDernierLancer >= intervalleLancer) {
            removeJournal().actionLancer(zEnfonce, xEnfonce);
            dtDernierLancer = 0;
        }
    }

    public List<Journal> getJournaux() {
        return journaux;
    }

    //Ajoute de l'argent au total
    public void ajouterArgent(int paie){
        argentTotal += paie;
    }
    public int getArgentTotal() {
        return argentTotal;
    }
    public void clearJournaux() {journaux.clear();}
}