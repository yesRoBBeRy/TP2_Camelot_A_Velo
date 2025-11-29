package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.geometry.Point2D;

public class Camera{
    private Point2D positionCamera;
    private double cibleX;

    public Camera(Point2D positionCamera){
        this.positionCamera = positionCamera;
    }
    /**
     * Fait avancer la caméra vers la droite automatiquement
     */
    public void update(Camelot camelot) {
        cibleX = camelot.getPosition().getX() - JeuCamelot.largeur * 0.2;
        positionCamera = new Point2D(positionCamera.getX() + (cibleX - positionCamera.getX()),0);
    }

    public Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);

    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }
    public void setPositionCamera(Point2D positionCamera) {
        this.positionCamera = positionCamera;
    }

    public double getCibleX() {
        return cibleX;
    }


}
