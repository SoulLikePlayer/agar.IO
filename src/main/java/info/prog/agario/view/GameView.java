package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.model.world.GameWorld;

public class GameView {
    private Scene scene;
    private Pane mainRoot;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private MiniMap miniMap;

    private ListView<String> listView;
    private ObservableList<String> effectsList;

    public GameView(String pseudo) {
        mainRoot = new Pane();
        root = new Pane();
        world = new GameWorld(pseudo);
        controller = new GameController(world, root);
        scene = new Scene(mainRoot, 800, 600);
        miniMap = new MiniMap(world.getEntities(), world.getPlayer());
        controller.initialize();

        // Initialisation de la ListView
        listView = new ListView<>();
        effectsList = FXCollections.observableArrayList();
        listView.setItems(effectsList);

        //miniMap.setLayoutX(scene.getWidth() - 210); // Décalage pour qu'elle reste visible
        //miniMap.setLayoutY(scene.getHeight() - 210);

// Position de la ListView en haut à droite
        //listView.setLayoutX(scene.getWidth() - 200);
        //listView.setLayoutY(10);
        //listView.setPrefWidth(180);
        //listView.setPrefHeight(100);

        miniMap.updateEntities(world.getEntities(), world.getPlayer());
        mainRoot.getChildren().add(root);
        mainRoot.getChildren().add(miniMap);
        mainRoot.getChildren().add(listView);



        new AnimationTimer() {
            @Override
            public void handle(long now) {
                miniMap.updateEntities(world.getEntities(), world.getPlayer());
            }
        }.start();
    }

    public void addEffect(String effect, int duration) {
        effectsList.add(effect);

        // Supprime l'effet après 'duration' secondes
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration), e -> effectsList.remove(effect))
        );
        timeline.play();
    }

    public Scene getScene() {
        return scene;
    }
}