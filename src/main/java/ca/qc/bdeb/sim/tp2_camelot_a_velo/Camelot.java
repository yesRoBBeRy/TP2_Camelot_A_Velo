package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.LinkedList;
import java.util.List;

public class Camelot extends ObjetDuJeu {
    private LinkedList<Journal> journaux = new LinkedList<>();
    private final Image camelot1;
    private final Image camelot2;
    private int dernierTemps = 0;
    private final int vitesseDeBase = 0; // ******remmettre a 400 lors des vrai tests******
    protected boolean toucheLeSol = true;
    private double dtDernierLancer = 0;

    public Camelot(Point2D velocite, Point2D position) {
        super(velocite, position);
        // Empêche d'aller plus vite que 200 ou 600
        this.velocite = new Point2D(
                Math.clamp(velocite.getX(), 200, 600),
                velocite.getY()
        );
        this.camelot1 = new Image("camelot1.png");
        image.setImage(camelot1);
        this.camelot2 = new Image("camelot2.png");
    }

    @Override
    public void draw(GraphicsContext context) {
        image.setX(position.getX());
        image.setY(position.getY());
        int temps = (int) (Math.floor(JeuCamelot.tempsTotal * 4) % 2);
        if (temps != dernierTemps) {
            dernierTemps = temps;
            if (temps == 0) {
                image.setImage(camelot1);
            } else {
                image.setImage(camelot2);
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
            if (Math.abs(velocite.getX()) > vitesseDeBase) {

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
            System.out.println("jump");
        }

        //Gravite
        if (!toucheLeSol) {
            velocite = new Point2D(velocite.getX(), velocite.getY() + acceleration.getY() * deltaTemps);
        }
        //MAJ
        super.updatePhysique(deltaTemps);
        //Gestion du double saut
        if (position.getY() + camelot1.getHeight() >= JeuCamelot.hauteur) {
            toucheLeSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }
        //Contrainte rester dans la fenêtre
        position = new Point2D(
                Math.clamp(position.getX(), 0, JeuCamelot.largeur - camelot1.getWidth()),
                Math.clamp(position.getY(), 0, JeuCamelot.hauteur - camelot1.getHeight())
        );
        System.out.println(position);

    }
    //Journaux transporté
    public void addJournaux(Journal journal) {
        journaux.push(journal);
    }

    public void removeJournal() {
        if (!journaux.isEmpty()) {
            journaux.pop();
        }

    }

    //if (Input.isKeyPressed(KeyCode.SHIFT) && ( Input.isKeyPressed(KeyCode.X) || Input.isKeyPressed(KeyCode.Z))) {
    //
    //
    //
    //        }
}
