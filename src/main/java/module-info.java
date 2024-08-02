module com.juzik.juzik {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.juzik.juzik to javafx.fxml;
    exports com.juzik.juzik;
}