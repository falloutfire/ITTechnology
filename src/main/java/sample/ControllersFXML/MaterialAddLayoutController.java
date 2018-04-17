package sample.ControllersFXML;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.MaterialDAO;
import sample.Objects.MaterialBase;

import java.util.ArrayList;

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

    private Stage dialogStage;
    private MaterialBase materialBase = new MaterialBase();
    private boolean okClicked = false;

    public MaterialAddLayoutController() {
    }

    public void initialize(){

    }

    public void onClickAddMaterial(ActionEvent actionEvent) {
        if(isInputValid()){
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

            int id = MaterialDAO.addMAterialInBase(materialBase, "Crystal");

            for(int i = 0; i < 10; i++){
                MaterialDAO.addMaterialInBase(id, i+1, values.get(i));
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
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
