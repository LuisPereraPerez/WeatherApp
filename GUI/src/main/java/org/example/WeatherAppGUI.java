package org.example;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

import static java.util.Arrays.asList;

public class WeatherAppGUI extends Application {
    private String currentIsland = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WeatherApp");

        Label titleLabel = new Label("Weather App");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #000000;"); // Blanco

        Button btnHome = new Button("Home");

        Button btnElHierro = new Button("El Hierro Forecast");
        Button btnLaGomera = new Button("La Gomera Forecast");
        Button btnLaPalma = new Button("La Palma Forecast");
        Button btnTenerife = new Button("Tenerife Forecast");
        Button btnGranCanaria = new Button("Gran Canaria Forecast");
        Button btnFuerteventura = new Button("Fuerteventura Forecast");
        Button btnLanzarote = new Button("Lanzarote Forecast");
        Button btnLaGraciosa = new Button("La Graciosa Forecast");

        Button btnSunrise = new Button("Sunrise");
        Button btnSunset = new Button("Sunset");
        btnSunrise.setVisible(false);
        btnSunset.setVisible(false);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(1300);
        imageView.setFitHeight(600);

        // Crear una tabla
        TableView<List<String>> tableView = new TableView<>();
        tableView.setVisible(false);
        TableColumn<List<String>, String> columnTemp = new TableColumn<>("Temperature (ºC)");
        TableColumn<List<String>, String> columnPop = new TableColumn<>("Probably of Precipitation");
        TableColumn<List<String>, String> columnHumidity = new TableColumn<>("Humidity (%)");
        TableColumn<List<String>, String> columnClouds = new TableColumn<>("Clouds (%)");
        TableColumn<List<String>, String> columnWS = new TableColumn<>("Wind Speed (m/s)");
        TableColumn<List<String>, String> columnPT = new TableColumn<>("Prediction Time Weather");
        TableColumn<List<String>, String> columnSunriseSunset = new TableColumn<>("SunriseSunset");
        TableColumn<List<String>, String> columnEval = new TableColumn<>("Valuation");
        TableColumn<List<String>, String> columnDate = new TableColumn<>("Date");

        List<TableColumn<List<String>, String>> columnList = asList(columnTemp, columnPop, columnHumidity, columnClouds, columnWS, columnPT, columnSunriseSunset, columnEval, columnDate);

        for (int i = 0;  i < columnList.size(); ++i){
            TableColumn<List<String>, String> column = columnList.get(i);
            int j = i;
            column.setCellValueFactory(cellData -> {
                List<String> row = cellData.getValue();
                return new SimpleStringProperty(row.get(j));
            });
        }

        columnTemp.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnPop.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnHumidity.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnClouds.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnWS.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnPT.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnSunriseSunset.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnEval.prefWidthProperty().bind(tableView.widthProperty().divide(9));
        columnDate.prefWidthProperty().bind(tableView.widthProperty().divide(9));

        tableView.getColumns().addAll(columnTemp, columnPop, columnHumidity, columnClouds, columnWS, columnPT, columnSunriseSunset, columnEval, columnDate);

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(btnElHierro);
        buttonList.add(btnLaGomera);
        buttonList.add(btnLaPalma);
        buttonList.add(btnTenerife);
        buttonList.add(btnGranCanaria);
        buttonList.add(btnFuerteventura);
        buttonList.add(btnLanzarote);
        buttonList.add(btnLaGraciosa);

        List<Button> listSunriseSunsetButton = asList(btnSunrise,btnSunset);

        List<String> urls = new ArrayList<>();
        urls.add("https://upload.wikimedia.org/wikipedia/commons/2/25/Santa_Cruz_de_Tenerife_SPOT_1320.jpg");
        urls.add("https://upload.wikimedia.org/wikipedia/commons/a/ac/%28Isla_de_la_Gomera%29_La_Palma_%26_La_Gomera_Islands%2C_Canary_Islands_%28cropped%29.jpg");
        urls.add("https://upload.wikimedia.org/wikipedia/commons/f/f0/%28Isla_de_la_Palma%29_La_Palma_%26_La_Gomera_Islands%2C_Canary_Islands_%28cropped%29.jpg");
        urls.add("https://viagallica.com/canaries/img/ile_teneriffe_006_(image_satellitaire).jpg");
        urls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Gran_Canaria_wildfire_%2848590670831%29.jpg/800px-Gran_Canaria_wildfire_%2848590670831%29.jpg");
        urls.add("https://upload.wikimedia.org/wikipedia/commons/3/39/Fuerteventura_NWW.png");
        urls.add("https://upload.wikimedia.org/wikipedia/commons/9/96/Lanzarote%27s_Lunar-Like_Landscape.jpg");
        urls.add("https://imgcap.capturetheatlas.com/wp-content/uploads/2020/01/mapa-geografico-de-la-graciosa.jpg");

        List<String> islandNames = asList("ElHierro", "LaGomera", "LaPalma", "Tenerife", "GranCanaria", "Fuerteventura", "Lanzarote", "LaGraciosa");
        List<List<String>> lists = new ArrayList<>();
        for (String island : islandNames){
            List<String> list = new ArrayList<>();
            String sunrise = island + "_sunrise";
            String sunset = island + "_sunset";
            list.add(sunrise);
            list.add(sunset);
            lists.add(list);
        }

        try {
            Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Canarias-rotulado.png/800px-Canarias-rotulado.png");
            imageView.setFitWidth(1300);
            imageView.setFitHeight(600);
            imageView.setImage(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btnHome.setOnAction(e -> {
            try {
                Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Canarias-rotulado.png/800px-Canarias-rotulado.png");
                imageView.setFitWidth(1300);
                imageView.setFitHeight(600);
                imageView.setImage(image);

                currentIsland = btnHome.getText();
                btnSunrise.setVisible(false);
                btnSunset.setVisible(false);
                tableView.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        for (int i = 0; i < buttonList.size(); ++i) {
            Button button = buttonList.get(i);
            String imageUrl = urls.get(i);
            Button buttonSunrise = listSunriseSunsetButton.get(0);
            Button buttonSunset = listSunriseSunsetButton.get(1);
            String sunrise = lists.get(i).get(0);
            String sunset = lists.get(i).get(1);
            button.setOnAction(e -> {
                if (!currentIsland.equals(button.getText())) {
                    buttonSunrise.setVisible(true);
                    buttonSunset.setVisible(true);
                    tableView.setVisible(false);
                }
                showImage(imageUrl, imageView);
                currentIsland = button.getText();
                buttonSunrise.setOnAction(e1 -> {
                    String type = "sunrise";
                    tableView.setVisible(true);
                    columnSunriseSunset.setText("Sunrise Time");
                    List<List<String>> dataList = new ConexionBD().ConexionBD(sunrise, type);
                    updateTableView(dataList, tableView);
                });


                buttonSunset.setOnAction(e2 -> {
                    String type = "sunset";
                    tableView.setVisible(true);
                    columnSunriseSunset.setText("Sunset Time");
                    List<List<String>> dataList = new ConexionBD().ConexionBD(sunset, type);
                    updateTableView(dataList, tableView);
                });
            });
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        HBox buttonsHBox = new HBox(btnElHierro, btnLaGomera, btnLaPalma, btnTenerife, btnGranCanaria, btnFuerteventura, btnLanzarote, btnLaGraciosa);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setSpacing(5);

        HBox buttonsHBox1 = new HBox(btnSunrise, btnSunset);
        buttonsHBox1.setAlignment(Pos.CENTER);
        buttonsHBox1.setSpacing(5);

        HBox homeAndCollect = new HBox(btnHome);
        homeAndCollect.setAlignment(Pos.CENTER);
        homeAndCollect.setSpacing(5);

        VBox root = new VBox(titleLabel, homeAndCollect, imageView, buttonsHBox, buttonsHBox1, tableView);
        root.setStyle("-fx-background-color: #add8e6;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        VBox.setMargin(imageView, new Insets(0, 0, 20, 0));

        scrollPane.setContent(root);
        primaryStage.setScene(new Scene(new BorderPane(scrollPane), 1500, 800));
        primaryStage.show();
    }

    private void showImage(String imageUrl, ImageView imageView) {
        try {
            Image image = new Image(imageUrl);
            imageView.setFitWidth(600);
            imageView.setFitHeight(600);
            imageView.setImage(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateTableView(List<List<String>> dataList, TableView<List<String>> tableView) {
        ObservableList<List<String>> tableData = FXCollections.observableArrayList(dataList);
        tableView.setItems(tableData);
    }
}
