package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;

/**
 * Classe qui s'occupe de montrer et suivre le camelot
 */
public class Camera{
    private Point2D positionCamera;

    public Camera(Point2D positionCamera){
        this.positionCamera = positionCamera;
    }
    /**
     * Fait avancer la caméra vers la droite automatiquement
     */
    public void update(Camelot camelot) {
        double cibleX = camelot.getPosition().getX() - JeuCamelot.largeur * 0.2;
        positionCamera = new Point2D(positionCamera.getX() + (cibleX - positionCamera.getX()),0);
    }

    /**
     * @param positionMonde Position selon le monde complet
     * @return la position selon la caméra
     */
    public Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);
    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }



}
