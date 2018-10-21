import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

public class PageCore extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button loadButton = new Button("Загрузить функцию из файла");
        Button findButton1 = new Button("Найти корень методом хорд:");
        Button findButton2 = new Button("Найти корень методом секущих");

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        File file = new File("./src/content.html");
        URL url = file.toURI().toURL();
        System.out.println(url.toString());

        VBox root = new VBox();
        root.setPadding(new Insets(5));
        root.setSpacing(15);
        webView.setPrefSize(700, 400);
        webView.setMaxWidth(1050);

        HBox equationRoot = new HBox();
        equationRoot.setSpacing(15);
        Label equationLabel = new Label("  Введите уравнение:     ");
        Label approxLabel = new Label("  Введите погрешность:  ");
        TextField equationTextField = new TextField();
        TextField approxTextField = new TextField();
        equationTextField.setMinWidth(250);
        approxTextField.setMinWidth(20);
        equationTextField.setText("1.62*x^3 - 8.15*x^2 + 4.39*x + 4.29");
        approxTextField.setText("0.001");
        equationRoot.getChildren().addAll(equationLabel, equationTextField, approxLabel, approxTextField, loadButton);

        HBox leftCheckRoot = new HBox();
        leftCheckRoot.setSpacing(15);
        TextField leftBorder = new TextField();
        leftBorder.setMinWidth(250);
        Label leftBorderLabel = new Label("Левая граница поиска:  ");
        leftCheckRoot.getChildren().addAll(leftBorderLabel, leftBorder);

        HBox rightCheckRoot = new HBox();
        rightCheckRoot.setSpacing(15);
        TextField rightBorder = new TextField();
        rightBorder.setMinWidth(250);
        Label rightBorderLabel = new Label("Правая граница поиска:");
        rightCheckRoot.getChildren().addAll(rightBorderLabel, rightBorder);

        HBox buttons = new HBox();
        buttons.setSpacing(20);
        buttons.getChildren().addAll(findButton1, findButton2);

        HBox results = new HBox();
        results.setSpacing(20);
        Label result1 = new Label();
        result1.setMinWidth(175);
        Label result2 = new Label();
        result2.setMinWidth(150);
        results.getChildren().addAll(result1, result2);

        root.getChildren().addAll(webView, equationRoot, leftCheckRoot, rightCheckRoot, buttons, results);

        webEngine.setJavaScriptEnabled(true);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Grapher");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(1090);
        primaryStage.show();

        webEngine.load(url.toString());

        equationTextField.textProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    String s = "'" + newValue + "'";
                    webEngine.executeScript("updateEquation(" + s + ");");
                })
        );

        findButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Secant.setEps(Double.parseDouble(approxTextField.getText()));
                String lb = "'" + leftBorder.getText() + "'";
                String rb = "'" + rightBorder.getText() + "'";
                webEngine.executeScript("findRoot(" + lb + "," + rb + ");");
                String answer = Chords.search(Double.parseDouble(leftBorder.getText()), Double.parseDouble(rightBorder.getText()), equationTextField.getText());
                result1.setText("   " + answer);
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./src/outputData")));
                    bw.write("Корень, найденный методом хорд: " + answer);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        findButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Secant.setEps(Double.parseDouble(approxTextField.getText()));
                String lb = "'" + leftBorder.getText() + "'";
                String rb = "'" + rightBorder.getText() + "'";
                webEngine.executeScript("findRoot(" + lb + "," + rb + ");");
                String answer = Secant.search(Double.parseDouble(leftBorder.getText()), Double.parseDouble(rightBorder.getText()), equationTextField.getText());
                result2.setText("   " + answer);
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./src/outputData")));
                    bw.write("Корень, найденный методом секущих: " + answer);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String equation = readData(new File("./src/inputData"));
                String s = "'" + equation + "'";
                equationTextField.setText(equation);
                webEngine.executeScript("updateEquation(" + s + ");");
            }
        });
    }

    public static String readData(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void go() {

        launch();

    }

}
