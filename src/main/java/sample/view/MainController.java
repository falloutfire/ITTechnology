package sample.view;

import javafx.event.Event;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.Objects.Channel;
import sample.Objects.EmpiricalCoefficients;
import sample.Objects.Material;

import java.text.NumberFormat;
import java.util.Locale;

public class MainController {

    public AnchorPane DependenceViscosity;
    public AnchorPane DependenceTemperature;
    public TextField temperatureField;
    public TextField speedField;
    public TextField stepField;
    public TextField lenghtField;
    public TextField widthField;
    public TextField heightField;
    public TextField densityField;
    public TextField heatField;
    public TextField meltingField;
    public Label timerLabel;
    public Label performanceLabel;

    private NumberAxis yAxisTemp = new NumberAxis();
    private NumberAxis xAxisTemp = new NumberAxis();
    private NumberAxis yAxisCons = new NumberAxis();
    private NumberAxis xAxisCons = new NumberAxis();

    public MainController() {
    }

    public void initialize() {
        /*temperatureField.setOnKeyTyped(Event::consume);
        speedField.setOnKeyTyped(Event::consume);
        stepField.setOnKeyTyped(Event::consume);
        lenghtField.setOnKeyTyped(Event::consume);
        widthField.setOnKeyTyped(Event::consume);
        heightField.setOnKeyTyped(Event::consume);
        densityField.setOnKeyTyped(Event::consume);
        heatField.setOnKeyTyped(Event::consume);
        meltingField.setOnKeyTyped(Event::consume);*/
    }


    public void onClickCalculate() {


        EmpiricalCoefficients empCoef = new EmpiricalCoefficients(50000, 0.03, 120,
                0.35, 250);

        double temperature = 0;
        double speed = 0;
        double step = 0;
        double width, height, lenght = 0;
        double density, heat, meltingTemperature = 0;

        try {
            temperature = Double.parseDouble(temperatureField.getText().replace(',','.'));
            speed = Double.parseDouble(speedField.getText().replace(',','.'));
            step = Double.parseDouble(stepField.getText().replace(',','.'));

            width = Double.parseDouble(widthField.getText().replace(',','.'));
            height = Double.parseDouble(heightField.getText().replace(',','.'));
            lenght = Double.parseDouble(lenghtField.getText().replace(',','.'));

            density = Double.parseDouble(densityField.getText().replace(',','.'));
            heat = Double.parseDouble(heightField.getText().replace(',','.'));
            meltingTemperature = Double.parseDouble(meltingField.getText().replace(',','.'));

            Channel channel = new Channel(width, height, lenght);
            Material material = new Material(density, heat, meltingTemperature);

            if (step > 0 || speed > 0 || temperature > -273) {
                long startTime = System.currentTimeMillis();

                LineChart<Number, Number> chart2dTemp = new LineChart<Number, Number>(xAxisTemp, yAxisTemp);
                XYChart.Series seriesTemp = calculateTemperature(temperature, speed, step);
                chart2dTemp.getData().add(seriesTemp);

                LineChart<Number, Number> chart2dConsist = new LineChart<Number, Number>(xAxisCons, yAxisCons);
                XYChart.Series seriesConsistencies = calculateConsistencies(temperature, speed, step);
                chart2dConsist.getData().add(seriesConsistencies);

                DependenceViscosity.getChildren().add(chart2dConsist);
                DependenceTemperature.getChildren().add(chart2dTemp);

                long timeSpent = System.currentTimeMillis() - startTime;

                timerLabel.setText("Время подсчета: " + String.valueOf(timeSpent) + " мс");
                performanceLabel.setText("Производительность: " + String.valueOf(performanceCalc(channel.getHeight(), channel.getWidth(), speed, material.getDensity())) + " Кг/Ч");

            } else {
                getAlert();
            }
        } catch (NumberFormatException e) {
            getAlert();
        }
    }

    private double performanceCalc(double height, double width, double speed, double density) {
        double Fch = 0.125 * Math.pow((height / width), 2) - 0.625 * (height / width) + 1;
        double Q = (width * height * speed) / 2 * Fch;
        return density * Q * 3600;
    }

    private XYChart.Series calculateTemperature(double temperature, double speed, double step) {

        XYChart.Series series = new XYChart.Series();

        Channel channel = new Channel(0.2, 0.005, 7);
        Material material = new Material(920, 2300, 120);
        EmpiricalCoefficients empCoef = new EmpiricalCoefficients(50000, 0.03, 120,
                0.35, 250);


        double Fch = 0.125 * Math.pow((channel.getHeight() / channel.getWidth()), 2) - 0.625 * (channel.getHeight() / channel.getWidth()) + 1;
        double Q = (channel.getWidth() * channel.getHeight() * speed) / 2 * Fch;
        double G = material.getDensity() * Q * 3600;


        for (double i = 0; i <= channel.getLenght(); i = i + step) {
            double gamma = speed / channel.getHeight();
            double qGamma = channel.getWidth() * channel.getHeight() * empCoef.getConsistention() * Math.pow(gamma, empCoef.getIndexMaterial() + 1);
            double qAlpha = channel.getWidth() * empCoef.getHeatTransfer() * ((1 / empCoef.getViscosity()) - temperature + empCoef.getAlignmentTemperature());
            double ae = ((empCoef.getViscosity() * qGamma + channel.getWidth() * empCoef.getHeatTransfer()) / (empCoef.getViscosity() * qAlpha)) * (1 - Math.exp(-(i * empCoef.getViscosity() * qAlpha) / (material.getDensity() * material.getHeat() * Q))) + Math.exp(empCoef.getViscosity() * (material.getMeltingTemperature() - empCoef.getAlignmentTemperature() - ((i * qAlpha) / (material.getDensity() * material.getHeat() * Q))));

            double T = empCoef.getAlignmentTemperature() + 1 / empCoef.getViscosity() * Math.log(ae); //Температура для зависимости

            series.getData().add(new XYChart.Data(i, T));
        }
        return series;
    }

    private XYChart.Series calculateConsistencies(double temperature, double speed, double step) {

        XYChart.Series series = new XYChart.Series();

        Channel channel = new Channel(0.2, 0.005, 7);
        Material material = new Material(920, 2300, 120);
        EmpiricalCoefficients empCoef = new EmpiricalCoefficients(50000, 0.03, 120,
                0.35, 250);

        double Fch = 0.125 * Math.pow((channel.getHeight() / channel.getWidth()), 2) - 0.625 * (channel.getHeight() / channel.getWidth()) + 1;
        double Q = (channel.getWidth() * channel.getHeight() * speed) / 2 * Fch;
        double G = material.getDensity() * Q * 3600;// кг час

        for (double i = 0; i <= channel.getLenght(); i = i + step) {
            double gamma = speed / channel.getHeight();
            double qGamma = channel.getWidth() * channel.getHeight() * empCoef.getConsistention() * Math.pow(gamma, empCoef.getIndexMaterial() + 1);
            double qAlpha = channel.getWidth() * empCoef.getHeatTransfer() * ((1 / empCoef.getViscosity()) - temperature + empCoef.getAlignmentTemperature());
            double ae = ((empCoef.getViscosity() * qGamma + channel.getWidth() * empCoef.getHeatTransfer()) / (empCoef.getViscosity() * qAlpha)) * (1 - Math.exp(-(i * empCoef.getViscosity() * qAlpha) / (material.getDensity() * material.getHeat() * Q))) + Math.exp(empCoef.getViscosity() * (material.getMeltingTemperature() - empCoef.getAlignmentTemperature() - ((i * qAlpha) / (material.getDensity() * material.getHeat() * Q))));

            double T = empCoef.getAlignmentTemperature() + 1 / empCoef.getViscosity() * Math.log(ae); //Температура для зависимости

            double consistention = empCoef.getConsistention() * Math.exp(-empCoef.getViscosity() * (T - empCoef.getAlignmentTemperature()));
            double viscosityMaterial = consistention * Math.pow(gamma, empCoef.getIndexMaterial() - 1); //взякость для зависимости

            series.getData().add(new XYChart.Data(i, viscosityMaterial));
        }
        return series;
    }

    private void getAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Неправильный формат чисел");
        alert.setContentText("Проверьте все ячейки на наличие ошибок!\n");
        alert.showAndWait();
    }
}