package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Maison extends ObjetDuJeu{
    protected int adresse;
    protected boolean estAbonne;
    protected Fenetre[] fenetres;
    protected BoiteAuLettre boiteAuLettre;

    public Maison(Point2D velocite, Point2D position, int positionX, int adresse) {
        super(velocite, position);
        this.acceleration = Point2D.ZERO;
        Random rand = new Random();
        this.estAbonne = rand.nextBoolean();
        this.adresse = adresse;
        this.fenetres = new Fenetre[rand.nextInt(2)];
        this.image = new Image("porte.png");
        this.position = new Point2D(positionX, JeuCamelot.hauteur - image.getHeight());
        this.boiteAuLettre = new BoiteAuLettre(Point2D.ZERO, new Point2D(this.position.getX() + 200, rand.nextDouble(.2, .7) * JeuCamelot.hauteur));


        for (int i = 0; i < fenetres.length; i++) {
            if(i == 0) {
                fenetres[i] = new Fenetre(Point2D.ZERO, new Point2D(this.position.getX() + 300, 50));
            } else fenetres[i] = new Fenetre(Point2D.ZERO, new Point2D(this.position.getX() + 600, 50));
        }
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {

        for(Fenetre f : fenetres) {
            f.draw(context, camera);
        }

        boiteAuLettre.draw(context, camera);

        var coordoEcran = camera.coordoEcran(position);

        if(coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());


        context.setFill(Color.YELLOW);
        context.setFont(Font.font(40));

        //Centrer le texte
        double centreX = image.getWidth() / 2 + coordoEcran.getX();
        double posX = coordoEcran.getX() + 40;
        double posY = coordoEcran.getY() + 50;

        context.fillText(String.valueOf(adresse), posX, posY);

    }
    @Override
    public String toString() {
        return "Maison{" +
                "adresse=" + adresse +
                ", estAbonne=" + estAbonne +
                ", fenetres=" + Arrays.toString(fenetres) +
                ", boiteAuLettre=" + boiteAuLettre +
                ", img=" + image +
                '}';
    }

    public Fenetre[] getFenetres() {
        return fenetres;
    }

    public BoiteAuLettre getBoiteAuLettre() {
        return boiteAuLettre;
    }
}
