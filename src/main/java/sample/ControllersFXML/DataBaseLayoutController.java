package sample.ControllersFXML;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import sample.DAO.MaterialDAO;
import sample.Main;
import sample.Objects.MaterialBase;
import sample.Util.DBUtil;

import java.sql.SQLException;

public class DataBaseLayoutController {


    public TableView<MaterialBase> dataBaseTable;
    public TableColumn<MaterialBase, Integer> idColumn;
    public TableColumn<MaterialBase, Double> densityColumn;
    public TableColumn<MaterialBase, Double>  heatCapColumn;
    public TableColumn<MaterialBase, Double>  meltingColumn;
    public TableColumn<MaterialBase, Double>  speedColumn;
    public TableColumn<MaterialBase, Double>  coverTempColumn;
    public TableColumn<MaterialBase, Double>  consistencyColumn;
    public TableColumn<MaterialBase, Double>  viscosityColumn;
    public TableColumn<MaterialBase, Double>  tempReduceColumn;
    public TableColumn<MaterialBase, Double>  flowColumn;
    public TableColumn<MaterialBase, Double>  heatTransferColumn;
    public TableColumn<MaterialBase, String> materialNameColumn;
    public ComboBox comboMaterialName;
    public TextField searchByNameField;

    public DataBaseLayoutController(){

    }

    public void initialize(){
        //data base factory
        idColumn.setCellValueFactory(cellData -> cellData.getValue().material_idProperty().asObject());
        materialNameColumn.setCellValueFactory(cellData -> cellData.getValue().materialNameProperty());
        densityColumn.setCellValueFactory(cellData -> cellData.getValue().densityProperty().asObject());
        heatCapColumn.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());
        meltingColumn.setCellValueFactory(cellData -> cellData.getValue().meltingProperty().asObject());
        speedColumn.setCellValueFactory(cellData -> cellData.getValue().coverSpeedProperty().asObject());
        coverTempColumn.setCellValueFactory(cellData -> cellData.getValue().coverTempProperty().asObject());
        consistencyColumn.setCellValueFactory(cellData -> cellData.getValue().consistProperty().asObject());
        viscosityColumn.setCellValueFactory(cellData -> cellData.getValue().viscosityProperty().asObject());
        tempReduceColumn.setCellValueFactory(cellData -> cellData.getValue().tempProperty().asObject());
        flowColumn.setCellValueFactory(cellData -> cellData.getValue().flowProperty().asObject());
        heatTransferColumn.setCellValueFactory(cellData -> cellData.getValue().heatTransProperty().asObject());

        materialNameColumn.setCellFactory(TextFieldTableCell.<MaterialBase>forTableColumn());
        materialNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<MaterialBase, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<MaterialBase, String> event) {
                TablePosition<MaterialBase, String> pos = event.getTablePosition();
                int row = pos.getRow();
                String newNameMaterial = event.getNewValue();
                if(newNameMaterial != null){
                    try {
                        /*MaterialBase materialBase = MaterialDAO.searchMaterialBase(event.getOldValue());
                        materialBase.setMaterialName(newNameMaterial);*/
                        MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                        materialBase.setMaterialName(newNameMaterial);
                        MaterialDAO.updateMaterial(materialBase);
                        addItemsCombo();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
        try {
            addItemsCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboMaterialName.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    searchMaterial((String) newValue);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        dataBaseTable.setEditable(true);
    }

    public void onClickFindAll(ActionEvent actionEvent) throws SQLException {
        ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
        //Populate material on TableView and Display on TextArea
        dataBaseTable.setItems(materialBases);
    }

    public void searchMaterial(String materialName) throws ClassNotFoundException, SQLException {
        try {
            //Get material information
            MaterialBase mat = MaterialDAO.searchMaterialBase(materialName);
            //Populate material on TableView and Display on TextArea
            populateMaterial(mat);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while getting employee information from DB.\n" + e);
            throw e;
        }
    }

    public void addItemsCombo() throws SQLException {
        ObservableList<String> materialNames = MaterialDAO.searchAllMaterialName();
        comboMaterialName.setItems(materialNames);
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

    public void onClickFindMaterialByName(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        MaterialBase materialBase = MaterialDAO.searchMaterialBase(searchByNameField.getText());
        populateMaterial(materialBase);
    }
}
