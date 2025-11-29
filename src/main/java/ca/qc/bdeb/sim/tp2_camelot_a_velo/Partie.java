package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Partie implements DeboggageLogique {
    private final List<ObjetDuJeu> OBJETS_DU_JEU = new ArrayList<>();
    private final List<Journal> JOURNAUX = new ArrayList<>();
    private final List<Collisions> COLLISIONS_STATIQUES = new ArrayList<>();
    private final List<ParticuleChargee> PARTICULES_CHARGEES = new ArrayList<>();
    private final Camera CAMERA;
    private final Camelot CAMELOT;
    private final int NB_MAISONS = 12;
    private int niveau;
    private final HUD hud;



    public Partie(int niveau) {
        this.niveau = niveau;

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
        CAMERA = new Camera(Point2D.ZERO);
        CAMELOT = new Camelot(new Point2D(100, 400), new Point2D(.2 * JeuCamelot.largeur, JeuCamelot.hauteur - 144));

        //Création des maisons
        for (int i = 0; i < positionsPortes.length; i++) {
            OBJETS_DU_JEU.add(new Maison(new Point2D(0, 0), new Point2D(0, 0), positionsPortes[i], adresses[i], CAMELOT));
        }
        List<ObjetDuJeu> aAjouter = new ArrayList<>();
        //Ajout à la liste des collisions pour les objets
        for (ObjetDuJeu obj : OBJETS_DU_JEU) {
            if (obj instanceof Maison) {
                Fenetre[] fenetre = ((Maison) obj).getFenetres();
                BoiteAuLettre boite = ((Maison) obj).getBoiteAuLettre();

                COLLISIONS_STATIQUES.addAll(Arrays.asList(fenetre));
                COLLISIONS_STATIQUES.add(boite);

                aAjouter.addAll(Arrays.asList(fenetre));
                aAjouter.add(boite);
            }
        }
        OBJETS_DU_JEU.addAll(aAjouter);
        OBJETS_DU_JEU.add(CAMELOT);
        //Création des particules chargées
        int nbParticules = Math.min((niveau - 1) * 30, 400);

        for (int i = 0; i < nbParticules; i++) {
            ParticuleChargee p = new ParticuleChargee(Point2D.ZERO, Point2D.ZERO);
            PARTICULES_CHARGEES.add(p);
            OBJETS_DU_JEU.add(p);
        }
        //Création des objets journal et ajout aux listes
        double masse = rand.nextDouble(2);
        for (int i = 0; i < 12; i++) {
            Journal journal = new Journal(new Point2D(0, 0), new Point2D(0, 0), masse, CAMELOT, PARTICULES_CHARGEES);
            OBJETS_DU_JEU.add(journal);
            CAMELOT.addJournaux(journal);
            JOURNAUX.add(journal);
        }

        this.hud = new HUD(this);

    }

    public void update(double deltaTemps) {
        for (var obj : OBJETS_DU_JEU) {
            obj.updatePhysique(deltaTemps);
        }
        //Détection de collisions
        for (Journal journal : JOURNAUX) {
            if (!journal.getPeutCollision()) continue;

            for (Collisions collision : COLLISIONS_STATIQUES) {
                if (journal.collision(collision)) {
                    journal.actionApresCollision();
                    collision.actionApresCollision();
                    break;
                }
            }
        }
        CAMERA.update(CAMELOT);
    }

    public void draw(GraphicsContext context){
        Mur.dessinerMur(context, CAMERA);
        for (ObjetDuJeu obj : OBJETS_DU_JEU) {
            obj.draw(context, CAMERA);
        }
        hud.drawHUD(context);
    }

    public List<Integer> getListeAdressesAbonnees() {
        List<Integer> adresses = new ArrayList<>();
        for (ObjetDuJeu obj : OBJETS_DU_JEU) {
            if (obj instanceof Maison) {
                if (((Maison) obj).getEstAbonne()) {
                    adresses.add(((Maison) obj).getAdresse());
                }
            }
        }
        return adresses;
    }
    public String adressesToString(List<Integer> adresses) {
        StringBuilder sb = new StringBuilder();
        for (Integer adresse : adresses) {
            sb.append(adresse).append(" ");
        }
        return sb.toString();
    }

    public boolean checkFin(){
        if(!CAMELOT.getJournaux().isEmpty()) return false;

        for(Journal journal : JOURNAUX){
            boolean actif = journal.getPeutCollision() && journal.getPosition().getY() + journal.getIMG().getHeight() < JeuCamelot.hauteur;
            if(actif) return false;
        }
        return true;
    }

    public void drawDebug(GraphicsContext context, boolean debugCollision,boolean debugChamp) {
        for(ObjetDuJeu obj : OBJETS_DU_JEU) {
            if (debugCollision) {
                obj.deboggage(context, CAMERA);
            }
            if (debugChamp && obj instanceof Journal) {
                obj.deboggageChamp(context, CAMERA);
            }
        }
    }

    public boolean checkNouveauNiveau(){
        boolean atteintFin = CAMELOT.getPosition().getX() + JeuCamelot.hauteur > Mur.longueurNiveau;
        if(!atteintFin) return false;
        for(Journal journal : JOURNAUX){
            if(!journal.isDejaLancer()) continue;
            if(journal.getPeutCollision()) return false;
        }
        return true;
    }



    public Camelot getCAMELOT() {
        return CAMELOT;
    }

    public int getNiveau() {
        return niveau;
    }


    public Camera getCAMERA() {
        return CAMERA;
    }

    @Override
    public void ajouterJournaux() {

    }

    @Override
    public void renitialiserJournaux() {

    }

    @Override
    public void prochainNiveau() {

    }
}
