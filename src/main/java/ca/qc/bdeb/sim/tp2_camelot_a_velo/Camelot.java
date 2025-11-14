package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Camelot extends ObjetDuJeu {
    protected int nbJournal;
    private final Image camelot1;
    private final Image camelot2;
    private int dernierTemps = 0;
    private final int vitesseDeBase = 0;
    protected boolean toucheLeSol = true;

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
        //mouvement en X
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
        //mouvement Y
        if ((Input.isKeyPressed(KeyCode.SPACE) || Input.isKeyPressed(KeyCode.UP)) && toucheLeSol) {
            velocite = new Point2D(velocite.getX(), -500); //sauter
            toucheLeSol = false;
            System.out.println("jump");
        }


        if (!toucheLeSol) {
            velocite = new Point2D(velocite.getX(), velocite.getY() + acceleration.getY() * deltaTemps);
        }

        super.updatePhysique(deltaTemps);

        if (position.getY() + camelot1.getHeight() >= JeuCamelot.hauteur) {
            toucheLeSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }

        position = new Point2D(
                Math.clamp(position.getX(), 0, JeuCamelot.largeur - camelot1.getWidth()),
                Math.clamp(position.getY(), 0, JeuCamelot.hauteur - camelot1.getHeight())
        );
        System.out.println(position);

    }
}
