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

    // Désactiver les événements de la souris
    tableView.setMouseTransparent(true);

    // Masquer les en-têtes de colonnes
    tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
        tableView.lookup(".column-header-background").setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    });

    // Appliquer un style au ScoreBoard
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

    // Supprimer les bordures des colonnes
    rankColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");
    nameColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");
    massColumn.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-text-fill: white;");

    tableView.getColumns().addAll(rankColumn, nameColumn, massColumn);
    tableView.setItems(scores);

    // Désactiver le message "vide"
    tableView.setPlaceholder(null);

    // Appliquer un style sur les lignes
    tableView.setRowFactory(tv -> {
        TableRow<PlayerScore> row = new TableRow<>();
        row.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        return row;
    });

    this.getChildren().addAll(title, tableView);
}

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

    public class PlayerScore {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty pseudo;
        private final SimpleDoubleProperty mass;

        public PlayerScore(String pseudo, double mass) {
            this.rank = new SimpleIntegerProperty(0);
            this.pseudo = new SimpleStringProperty(pseudo);
            this.mass = new SimpleDoubleProperty(mass);
        }

        public int getRank() { return rank.get(); }
        public void setRank(int rank) { this.rank.set(rank); }

        public String getPseudo() { return pseudo.get(); }
        public double getMass() { return mass.get(); }

        @Override
        public String toString() {
            return "Rank: " + rank + ", Pseudo: " + pseudo + ", Mass: " + mass;
        }
    }
}
