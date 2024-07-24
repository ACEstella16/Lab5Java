module com.example.lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    opens com.example.lab5 to javafx.fxml;
    exports com.example.lab5;
}