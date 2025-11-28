package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class HUD {
    private Partie partie;
    private Image iconeJournal;
    private Image iconeDollar;
    private Image iconeMaison;
    private final Camelot CAMELOT;

    public HUD(Partie partie) {
        this.partie = partie;
        this.CAMELOT = partie.getCAMELOT();
        this.iconeJournal = new Image("icone-journal.png");
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
        int nbJournaux = CAMELOT.getJournaux().size();
        context.fillText(String.valueOf(nbJournaux), espace + iconeJournal.getWidth(), 35);

        Image iconeDollar = new Image("icone-dollar.png");
        context.drawImage(iconeDollar, 2 * espace + 2 * iconeJournal.getWidth(), 12);

        int argentTotal = CAMELOT.getArgentTotal();
        context.fillText(String.valueOf(argentTotal), 4 * espace + 3 * iconeJournal.getWidth(), 35);

        Image iconeMaison = new Image("icone-maison.png");
        context.drawImage(iconeMaison, 2 * espace + 2 * iconeJournal.getWidth() + 3 * iconeDollar.getWidth(), 8.5);

        List<Integer> adresses = partie.getListeAdressesAbonnees();
        String adressesString = partie.adressesToString(adresses);

        context.fillText(adressesString, 3 * espace + 3 * iconeDollar.getWidth() + 2 * iconeJournal.getWidth() + iconeMaison.getWidth(), 35);
    }
}
