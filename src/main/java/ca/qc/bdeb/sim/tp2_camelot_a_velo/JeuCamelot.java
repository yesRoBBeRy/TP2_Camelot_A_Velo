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
import java.util.Random;

public class JeuCamelot extends Application {
    public static double tempsTotal;
    public static double largeur = 900;
    public static double hauteur = 580;

    @Override
    public void start(Stage stage) throws IOException {


        var scene = sceneJeu();
        stage.setTitle("Camelot à vélo");
        stage.setScene(scene);
        stage.show();
    }

    private Scene sceneJeu() {
        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);
        Camera camera = new Camera(new Point2D(0, 0));

        Random rand = new Random();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<ObjetDuJeu> objetsDuJeu = new ArrayList<>();
        Camelot camelot = new Camelot(new Point2D(100, 400), new Point2D(.2 * largeur, hauteur - 144));
        objetsDuJeu.add(camelot);

        double mass = rand.nextDouble(2);
        for(int i = 0; i < 500; i++){
            objetsDuJeu.add(new Journal(new Point2D(0, 0), new Point2D(0, 0), mass, camelot));
        }
        for(ObjetDuJeu obj : objetsDuJeu){
            if(obj instanceof Journal){
                camelot.addJournaux((Journal) obj);
            }
        }

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, largeur, hauteur);
                Mur.dessinerMur(gc, camera);
                for (var objet : objetsDuJeu) {
                    objet.draw(gc, camera);
                    objet.updatePhysique(deltaTemps);
                }
                camera.update(deltaTemps, camelot);
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

    public boolean niveauComplete(Camelot camelot){
        double positionDeFin = 15600 + 1.5*largeur;
        return camelot.getPosition().getX() > positionDeFin;
    }

    public static void main(String[] args) {
        launch();
    }
}