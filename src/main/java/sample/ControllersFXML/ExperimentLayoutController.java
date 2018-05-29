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
    public TextField deltaTempField;
    public TextField deltaSpeedField;
    NumberFormat formaterTemp = new DecimalFormat("#0.0");
    NumberFormat formater = new DecimalFormat("#0");
    private Stage dialogStage;
    private boolean okClicked = false;
    private Material material;
    private EmpiricalCoefficients empCoef;
    private Channel channel;
    private String nameMaterial;
    private List<String> columnsTemperature;
    //для таблиц
    private ArrayList<ObservableList<String>> rowTemp;
    private ArrayList<ObservableList<String>> rowVisc;
    private ArrayList<ObservableList<String>> rowPerf;
    //для графиков
    private ArrayList<ObservableList<String>> grafTemp;
    private ArrayList<ObservableList<String>> grafVisc;
    private ArrayList<ObservableList<String>> grafPerf;
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
        tempView.getItems().clear();
        perfomanceView.getItems().clear();
        viscosityView.getItems().clear();
    }

    public void onClickCalculate(ActionEvent actionEvent) {

        clearing();

        if (Float.parseFloat(tempFromField.getText()) < Float.parseFloat(tempToField.getText()) && Float.parseFloat(speedFromField.getText()) < Float.parseFloat(speedToField.getText())) {

            columnsTemperature = new ArrayList<String>(getDiapazon(Float.parseFloat(tempFromField.getText()), Float.parseFloat(tempToField.getText()), Float.parseFloat(deltaTempField.getText())));
            List<String> columnsSpeed = new ArrayList<String>(getDiapazon(Float.parseFloat(speedFromField.getText()), Float.parseFloat(speedToField.getText()), Float.parseFloat(deltaSpeedField.getText())));
            columnsPerf = FXCollections.observableArrayList();
            columnsPerf.add("Скорость \nкрышки, м/с");
            columnsPerf.add("Производительность, Па*с");

            ArrayList<String> tempsGraf = new ArrayList<String>(getDiapazon(Float.parseFloat(tempFromField.getText()), Float.parseFloat(tempToField.getText())));
            ArrayList<String> speedGraf = new ArrayList<String>(getDiapazon(Float.parseFloat(speedFromField.getText()), Float.parseFloat(speedToField.getText())));

            long startTime = System.currentTimeMillis();

            setCellFactoryTable(columnsTemperature, tempView.getColumns());
            setCellFactoryTable(columnsTemperature, viscosityView.getColumns());
            setCellFactoryTable(columnsPerf, perfomanceView.getColumns());

            rowTemp = new ArrayList<>();
            rowVisc = new ArrayList<>();
            rowPerf = new ArrayList<>();

            grafTemp = new ArrayList<>();
            grafVisc = new ArrayList<>();
            grafPerf = new ArrayList<>();

            for(int i = 1; i < columnsSpeed.size(); i++){
                calc(Float.parseFloat(columnsSpeed.get(i)), columnsTemperature, rowTemp, rowVisc, rowPerf);
            }

            tempView.getItems().addAll(rowTemp);
            viscosityView.getItems().addAll(rowVisc);
            perfomanceView.getItems().addAll(rowPerf);

            for (int i = 1; i < speedGraf.size(); i++) {
                calc(Float.parseFloat(speedGraf.get(i)), tempsGraf, grafTemp, grafVisc, grafPerf);
            }

            NumberAxis yAxisTemp = new NumberAxis(Float.parseFloat(grafTemp.get(0).get(1)) - 4, Float.parseFloat(grafTemp.get(0).get(4)) + 4, 5);
            if (Float.parseFloat(grafTemp.get(0).get(4)) < Float.parseFloat(grafTemp.get(4).get(4))) {
                yAxisTemp.setUpperBound(Float.parseFloat(grafTemp.get(4).get(4)) + 4);
            }
            NumberAxis xAxisTemp = new NumberAxis(Float.parseFloat(tempsGraf.get(1)), Float.parseFloat(tempsGraf.get(5)) + 5, 5);
            NumberAxis yAxisVisc = new NumberAxis();
            NumberAxis xAxisVisc = new NumberAxis(Float.parseFloat(tempsGraf.get(1)), Float.parseFloat(tempsGraf.get(5)) + 5, 5);
            NumberAxis yAxisPerf = new NumberAxis();
            NumberAxis xAxisPerf = new NumberAxis(Float.parseFloat(grafPerf.get(0).get(0)), Float.parseFloat(grafPerf.get(4).get(0)), 1);

            yAxisTemp.setLabel("Температура продукта,°С");
            xAxisTemp.setLabel("Температура крышки,°С");
            yAxisVisc.setLabel("Вязкость продукта, Па*с");
            xAxisVisc.setLabel("Температура крышки,°С");
            yAxisPerf.setLabel("Производительность,кг/ч");
            xAxisPerf.setLabel("Скорость крышки,м/с");

            LineChart<Number, Number> tempChart = new LineChart<>(xAxisTemp, yAxisTemp);
            LineChart<Number, Number> viscChart = new LineChart<>(xAxisVisc, yAxisVisc);
            LineChart<Number, Number> perfChart = new LineChart<>(xAxisPerf, yAxisPerf);

            for(int i = 0; i < 5; i++){
                XYChart.Series tempSeries = getSeries(tempsGraf, grafTemp.get(i));
                XYChart.Series viscSeries = getSeries(tempsGraf, grafVisc.get(i));
                viscSeries.setName("Вязкость материала от температуры крышки при скорости крышки " + grafVisc.get(i).get(0) + "м/с");
                tempSeries.setName("Температура материала от температуры крышки при скорости крышки " + grafTemp.get(i).get(0) + "м/с");
                tempChart.getData().add(tempSeries);
                viscChart.getData().add(viscSeries);
            }

            XYChart.Series perfSeries = getSeries(grafPerf);
            perfChart.getData().add(perfSeries);
            perfChart.setLegendVisible(false);

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

    private XYChart.Series getSeries(ArrayList<ObservableList<String>> row) {
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < row.size(); i++){
            series.getData().add(new XYChart.Data<>(Float.parseFloat(row.get(i).get(0)), Float.parseFloat(row.get(i).get(0))));
        }
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

    private void calc(float speed, List<String> tempList, ArrayList<ObservableList<String>> rowTemp, ArrayList<ObservableList<String>> rowVisc, ArrayList<ObservableList<String>> rowPerf) {
        ObservableList<String> temp = FXCollections.observableArrayList();
        ObservableList<String> vis = FXCollections.observableArrayList();
        ObservableList<String> perf = FXCollections.observableArrayList();
        temp.add(String.valueOf(speed));
        vis.add(String.valueOf(speed));
        double Fch = 0.125 * Math.pow((channel.getHeight() / channel.getWidth()), 2) - 0.625 * (channel.getHeight() / channel.getWidth()) + 1;
        double Q = (channel.getWidth() * channel.getHeight() * speed) / 2 * Fch;
        double G = material.getDensity() * Q * 3600;// кг час
        perf.add(String.valueOf(speed));
        perf.add(formater.format(G));
        for (int i = 1; i < tempList.size(); i++) {
            double temperature = Double.parseDouble(tempList.get(i));

            double gamma = speed / channel.getHeight();
            double qGamma = channel.getWidth() * channel.getHeight() * empCoef.getConsistention() * Math.pow(gamma, empCoef.getIndexMaterial() + 1);
            double qAlpha = channel.getWidth() * empCoef.getHeatTransfer() * ((1 / empCoef.getViscosity()) - temperature + empCoef.getAlignmentTemperature());
            double ae = ((empCoef.getViscosity() * qGamma + channel.getWidth() * empCoef.getHeatTransfer()) / (empCoef.getViscosity() * qAlpha)) * (1 - Math.exp(-(channel.getLenght() * empCoef.getViscosity() * qAlpha) / (material.getDensity() * material.getHeat() * Q))) + Math.exp(empCoef.getViscosity() * (material.getMeltingTemperature() - empCoef.getAlignmentTemperature() - ((channel.getLenght() * qAlpha) / (material.getDensity() * material.getHeat() * Q))));

            double T = empCoef.getAlignmentTemperature() + 1 / empCoef.getViscosity() * Math.log(ae); //Температура для зависимости
            temp.add(formaterTemp.format(T).replace(",", "."));
            double consistention = empCoef.getConsistention() * Math.exp(-empCoef.getViscosity() * (T - empCoef.getAlignmentTemperature()));
            double viscosityMaterial = consistention * Math.pow(gamma, empCoef.getIndexMaterial() - 1); //взякость для зависимости
            vis.add(formater.format(viscosityMaterial));
        }
        rowTemp.add(temp);
        rowVisc.add(vis);
        rowPerf.add(perf);
    }

    private ArrayList<String> getDiapazon(float min, float max, float delta) {
        ArrayList<String> diapazon = new ArrayList<>();
        diapazon.add("Температура,°С");
        for (float i = min; i <= max; i += delta) {
            diapazon.add(String.valueOf(i));
        }
        return diapazon;
    }

    private ArrayList<String> getDiapazon(float min, float max){
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
        if(rowTemp != null){
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
            Sheet sheetInfo = book.createSheet("Информация");
            Sheet sheetTemp = book.createSheet("Температура");
            Sheet sheetVisc = book.createSheet("Вязкость");
            Sheet sheetPerf = book.createSheet("Производительность");


            Row row = sheetInfo.createRow(0);
            //Cell table1Label = row.createCell(0);
            //table1Label.setCellValue("Температура материала от температуры крышки при заданной скорости крышки");
            Cell name = row.createCell(1);
            name.setCellValue(nameMaterial);

            //channel
            Cell lenght = row.createCell(2);
            Cell width = row.createCell(3);
            Cell height = row.createCell(4);
            lenght.setCellValue("Длина, м");
            width.setCellValue("Ширина, м");
            height.setCellValue("Глубина, м");

            //material
            Cell density = row.createCell(5);
            Cell heat = row.createCell(6);
            Cell melting = row.createCell(7);
            density.setCellValue("Плотность, кг/м³");
            heat.setCellValue("Удельная теплоемкость, Дж/(°C*кг)");
            melting.setCellValue("Температура плавления, °C");

            //Emp coef
            Cell alignTemp = row.createCell(8);
            Cell consistention = row.createCell(9);
            Cell heatTransfer = row.createCell(10);
            Cell indexMaterial = row.createCell(11);
            Cell viscosity = row.createCell(12);
            alignTemp.setCellValue("Коэффициент консистенции материала при температуре приведения, Па·с^n");
            consistention.setCellValue("Температурный коэффициент вязкости материала, 1/°С");
            indexMaterial.setCellValue("Индекс течения материала");
            viscosity.setCellValue("Коэффициент теплоотдачи от крышки канала к материалу, Вт/(м^2·°С)");
            heatTransfer.setCellValue("Температура приведения, °С");

            Row rows = sheetInfo.createRow(1);
            for(int i = 0; i < 13; i++){
                //channel
                Cell lenghtVal = rows.createCell(2);
                Cell widthVal = rows.createCell(3);
                Cell heightVal = rows.createCell(4);
                lenghtVal.setCellValue(channel.getLenght());
                widthVal.setCellValue(channel.getWidth());
                heightVal.setCellValue(channel.getHeight());

                //material
                Cell densityVal = rows.createCell(5);
                Cell heatVal = rows.createCell(6);
                Cell meltingVal = rows.createCell(7);
                densityVal.setCellValue(material.getDensity());
                heatVal.setCellValue(material.getHeat());
                meltingVal.setCellValue(material.getMeltingTemperature());

                //Emp coef
                Cell alignTempVal = rows.createCell(8);
                Cell consistentionVal = rows.createCell(9);
                Cell heatTransferVal = rows.createCell(10);
                Cell indexMaterialVal = rows.createCell(11);
                Cell viscosityVal = rows.createCell(12);
                alignTempVal.setCellValue(empCoef.getAlignmentTemperature());
                consistentionVal.setCellValue(empCoef.getConsistention());
                indexMaterialVal.setCellValue(empCoef.getIndexMaterial());
                viscosityVal.setCellValue(empCoef.getViscosity());
                heatTransferVal.setCellValue(empCoef.getHeatTransfer());
            }

            for (int i = 0; i < rowTemp.size()+2; i++) {
                Row rowsTemp = sheetTemp.createRow(i);
                if(i == 0){
                    Cell table1Label = rowsTemp.createCell(0);
                    table1Label.setCellValue("Температура материала от температуры крышки при заданной скорости крышки");
                } else {
                    creatorTables(i, rowsTemp);
                }
            }

            for (int i = 0; i < rowVisc.size()+2; i++) {
                Row rowsVisc = sheetVisc.createRow(i);
                if(i == 0){
                    Cell table1Label = rowsVisc.createCell(0);
                    table1Label.setCellValue("Вязкость материала от температуры крышки при заданной скорости крышки");
                } else {
                    creatorTables(i, rowsVisc);
                }
            }


            for(int i = 0; i < rowPerf.size()+1; i++){
                Row rowsPerf = sheetPerf.createRow(i);
                if(i == 0){
                    for(int a = 0; a < columnsPerf.size(); a++){
                        Cell cell = rowsPerf.createCell(a);
                        cell.setCellValue(columnsPerf.get(a));
                    }
                } else {
                    creatorCell(rowsPerf, rowPerf.get(i-1));
                }
            }

            // Записываем всё в файл
            sheetInfo.autoSizeColumn(1);
            sheetPerf.autoSizeColumn(1);
            sheetTemp.autoSizeColumn(1);
            sheetVisc.autoSizeColumn(1);
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

    private void creatorTables(int i, Row rowsVisc) {
        if (i == 1) {
            Cell speedLabel = rowsVisc.createCell(0);
            speedLabel.setCellValue("Скорость крышки,м/с");
            for(int a = columnsTemperature.size()-1; a > 0; a--){
                Cell tempLabel = rowsVisc.createCell(a);
                tempLabel.setCellValue(columnsTemperature.get(a));
            }
            Cell tempL = rowsVisc.createCell(columnsTemperature.size());
            tempL.setCellValue("Температура продукта, °C");
        } else {
            creatorCell(rowsVisc, rowTemp.get(i-2));
        }
    }

    private void creatorCell(Row rows, ObservableList<String> rowsValues) {
        for (int a = 0; a < rowsValues.size(); a++) {
            Cell cell = rows.createCell(a);
            cell.setCellValue(Float.parseFloat(rowsValues.get(a)));
        }
    }
}
