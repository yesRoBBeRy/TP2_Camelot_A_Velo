package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        Random rand = new Random();
        //positions et adresses
        int[] positionsPortes = new int[12];
        int j = 1300;
        int k = 0;
        while (j <= 15600) {
            positionsPortes[k] = j;
            j += 1300;
            k++;
        }
        int[] adresses = new int[12];
        adresses[0] = rand.nextInt(100, 950);
        for (int i = 1; i < adresses.length; i++) {
            adresses[i] = adresses[i - 1] + 2;
        }
        //Structure
        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        Camera camera = new Camera(Point2D.ZERO);

        List<ObjetDuJeu> objetsDuJeu = new ArrayList<>();

        for (int i = 0; i < positionsPortes.length; i++) {
            objetsDuJeu.add(new Maison(new Point2D(0, 0), new Point2D(0, 0), positionsPortes[i], adresses[i]));
        }

        Camelot camelot = new Camelot(new Point2D(100, 400), new Point2D(.2 * largeur, hauteur - 144));
        objetsDuJeu.add(camelot);

        double masse = rand.nextDouble(2);

        List<Journal> journaux = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            Journal journal = new Journal(new Point2D(0, 0), new Point2D(0, 0), masse, camelot);
            objetsDuJeu.add(journal);
            camelot.addJournaux(journal);
            journaux.add(journal);
        }
        //Liste d'objet susceptibles d'entrer en collision
        List<Collisions> collisionsStatiques = new ArrayList<>();

        //Ajout des objets qui peuvent entrer en collision sauf les journaux
        for(ObjetDuJeu obj : objetsDuJeu){
            if(obj instanceof Maison) {
                Fenetre[] fenetre = ((Maison) obj).getFenetres();
                BoiteAuLettre boite = ((Maison) obj).getBoiteAuLettre();

                collisionsStatiques.addAll(Arrays.asList(fenetre));
                collisionsStatiques.add(boite);
            }
        }

        List<ParticuleChargee> particuleChargees = new ArrayList<>();
        int niveau = 2;
        int nbParticules = Math.min((niveau - 1) * 30, 400);

        for(int i = 0; i < nbParticules; i++){
            ParticuleChargee p = new ParticuleChargee(Point2D.ZERO,Point2D.ZERO);
            particuleChargees.add(p);
            objetsDuJeu.add(p);
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
                //Détection de collisions
                for(Journal journal : journaux){
                    if(!journal.getPeutCollision()) continue;

                    for(Collisions collision : collisionsStatiques){
                        if(journal.collision(collision)){
                            System.out.println("collision");
                            journal.actionApresCollision();
                            collision.actionApresCollision();
                        }
                    }
                }
                camera.update(camelot);
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
        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        return scene;
    }

    public boolean niveauComplete(Camelot camelot){
        double positionDeFin = 15600 + 1.5*largeur;
        return camelot.getPosition().getX() > positionDeFin;
    }

    public static void main(String[] args) {
        launch(args);
    }
}