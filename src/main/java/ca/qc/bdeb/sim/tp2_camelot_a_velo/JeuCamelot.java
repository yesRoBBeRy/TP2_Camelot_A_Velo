package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        Partie partie = new Partie();

        root.getChildren().add(canvas);

        AnimationTimer timer = animer(canvas, partie);

        introTransition(root, partie, timer);



        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        return scene;
    }

    public AnimationTimer animer(Canvas canvas, Partie partie) {
        GraphicsContext context = canvas.getGraphicsContext2D();

        return new AnimationTimer() {
            private long dernierTemps = 0L;
            private boolean premiereImage = true;

            @Override
            public void handle(long temps) {
                if(premiereImage){
                    dernierTemps = temps;
                    premiereImage = false;
                    return;
                }

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;

                partie.update(deltaTemps);
                partie.draw(context);
                partie.drawHUD(context);

                dernierTemps = temps;

                if (partie.checkFin()) {
                    context.clearRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);
                    stop();
                }
            }
        };
    }
    public void introTransition(Pane root, Partie partie, AnimationTimer timer) {
        int niveau = partie.getNiveau();
        Text texte = new Text("Niveau " + niveau);
        texte.setFill(Color.GREEN);
        texte.setFont(Font.font(70));

        //Centrer le texte
        double hauteur = texte.getLayoutBounds().getHeight();
        double largeur = texte.getLayoutBounds().getWidth();
        texte.setX((root.getWidth() - largeur) / 2);
        texte.setY((root.getHeight() - hauteur) / 2);

        root.getChildren().add(texte);

        //Délai de 3 secondes au début
        PauseTransition transition = new PauseTransition(Duration.seconds(3));

        transition.setOnFinished(event ->{
            root.getChildren().remove(texte);
            timer.start();
        });
        transition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}