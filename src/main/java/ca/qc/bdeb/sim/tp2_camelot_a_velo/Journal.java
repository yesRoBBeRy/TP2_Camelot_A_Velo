package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Journal extends ObjetDuJeu implements Collisions {
    private final Camelot camelot;
    private final double masse;
    private static final Point2D qtDeMouvementZ = new Point2D(900, -900);
    private static final Point2D qtDeMouvementX = new Point2D(150, -1100);
    private boolean dejaLancer = false;
    private final Image img;

    public boolean getPeutCollision() {
        return peutCollision;
    }

    private boolean peutCollision = true;

    public Journal(Point2D velocite, Point2D position, double masse, Camelot camelot) {
        super(velocite, position);
        this.masse = masse;
        this.camelot = camelot;
        this.img = new Image("journal.png", false);
        this.image = img;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        if (!dejaLancer || image == null) return;
        var coordoEcran = camera.coordoEcran(position);
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());

    }

    @Override
    public void updatePhysique(double deltaTemps) {
        super.updatePhysique(deltaTemps);
        //module de la velocite maximum
        double max = 1500;
        if (velocite.magnitude() > max) {
            velocite = velocite.multiply(max / velocite.magnitude());
        }

        if (!dejaLancer) {
            position = camelot.getPosition();
        }


    }

    public void actionLancer(boolean z, boolean x) {
        image = img;
        if (dejaLancer) {
            return;
        }

        Point2D qtDeMouvement;

        if (z) {
            qtDeMouvement = qtDeMouvementZ;
        } else if (x) {
            qtDeMouvement = qtDeMouvementX;
        } else return;

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            qtDeMouvement = qtDeMouvement.multiply(1.5);
        }

        //vinitiale = ⃗vcamelot + pinitiale/m
        velocite = camelot.getVelocite();
        velocite = velocite.add(qtDeMouvement.multiply(1.0 / masse));
        dejaLancer = true;

    }

    public boolean estDejaLancer() {
        return dejaLancer;
    }

    public double getMasse() {
        return masse;
    }


    public Point2D calculQtDeMouvementZInitial(Point2D velocite, double masse) {
        return velocite.multiply(masse);
    }


    @Override
    public Rectangle2D getBounds() {
        if(image == null) return Rectangle2D.EMPTY;
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public boolean collision(Collisions autreObj) {
        if(image == null) return false;
        if(!dejaLancer) return false;
        return Collisions.super.collision(autreObj);
    }

    @Override
    public void actionApresCollision() {
        velocite = Point2D.ZERO;
        acceleration = Point2D.ZERO;
        image = null;
        peutCollision = false;
    }
}