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
    private boolean particulesDebug = false;
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

    /**
     * Création de la scene de jeu
     * - On met le fond noir
     * - On appelle premierePartie() pour débuter le jeu
     * @return Scene
     */
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

    /**
     * Méthode qui s'occupe de vérifier si les touches de débogage sont enfoncées
     * et ainsi appeler leurs méthodes respectives
     * @param partie : Partie actuelle
     */
    public void toucheDeboggage(Partie partie) {
        if (Input.isKeyPressed(KeyCode.D)) {
            debugCollision = !debugCollision;
            Input.setKeyPressed(KeyCode.D, false);
        }
        if (Input.isKeyPressed(KeyCode.F)) {
            debugChamp = !debugChamp;
            Input.setKeyPressed(KeyCode.F, false);
        }
        if (Input.isKeyPressed(KeyCode.Q)) {
            if (!qEnfonce) {
                partie.ajouterJournaux();
                qEnfonce = true;
            }
        } else qEnfonce = false;

        if (Input.isKeyPressed(KeyCode.K)) {
            if (!kEnfonce) {
                partie.renitialiserJournaux();
                kEnfonce = true;
            }
        } else kEnfonce = false;

        if (Input.isKeyPressed(KeyCode.I)) {
            if (!iEnfonce) {
                particulesDebug = !particulesDebug;
                partie.deboggageParticules(particulesDebug);
                iEnfonce = true;
            }
        } else iEnfonce = false;
    }

    /**
     * Création de l'AnimationTimer
     *
     * @param canvas : Canvas
     * @param partie : Partie actuelle
     * @param root   : Pane
     * @return AnimationTimer
     */
    public AnimationTimer animer(Canvas canvas, Partie partie, Pane root) {
        GraphicsContext context = canvas.getGraphicsContext2D();

        return new AnimationTimer() {
            private long dernierTemps = 0L;
            private boolean premiereImage = true;

            @Override
            public void handle(long temps) {
                //Pour les bugs de premiere image
                if (premiereImage) {
                    dernierTemps = temps;
                    premiereImage = false;
                    return;
                }

                double deltaTemps = (temps - dernierTemps) * 1e-9;
                tempsTotal += deltaTemps;

                context.setFill(Color.BLACK);
                context.fillRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);

                //Mises à jour
                partie.update(deltaTemps);
                partie.draw(context);

                //Deboggage
                toucheDeboggage(partie);
                if (debugCollision || debugChamp) {
                    partie.drawDebug(context, debugCollision, debugChamp);
                }
                boolean deboggageSkipNiveau = Input.isKeyPressed(KeyCode.L); //skipNiveau

                dernierTemps = temps;

                //Vérification de fin du niveau
                if (partie.checkFin()) {
                    context.clearRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);
                    finDePartie(root, partie);
                }
                if (partie.checkNouveauNiveau(deboggageSkipNiveau)) {
                    transitions(root, partie, canvas);
                }

            }
        };
    }

    /**
     * Initialise la première partie du jeu
     * <p>
     * Utilise une partie transitoire pour faciliter l'utilisation du reste du code
     *
     * @param root   Le Pane où sera dessiné les objets
     * @param canvas : Canvas
     */
    public void premierePartie(Pane root, Canvas canvas) {
        //Partie seulement utilisée pour avoir une partie transitoire, elle est jamais utilisée
        Partie partie = new Partie(0, 0, 0);
        transitions(root, partie, canvas);
    }

    /**
     * Méthode qui s'occupe de la transition entre les différents niveaux.
     *
     * @param root   : Pane
     * @param partie : Ancienne partie
     * @param canvas : Canvas actuel
     */
    public void transitions(Pane root, Partie partie, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, JeuCamelot.largeur, JeuCamelot.hauteur);
        //Arrêt de l'AnimationTimerActuel
        if (timerActuel != null) {
            timerActuel.stop();
        }
        //On ajoute 1 au niveau précédent
        int niveau = partie.getNiveau() + 1;

        //On ajoute 12 aux anciens journaux
        int nouveauNbJournaux = partie.getNbJournaux() + 12;

        //On garde l'argent déjà collecté
        int argentTotal = partie.getCAMELOT().getArgentTotal();
        Text texte = new Text("Niveau " + niveau);
        texte.setFill(Color.GREEN);

        positionnerTexte(texte, root);

        root.getChildren().add(texte);

        //Délai de 3 secondes au début
        PauseTransition transition = new PauseTransition(Duration.seconds(3));

        transition.setOnFinished(event -> {
            root.getChildren().remove(texte);
            Partie prochainePartie = new Partie(niveau, nouveauNbJournaux, argentTotal);
            tempsTotal = 0;
            timerActuel = animer(canvas, prochainePartie, root);
            timerActuel.start();
        });
        transition.play();
    }

    /**
     * Méthode qui s'occupe de montrer les information lorsque la partie est perdue
     * et appelle ensuite creerPauseFinDePartie.
     *
     * @param root   : Pane
     * @param partie : Dernière partie jouée
     */
    public void finDePartie(Pane root, Partie partie) {
        timerActuel.stop();

        //Texte quand on perd la partie
        Text texte1 = new Text("Rupture de stock!");
        texte1.setFill(Color.RED);
        positionnerTexte(texte1, root);

        //Texte qui montre l'argent collecté
        int argentCollecte = partie.getCAMELOT().getArgentTotal();

        Text texte2 = new Text("Argent collecté : " + argentCollecte + "$");
        texte2.setFill(Color.GREEN);
        positionnerTexte(texte2, root);
        texte2.setY(texte2.getY() + 200);

        root.getChildren().addAll(texte1, texte2);

        PauseTransition transition = creerPauseFinDePartie(root, texte1, texte2);
        transition.play();
    }

    /**
     * Création de la PauseTransition de 3 seconde quand on perd la partie.
     *
     * @param root   : Pane
     * @param texte1 : Texte de rupture de stock
     * @param texte2 : Texte de l'argent collecté
     * @return PauseTransition
     */
    private PauseTransition creerPauseFinDePartie(Pane root, Text texte1, Text texte2) {
        PauseTransition transition = new PauseTransition(Duration.seconds(3));
        transition.setOnFinished(event -> {
            root.getChildren().removeAll(texte1, texte2);
            Canvas canvas = (Canvas) root.getChildren().getFirst();
            tempsTotal = 0;
            //On recommence le jeu
            premierePartie(root, canvas);
        });
        return transition;
    }

    /**
     * Méthode qui positionne le texte de transition
     * Pour éviter la redondance dans les méthodes, voici ce que nous avons créé
     *
     * @param texte1 : Texte à placer
     * @param root   : Pane
     */
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