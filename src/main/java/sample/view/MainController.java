package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.Objects.Channel;
import sample.Objects.EmpiricalCoefficients;
import sample.Objects.Material;
import sample.Objects.ValueAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class MainController {

    public AnchorPane grapfPane;
    public AnchorPane valuePane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane DependenceViscosity;
    @FXML
    private AnchorPane DependenceTemperature;
    @FXML
    private TextField temperatureField;
    @FXML
    private TextField speedField;
    @FXML
    private TextField stepField;
    @FXML
    private TextField lenghtField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField densityField;
    @FXML
    private TextField heatField;
    @FXML
    private TextField meltingField;
    @FXML
    private Label timerLabel;
    @FXML
    private Label performanceLabel;

    private NumberAxis yAxisTemp = new NumberAxis();
    private NumberAxis xAxisTemp = new NumberAxis();
    private NumberAxis yAxisCons = new NumberAxis();
    private NumberAxis xAxisCons = new NumberAxis();

    public TableView<ValueAdapter> reportView;
    public TableColumn<ValueAdapter, Integer> numberColumn;
    public TableColumn<ValueAdapter, String> lenghtColumn;
    public TableColumn<ValueAdapter, String> consistColumn;
    public TableColumn<ValueAdapter, String> temperatureColumn;

    private ObservableList<ValueAdapter> values = FXCollections.observableArrayList();

    Channel channel;
    Material material;
    EmpiricalCoefficients empCoef;
    double temperature;
    double speed;

    public MainController() {
    }

    public void initialize() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        lenghtColumn.setCellValueFactory(new PropertyValueFactory<>("lenght"));
        consistColumn.setCellValueFactory(new PropertyValueFactory<>("consist"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        reportView.setItems(values);

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        stepField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) stepField.setText(oldValue);
        });
        temperatureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) temperatureField.setText(oldValue);
        });
        speedField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) speedField.setText(oldValue);
        });
        lenghtField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) lenghtField.setText(oldValue);
        });
        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) widthField.setText(oldValue);
        });
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) heightField.setText(oldValue);
        });
        densityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) densityField.setText(oldValue);
        });
        heatField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) heatField.setText(oldValue);
        });
        meltingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!p.matcher(newValue).matches()) meltingField.setText(oldValue);
        });
    }


    public void onClickCalculate() {

        double step;
        double width;
        double height;
        double lenght;
        double density;
        double heat;
        double meltingTemperature;

        try {
            temperature = Double.parseDouble(temperatureField.getText().replace(',', '.'));
            speed = Double.parseDouble(speedField.getText().replace(',', '.'));
            step = Double.parseDouble(stepField.getText().replace(',', '.'));

            width = Double.parseDouble(widthField.getText().replace(',', '.'));
            height = Double.parseDouble(heightField.getText().replace(',', '.'));
            lenght = Double.parseDouble(lenghtField.getText().replace(',', '.'));

            density = Double.parseDouble(densityField.getText().replace(',', '.'));
            heat = Double.parseDouble(heatField.getText().replace(',', '.'));
            meltingTemperature = Double.parseDouble(meltingField.getText().replace(',', '.'));

            System.out.println(density + " " + heat + " " + meltingTemperature);

            channel = new Channel(width, height, lenght);
            material = new Material(density, heat, meltingTemperature);
            empCoef = new EmpiricalCoefficients(50000, 0.03, 120,
                    0.35, 250);

            if (step > 0 && step <= channel.getLenght() && speed > 0 && temperature > -273 && width > 0 && height > 0 && lenght > 0 && density > 0 && heat > 0 && meltingTemperature > 0 ) {
                values.clear();
                long startTime = System.currentTimeMillis();

                LineChart<Number, Number> chart2dTemp = new LineChart<>(xAxisTemp, yAxisTemp);
                XYChart.Series seriesTemp = calculateTemperature(temperature, speed, step, channel, material, empCoef);
                xAxisTemp.setLabel("Длина канала, м");
                yAxisTemp.setLabel("Температура, °C");
                seriesTemp.setName("Зависимость температуры от длины канала");
                chart2dTemp.getData().add(seriesTemp);

                LineChart<Number, Number> chart2dConsist = new LineChart<Number, Number>(xAxisCons, yAxisCons);
                XYChart.Series seriesConsistencies = calculateConsistencies(temperature, speed, step, channel, material, empCoef, values);
                xAxisCons.setLabel("Длина канала, м");
                yAxisCons.setLabel("Вязкость, Па*с");
                seriesConsistencies.setName("Зависимость вязкость от длины канала");
                chart2dConsist.getData().add(seriesConsistencies);

                DependenceViscosity.getChildren().add(chart2dConsist);
                DependenceTemperature.getChildren().add(chart2dTemp);

                long timeSpent = System.currentTimeMillis() - startTime;

                timerLabel.setText("Время выполнения: " + String.valueOf(timeSpent) + " мс");
                performanceLabel.setText("Производительность: " + String.valueOf(String.format("%.0f", performanceCalc(channel.getHeight(), channel.getWidth(), speed, material.getDensity()))) + " Кг/ч");
                System.out.println("Complete");
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

    private XYChart.Series calculateTemperature(double temperature, double speed, double step, Channel channel, Material material, EmpiricalCoefficients empCoef) {

        XYChart.Series series = new XYChart.Series();

        /*Channel channel = new Channel(0.2, 0.005, 7);
        Material material = new Material(920, 2300, 120);
        EmpiricalCoefficients empCoef = new EmpiricalCoefficients(50000, 0.03, 120,
                0.35, 250);*/

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

    private XYChart.Series calculateConsistencies(double temperature, double speed, double step, Channel channel, Material material, EmpiricalCoefficients empCoef, ObservableList<ValueAdapter> values) {

        XYChart.Series series = new XYChart.Series();
        NumberFormat formater = new DecimalFormat("#0");
        NumberFormat formater1 = new DecimalFormat("#0.0");

        double Fch = 0.125 * Math.pow((channel.getHeight() / channel.getWidth()), 2) - 0.625 * (channel.getHeight() / channel.getWidth()) + 1;
        double Q = (channel.getWidth() * channel.getHeight() * speed) / 2 * Fch;
        double G = material.getDensity() * Q * 3600;// кг час

        int a = 0;

        for (double i = 0; i <= channel.getLenght(); i = i + step) {
            a++;
            double gamma = speed / channel.getHeight();
            double qGamma = channel.getWidth() * channel.getHeight() * empCoef.getConsistention() * Math.pow(gamma, empCoef.getIndexMaterial() + 1);
            double qAlpha = channel.getWidth() * empCoef.getHeatTransfer() * ((1 / empCoef.getViscosity()) - temperature + empCoef.getAlignmentTemperature());
            double ae = ((empCoef.getViscosity() * qGamma + channel.getWidth() * empCoef.getHeatTransfer()) / (empCoef.getViscosity() * qAlpha)) * (1 - Math.exp(-(i * empCoef.getViscosity() * qAlpha) / (material.getDensity() * material.getHeat() * Q))) + Math.exp(empCoef.getViscosity() * (material.getMeltingTemperature() - empCoef.getAlignmentTemperature() - ((i * qAlpha) / (material.getDensity() * material.getHeat() * Q))));

            double T = empCoef.getAlignmentTemperature() + 1 / empCoef.getViscosity() * Math.log(ae); //Температура для зависимости

            double consistention = empCoef.getConsistention() * Math.exp(-empCoef.getViscosity() * (T - empCoef.getAlignmentTemperature()));
            double viscosityMaterial = consistention * Math.pow(gamma, empCoef.getIndexMaterial() - 1); //взякость для зависимости

            series.getData().add(new XYChart.Data(i, viscosityMaterial));
            values.add(new ValueAdapter(a, formater1.format(i) , formater.format(viscosityMaterial), formater.format(T)));
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

    public ObservableList<ValueAdapter> getValues() {
        return values;
    }

    public Channel getChannel() {
        return channel;
    }

    public Material getMaterial() {
        return material;
    }

    public EmpiricalCoefficients getEmpCoef() {
        return empCoef;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getSpeed() {
        return speed;
    }

}
