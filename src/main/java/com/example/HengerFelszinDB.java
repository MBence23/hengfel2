package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HengerFelszinDB extends Application {
    
    private static final String DB_URL = "jdbc:sqlite:henger.db"; 
    
    @Override
    public void start(Stage primaryStage) {
        Label lblSugár = new Label("Sugár (r):");
        TextField txtSugár = new TextField();
        
        Label lblMagasság = new Label("Magasság (h):");
        TextField txtMagasság = new TextField();
        
        Button btnSzámít = new Button("Számítás és Mentés");
        Label lblEredmény = new Label("A felszín: ");


        inicializalAdatbazis();

        btnSzámít.setOnAction(e -> {
            try {
                double r = Double.parseDouble(txtSugár.getText());
                double h = Double.parseDouble(txtMagasság.getText());
                
                double felszín = 2 * Math.PI * r * (r + h);
                lblEredmény.setText(String.format("A felszín: %.2f", felszín));

                mentesAdatbazisba(r, h, felszín);
            } catch (NumberFormatException ex) {
                lblEredmény.setText("Hibás bevitel! Adj meg számokat.");
            }
        });

        VBox root = new VBox(10, lblSugár, txtSugár, lblMagasság, txtMagasság, btnSzámít, lblEredmény);
        root.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(root, 350, 300);
        primaryStage.setTitle("Henger Felszín Számítás és Mentés");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void inicializalAdatbazis() {
        String sql = "CREATE TABLE IF NOT EXISTS henger_adatok (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "sugar REAL, " +
                     "magassag REAL, " +
                     "felszin REAL" +
                     ");";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Hiba az adatbázis inicializálásakor: " + e.getMessage());
        }
    }

    private void mentesAdatbazisba(double r, double h, double felszin) {
        String sql = "INSERT INTO henger_adatok (sugar, magassag, felszin) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, r);
            pstmt.setDouble(2, h);
            pstmt.setDouble(3, felszin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Hiba az adatok mentésekor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
