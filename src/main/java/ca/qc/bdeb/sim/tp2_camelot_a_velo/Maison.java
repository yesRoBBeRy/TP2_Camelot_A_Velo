package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.Random;
/**
 * Représente une maison dans le jeu.
 * Une maison possède une adresse, peut être abonnée ou non, contient des fenêtres
 * et une boîte aux lettres, et est associée au joueur Camelot pour la gestion
 * des interactions (argent et lancements de journaux).
 */
public class Maison extends ObjetDuJeu{
    protected int adresse;
    protected boolean estAbonne;
    protected Fenetre[] fenetres;
    protected BoiteAuLettre boiteAuLettre;
    protected Camelot camelot;

    /**
     * Constructeur d'une maison.
     *
     * @param velocite  Vélocité initiale de la maison
     * @param position  Position de base dans le jeu
     * @param positionX Position horizontale spécifique dans le jeu
     * @param adresse   Numéro d'adresse de la maison
     * @param camelot   Référence au joueur Camelot
     */
    public Maison(Point2D velocite, Point2D position, int positionX, int adresse, Camelot camelot) {
        super(velocite, position);
        this.acceleration = Point2D.ZERO;
        Random rand = new Random();
        this.estAbonne = rand.nextBoolean();
        this.adresse = adresse;
        this.fenetres = new Fenetre[rand.nextInt(3)]; //Crée un nombre aléatoire de fenêtres (0 à 2)
        this.image = new Image("porte.png");
        this.position = new Point2D(positionX, JeuCamelot.hauteur - image.getHeight());// Position verticale de la maison (au sol)
        // Création de la boîte aux lettres à une position aléatoire
        this.boiteAuLettre = new BoiteAuLettre(Point2D.ZERO, new Point2D(this.position.getX() + 200, rand.nextDouble(.2, .7) * JeuCamelot.hauteur));
        this.camelot = camelot;
        // Références pour la boîte aux lettres
        boiteAuLettre.setRefMaison(this);
        boiteAuLettre.setRefCamelot(camelot);

        // Création des fenêtres avec positionnement spécifique
        for (int i = 0; i < fenetres.length; i++) {
            Fenetre f;
            if(i == 0) {
                f = new Fenetre(Point2D.ZERO, new Point2D(this.position.getX() + 300, 50));
            } else {
                f = new Fenetre(Point2D.ZERO, new Point2D(this.position.getX() + 600, 50));
            }
            fenetres[i] = f;
            f.setRefMaison(this);
            f.setRefCamelot(camelot);
        }
    }

    /**
     * Dessine la maison à l'écran, ainsi que son numéro d'adresse.
     *
     * @param context Contexte graphique du Canvas
     * @param camera  Caméra pour convertir la position en coordonnées écran
     */
    @Override
    public void draw(GraphicsContext context, Camera camera) {
        var coordoEcran = camera.coordoEcran(position);
        // Ne dessine pas si hors écran
        if(coordoEcran.getX() + image.getWidth() < 0 || coordoEcran.getX() > JeuCamelot.largeur) return;
        context.drawImage(image, coordoEcran.getX(), coordoEcran.getY());

        // Affiche l'adresse en jaune
        context.setFill(Color.YELLOW);
        context.setFont(Font.font(40));

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

    public boolean getEstAbonne() {
        return estAbonne;
    }

    public int getAdresse() {
        return adresse;
    }
}
