package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class Camelot extends ObjetDuJeu {
    private final List<Journal> journaux = new ArrayList<>();
    private final Image camelot1;
    private final Image camelot2;
    private int dernierTemps = 0;
    protected boolean toucheLeSol = true;
    private double dtDernierLancer = 0;
    private int argentTotal = 0;

    public Camelot(Point2D velocite, Point2D position) {
        super(velocite, position);
        // Empêche d'aller plus vite que 200 ou 600
        this.velocite = velocite;
        this.camelot1 = new Image("camelot1.png");
        this.camelot2 = new Image("camelot2.png");
        image = camelot1;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());
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

    @Override
    public void updatePhysique(double deltaTemps) {
        //Déplacement en X
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
        //Déplacement en Y
        if ((Input.isKeyPressed(KeyCode.SPACE) || Input.isKeyPressed(KeyCode.UP)) && toucheLeSol) {
            velocite = new Point2D(velocite.getX(), -500); //sauter
            toucheLeSol = false;
        }

        //Gravite
        if (!toucheLeSol) {
            velocite = new Point2D(velocite.getX(), velocite.getY() + acceleration.getY() * deltaTemps);
        }
        //MAJ
        super.updatePhysique(deltaTemps);

        //limite de la vitesse en x pour éviter de s'arrêter ou reculer
        if (velocite.getX() < 200|| velocite.getX() > 600) {
            velocite = new Point2D(Math.clamp(velocite.getX(), 200, 600), velocite.getY());
        }




        //Gestion du double saut
        if (position.getY() + camelot1.getHeight() >= JeuCamelot.hauteur) {
            toucheLeSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }
        //Contrainte rester dans la fenêtre
        position = new Point2D(
                Math.max(0, position.getX()),
                Math.clamp(position.getY(), 0, JeuCamelot.hauteur - camelot1.getHeight())
        );
        checkLancer(deltaTemps);
    }

    //Journaux transporté
    public void addJournaux(Journal journal) {
        journaux.add(journal);
    }

    public Journal removeJournal() {
        if (!journaux.isEmpty()) {
            return journaux.removeFirst();
        }

        return null;
    }

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

    public void ajouterArgent(int paie){
        argentTotal += paie;
    }
    public int getArgentTotal() {
        return argentTotal;
    }
    public void clearJournaux() {journaux.clear();}
}