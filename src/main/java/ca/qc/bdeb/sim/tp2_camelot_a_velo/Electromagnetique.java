package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;
/**
 * Interface pour les objets soumis à un champ électromagnétique.
 * Fournit une méthode pour calculer l'accélération résultante du champ.
 */
public interface Electromagnetique {

    Point2D calculerAccelChampElect();
}
