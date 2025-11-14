package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Journal extends ObjetDuJeu {
    protected ImageView img;
    protected double masse;

    public Journal(Point2D velocite, Point2D position, double masse) {
        super(velocite, position);
        this.img = new ImageView(new Image("journal.png"));
        this.masse = masse;
    }

    @Override
    public void draw(GraphicsContext context) {

    }
}
