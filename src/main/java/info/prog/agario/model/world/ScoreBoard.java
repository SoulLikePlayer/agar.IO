package info.prog.agario.model.world;

import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Player;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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

        tableView = new TableView<>();
        tableView.setPrefWidth(300);
        tableView.setPrefHeight(200);

        TableColumn<PlayerScore, Number> rankColumn = new TableColumn<>("Rang");
        rankColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRank()));

        TableColumn<PlayerScore, String> nameColumn = new TableColumn<>("Pseudo");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPseudo()));

        TableColumn<PlayerScore, Number> massColumn = new TableColumn<>("Masse");
        massColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMass()));

        // Ajout des colonnes au tableau
        tableView.getColumns().addAll(rankColumn, nameColumn, massColumn);
        tableView.setItems(scores);

        this.getChildren().add(tableView);
    }

    // Mise à jour du scoreboard
    public void updateScores() {
        List<PlayerScore> updatedScores = new ArrayList<>();

        // Ajouter le joueur
        updatedScores.add(new PlayerScore(gameWorld.getPlayer().getPseudo(),
                gameWorld.getPlayer().getPlayerGroup().getMass()));

        // Ajouter les ennemis
        for (Enemy enemy : gameWorld.getEnemies()) {
            updatedScores.add(new PlayerScore(enemy.getPseudo(), enemy.getEnemyGroup().getMass()));
        }

        // Trier par masse décroissante
        updatedScores.sort(Comparator.comparingDouble(PlayerScore::getMass).reversed());

        // Assignation des rangs
        for (int i = 0; i < updatedScores.size(); i++) {
            updatedScores.get(i).setRank(i + 1);
        }

        scores.setAll(updatedScores);

        // Debugging
        System.out.println("Mise à jour du ScoreBoard : " + updatedScores);
    }

    // Classe interne pour stocker les infos du scoreboard
    public class PlayerScore {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty pseudo;
        private final SimpleDoubleProperty mass;

        public PlayerScore(String pseudo, double mass) {
            this.rank = new SimpleIntegerProperty(0);  // Initialiser le rang à 0
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
