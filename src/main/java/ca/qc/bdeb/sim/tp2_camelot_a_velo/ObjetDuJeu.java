package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ObjetDuJeu {
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
}
