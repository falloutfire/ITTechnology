package sample.ControllersFXML;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import sample.Objects.Channel;
import sample.Objects.EmpiricalCoefficients;
import sample.Objects.Material;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExperimentLayoutController {

    public TextField tempFromField;
    public TextField tempToField;
    public TextField speedToField;
    public TextField speedFromField;
    public Label materialNameLabel;
    public TableView viscosityView;
    public TableView tempView;
    public AnchorPane viscosityPane;
    public AnchorPane temperaturePane;
    public AnchorPane performancePane;
    public TableView perfomanceView;
    public Label timeLabel;
    NumberFormat formater = new DecimalFormat("#0");
    private Stage dialogStage;
    private boolean okClicked = false;
    private Material material;
    private EmpiricalCoefficients empCoef;
    private Channel channel;
    private String nameMaterial;
    private List<String> columnsTemperature;
    private ObservableList<String> rowTemp1, rowTemp2, rowTemp3, rowTemp4, rowTemp5;
    private ObservableList<String> rowVisc1, rowVisc2, rowVisc3, rowVisc4, rowVisc5;
    private ObservableList<String> rowPerfomance, rowPerfomance1, rowPerfomance2, rowPerfomance3, rowPerfomance4;
    private ObservableList<String> columnsPerf;

    public ExperimentLayoutController() {
    }

    static void getAlertFile(File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Невзможно сохранить файл");
        alert.setContentText("Невзможно сохранить значения в файл:\n" + file.getPath());

        alert.showAndWait();
    }

    public void initialize() {
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        tempFromField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) tempFromField.setText(oldValue);
        });
        tempToField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) tempToField.setText(oldValue);
        });
        speedFromField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) speedFromField.setText(oldValue);
        });
        speedToField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) speedToField.setText(oldValue);
        });
    }

    private void clearing() {
        temperaturePane.getChildren().clear();
        viscosityPane.getChildren().clear();
        performancePane.getChildren().clear();
        tempView.getColumns().clear();
        perfomanceView.getColumns().clear();
        viscosityView.getColumns().clear();
    }

    public void onClickCalculate(ActionEvent actionEvent) {

        clearing();

        if (Float.parseFloat(tempFromField.getText()) < Float.parseFloat(tempToField.getText()) && Float.parseFloat(speedFromField.getText()) < Float.parseFloat(speedToField.getText())) {
            columnsTemperature = new ArrayList<String>(getDiapazon(Float.parseFloat(tempFromField.getText()), Float.parseFloat(tempToField.getText())));
            List<String> columnsSpeed = new ArrayList<String>(getDiapazon(Float.parseFloat(speedFromField.getText()), Float.parseFloat(speedToField.getText())));
            columnsPerf = FXCollections.observableArrayList();
            columnsPerf.add("Скорость \nкрышки, м/с");
            columnsPerf.add("Производительность, Па*с");
            long startTime = System.currentTimeMillis();
            setCellFactoryTable(columnsTemperature, tempView.getColumns());
            setCellFactoryTable(columnsTemperature, viscosityView.getColumns());
            setCellFactoryTable(columnsPerf, perfomanceView.getColumns());

            rowTemp1 = FXCollections.observableArrayList();
            rowTemp2 = FXCollections.observableArrayList();
            rowTemp3 = FXCollections.observableArrayList();
            rowTemp4 = FXCollections.observableArrayList();
            rowTemp5 = FXCollections.observableArrayList();
            rowVisc1 = FXCollections.observableArrayList();
            rowVisc2 = FXCollections.observableArrayList();
            rowVisc3 = FXCollections.observableArrayList();
            rowVisc4 = FXCollections.observableArrayList();
            rowVisc5 = FXCollections.observableArrayList();
            rowPerfomance = FXCollections.observableArrayList();
            rowPerfomance1 = FXCollections.observableArrayList();
            rowPerfomance2 = FXCollections.observableArrayList();
            rowPerfomance3 = FXCollections.observableArrayList();
            rowPerfomance4 = FXCollections.observableArrayList();

            calc(Float.parseFloat(columnsSpeed.get(1)), columnsTemperature, rowTemp1, rowVisc1, rowPerfomance);
            calc(Float.parseFloat(columnsSpeed.get(2)), columnsTemperature, rowTemp2, rowVisc2, rowPerfomance1);
            calc(Float.parseFloat(columnsSpeed.get(3)), columnsTemperature, rowTemp3, rowVisc3, rowPerfomance2);
            calc(Float.parseFloat(columnsSpeed.get(4)), columnsTemperature, rowTemp4, rowVisc4, rowPerfomance3);
            calc(Float.parseFloat(columnsSpeed.get(5)), columnsTemperature, rowTemp5, rowVisc5, rowPerfomance4);
            tempView.getItems().addAll(rowTemp1, rowTemp2, rowTemp3, rowTemp4, rowTemp5);
            viscosityView.getItems().addAll(rowVisc1, rowVisc2, rowVisc3, rowVisc4, rowVisc5);
            perfomanceView.getItems().addAll(rowPerfomance, rowPerfomance1, rowPerfomance2, rowPerfomance3, rowPerfomance4);

            NumberAxis yAxisTemp = new NumberAxis(Float.parseFloat(rowTemp1.get(1)) - 4, Float.parseFloat(rowTemp5.get(5)) + 4, 5);
            if (Float.parseFloat(rowTemp5.get(5)) < Float.parseFloat(rowTemp1.get(5))) {
                yAxisTemp.setUpperBound(Float.parseFloat(rowTemp1.get(5)) + 4);
            }
            NumberAxis xAxisTemp = new NumberAxis(Float.parseFloat(columnsTemperature.get(1)), Float.parseFloat(columnsTemperature.get(5)) + 5, 5);
            NumberAxis yAxisVisc = new NumberAxis();
            NumberAxis xAxisVisc = new NumberAxis(Float.parseFloat(columnsTemperature.get(1)), Float.parseFloat(columnsTemperature.get(5)) + 5, 5);
            NumberAxis yAxisPerf = new NumberAxis();
            NumberAxis xAxisPerf = new NumberAxis(Float.parseFloat(rowPerfomance.get(0)), Float.parseFloat(rowPerfomance4.get(0)), 1);

            yAxisTemp.setLabel("Температура продукта,°С");
            xAxisTemp.setLabel("Температура крышки,°С");
            yAxisVisc.setLabel("Вязкость продукта, Па*с");
            xAxisVisc.setLabel("Температура крышки,°С");
            yAxisPerf.setLabel("Производительность,кг/ч");
            xAxisPerf.setLabel("Скорость крышки,м/с");

            LineChart<Number, Number> tempChart = new LineChart<>(xAxisTemp, yAxisTemp);
            LineChart<Number, Number> viscChart = new LineChart<>(xAxisVisc, yAxisVisc);
            LineChart<Number, Number> perfChart = new LineChart<>(xAxisPerf, yAxisPerf);

            XYChart.Series tempSeries1 = getSeries(columnsTemperature, rowTemp1);
            XYChart.Series tempSeries2 = getSeries(columnsTemperature, rowTemp2);
            XYChart.Series tempSeries3 = getSeries(columnsTemperature, rowTemp3);
            XYChart.Series tempSeries4 = getSeries(columnsTemperature, rowTemp4);
            XYChart.Series tempSeries5 = getSeries(columnsTemperature, rowTemp5);
            tempSeries1.setName("Температура материала от температуры крышки при скорости крышки " + rowTemp1.get(0) + "м/с");
            tempSeries2.setName("Температура материала от температуры крышки при скорости крышки " + rowTemp2.get(0) + "м/с");
            tempSeries3.setName("Температура материала от температуры крышки при скорости крышки " + rowTemp3.get(0) + "м/с");
            tempSeries4.setName("Температура материала от температуры крышки при скорости крышки " + rowTemp4.get(0) + "м/с");
            tempSeries5.setName("Температура материала от температуры крышки при скорости крышки " + rowTemp5.get(0) + "м/с");
            XYChart.Series viscSeries1 = getSeries(columnsTemperature, rowVisc1);
            XYChart.Series viscSeries2 = getSeries(columnsTemperature, rowVisc2);
            XYChart.Series viscSeries3 = getSeries(columnsTemperature, rowVisc3);
            XYChart.Series viscSeries4 = getSeries(columnsTemperature, rowVisc4);
            XYChart.Series viscSeries5 = getSeries(columnsTemperature, rowVisc5);
            viscSeries1.setName("Вязкость материала от температуры крышки при скорости крышки " + rowVisc1.get(0) + "м/с");
            viscSeries2.setName("Вязкость материала от температуры крышки при скорости крышки " + rowVisc2.get(0) + "м/с");
            viscSeries3.setName("Вязкость материала от температуры крышки при скорости крышки " + rowVisc3.get(0) + "м/с");
            viscSeries4.setName("Вязкость материала от температуры крышки при скорости крышки " + rowVisc4.get(0) + "м/с");
            viscSeries5.setName("Вязкость материала от температуры крышки при скорости крышки " + rowVisc5.get(0) + "м/с");
            XYChart.Series perfSeries = getSeries(rowPerfomance, rowPerfomance1, rowPerfomance2, rowPerfomance3, rowPerfomance4);

            tempChart.getData().addAll(tempSeries1, tempSeries2, tempSeries3, tempSeries4, tempSeries5);
            viscChart.getData().addAll(viscSeries1, viscSeries2, viscSeries3, viscSeries4, viscSeries5);
            perfChart.getData().add(perfSeries);
            temperaturePane.getChildren().add(tempChart);
            viscosityPane.getChildren().add(viscChart);
            performancePane.getChildren().add(perfChart);
            long timeSpent = System.currentTimeMillis() - startTime;
            timeLabel.setText("Время выполнения: " + String.valueOf(timeSpent) + " мс");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверно заданы итервалы!");
            alert.setContentText("Проверьте все ячейки на наличие ошибок!\n");
            alert.showAndWait();
        }
    }

    private XYChart.Series getSeries(List<String> temp, ObservableList<String> row) {
        XYChart.Series series = new XYChart.Series();
        for (int i = 1; i < temp.size(); i++) {
            series.getData().add(new XYChart.Data<>(Float.parseFloat(temp.get(i)), Float.parseFloat(row.get(i))));
        }
        return series;
    }

    private XYChart.Series getSeries(ObservableList<String> row1, ObservableList<String> row2, ObservableList<String> row3, ObservableList<String> row4, ObservableList<String> row5) {
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<>(Float.parseFloat(row1.get(0)), Float.parseFloat(row1.get(1))));
        series.getData().add(new XYChart.Data<>(Float.parseFloat(row2.get(0)), Float.parseFloat(row2.get(1))));
        series.getData().add(new XYChart.Data<>(Float.parseFloat(row3.get(0)), Float.parseFloat(row3.get(1))));
        series.getData().add(new XYChart.Data<>(Float.parseFloat(row4.get(0)), Float.parseFloat(row4.get(1))));
        series.getData().add(new XYChart.Data<>(Float.parseFloat(row5.get(0)), Float.parseFloat(row5.get(1))));
        return series;
    }

    private void setCellFactoryTable(List<String> columnsList, ObservableList columns) {
        for (int i = 0; i < columnsList.size(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(columnsList.get(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            columns.addAll(col);
        }
    }

    private void calc(float speed, List<String> tempList, ObservableList<String> rowTemp, ObservableList<String> rowVis, ObservableList<String> rowPerf) {
        rowTemp.add(String.valueOf(speed));
        rowVis.add(String.valueOf(speed));
        double Fch = 0.125 * Math.pow((channel.getHeight() / channel.getWidth()), 2) - 0.625 * (channel.getHeight() / channel.getWidth()) + 1;
        double Q = (channel.getWidth() * channel.getHeight() * speed) / 2 * Fch;
        double G = material.getDensity() * Q * 3600;// кг час
        rowPerf.add(String.valueOf(speed));
        rowPerf.add(formater.format(G));
        for (int i = 1; i < 6; i++) {
            double temperature = Double.parseDouble(tempList.get(i));

            double gamma = speed / channel.getHeight();
            double qGamma = channel.getWidth() * channel.getHeight() * empCoef.getConsistention() * Math.pow(gamma, empCoef.getIndexMaterial() + 1);
            double qAlpha = channel.getWidth() * empCoef.getHeatTransfer() * ((1 / empCoef.getViscosity()) - temperature + empCoef.getAlignmentTemperature());
            double ae = ((empCoef.getViscosity() * qGamma + channel.getWidth() * empCoef.getHeatTransfer()) / (empCoef.getViscosity() * qAlpha)) * (1 - Math.exp(-(channel.getLenght() * empCoef.getViscosity() * qAlpha) / (material.getDensity() * material.getHeat() * Q))) + Math.exp(empCoef.getViscosity() * (material.getMeltingTemperature() - empCoef.getAlignmentTemperature() - ((channel.getLenght() * qAlpha) / (material.getDensity() * material.getHeat() * Q))));

            double T = empCoef.getAlignmentTemperature() + 1 / empCoef.getViscosity() * Math.log(ae); //Температура для зависимости
            rowTemp.add(formater.format(T));

            double consistention = empCoef.getConsistention() * Math.exp(-empCoef.getViscosity() * (T - empCoef.getAlignmentTemperature()));
            double viscosityMaterial = consistention * Math.pow(gamma, empCoef.getIndexMaterial() - 1); //взякость для зависимости
            rowVis.add(formater.format(viscosityMaterial));
        }
    }

    private ArrayList<String> getDiapazon(float min, float max) {
        ArrayList<String> diapazon = new ArrayList<>();
        diapazon.add("Температура,°С");
        float add = min;
        diapazon.add(String.valueOf(add));
        for (int i = 0; i < 4; i++) {
            add = add + ((max - min) / 4);
            diapazon.add(String.valueOf(add));
        }
        return diapazon;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setEmpCoef(EmpiricalCoefficients empCoef) {
        this.empCoef = empCoef;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setName(String name) {
        this.nameMaterial = name;
        materialNameLabel.setText(name);
    }

    public void onClickExportExcel(ActionEvent actionEvent) {
        if(rowTemp1 != null){
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                    "XLS files (*.xls)", "*.xls");
            fileChooser.getExtensionFilters().add(extensionFilter);

            File file = fileChooser.showSaveDialog(dialogStage);
            if (file != null) {
                if (!file.getPath().endsWith(".xls")) {
                    file = new File(file.getPath() + ".xls");
                }
            }

            exportDataToFile(file);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невзможно сохранить файл");
            alert.setContentText("Необходимо провести расчет, прежде чем сохранять");
            alert.showAndWait();
        }
    }

    @SuppressWarnings("deprecation")
    private void exportDataToFile(File file) {
        try {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Расчет");

            Row row = sheet.createRow(0);
            Cell table1Label = row.createCell(0);
            table1Label.setCellValue("Температура материала от температуры крышки при заданной скорости крышки");
            Cell name = row.createCell(7);
            name.setCellValue(nameMaterial);

            //channel
            Cell width = row.createCell(8);
            Cell height = row.createCell(9);
            width.setCellValue("Ширина, м");
            height.setCellValue("Глубина, м");

            //material
            Cell density = row.createCell(10);
            Cell heat = row.createCell(11);
            Cell melting = row.createCell(12);
            density.setCellValue("Плотность, кг/м³");
            heat.setCellValue("Удельная теплоемкость, Дж/(°C*кг)");
            melting.setCellValue("Температура плавления, °C");

            //Emp coef
            Cell alignTemp = row.createCell(13);
            Cell consistention = row.createCell(14);
            Cell heatTransfer = row.createCell(15);
            Cell indexMaterial = row.createCell(16);
            Cell viscosity = row.createCell(17);
            alignTemp.setCellValue("Коэффициент консистенции материала при температуре приведения, Па·с^n");
            consistention.setCellValue("Температурный коэффициент вязкости материала, 1/°С");
            indexMaterial.setCellValue("Индекс течения материала");
            viscosity.setCellValue("Коэффициент теплоотдачи от крышки канала к материалу, Вт/(м^2·°С)");
            heatTransfer.setCellValue("Температура приведения, °С");

            for (int i = 1; i < 7 ; i++) {
                Row rows = sheet.createRow(i);
                if (i == 1) {
                    //temp
                    Cell speedLabel = rows.createCell(0);
                    speedLabel.setCellValue("Скорость крышки,м/с");
                    for(int a = columnsTemperature.size()-1; a > 0; a--){
                        Cell tempLabel = rows.createCell(a);
                        tempLabel.setCellValue(columnsTemperature.get(a));
                    }
                    Cell tempL = rows.createCell(6);
                    tempL.setCellValue("Температура продукта, °C");
                    //channel
                    Cell widthVal = rows.createCell(8);
                    Cell heightVal = rows.createCell(9);
                    widthVal.setCellValue(channel.getWidth());
                    heightVal.setCellValue(channel.getHeight());

                    //material
                    Cell densityVal = rows.createCell(10);
                    Cell heatVal = rows.createCell(11);
                    Cell meltingVal = rows.createCell(12);
                    densityVal.setCellValue(material.getDensity());
                    heatVal.setCellValue(material.getHeat());
                    meltingVal.setCellValue(material.getMeltingTemperature());

                    //Emp coef
                    Cell alignTempVal = rows.createCell(13);
                    Cell consistentionVal = rows.createCell(14);
                    Cell heatTransferVal = rows.createCell(15);
                    Cell indexMaterialVal = rows.createCell(16);
                    Cell viscosityVal = rows.createCell(17);
                    alignTempVal.setCellValue(empCoef.getAlignmentTemperature());
                    consistentionVal.setCellValue(empCoef.getConsistention());
                    indexMaterialVal.setCellValue(empCoef.getIndexMaterial());
                    viscosityVal.setCellValue(empCoef.getViscosity());
                    heatTransferVal.setCellValue(empCoef.getHeatTransfer());
                }

                if (i == 2) {
                    creatorCell(rows, rowTemp1);
                }
                if (i == 3) {
                    creatorCell(rows, rowTemp2);
                }
                if (i == 4) {
                    creatorCell(rows, rowTemp3);
                }
                if (i == 5) {
                    creatorCell(rows, rowTemp4);
                }
                if (i == 6) {
                    creatorCell(rows, rowTemp5);
                }
            }

            Row rowLabel1 = sheet.createRow(8);
            Cell cellViscLabel = rowLabel1.createCell(0);
            cellViscLabel.setCellValue("Вязкость материала от температуры крышки при заданной скорости крышки");
            for(int i = 9; i < 15; i++){
                Row rows = sheet.createRow(i);
                if(i == 9){
                    Cell speedLabel = rows.createCell(0);
                    speedLabel.setCellValue("Скорость крышки,м/с");
                    for(int a = columnsTemperature.size()-1; a > 0; a--){
                        Cell tempLabel = rows.createCell(a);
                        tempLabel.setCellValue(columnsTemperature.get(a));
                    }
                    Cell tempL = rows.createCell(6);
                    tempL.setCellValue("Температура продукта, °C");
                }
                if(i == 10){
                    creatorCell(rows, rowVisc1);
                }
                if(i == 11){
                    creatorCell(rows, rowVisc2);
                }
                if(i == 12){
                    creatorCell(rows, rowVisc3);
                }
                if(i == 13){
                    creatorCell(rows, rowVisc4);
                }
                if(i == 14){
                    creatorCell(rows, rowVisc5);
                }
            }

            for(int i = 17; i < 23; i++){
                Row rows = sheet.createRow(i);
                if(i == 17){
                    for(int a = 0; a < columnsPerf.size(); a++){
                        Cell cell = rows.createCell(a);
                        cell.setCellValue(columnsPerf.get(a));
                    }
                }
                if(i == 18){
                    creatorCell(rows, rowPerfomance);
                }
                if(i == 19){
                    creatorCell(rows, rowPerfomance1);
                }
                if(i == 20){
                    creatorCell(rows, rowPerfomance2);
                }
                if(i == 21){
                    creatorCell(rows, rowPerfomance3);
                }
                if(i == 22){
                    creatorCell(rows, rowPerfomance4);
                }
            }

            // Записываем всё в файл
            sheet.autoSizeColumn(1);
            book.write(new FileOutputStream(file));
            book.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Экспорт");
            alert.setHeaderText("Экспорт завершен");
            alert.setContentText("Результаты моделирования сохранены в отчет:\n" + file.getPath());

            alert.showAndWait();

        } catch (IOException e) {
            getAlertFile(file);
        }
    }

    private void creatorCell(Row rows, ObservableList<String> rowsValues) {
        for (int a = 0; a < rowsValues.size(); a++) {
            Cell cell = rows.createCell(a);
            cell.setCellValue(Float.parseFloat(rowsValues.get(a)));
        }
    }
}
