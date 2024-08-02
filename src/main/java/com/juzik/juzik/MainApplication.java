package com.juzik.juzik;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 600);
        stage.setMinHeight(600);
        stage.setMinWidth(750);
        stage.setTitle("Juzik");
        stage.setScene(scene);
//        if (OsThemeDetector.getDetector().isDark()) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
//        } else {
//            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//        }
        stage.show();

//        final OsThemeDetector detector = OsThemeDetector.getDetector();
//        detector.registerListener(isDark -> Platform.runLater(() -> {
//            if (isDark) {
//                Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
//            } else {
//                Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//            }
//        }));
    }

    public static void main(String[] args) {
        launch();
    }
}