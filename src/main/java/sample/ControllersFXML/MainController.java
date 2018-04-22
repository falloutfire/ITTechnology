package sample.ControllersFXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.DAO.MaterialDAO;
import sample.Objects.*;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class MainController {

    public AnchorPane grapfPane;
    public AnchorPane valuePane;

    public TableView<MaterialBase> dataBaseTable;
    public TableColumn<MaterialBase, String> materialNameColumn;
    public Label materialNameLabel;

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
    private TextField heatCapField;
    @FXML
    private TextField meltingField;
    @FXML
    private TextField consistencyField;
    @FXML
    private TextField viscosityField;
    @FXML
    private TextField reducField;
    @FXML
    private TextField indexFlowField;
    @FXML
    private TextField heatField;
    @FXML
    private TextField searchByNameField;
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
    MaterialBase materialBase;
    double temperature, speed;

    public MainController() {
    }

    public void initialize() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        lenghtColumn.setCellValueFactory(new PropertyValueFactory<>("lenght"));
        consistColumn.setCellValueFactory(new PropertyValueFactory<>("consist"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        reportView.setItems(values);

        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().materialNameProperty());

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        stepField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) stepField.setText(oldValue);
        });
        temperatureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) temperatureField.setText(oldValue);
        });
        speedField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) speedField.setText(oldValue);
        });
        lenghtField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) lenghtField.setText(oldValue);
        });
        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) widthField.setText(oldValue);
        });
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heightField.setText(oldValue);
        });
        densityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) densityField.setText(oldValue);
        });
        heatCapField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heatCapField.setText(oldValue);
        });
        meltingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) meltingField.setText(oldValue);
        });
        indexFlowField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) indexFlowField.setText(oldValue);
        });
        viscosityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) viscosityField.setText(oldValue);
        });
        consistencyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) consistencyField.setText(oldValue);
        });
        reducField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) reducField.setText(oldValue);
        });
        heatField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heatField.setText(oldValue);
        });

        try {
            ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
            dataBaseTable.setItems(materialBases);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickCalculate() {

        double step;
        double width;
        double height;
        double lenght;
        double density;
        double heatCap;
        double meltingTemperature;
        double indexMaterial;
        double heatTransfer;
        double consistention;
        double viscosity;
        double reduc;


        try {
            DependenceTemperature.getChildren().clear();
            DependenceViscosity.getChildren().clear();
            temperature = Double.parseDouble(temperatureField.getText().replace(',', '.'));
            speed = Double.parseDouble(speedField.getText().replace(',', '.'));
            step = Double.parseDouble(stepField.getText().replace(',', '.'));

            width = Double.parseDouble(widthField.getText().replace(',', '.'));
            height = Double.parseDouble(heightField.getText().replace(',', '.'));
            lenght = Double.parseDouble(lenghtField.getText().replace(',', '.'));

            density = Double.parseDouble(densityField.getText().replace(',', '.'));
            heatCap = Double.parseDouble(heatCapField.getText().replace(',', '.'));
            meltingTemperature = Double.parseDouble(meltingField.getText().replace(',', '.'));

            consistention = Double.parseDouble(consistencyField.getText());
            viscosity = Double.parseDouble(viscosityField.getText());
            reduc = Double.parseDouble(viscosityField.getText());
            indexMaterial = Double.parseDouble(indexFlowField.getText());
            heatTransfer = Double.parseDouble(heatField.getText());

            System.out.println(density + " " + heatCap + " " + meltingTemperature);

            channel = new Channel(width, height, lenght);
            material = new Material(density, heatCap, meltingTemperature);
            empCoef = new EmpiricalCoefficients(consistention, viscosity, reduc,
                    indexMaterial, heatTransfer);

            if (consistention > 0 && viscosity > 0 && reduc > 0 && indexMaterial > 0 && heatTransfer > 0 &&
                    step > 0 && step <= channel.getLenght() && speed > 0 && temperature > -273 && width > 0 && height > 0 &&
                    lenght > 0 && density > 0 && heatCap > 0 && meltingTemperature > 0) {


                values.clear();
                long startTime = System.currentTimeMillis();

                LineChart<Number, Number> chart2dTemp = new LineChart<>(xAxisTemp, yAxisTemp);
                XYChart.Series seriesTemp = calculateTemperature(temperature, speed, step, channel, material, empCoef);
                xAxisTemp.setLabel("Координата по длине канала, м");
                yAxisTemp.setLabel("Температура, °C");
                chart2dTemp.setLegendVisible(false);
                chart2dTemp.getData().add(seriesTemp);

                LineChart<Number, Number> chart2dConsist = new LineChart<Number, Number>(xAxisCons, yAxisCons);
                XYChart.Series seriesConsistencies = calculateConsistencies(temperature, speed, step, channel, material, empCoef, values);
                xAxisCons.setLabel("Координата по длине канала, м");
                yAxisCons.setLabel("Вязкость, Па*с");
                chart2dConsist.setLegendVisible(false);
                chart2dConsist.getData().add(seriesConsistencies);

                DependenceViscosity.getChildren().add(chart2dConsist);
                DependenceTemperature.getChildren().add(chart2dTemp);

                long timeSpent = System.currentTimeMillis() - startTime;

                timerLabel.setText("Время выполнения: " + String.valueOf(timeSpent) + " мс");
                performanceLabel.setText("Производительность: " + String.valueOf(String.format("%.0f", performanceCalc(channel.getHeight(), channel.getWidth(), speed, material.getDensity()))) + " кг/ч");
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
            values.add(new ValueAdapter(a, formater1.format(i), formater.format(viscosityMaterial), formater.format(T)));
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

    public void onClickFindAll() throws SQLException {
        ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
        dataBaseTable.setItems(materialBases);
    }

    public void onClickChooseMaterial(ActionEvent actionEvent) {
        materialBase = dataBaseTable.getSelectionModel().getSelectedItem();
        temperatureField.setText(String.valueOf(materialBase.getCoverTemp()));
        speedField.setText(String.valueOf(materialBase.getCoverSpeed()));
        densityField.setText(String.valueOf(materialBase.getDensity()));
        heatCapField.setText(String.valueOf(materialBase.getCapacity()));
        meltingField.setText(String.valueOf(materialBase.getMelting()));
        reducField.setText(String.valueOf(materialBase.getTemp()));
        consistencyField.setText(String.valueOf(materialBase.getConsist()));
        heatField.setText(String.valueOf(materialBase.getHeatTrans()));
        viscosityField.setText(String.valueOf(materialBase.getViscosity()));
        indexFlowField.setText(String.valueOf(materialBase.getFlow()));
        materialNameLabel.setText(materialBase.getMaterialName());
    }

    @FXML
    private void onClickFindMaterialByName(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ObservableList<MaterialBase> materialBases = MaterialDAO.searchMaterialBaseLike(searchByNameField.getText());
        dataBaseTable.setItems(materialBases);
    }
}
