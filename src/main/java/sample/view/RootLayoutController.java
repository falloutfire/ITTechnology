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

    public void handleExport(ActionEvent actionEvent){
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

            Row row = sheet.createRow(0);
            Cell lenght = row.createCell(0);
            Cell consist = row.createCell(1);
            Cell temp = row.createCell(2);
            lenght.setCellValue("Длина, m");
            consist.setCellValue("Вязкость, Па*с");
            temp.setCellValue("Temperature, °C");

            for (int i = 0; i < values.size(); i++) {
                Row rows = sheet.createRow(i + 1);
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
