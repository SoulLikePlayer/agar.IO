module info.prog.agario {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens info.prog.agario to javafx.fxml;
    exports info.prog.agario.launcher;
}