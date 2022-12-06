module cc102 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens cc102 to javafx.fxml;
    exports cc102;
}
