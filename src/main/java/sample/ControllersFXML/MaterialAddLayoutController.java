package sample.ControllersFXML;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.MaterialDAO;
import sample.Objects.MaterialBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MaterialAddLayoutController {

    public TextField nameField;
    public TextField densityField;
    public TextField heatCapField;
    public TextField meltingField;
    public TextField speedField;
    public TextField temperatureField;
    public TextField consistencyField;
    public TextField viscosityField;
    public TextField tempReducField;
    public TextField flowField;
    public TextField heatTransfField;
    public ComboBox comboMaterialType;

    private Stage dialogStage;
    private MaterialBase materialBase = new MaterialBase();
    private boolean okClicked = false;
    private String setType = null;

    public MaterialAddLayoutController() {
    }

    public void initialize() {

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        heatCapField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heatCapField.setText(oldValue);
        });
        temperatureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) temperatureField.setText(oldValue);
        });
        speedField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) speedField.setText(oldValue);
        });
        consistencyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) consistencyField.setText(oldValue);
        });
        viscosityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) viscosityField.setText(oldValue);
        });
        tempReducField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) tempReducField.setText(oldValue);
        });
        densityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) densityField.setText(oldValue);
        });
        flowField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) flowField.setText(oldValue);
        });
        meltingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) meltingField.setText(oldValue);
        });
        heatTransfField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) heatTransfField.setText(oldValue);
        });

        try {
            addItemsCombo();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        comboMaterialType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setType = String.valueOf(newValue);
            }
        });

    }

    private void addItemsCombo() throws SQLException, ClassNotFoundException {
        ObservableList<String> materialNames = MaterialDAO.getTypeMaterial();
        comboMaterialType.setItems(materialNames);
    }

    public void onClickAddMaterial(ActionEvent actionEvent) {
        if (isInputValid()) {
            materialBase.setMaterialName(nameField.getText());
            ArrayList<Double> values = new ArrayList<>();
            values.add(Double.parseDouble(densityField.getText()));
            values.add(Double.parseDouble(heatCapField.getText()));
            values.add(Double.parseDouble(meltingField.getText()));
            values.add(Double.parseDouble(speedField.getText()));
            values.add(Double.parseDouble(temperatureField.getText()));
            values.add(Double.parseDouble(consistencyField.getText()));
            values.add(Double.parseDouble(viscosityField.getText()));
            values.add(Double.parseDouble(tempReducField.getText()));
            values.add(Double.parseDouble(flowField.getText()));
            values.add(Double.parseDouble(heatTransfField.getText()));

            //TODO написать chooser для типа материала

            int id = MaterialDAO.addMaterialInBase(materialBase, setType);

            for (int i = 0; i < 10; i++) {
                MaterialDAO.addMaterialInBase(id, i + 1, values.get(i));
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        if (nameField.getText() == null || densityField.getText() == null || heatCapField.getText() == null ||
                meltingField.getText() == null || speedField.getText() == null || temperatureField.getText() == null ||
                consistencyField.getText() == null || viscosityField.getText() == null || tempReducField.getText() == null ||
                flowField.getText() == null || heatTransfField.getText() == null || heatTransfField.getText() == null || setType == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Присутствует ошибка в одном из полей");
            alert.setContentText("Проверьте все поля на наличие ошибок!");

            alert.showAndWait();
            return false;
        }

        return true;
    }

    public void onClickCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
