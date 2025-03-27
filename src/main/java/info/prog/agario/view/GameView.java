package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.model.world.GameWorld;

public class GameView  {
    private Scene scene;
    private AnchorPane mainRoot;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private MiniMap miniMap;





    public GameView(String pseudo) {
        mainRoot = new AnchorPane();
        root = new Pane();
        root.setPrefSize(10000, 10000);
        world = new GameWorld(pseudo);
        controller = new GameController(world, root);
        scene = new Scene(mainRoot, 1080, 720);
        controller.initialize();
        miniMap = new MiniMap(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());
        miniMap.updateEntities(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());



        mainRoot.getChildren().add(root);
        AnchorPane.setBottomAnchor(miniMap, 10.);
        AnchorPane.setRightAnchor(miniMap, 10.);
        mainRoot.getChildren().add(miniMap);


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!world.getPlayer().getPlayerGroup().getCells().isEmpty()){
                    miniMap.updateEntities(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());


                }
            }
        }.start();
    }


    public Scene getScene() {
        return scene;
    }
}
