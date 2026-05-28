/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package agencia_viajes;

import javafx.application.Application;
import javafx.stage.Stage;

public class Agencia_Viajes extends Application {

    @Override
    public void start(Stage primaryStage) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new PantallaIncio().setVisible(true);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}