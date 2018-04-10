package sample.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import sample.Main;
import sample.Objects.Channel;
import sample.Objects.EmpiricalCoefficients;
import sample.Objects.Material;
import sample.Objects.ValueAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RootLayoutController {

    private Main main;

    ObservableList<ValueAdapter> values;

    public void handleClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mathematics Model ");
        alert.setHeaderText("О программе");
        alert.setContentText("Авторы: Илья Лихачев, Илья Родионов\nГруппа 455");

        alert.showAndWait();
    }

    public void setMainApp(Main main) {
        this.main = main;
    }

    public void handleExport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(main.getPrimaryStage());
        if (file != null) {
            if (!file.getPath().endsWith(".xls")) {
                file = new File(file.getPath() + ".xls");
            }
        }

        exportDataToFile(file);
    }

    @SuppressWarnings("deprecation")
    private void exportDataToFile(File file) {
        try {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Расчет");

            values = main.getMainController().getValues();

            Channel channel = main.getMainController().getChannel();
            Material material = main.getMainController().getMaterial();
            EmpiricalCoefficients empCoef = main.getMainController().getEmpCoef();
            double temperatureCap = main.getMainController().getTemperature();
            double speedDouble = main.getMainController().getSpeed();

            System.out.println(channel.getHeight());
            Row row = sheet.createRow(0);
            Cell lenght = row.createCell(0);
            Cell consist = row.createCell(1);
            Cell temp = row.createCell(2);
            lenght.setCellValue("Длина, м");
            consist.setCellValue("Вязкость, Па*с");
            temp.setCellValue("Температура, °C");

            //Режим
            Cell tempCap = row.createCell(14);
            Cell speed = row.createCell(15);
            tempCap.setCellValue("Температура крышки, °C");
            speed.setCellValue("Скорость крышки, м/с");
            //channel
            Cell width = row.createCell(4);
            Cell height = row.createCell(5);
            width.setCellValue("Ширина, м");
            height.setCellValue("Глубина, м");

            //material
            Cell density = row.createCell(6);
            Cell heat = row.createCell(7);
            Cell melting = row.createCell(8);
            density.setCellValue("Плотность, кг/м³");
            heat.setCellValue("Удельная теплоемкость, Дж/(°C*кг)");
            melting.setCellValue("Температура плавления, °C");

            //Emp coef
            Cell alignTemp = row.createCell(9);
            Cell consistention = row.createCell(10);
            Cell heatTransfer = row.createCell(11);
            Cell indexMaterial = row.createCell(12);
            Cell viscosity = row.createCell(13);
            alignTemp.setCellValue("Коэффициент консистенции материала при температуре приведения, Па·с^n");
            consistention.setCellValue("Температурный коэффициент вязкости материала, 1/°С");
            indexMaterial.setCellValue("Индекс течения материала");
            viscosity.setCellValue("Коэффициент теплоотдачи от крышки канала к материалу, Вт/(м^2·°С)");
            heatTransfer.setCellValue("Температура приведения, °С");

            for (int i = 0; i < values.size(); i++) {
                Row rows = sheet.createRow(i + 1);
                if (i == 0) {
                    //Режим
                    Cell tempCapVal = rows.createCell(14);
                    Cell speedVal = rows.createCell(15);
                    tempCapVal.setCellValue(temperatureCap);
                    speedVal.setCellValue(speedDouble);

                    //channel
                    Cell widthVal = rows.createCell(4);
                    Cell heightVal = rows.createCell(5);
                    widthVal.setCellValue(channel.getWidth());
                    heightVal.setCellValue(channel.getHeight());

                    //material
                    Cell densityVal = rows.createCell(6);
                    Cell heatVal = rows.createCell(7);
                    Cell meltingVal = rows.createCell(8);
                    densityVal.setCellValue(material.getDensity());
                    heatVal.setCellValue(material.getHeat());
                    meltingVal.setCellValue(material.getMeltingTemperature());

                    //Emp coef
                    Cell alignTempVal = rows.createCell(9);
                    Cell consistentionVal = rows.createCell(10);
                    Cell heatTransferVal = rows.createCell(11);
                    Cell indexMaterialVal = rows.createCell(12);
                    Cell viscosityVal = rows.createCell(13);
                    alignTempVal.setCellValue(empCoef.getAlignmentTemperature());
                    consistentionVal.setCellValue(empCoef.getConsistention());
                    indexMaterialVal.setCellValue(empCoef.getIndexMaterial());
                    viscosityVal.setCellValue(empCoef.getViscosity());
                    heatTransferVal.setCellValue(empCoef.getHeatTransfer());
                }
                Cell lenghtCell = rows.createCell(0);
                Cell consistCell = rows.createCell(1);
                Cell temperatureCell = rows.createCell(2);
                lenghtCell.setCellValue(Double.parseDouble(values.get(i).getLenght().replace(',', '.')));
                consistCell.setCellValue(Double.parseDouble(values.get(i).getConsist().replace(',', '.')));
                temperatureCell.setCellValue(Double.parseDouble(values.get(i).getTemperature().replace(',', '.')));
            }

            // Записываем всё в файл
            sheet.autoSizeColumn(1);
            book.write(new FileOutputStream(file));
            book.close();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невзможно сохранить файл");
            alert.setContentText("Невзможно сохранить значения в файл:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
