package info.prog.agario.model.world;

import info.prog.agario.model.entity.ai.Enemy;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard extends VBox {
    private TableView<PlayerScore> tableView;
    private ObservableList<PlayerScore> scores;
    private GameWorld gameWorld;

    /**
     * Constructor of the ScoreBoard class
     * @param gameWorld The game world to which the ScoreBoard is attached
     */
   public ScoreBoard(GameWorld gameWorld) {
    this.gameWorld = gameWorld;
    this.scores = FXCollections.observableArrayList();

    // Titre principal
    Label title = new Label("ScoreBoard");
    title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

    tableView = new TableView<>();
    tableView.setPrefWidth(200);
    tableView.setPrefHeight(300);
    tableView.setFixedCellSize(25);
    tableView.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"); // Fond transparent et texte en blanc

    tableView.setMouseTransparent(true);

    tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
        tableView.lookup(".column-header-background").setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    });

    this.setBackground(new Background(new BackgroundFill(
            Color.rgb(50, 50, 50, 0.6), // Gris foncé avec opacité faible
            new CornerRadii(10),
            Insets.EMPTY
    )));
    this.setPadding(new Insets(5));
    this.setSpacing(5);

    TableColumn<PlayerScore, Number> rankColumn = new TableColumn<>();
    rankColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRank()));

    TableColumn<PlayerScore, String> nameColumn = new TableColumn<>();
    nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPseudo()));

    TableColumn<PlayerScore, Number> massColumn = new TableColumn<>();
    massColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMass()));

    rankColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");
    nameColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");
    massColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");

    tableView.getColumns().addAll(rankColumn, nameColumn, massColumn);
    tableView.setItems(scores);

    tableView.setPlaceholder(null);

    tableView.setRowFactory(tv -> {
        TableRow<PlayerScore> row = new TableRow<>();
        row.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        return row;
    });

    this.getChildren().addAll(title, tableView);
}

    /**
     * Update the ScoreBoard with the players and enemies scores
     */
    public void updateScores() {
        List<PlayerScore> updatedScores = new ArrayList<>();

        updatedScores.add(new PlayerScore(gameWorld.getPlayer().getPseudo(),
                gameWorld.getPlayer().getPlayerGroup().getMass()));

        for (Enemy enemy : gameWorld.getEnemies()) {
            updatedScores.add(new PlayerScore(enemy.getPseudo(), enemy.getEnemyGroup().getMass()));
        }

        updatedScores.sort(Comparator.comparingDouble(PlayerScore::getMass).reversed());

        if (updatedScores.size() > 10) {
            updatedScores = updatedScores.subList(0, 10);
        }

        for (int i = 0; i < updatedScores.size(); i++) {
            updatedScores.get(i).setRank(i + 1);
        }

        scores.setAll(updatedScores);
    }

    /**
     * Inner class representing a player score
     */
    public class PlayerScore {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty pseudo;
        private final SimpleDoubleProperty mass;

        /**
         * Constructor of the PlayerScore class
         * @param pseudo The pseudo of the player
         * @param mass The mass of the player
         */
        public PlayerScore(String pseudo, double mass) {
            this.rank = new SimpleIntegerProperty(0);
            this.pseudo = new SimpleStringProperty(pseudo);
            this.mass = new SimpleDoubleProperty(mass);
        }

        /**
         * Get the Player's rank
         * @return int the rank of the player
         */
        public int getRank() { return rank.get(); }

        /**
         * Set the Player's rank
         * @param rank the new rank of the player
         */
        public void setRank(int rank) { this.rank.set(rank); }

        /**
         * Get the Player's pseudo
         * @return String the pseudo of the player
         */
        public String getPseudo() { return pseudo.get(); }

        /**
         * Get the Player's mass
         * @return double the mass of the player
         */
        public double getMass() { return mass.get(); }

        /**
         * Override the toString method
         * @return String the string representation of the PlayerScore
         */
        @Override
        public String toString() {
            return "Rank: " + rank + ", Pseudo: " + pseudo + ", Mass: " + mass;
        }
    }
}
