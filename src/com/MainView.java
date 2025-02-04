package com;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import com.controller.Client;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar la vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);

        // Configurar y mostrar el escenario
        primaryStage.setTitle(Client.getClientInstance().getNick());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

 
    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX (hilo principal para la interfaz)
        launch(args);
    }
}
