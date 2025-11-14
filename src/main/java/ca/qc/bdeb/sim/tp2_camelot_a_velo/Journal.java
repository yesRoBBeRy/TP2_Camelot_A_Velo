package ca.qc.bdeb.sim.tp2_camelot_a_velo;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Journal extends ObjetDuJeu {
    protected ImageView img;
    protected double masse;
    protected Point2D qtDeMouvementZ;
    protected Point2D qtDeMouvementX;
    protected boolean visible;
    protected boolean lancer;


    public Journal(Point2D velocite, Point2D position, double masse) {
        super(velocite, position);
        this.img = new ImageView(new Image("journal.png"));
        this.masse = masse;
        this.qtDeMouvementZ = new Point2D(900,-900); //init Z
        this.qtDeMouvementX = new Point2D(150,-1100); //init X


    }

    @Override
    public void draw(GraphicsContext context) {
        image.setX(position.getX());
        image.setY(position.getY());
    }
    @Override
    public void updatePhysique(double deltaTemps){


        if (Input.isKeyPressed(KeyCode.SHIFT) && ( Input.isKeyPressed(KeyCode.X) || Input.isKeyPressed(KeyCode.Z))) {
            

        }

        if(deltaTemps < 0.5){
            lancer = false;
        }


        //module de la velocite maximum
        double max = 1500;
        if(velocite.magnitude() > max){

            velocite = velocite.multiply(max / velocite.magnitude());
        }

    }


    public Point2D calculQtDeMouvementZInitial(Point2D velocite, double masse) {
        return velocite.multiply(masse);
    }
}
