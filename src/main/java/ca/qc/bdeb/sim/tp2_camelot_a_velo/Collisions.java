package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Rectangle2D;

public interface Collisions {

    Rectangle2D getBounds();
    default boolean collision(Collisions autreObj){
        return this.getBounds().intersects(autreObj.getBounds());
    }

}
