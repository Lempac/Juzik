module com.juzik.juzik {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires jaudiotagger;
    requires atlantafx.base;
    requires java.desktop;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    opens com.juzik.juzik to javafx.fxml;
    exports com.juzik.juzik;
}