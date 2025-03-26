package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.model.world.GameWorld;

public class GameView implements EffectListener {
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
        listView = new ListView<>();

        controller.setEffectListener(this);
        scene.getStylesheets().add(getClass().getResource("/info/prog/agario/view/style.css").toExternalForm());
        controller.initialize();
        effectsList = FXCollections.observableArrayList();
        listView.setItems(effectsList);
        listView.setVisible(false);


        effectsList.addListener((ListChangeListener<String>) change -> {
            listView.setVisible(!effectsList.isEmpty());
            adjustListViewSize();
        });

        mainRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                updateListeViewPlace();
                System.out.println("***************************************************************");
            }
        });


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

    public void updateListeViewPlace(){
        listView.setLayoutX(mainRoot.getWidth()-250);
    }
    @Override
    public void onEffectActivated(String effect, int duration) {
        effectsList.add(effect);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration), e -> effectsList.remove(effect))
        );
        timeline.play();
    }

    public Scene getScene() {
        return scene;
    }


    private void adjustListViewSize() {
        int itemCount = effectsList.size();
        double itemHeight = 30  ;
        double listHeight = itemCount * itemHeight+30;
        listView.setPrefHeight(listHeight);
    }
}

