package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Journal extends ObjetDuJeu {
    private final ImageView img;
    private final double masse;
    private static final Point2D qtDeMouvementZ = new Point2D(900,-900);
    private static final Point2D qtDeMouvementX = new Point2D(150,-1100);
    private boolean dejaLancer = false;


    public Journal(Point2D velocite, Point2D position, double masse) {
        super(velocite, position);
        this.img = new ImageView(new Image("journal.png"));
        this.masse = masse;
    }

    @Override
    public void draw(GraphicsContext context) {
        image.setX(position.getX());
        image.setY(position.getY());
    }

    @Override
    public void updatePhysique(double deltaTemps){
        super.updatePhysique(deltaTemps);
        //module de la velocite maximum
        double max = 1500;
        if(velocite.magnitude() > max){

            velocite = velocite.multiply(max / velocite.magnitude());
        }

    }

    public void actionLancer(boolean z, boolean x, boolean shift){
        if(dejaLancer){
            return;
        }

        Point2D qtDeMouvement = null;

        if(z){
            qtDeMouvement = qtDeMouvementZ;
        }
        if(x){
            qtDeMouvement = qtDeMouvementX;
        }

        if(qtDeMouvement == null){
            return;
        }

        if (shift){
            qtDeMouvement = qtDeMouvement.multiply(1.5);
        }

        //vinitiale = ⃗vcamelot + pinitiale/m
        velocite = velocite.add(qtDeMouvement.multiply(1.0/masse));
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

    public ImageView getImg() {
        return img;
    }
}