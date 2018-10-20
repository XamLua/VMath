import com.sun.deploy.util.StringUtils;
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
import java.util.ArrayList;
import java.util.Formatter;

public class PageCore extends Application {

    private static ArrayList<Double> xs = new ArrayList<>();
    private static ArrayList<Double> ys = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button exec = new Button("Произвести расчёты");

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        File file = new File("./src/content.html");
        URL url = file.toURI().toURL();

        VBox root = new VBox();
        root.setPadding(new Insets(5));
        root.setSpacing(15);
        webView.setPrefSize(700, 400);
        webView.setMaxWidth(1050);

        root.getChildren().addAll(webView, exec);

        webEngine.setJavaScriptEnabled(true);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Grapher");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(1090);
        primaryStage.show();

        webEngine.load(url.toString());

        exec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for (int i = 0; i < xs.size(); i++){
                    String id = "'point" + i + "'";
                    String exp = "'(" + xs.get(i) + "," + ys.get(i) + ")'";
                    String label =  "''" ;
                    String color = "'" + "Desmos.Colors.Black" + "'";
                    webEngine.executeScript("updateEquation(" + id + "," + exp + ",false," + label + "," + color + ");");
                }

                String s = calculate();
                String id = "'" + s.split(" ")[0] + "'";
                String exp = "'" + s.split(" ")[1] + "'";
                String label = "'" + s.split(" ")[2] + "'";
                webEngine.executeScript("updateEquation(" + id + "," + exp + ",true," + label + ");");
            }
        });
    }

    public static void go() {

        readData(new File("./src/inputData"));
        launch();

    }

    public static void readData(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] xString = br.readLine().replace(',', '.').split("\\s+");
            for (int i = 1; i < xString.length; i++)
                xs.add(Double.parseDouble(xString[i]));
            String[] yString = br.readLine().replace(',', '.').split("\\s+");
            for (int i = 1; i < yString.length; i++)
                ys.add(Double.parseDouble(yString[i]));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String calculate() {

        LinearApproximation la = new LinearApproximation();
        la.execute(xs, ys);

        QuadraticApproximation qa = new QuadraticApproximation();
        qa.execute(xs, ys);

        ExponentialApproximation ea = new ExponentialApproximation();
        ea.execute(xs, ys);

        LogarithmicApproximation loga = new LogarithmicApproximation();
        loga.execute(xs, ys);

        PowerApproximation pa = new PowerApproximation();
        pa.execute(xs, ys);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./src/outputData")));
            String format = "%c %19s %c %20s %c %20s %c %20s %c %20s %c %20s %c";
            bw.write(String.format(format, '|', center("Вид φ(х)", 20), '|', center("Линейная", 20), '|',
                    center("Полиномиальная", 20), '|', center("Экспоненциальная", 20), '|',
                    center("Логарифмическая", 20), '|', center("Степенная", 20), '|'));
            bw.newLine();

            for (int i = 0; i < 140; i++)
                bw.write('-');
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("X|Y", 20), '|',
                    center("F = ax + b", 20), '|',
                    center("F = ax^2 + bx + c", 20), '|',
                    center("F = ae ^ bx", 20), '|',
                    center("F = a + bln(x)", 20), '|',
                    center("F = ax^b", 20), '|'
            ));
            bw.newLine();

            for (int i = 0; i < xs.size(); i++){
                bw.write(String.format(format, '|',
                        center(String.format("%.3s", String.valueOf(xs.get(i))) + '|' + String.format("%.7s", String.valueOf(ys.get(i))), 20), '|',
                        center(String.format("%.6s", String.valueOf(la.calculate(xs.get(i)))), 20), '|',
                        center(String.format("%.6s", String.valueOf(qa.calculate(xs.get(i)))), 20), '|',
                        center(String.format("%.6s", String.valueOf(ea.calculate(xs.get(i)))), 20), '|',
                        center(String.format("%.6s", String.valueOf(loga.calculate(xs.get(i)))), 20), '|',
                        center(String.format("%.6s", String.valueOf(pa.calculate(xs.get(i)))), 20), '|'
                        ));
                bw.newLine();

            }

            for (int i = 0; i < 140; i++)
                bw.write('-');
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("S", 20), '|',
                    center(String.format("%.6s", String.valueOf(la.getS())), 20), '|',
                    center(String.format("%.6s", String.valueOf(qa.getS())), 20), '|',
                    center(String.format("%.6s", String.valueOf(ea.getS())), 20), '|',
                    center(String.format("%.6s", String.valueOf(loga.getS())), 20), '|',
                    center(String.format("%.6s", String.valueOf(pa.getS())), 20), '|'
            ));
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("Sigma", 20), '|',
                    center(String.format("%.6s", String.valueOf(la.getSigma())), 20), '|',
                    center(String.format("%.25s", String.valueOf(qa.getSigma())), 20), '|',
                    center(String.format("%.6s", String.valueOf(ea.getSigma())), 20), '|',
                    center(String.format("%.6s", String.valueOf(loga.getSigma())), 20), '|',
                    center(String.format("%.6s", String.valueOf(pa.getSigma())), 20), '|'
            ));
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("a", 20), '|',
                    center(String.format("%.6s", String.valueOf(la.getA())), 20), '|',
                    center(String.format("%.6s", String.valueOf(qa.getA())), 20), '|',
                    center(String.format("%.6s", String.valueOf(ea.getA())), 20), '|',
                    center(String.format("%.6s", String.valueOf(loga.getA())), 20), '|',
                    center(String.format("%.6s", String.valueOf(pa.getA())), 20), '|'
            ));
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("b", 20), '|',
                    center(String.format("%.6s", String.valueOf(la.getB())), 20), '|',
                    center(String.format("%.6s", String.valueOf(qa.getB())), 20), '|',
                    center(String.format("%.6s", String.valueOf(ea.getB())), 20), '|',
                    center(String.format("%.6s", String.valueOf(loga.getB())), 20), '|',
                    center(String.format("%.6s", String.valueOf(pa.getB())), 20), '|'
            ));
            bw.newLine();

            bw.write(String.format(format, '|',
                    center("c", 20), '|',
                    center(" ", 20), '|',
                    center(String.format("%.6s", String.valueOf(qa.getC())), 20), '|',
                    center(" ", 20), '|',
                    center(" ", 20), '|',
                    center(" ", 20), '|'
            ));
            bw.newLine();

            for (int i = 0; i < 140; i++)
                bw.write('-');
            bw.newLine();

            Double sigmaMin = la.getSigma();
            String res = "Линейная";
            String resExp = "la " + la.toExpression();

            if (sigmaMin > qa.getSigma()){
                sigmaMin = qa.getSigma();
                res = "Полиномиальная";
                resExp = "qa " + qa.toExpression();
            }

            if (sigmaMin > ea.getSigma()){
                sigmaMin = ea.getSigma();
                res = "Экспоненциальная";
                resExp = "ea " + ea.toExpression();
            }

            if (sigmaMin > loga.getSigma()){
                sigmaMin = loga.getSigma();
                res = "Логарифмическая";
                resExp = "loga " + loga.toExpression();
            }

            if (sigmaMin > pa.getSigma()){
                res = "Степенная";
                resExp = "pa " + pa.toExpression();
            }

            bw.write("Наилучшая апроксимирующая функция: " + res);

            bw.flush();
            bw.close();

            return resExp + " " + res;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return " ";
    }

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

}
