package com.regularExpressionChecking;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getStage(primaryStage);
        primaryStage.setTitle("Regular Expression Checking App");
        primaryStage.setScene(new Scene(root, 424, 221));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
