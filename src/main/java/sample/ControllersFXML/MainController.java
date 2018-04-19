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
    public TableColumn<MaterialBase, Integer> idColumn;
    public TableColumn<MaterialBase, Double> densityColumn;
    public TableColumn<MaterialBase, Double> heatCapColumn;
    public TableColumn<MaterialBase, Double> meltingcolumn;
    public TableColumn<MaterialBase, Double> speedColumn;
    public TableColumn<MaterialBase, Double> covertempColumn;
    public TableColumn<MaterialBase, Double> consistencyColumn;
    public TableColumn<MaterialBase, Double> viscosityColumn;
    public TableColumn<MaterialBase, Double> tempReduceColumn;
    public TableColumn<MaterialBase, Double> flowColumn;
    public TableColumn<MaterialBase, Double> heattransferColumn;
    public TableColumn<MaterialBase, String> materialNameColumn;
    public TextField materialIdText;
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
    MaterialBase materialBase;
    double temperature, speed, alignmentTemperature, consistention, heatTransfer, indexMaterial, viscosity;

    public MainController() {
    }

    public void initialize() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        lenghtColumn.setCellValueFactory(new PropertyValueFactory<>("lenght"));
        consistColumn.setCellValueFactory(new PropertyValueFactory<>("consist"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        reportView.setItems(values);

        idColumn.setCellValueFactory(cellData -> cellData.getValue().material_idProperty().asObject());
        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().materialNameProperty());
        densityColumn.setCellValueFactory(cellData -> cellData.getValue().densityProperty().asObject());
        heatCapColumn.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());
        meltingcolumn.setCellValueFactory(cellData -> cellData.getValue().meltingProperty().asObject());
        speedColumn.setCellValueFactory(cellData -> cellData.getValue().coverSpeedProperty().asObject());
        covertempColumn.setCellValueFactory(cellData -> cellData.getValue().coverTempProperty().asObject());
        consistencyColumn.setCellValueFactory(cellData -> cellData.getValue().consistProperty().asObject());
        viscosityColumn.setCellValueFactory(cellData -> cellData.getValue().viscosityProperty().asObject());
        tempReduceColumn.setCellValueFactory(cellData -> cellData.getValue().tempProperty().asObject());
        flowColumn.setCellValueFactory(cellData -> cellData.getValue().flowProperty().asObject());
        heattransferColumn.setCellValueFactory(cellData -> cellData.getValue().heatTransProperty().asObject());


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
        heatField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heatField.setText(oldValue);
        });
        meltingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) meltingField.setText(oldValue);
        });

    }

    @FXML
    private void searchMaterial(String materialName) throws ClassNotFoundException, SQLException {
        try {
            MaterialBase mat = MaterialDAO.searchMaterialBase(materialName);
            populateMaterial(mat);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void populateMaterial (MaterialBase mat) throws ClassNotFoundException {
        //Declare and ObservableList for table view
        ObservableList<MaterialBase> matData = FXCollections.observableArrayList();
        //Add material to the ObservableList
        matData.add(mat);
        //Set items to the materialTable
        dataBaseTable.setItems(matData);
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
            DependenceTemperature.getChildren().removeAll();
            DependenceViscosity.getChildren().removeAll();
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


            if (consistention > 0 && viscosity > 0 && alignmentTemperature > 0 && indexMaterial > 0 && heatTransfer > 0 &&
                    step > 0 && step <= channel.getLenght() && speed > 0 && temperature > -273 && width > 0 && height > 0 &&
                    lenght > 0 && density > 0 && heat > 0 && meltingTemperature > 0) {

                empCoef = new EmpiricalCoefficients(consistention, viscosity, alignmentTemperature,
                        indexMaterial, heatTransfer);
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
        heatField.setText(String.valueOf(materialBase.getCapacity()));
        meltingField.setText(String.valueOf(materialBase.getMelting()));
        alignmentTemperature = materialBase.getTemp();
        consistention = materialBase.getConsist();
        heatTransfer = materialBase.getHeatTrans();
        viscosity = materialBase.getViscosity();
        indexMaterial = materialBase.getFlow();
        materialNameLabel.setText(materialBase.getMaterialName());
    }
}
