package com;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import com.controller.Client;

public class MainView extends Application {

    public static Client clientInstance = null;

    public static Client getClient(String name) {
        if (clientInstance != null) {
            return clientInstance;
        } else {
            return new Client(name);
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Crear un diálogo de entrada de texto para pedir el nombre del usuario
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ingreso de Nombre");
        dialog.setHeaderText("Por favor ingresa tu nombre");
        dialog.setContentText("Nombre:");

        // Mostrar el diálogo y esperar la entrada
        Optional<String> result = dialog.showAndWait();

        // Si el usuario introduce un nombre, lo mostramos en la consola
        result.ifPresent(name -> System.out.println("Bienvenido, " + name + "!"));

        clientInstance = getClient(result.get());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle(result.get());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
