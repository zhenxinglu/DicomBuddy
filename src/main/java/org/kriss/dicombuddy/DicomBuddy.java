package org.kriss.dicombuddy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class DicomBuddy extends Application {
    private static DicomBuddy instance;

    private TreeTableView<DicomAttribute> treeTableView;

    @Override
    public void init() throws Exception {
        super.init();
        instance = this;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1024, 768);

        Image appIcon = new Image(getClass().getResourceAsStream("/icons/dicomBuddy.png"));

        primaryStage.getIcons().add(appIcon);
        primaryStage.setTitle("DICOM Buddy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static DicomBuddy getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch();
    }

}