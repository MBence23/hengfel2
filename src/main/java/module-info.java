module com.example {
    requires transitive javafx.graphics;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example to javafx.fxml;
    exports com.example;
}
