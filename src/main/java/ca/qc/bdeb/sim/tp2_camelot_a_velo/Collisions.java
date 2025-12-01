package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Rectangle2D;

/**
 * Interface qui s'occupe des éléments qui peuvent entrer en collision
 */
public interface Collisions {

    Rectangle2D getBounds();

    default boolean collision(Collisions autreObj) {
        return this.getBounds().intersects(autreObj.getBounds());
    }

    void actionApresCollision();

}
