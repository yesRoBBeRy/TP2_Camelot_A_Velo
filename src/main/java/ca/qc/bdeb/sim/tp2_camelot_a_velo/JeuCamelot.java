package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private boolean debugCollision = false;
    private boolean debugChamp = false;
    private boolean kEnfonce = false;
    private boolean qEnfonce = false;
    private boolean iEnfonce = false;
    private AnimationTimer timerActuel = null;

    @Override
    public void start(Stage stage) {
        var scene = sceneJeu();

        stage.setTitle("Camelot à vélo");
        stage.setScene(scene);
        stage.getIcons().add(new Image("journal.png"));
        stage.setResizable(false);
        stage.show();
    }

    private Scene sceneJeu() {
        //Structure
        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);
        root.setStyle("-fx-background-color: #000000;");
        Canvas canvas = new Canvas(largeur, hauteur);

        root.getChildren().add(canvas);

        premierePartie(root, canvas);

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

    public void toucheDeboggage(Partie partie) {
        if (Input.isKeyPressed(KeyCode.D)) {
            debugCollision = !debugCollision;
            Input.setKeyPressed(KeyCode.D, false);
        }
        if (Input.isKeyPressed(KeyCode.F)) {
            debugChamp = !debugChamp;
            Input.setKeyPressed(KeyCode.F, false);
        }
        if(Input.isKeyPressed(KeyCode.Q)) {
            if(!qEnfonce) {
                partie.ajouterJournaux();
                qEnfonce = true;
            }
        } else qEnfonce = false;

        if(Input.isKeyPressed(KeyCode.K)) {
            if(!kEnfonce) {
                partie.renitialiserJournaux();
                kEnfonce = true;
            }
        } else kEnfonce = false;

        if(Input.isKeyPressed(KeyCode.I)) {
            if(!iEnfonce) {
                partie.deboggageParticules();
                iEnfonce = true;
            }
        } else iEnfonce = false;
    }


    public AnimationTimer animer(Canvas canvas, Partie partie, Pane root) {
        GraphicsContext context = canvas.getGraphicsContext2D();

        return new AnimationTimer() {
            private long dernierTemps = 0L;
            private boolean premiereImage = true;

            @Override
            public void handle(long temps) {
                if (premiereImage) {
                    dernierTemps = temps;
                    premiereImage = false;
                    return;
                }

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;

                context.setFill(Color.BLACK);
                context.fillRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);

                partie.update(deltaTemps);
                partie.draw(context);

                toucheDeboggage(partie);
                if (debugCollision || debugChamp) {
                    partie.drawDebug(context,debugCollision, debugChamp);
                }
                boolean deboggageSkipNiveau = Input.isKeyPressed(KeyCode.L); //skipNiveau

                dernierTemps = temps;

                if (partie.checkFin()) {
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.play();
                    context.clearRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);
                    finDePartie(root, partie, this);
                }
                if(partie.checkNouveauNiveau(deboggageSkipNiveau)){
                    transitions(root, partie, canvas, context);
                }

            }
        };
    }

    public void premierePartie(Pane root, Canvas canvas) {
        Partie partie = new Partie(0, 0);
        transitions(root, partie, canvas, canvas.getGraphicsContext2D());
    }

    public void transitions(Pane root, Partie partie,Canvas canvas, GraphicsContext context) {
        context.clearRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);
        if(timerActuel != null) {
            timerActuel.stop();
        }
        int niveau = partie.getNiveau() + 1;

        int nouveauNbJournaux = partie.getNbJournaux() + 12;
        Text texte = new Text("Niveau " + niveau);
        texte.setFill(Color.GREEN);

        positionnerTexte(texte, root);

        root.getChildren().add(texte);

        //Délai de 3 secondes au début
        PauseTransition transition = new PauseTransition(Duration.seconds(3));

        transition.setOnFinished(event -> {
            root.getChildren().remove(texte);
            Partie prochainePartie = new Partie(niveau, nouveauNbJournaux);
            tempsTotal = 0;
            timerActuel = animer(canvas, prochainePartie, root);
            timerActuel.start();
        });
        transition.play();
    }

    public void finDePartie(Pane root, Partie partie, AnimationTimer timer) {
        timer.stop();

        int argentCollecte = partie.getCAMELOT().getArgentTotal();

        Text texte1 = new Text("Rupture de stock!");
        texte1.setFill(Color.RED);
        positionnerTexte(texte1, root);

        Text texte2 = new Text("Argent collecté : " + argentCollecte + "$");
        texte2.setFill(Color.GREEN);
        positionnerTexte(texte2, root);
        texte2.setY(texte2.getY() + 200);

        root.getChildren().addAll(texte1, texte2);

        PauseTransition transition = new PauseTransition(Duration.seconds(6));
        transition.setOnFinished(event -> {
            root.getChildren().removeAll(texte1, texte2);

            Partie nouvellePartie = new Partie(1, 12);

            Canvas canvas = (Canvas) root.getChildren().getFirst();

            tempsTotal = 0;

            timerActuel = animer(canvas, nouvellePartie, root);
            timerActuel.start();
        });
        transition.play();
    }

    private void positionnerTexte(Text texte1, Pane root) {
        texte1.setFont(Font.font(70));

        //Centrer le texte
        double hauteur1 = texte1.getLayoutBounds().getHeight();
        double largeur1 = texte1.getLayoutBounds().getWidth();
        texte1.setX((root.getWidth() - largeur1) / 2);
        texte1.setY((root.getHeight() - hauteur1) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}