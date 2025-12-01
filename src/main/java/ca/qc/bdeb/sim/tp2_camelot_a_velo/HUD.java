package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
/**
 * Classe représentant l'affichage tête haute (HUD) du jeu.
 * <p>
 * Affiche les informations importantes pour le joueur : nombre de journaux,
 * argent total et adresses des maisons abonnées.
 */
public class HUD {
    private final Partie partie;
    private final Image iconeJournal;
    private final Image iconeDollar;
    private final Image iconeMaison;
    private final Camelot CAMELOT;

    public HUD(Partie partie) {
        this.partie = partie;
        this.CAMELOT = partie.getCAMELOT();
        this.iconeJournal = new Image("icone-journal.png");
        this.iconeDollar = new Image("icone-dollar.png");
        this.iconeMaison = new Image("icone-maison.png");
    }

    /**
     * Dessine le HUD sur le canvas.
     * <p>
     * Affiche une barre semi-transparente en haut de l'écran avec les icônes
     * et les informations : nombre de journaux, argent total, adresses des abonnés.
     *
     * @param context Contexte graphique du Canvas
     */
    public void drawHUD(GraphicsContext context) {
        double hauteurBarre = 50;
        double espace = 20;

        //Barre semi-transparente
        context.setFill(Color.rgb(0, 0, 0, .5));
        context.fillRect(0, 0, JeuCamelot.largeur, hauteurBarre);

        //Icone du journal
        context.drawImage(iconeJournal, 10, 8.5);

        //Nombre total de journaux
        context.setFont(Font.font(35));
        context.setFill(Color.GRAY);
        int nbJournaux = CAMELOT.getJournaux().size();
        context.fillText(String.valueOf(nbJournaux), espace + iconeJournal.getWidth(), 35);

        // Dessine l'icône du dollar
        context.drawImage(iconeDollar, 2 * espace + 2 * iconeJournal.getWidth(), 12);

        // Affiche le montant d'argent total
        int argentTotal = CAMELOT.getArgentTotal();
        context.fillText(String.valueOf(argentTotal), 4 * espace + 3 * iconeJournal.getWidth(), 35);

        // Dessine l'icône de la maison
        context.drawImage(iconeMaison, 2 * espace + 2 * iconeJournal.getWidth() + 3 * iconeDollar.getWidth(), 8.5);

        // Affiche les adresses des maisons abonnées
        List<Integer> adresses = partie.getListeAdressesAbonnees();
        String adressesString = partie.adressesToString(adresses);

        context.fillText(adressesString, 3 * espace + 3 * iconeDollar.getWidth() + 2 * iconeJournal.getWidth() + iconeMaison.getWidth(), 35);
    }
}
