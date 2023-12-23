package com.malyart;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayController {

    // Récupérer la valeur depuis le modèle partagé et l'afficher dans un Label
    private String selectedOption = SelectSalle.getInstance().getSelectedOption();
    private String csvData = "./data.csv";
    private String csvAlert = "./alert.csv";
    private String targetRoom = selectedOption; 


    // Renommez mutableValues en Valeurs
    private double[] Valeurs = new double[9];

    @FXML
    private Label labelSalle;
    @FXML
    private Button buttonQuitter;
    @FXML
    private TextArea temperatureTextArea;
    @FXML
    private TextArea humidityTextArea;
    @FXML
    private TextArea co2TextArea;
    @FXML
    private TextArea activityTextArea;
    @FXML
    private TextArea tvocTextArea;
    @FXML
    private TextArea illuminationTextArea;
    @FXML
    private TextArea infraredTextArea;
    @FXML
    private TextArea infrared_and_visibleTextArea;
    @FXML
    private TextArea pressureTextArea;

    // Ajouter Timer et TimerTask
    private Timer refreshTimer;

    @FXML
    public void initialize() throws CsvException {
        labelSalle.setText(targetRoom);

        // Initialiser Timer et TimerTask
        refreshTimer = new Timer(true);
        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        };

        // Démarrer TimerTask toutes les 5000 millisecondes (5 secondes)
        refreshTimer.scheduleAtFixedRate(refreshTask, 0, 5000);
    }

    private void refreshData() {

        
        // Réinitialiser Valeurs
        for (int i = 0; i < Valeurs.length; i++) {
            Valeurs[i] = 0.0;
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvData))) {
            List<String[]> dataLignes = reader.readAll();

            for (String[] dataLigne : dataLignes) {
                if (dataLigne.length > 0 && dataLigne[0].equals(targetRoom)) {
                    Valeurs[0] = Double.parseDouble(dataLigne[2]);
                    Valeurs[1] = Double.parseDouble(dataLigne[3]);
                    Valeurs[2] = Integer.parseInt(dataLigne[4]);
                    Valeurs[3] = Integer.parseInt(dataLigne[5]);
                    Valeurs[4] = Integer.parseInt(dataLigne[6]);
                    Valeurs[5] = Integer.parseInt(dataLigne[7]);
                    Valeurs[6] = Integer.parseInt(dataLigne[8]);
                    Valeurs[7] = Integer.parseInt(dataLigne[9]);
                    Valeurs[8] = Double.parseDouble(dataLigne[10]);
                }
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Afficher les nouvelles valeurs
                    temperatureTextArea.setText(String.valueOf(Valeurs[0]));
                    humidityTextArea.setText(String.valueOf(Valeurs[1]));
                    co2TextArea.setText(String.valueOf(Valeurs[2]));
                    activityTextArea.setText(String.valueOf(Valeurs[3]));
                    tvocTextArea.setText(String.valueOf(Valeurs[4]));
                    illuminationTextArea.setText(String.valueOf(Valeurs[5]));
                    infraredTextArea.setText(String.valueOf(Valeurs[6]));
                    infrared_and_visibleTextArea.setText(String.valueOf(Valeurs[7]));
                    pressureTextArea.setText(String.valueOf(Valeurs[8]));
                }
            });

        } catch (IOException | NumberFormatException | CsvException e) {
            e.printStackTrace();
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvAlert))) {
            List<String[]> dataLignes = reader.readAll();

            for (String[] dataLigne : dataLignes) {
                if (dataLigne.length > 0 && dataLigne[0].equals(targetRoom)) {
                    if (dataLigne[2] != null) {
                        // Remet tout en noir puis change la couleur des éléments qui dépassent les limites
                        Platform.runLater(() -> updateColor(""));
                        Platform.runLater(() -> updateColor(dataLigne[2]));
                    } else {
                        // Remet tout en noir
                         Platform.runLater(() -> updateColor(""));
                    }
                }
            }
        } catch (IOException | NumberFormatException | CsvException e) {
            e.printStackTrace();
        }

    }

    private void updateColor(String alert) {
        // Remet tout en noir
        temperatureTextArea.setStyle("-fx-text-fill: black;");
        humidityTextArea.setStyle("-fx-text-fill: black;");
        co2TextArea.setStyle("-fx-text-fill: black;");
        activityTextArea.setStyle("-fx-text-fill: black;");
        tvocTextArea.setStyle("-fx-text-fill: black;");
        illuminationTextArea.setStyle("-fx-text-fill: black;");
        infraredTextArea.setStyle("-fx-text-fill: black;");
        infrared_and_visibleTextArea.setStyle("-fx-text-fill: black;");
        pressureTextArea.setStyle("-fx-text-fill: black;");
    
        // Change la couleur des éléments qui dépassent les limites
        if (alert.contains("temperature")) {
            temperatureTextArea.setStyle("-fx-text-fill: red;");
        } 
        if (alert.contains("humidity")) {
            humidityTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("co2")) {
            co2TextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("activity")) {
            activityTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("tvoc")) {
            tvocTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("illumination")) {
            illuminationTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("infrared ")) {
            infraredTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("infrared_and_visible")) {
            infrared_and_visibleTextArea.setStyle("-fx-text-fill: red;");
        }
        if (alert.contains("pressure")) {
            pressureTextArea.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void actionQuitter() throws IOException {
        Stage stage = (Stage) buttonQuitter.getScene().getWindow();
        stage.close();
    }
}
