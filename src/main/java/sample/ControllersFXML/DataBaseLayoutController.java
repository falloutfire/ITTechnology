package sample.ControllersFXML;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import sample.DAO.MaterialDAO;
import sample.Main;
import sample.Objects.MaterialBase;

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
    private Main main;
    Object val;


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

        materialNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        densityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        heatCapColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        meltingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        speedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        coverTempColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        consistencyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        viscosityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tempReduceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        flowColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        heatTransferColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        materialNameColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, String> pos = event.getTablePosition();
            int row = pos.getRow();
            String newNameMaterial = event.getNewValue();
            if(newNameMaterial != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setMaterialName(newNameMaterial);
                    MaterialDAO.updateMaterialName(materialBase);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setMaterialName(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        heatCapColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setCapacity(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        consistencyColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setConsist(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        coverTempColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setCoverTemp(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        speedColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setCoverSpeed(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        flowColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setFlow(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        meltingColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setMelting(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        viscosityColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setViscosity(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        densityColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            int column = pos.getColumn();
            System.out.println(column);
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setDensity(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, column, newValue);
                    //MaterialDAO.updateMaterialDensity(materialBase, column, newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        heatTransferColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setHeatTrans(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });
        tempReduceColumn.setOnEditCommit(event -> {
            TablePosition<MaterialBase, Double> pos = event.getTablePosition();
            int row = pos.getRow();
            Double newValue = event.getNewValue();
            if(newValue != null){
                try {
                    MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                    materialBase.setTemp(newValue);
                    MaterialDAO.updateMaterialValue(materialBase, pos.getColumn(), newValue);
                    addItemsCombo();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                MaterialBase materialBase = pos.getTableView().getItems().get(pos.getRow());
                materialBase.setDensity(event.getOldValue());
                pos.getTableView().getItems().set(pos.getRow(),materialBase);
            }
        });

        try {
            addItemsCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboMaterialName.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchMaterial((String) newValue);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

        dataBaseTable.setEditable(true);
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

    private void addItemsCombo() throws SQLException {
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
        //MaterialBase materialBase = MaterialDAO.searchMaterialBase(searchByNameField.getText());
        //populateMaterial(materialBase);
        ObservableList<MaterialBase> materialBases = MaterialDAO.searchMaterialBaseLike(searchByNameField.getText());
        dataBaseTable.setItems(materialBases);
    }

    public void onClickAddMaterial(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean okClicked = main.showMaterialAddDialog();
        if (okClicked) {
            ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
            dataBaseTable.setItems(materialBases);
            addItemsCombo();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void onClickFindAll(ActionEvent actionEvent) throws SQLException {
        ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
        //Populate material on TableView and Display on TextArea
        dataBaseTable.setItems(materialBases);
    }

    public void onClickDeleteMaterial(ActionEvent actionEvent) {
        MaterialBase materialBase = dataBaseTable.getSelectionModel().getSelectedItem();
        try {
            MaterialDAO.deleteMaterialBase(materialBase.getMaterial_id());
            ObservableList<MaterialBase> materialBases = MaterialDAO.searchAllMaterial();
            dataBaseTable.setItems(materialBases);
            addItemsCombo();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
