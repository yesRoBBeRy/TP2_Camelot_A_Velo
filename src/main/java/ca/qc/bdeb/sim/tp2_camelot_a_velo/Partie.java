package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Classe qui s'occupe de gérer une partie
 * Responsabilités :
 * - création des objets du jeu
 * - appel aux méthodes de mise à jour physique et dessinage
 * - appel aux méthodes de déboggage
 * - vérification de la fin ou du changement de niveau
 */
public class Partie implements DeboggageLogique {
    private final List<ObjetDuJeu> OBJETS_DU_JEU = new ArrayList<>();
    private final List<Journal> JOURNAUX = new ArrayList<>();
    private final List<Collisions> COLLISIONS_STATIQUES = new ArrayList<>();
    private final List<ParticuleChargee> PARTICULES_CHARGEES = new ArrayList<>();
    private final Camera CAMERA;
    private final Camelot CAMELOT;
    private final int niveau;
    private final HUD hud;
    private final int nbJournaux;
    private final double masse;

    /**
     * Initialisation des objets du jeu
     *
     * @param niveau      niveau actuel
     * @param nbJournaux  nombre de journaux actuel
     * @param argentTotal nombre d'argent actuel
     */
    public Partie(int niveau, int nbJournaux, int argentTotal) {
        this.niveau = niveau;
        this.nbJournaux = nbJournaux;

        Random rand = new Random();
        //Objets caméra et camelot
        CAMERA = new Camera(Point2D.ZERO);
        CAMELOT = new Camelot(new Point2D(100, 400), new Point2D(.2 * JeuCamelot.largeur, JeuCamelot.hauteur - 144), argentTotal);
        
        //Créer les positions des maisons
        int intervalleX = 1300;
        int NB_MAISONS = 12;
        int[] positionsPortes = getPositionsPortes(NB_MAISONS, intervalleX);

        //Adresses
        int[] adresses = getAdresses(NB_MAISONS, rand);

        //Créer les maisons
        creerMaisons(positionsPortes, adresses);

        //Création des particules chargées
        creerParticules(niveau);

        //Création des objets de type Journal et ajout aux listes
        this.masse = rand.nextDouble(1, 2);
        creerJournaux(nbJournaux);

        //Objet HUD qui s'occupe des informations sur l'écran
        this.hud = new HUD(this);
    }

    private void creerJournaux(int nbJournaux) {
        for (int i = 0; i < nbJournaux; i++) {
            Journal journal = new Journal(new Point2D(0, 0), new Point2D(0, 0), masse, CAMELOT, PARTICULES_CHARGEES);
            OBJETS_DU_JEU.add(journal);
            CAMELOT.addJournaux(journal);
            JOURNAUX.add(journal);
        }
    }

    private void creerParticules(int niveau) {
        int nbParticules = Math.min((niveau - 1) * 30, 400);

        for (int i = 0; i < nbParticules; i++) {
            ParticuleChargee p = new ParticuleChargee(Point2D.ZERO, Point2D.ZERO);
            PARTICULES_CHARGEES.add(p);
            OBJETS_DU_JEU.add(p);
        }
    }

    private void creerMaisons(int[] positionsPortes, int[] adresses) {
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
                
                //Liste temporaire, car on ne peut pas modifier une liste quand on itère dans celle-ci
                aAjouter.addAll(Arrays.asList(fenetre));
                aAjouter.add(boite);
            }
        }
        OBJETS_DU_JEU.addAll(aAjouter);
        OBJETS_DU_JEU.add(CAMELOT);
    }

    private int[] getAdresses(int NB_MAISONS, Random rand) {
        int[] adresses = new int[NB_MAISONS];
        adresses[0] = rand.nextInt(100, 950);
        for (int i = 1; i < NB_MAISONS; i++) {
            adresses[i] = adresses[i - 1] + 2;
        }
        return adresses;
    }

    private int[] getPositionsPortes(int NB_MAISONS, int intervalleX) {
        int[] positionsPortes = new int[NB_MAISONS];
        positionsPortes[0] = intervalleX;
        for (int i = 1; i < NB_MAISONS; i++) {
            positionsPortes[i] = positionsPortes[i - 1] + intervalleX;
        }
        return positionsPortes;
    }

    /**
     * Mise à jour de la physique
     *
     * @param deltaTemps : double
     */
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

    /**
     * Dessinage des objets sur le canvas
     *
     * @param context GraphicsContext
     */
    public void draw(GraphicsContext context) {
        Mur.dessinerMur(context, CAMERA);
        for (ObjetDuJeu obj : OBJETS_DU_JEU) {
            obj.draw(context, CAMERA);
        }
        hud.drawHUD(context);
    }

    /**
     * @return la liste des adresses abonnées
     */
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

    /**
     * @param adresses : List des adresses
     * @return un String pour le HUD
     */
    public String adressesToString(List<Integer> adresses) {
        StringBuilder sb = new StringBuilder();
        for (Integer adresse : adresses) {
            sb.append(adresse).append(" ");
        }
        return sb.toString();
    }

    /**
     * Vérifie si la fin du niveau est atteinte selon les journaux en mouvement, la longueur du niveau
     * ainsi que le nombre de journaux restants.
     *
     * @return boolean
     */
    public boolean checkFin() {
        if (!CAMELOT.getJournaux().isEmpty()) return false;

        for (Journal journal : JOURNAUX) {
            boolean actif = journal.getPeutCollision() && journal.getPosition().getY() + journal.getIMG().getHeight() < JeuCamelot.hauteur;
            if (actif) return false;
        }
        return true;
    }

    /**
     * Dessine les deboggages selon ce qui est enfoncé
     *
     * @param context        GraphicsContext
     * @param debugCollision boolean
     * @param debugChamp     boolean
     */
    public void drawDebug(GraphicsContext context, boolean debugCollision, boolean debugChamp) {
        for (ObjetDuJeu obj : OBJETS_DU_JEU) {
            if (debugCollision) {
                obj.deboggage(context, CAMERA);
            }
            if (debugChamp && obj instanceof Journal) {
                obj.deboggageChamp(context, CAMERA);
            }
        }
    }

    /**
     * Vérifie les conditions pour commencer un nouveau niveau
     *
     * @param deboggageSkip : Pour le deboggage
     * @return boolean
     */
    public boolean checkNouveauNiveau(boolean deboggageSkip) {
        if (deboggageSkip) return true;
        return CAMELOT.getPosition().getX() + JeuCamelot.largeur > Mur.longueurNiveau;
    }

    /**
     * Modifie les particules présentes pour mettre les particules en haut et en bas de l'écran
     *
     * @param particuleDebug : Activé/Désactivé
     */
    public void deboggageParticules(boolean particuleDebug) {
        OBJETS_DU_JEU.removeIf(obj -> obj instanceof ParticuleChargee);
        PARTICULES_CHARGEES.clear();
        if (!particuleDebug) return;

        for (int x = 0; x <= Mur.margeNiveau; x += 50) {
            ParticuleChargee p1 = new ParticuleChargee(Point2D.ZERO, Point2D.ZERO);
            p1.setPosition(new Point2D(x, 10));
            ParticuleChargee p2 = new ParticuleChargee(Point2D.ZERO, Point2D.ZERO);
            p2.setPosition(new Point2D(x, JeuCamelot.hauteur - 10));
            PARTICULES_CHARGEES.add(p1);
            PARTICULES_CHARGEES.add(p2);
            OBJETS_DU_JEU.add(p1);
            OBJETS_DU_JEU.add(p2);
        }
    }

    /**
     * Ajoute 10 journaux
     */
    @Override
    public void ajouterJournaux() {
        creerJournaux(10);
    }

    /**
     * Rénitialise les journaux, ce qui termine la partie présente
     */
    @Override
    public void renitialiserJournaux() {
        JOURNAUX.clear();
        CAMELOT.clearJournaux();
        OBJETS_DU_JEU.removeIf(obj -> obj instanceof Journal);
    }

    public int getNbJournaux() {
        return nbJournaux;
    }
    public Camelot getCAMELOT() {
        return CAMELOT;
    }

    public int getNiveau() {
        return niveau;
    }
}
