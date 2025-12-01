package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.canvas.GraphicsContext;
/**
 * Interface du débogage visuel dans le jeu.
 * Permet d'afficher les boites de collision et la ligne de caméra.
 */
public interface DeboggageVisuel {
    void deboggage(GraphicsContext gc, Camera camera);

}
