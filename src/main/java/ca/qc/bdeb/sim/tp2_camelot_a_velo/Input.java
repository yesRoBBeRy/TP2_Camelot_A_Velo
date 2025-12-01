package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;
/**
 * Classe utilitaire pour gérer l'état des touches du clavier.
 * Permet de savoir quelles touches sont actuellement enfoncées et de mettre à jour leur état.
 */
public class Input {


    // Ensemble des touches actuellement enfoncées
    private static final Set<KeyCode> TOUCHES = new HashSet<>();

    /**
     * Vérifie si une touche est actuellement enfoncée.
     *
     * @param keyCode La touche à vérifier
     * @return true si la touche est enfoncée, false sinon
     */
    public static boolean isKeyPressed(KeyCode keyCode){
        return TOUCHES.contains(keyCode);
    }

    /**
     * Met à jour l'état d'une touche.
     *
     * @param keyCode La touche à mettre à jour
     * @param appuie  true si la touche est pressée, false si elle est relâchée
     */
    public static void setKeyPressed(KeyCode keyCode, boolean appuie){
        if(appuie){
            TOUCHES.add(keyCode);
        } else TOUCHES.remove(keyCode);
    }
}
