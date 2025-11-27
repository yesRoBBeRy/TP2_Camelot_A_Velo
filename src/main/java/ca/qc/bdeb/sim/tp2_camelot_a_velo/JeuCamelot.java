package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JeuCamelot extends Application {
    public static double tempsTotal;
    public static double largeur = 900;
    public static double hauteur = 580;

    @Override
    public void start(Stage stage) {
        var scene = sceneJeu();

        stage.setTitle("Camelot à vélo");
        stage.setScene(scene);
        stage.show();
    }

    private Scene sceneJeu() {
        //Structure
        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);

        Partie partie = new Partie(2);

        AnimationTimer timer = getAnimationTimer(canvas, partie);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                timer.stop();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        return scene;
    }

    private static AnimationTimer getAnimationTimer(Canvas canvas, Partie partie) {
        GraphicsContext context = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;

                partie.update(deltaTemps);
                partie.draw(context);
                partie.drawHUD(context);

                dernierTemps = temps;
            }
        };
        timer.start();
        return timer;
    }

    public boolean niveauComplete(Camera camera, Camelot camelot){
        double positionDeFin = 15600 + 1.5*largeur;
        return camera.getPositionCamera().getX() + largeur > positionDeFin || camelot.getJournaux().isEmpty();
    }

    public static void main(String[] args) {
        launch(args);
    }
}