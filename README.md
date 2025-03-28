# Agar.IO en JavaFX

## Branches

- **mergeVersionLocale** : Utilisée pour jouer en local.
- **Server** : Utilisée pour le jeu en réseau.

## Lancer l'application

Pour lancer l'application, il suffit de démarrer la méthode `main`.

## Fonctionnalités

### 1. Jeu en local

La branche `mergeVersionLocale` permet de jouer en local. Voici quelques fonctionnalités clés :

- **Lancement du jeu** : La méthode `main` dans `Main.java` lance l'application en JavaFX.
- **Contrôle du joueur** : Le joueur peut se déplacer et interagir avec l'environnement du jeu.
- **Gestion des entités** : Le jeu gère différentes entités comme les cellules et les pellets à travers des classes comme `GameWorld`, `EntityFactory`, et `GameController`.

### 2. Jeu en réseau

La branche `Server` permet de jouer en réseau. Voici quelques fonctionnalités clés :

- **Connexion au serveur** : La classe `GameClient` se connecte à un serveur de jeu pour permettre le jeu en réseau.
- **Serveur de jeu** : La classe `GameServer` gère les connexions des clients et maintient l'état du jeu à jour pour tous les joueurs connectés.
- **Communication client-serveur** : Les clients envoient et reçoivent des messages pour synchroniser l'état du jeu à travers des méthodes comme `sendPlayerPosition` et `listenForMessages`.

### 3. Autres fonctionnalités

- **Caméra** : La classe `Camera` gère le suivi et le zoom de la vue en fonction de la position et de la taille des cellules du joueur.
- **Interface utilisateur** : Le fichier FXML `hello-view.fxml` définit l'interface utilisateur pour la connexion des joueurs.

### Exemples de code

#### Lancement du jeu
```java
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new GameLauncher().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

#### Client de jeu en réseau
```java
public class GameClient {
    public void connect() {
        // Code pour se connecter au serveur et gérer la communication
    }
}
```

#### Serveur de jeu
```java
public class GameServer {
    public void start() {
        // Code pour démarrer le serveur et gérer les clients
    }
}
```

Pour plus de détails, vous pouvez consulter les fichiers source du projet.

Bonne lecture et bon jeu !
