package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Partie {
    private List<ObjetDuJeu> objetsDuJeu = new ArrayList<>();
    private List<Journal> journaux = new ArrayList<>();
    private List<Collisions> collisionsStatiques = new ArrayList<>();
    private List<ParticuleChargee> particulesChargees = new ArrayList<>();
    private Camera camera;
    private Camelot camelot;
    private static int NB_MAISONS = 12;

    public Partie(int niveau) {
        Random rand = new Random();

        //Créer les positions des maisons
        int intervalleX = 1300;
        int[] positionsPortes = new int[NB_MAISONS];
        positionsPortes[0] = intervalleX;
        for (int i = 1; i < NB_MAISONS; i++) {
            positionsPortes[i] = positionsPortes[i - 1] + intervalleX;
        }
        //Adresses
        int[] adresses = new int[NB_MAISONS];
        adresses[0] = rand.nextInt(100, 950);
        for (int i = 1; i < NB_MAISONS; i++) {
            adresses[i] = adresses[i - 1] + 2;
        }

        //Objets caméra et camelot
        camera = new Camera(Point2D.ZERO);
        camelot = new Camelot(new Point2D(100, 400), new Point2D(.2 * JeuCamelot.largeur, JeuCamelot.hauteur - 144));

        //Création des maisons
        for (int i = 0; i < positionsPortes.length; i++) {
            objetsDuJeu.add(new Maison(new Point2D(0, 0), new Point2D(0, 0), positionsPortes[i], adresses[i]));
        }

        //Création des objets journal et ajout aux listes
        double masse = rand.nextDouble(2);
        for (int i = 0; i < 12; i++) {
            Journal journal = new Journal(new Point2D(0, 0), new Point2D(0, 0), masse, camelot);
            objetsDuJeu.add(journal);
            camelot.addJournaux(journal);
            journaux.add(journal);
        }

        //Ajout à la liste des collisions pour les objets
        for (ObjetDuJeu obj : objetsDuJeu) {
            if (obj instanceof Maison) {
                Fenetre[] fenetre = ((Maison) obj).getFenetres();
                BoiteAuLettre boite = ((Maison) obj).getBoiteAuLettre();

                collisionsStatiques.addAll(Arrays.asList(fenetre));
                collisionsStatiques.add(boite);
            }
        }
        objetsDuJeu.add(camelot);
        //Création des particules chargées
        int nbParticules = Math.min((niveau - 1) * 30, 400);

        for (int i = 0; i < nbParticules; i++) {
            ParticuleChargee p = new ParticuleChargee(Point2D.ZERO, Point2D.ZERO);
            particulesChargees.add(p);
            objetsDuJeu.add(p);
        }

    }

    public void update(double deltaTemps) {
        for (var obj : objetsDuJeu) {
            obj.updatePhysique(deltaTemps);
        }
        //Détection de collisions
        for (Journal journal : journaux) {
            if (!journal.getPeutCollision()) continue;

            for (Collisions collision : collisionsStatiques) {
                if (journal.collision(collision)) {
                    System.out.println("collision");
                    journal.actionApresCollision();
                    collision.actionApresCollision();
                    break;
                }
            }
        }
        camera.update(camelot);
    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);

        Mur.dessinerMur(context, camera);
        for (ObjetDuJeu obj : objetsDuJeu) {
            obj.draw(context, camera);
        }
    }

    public List<Integer> getListeAdressesAbonnees() {
        List<Integer> adresses = new ArrayList<>();
        for (ObjetDuJeu obj : objetsDuJeu) {
            if (obj instanceof Maison) {
                if (((Maison) obj).getEstAbonne()) {
                    adresses.add(((Maison) obj).getAdresse());
                }
            }
        }
        return adresses;
    }
    public void drawHUD(GraphicsContext context) {
        double hauteurBarre = 50;
        double espace = 20;

        //Barre
        context.setFill(Color.rgb(0, 0, 0, .5));
        context.fillRect(0, 0, JeuCamelot.largeur, hauteurBarre);

        //Icone du journal
        Image iconeJournal = new Image("icone-journal.png");
        context.drawImage(iconeJournal, 10, 8.5);

        //Total de journaux
        context.setFont(Font.font(35));
        context.setFill(Color.GRAY);
        context.fillText(String.valueOf(camelot.getJournaux().size()), espace + iconeJournal.getWidth(), 35);

        Image iconeDollar = new Image("icone-dollar.png");
        context.drawImage(iconeDollar, 2 * espace + 2 * iconeJournal.getWidth(), 12);

        //TODO : Logique de l'argent!!!! + texte de l'argent total

        Image iconeMaison = new Image("icone-maison.png");
        context.drawImage(iconeMaison, 2 * espace + 2 * iconeJournal.getWidth() + 3 * iconeDollar.getWidth(), 8.5);

        List<Integer> adresses = getListeAdressesAbonnees();
        String adressesString = adressesToString(adresses);

        context.fillText(adressesString, 3 * espace + 3 * iconeDollar.getWidth() + 2 * iconeJournal.getWidth() + iconeMaison.getWidth(), 35);


    }
    public String adressesToString(List<Integer> adresses) {
        StringBuilder sb = new StringBuilder();
        for (Integer adresse : adresses) {
            sb.append(adresse).append(" ");
        }
        return sb.toString();
    }

}
