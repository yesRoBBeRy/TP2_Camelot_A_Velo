package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JeuCamelot extends Application {
    public static double tempsTotal;
    public static double largeur = 900;
    public static double hauteur = 580;

    @Override
    public void start(Stage stage) throws IOException {/*

        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<ObjetDuJeu> objetsDuJeu = new ArrayList<>();
        objetsDuJeu.add(new Camelot(new Point2D(100, 400), new Point2D(.2 * largeur, hauteur - 144), new  Point2D(0,0)));
        for(var objet : objetsDuJeu){
            ImageView imageView = objet.getImage();
            root.getChildren().add(imageView);
        }
        animer(objetsDuJeu, gc);
        */
        var scene = sceneJeu();

        stage.setTitle("Camelot à vélo");
        stage.setScene(scene);
        stage.show();
    }

    /*
    public void animer(List<ObjetDuJeu> objetsDuJeu,  GraphicsContext gc){
        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;
                for(var objet : objetsDuJeu){
                    objet.draw(gc);
                    objet.updatePhysique(deltaTemps);
                }
                dernierTemps = temps;
            }
        };
        timer.start();
    }

     */
    private Scene sceneJeu() {
        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<ObjetDuJeu> objetsDuJeu = new ArrayList<>();
        objetsDuJeu.add(new Camelot(new Point2D(100, 400), new Point2D(.2 * largeur, hauteur - 144)));

        for (var objet : objetsDuJeu) {
            ImageView imageView = objet.getImage();
            root.getChildren().add(imageView);
        }

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;
                for (var objet : objetsDuJeu) {
                    objet.draw(gc);
                    objet.updatePhysique(deltaTemps);
                }
                dernierTemps = temps;
            }
        };
        timer.start();
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                timer.stop();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}