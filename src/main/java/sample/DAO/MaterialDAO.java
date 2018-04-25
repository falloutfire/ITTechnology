package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Objects.Material;
import sample.Objects.MaterialBase;
import sample.Util.DBUtil;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialDAO {

    public static MaterialBase searchMaterialBase(String materialName) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmtName = "Select Material_ID, Material_name from material where Material_name = '" + materialName + "'";
        int materialId = 0;
        String name = null;
        try {
            ResultSet rsMatName = DBUtil.dbExecuteQuery(selectStmtName);
            while (rsMatName.next()) {
                materialId = rsMatName.getInt(1);
                name = rsMatName.getString(2);
            }
            String selectStmtValue = "Select Parameter_value from parameter_value WHERE Material_id = " + materialId;
            ResultSet rsMatValue = DBUtil.dbExecuteQuery(selectStmtValue);

            //Send ResultSet to the getMaterialFromResultSet method and get employee object

            //Return materialBase object
            return getMaterialBaseFromResultSet(rsMatValue, materialId, name);
        } catch (SQLException e) {
            System.out.println("While searching an material with " + materialName + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    public static ObservableList<String> searchAllMaterialName() throws SQLException {
        String selectStmt = "SELECT Material_name FROM material";
        ObservableList<String> materialBases = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DBUtil.dbExecuteQuery(selectStmt);
            while (resultSet.next()) {
                materialBases.add(resultSet.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return materialBases;
    }

    public static ObservableList<MaterialBase> searchAllMaterial() throws SQLException {
        String materialName;
        String selectStmtId = "SELECT Material_name FROM material";

        ObservableList<MaterialBase> materialBases = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DBUtil.dbExecuteQuery(selectStmtId);
            while (resultSet.next()) {
                materialName = resultSet.getString(1);
                materialBases.add(searchMaterialBase(materialName));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return materialBases;
    }

    private static MaterialBase getMaterialBaseFromResultSet(ResultSet rsMat, int materialId, String materialName) throws SQLException {
        MaterialBase materialBase;
        ArrayList<Double> values = new ArrayList<>();
        while (rsMat.next()) {
            values.add(rsMat.getDouble(1));
        }
        materialBase = new MaterialBase();
        materialBase.setMaterial_id(materialId);
        materialBase.setMaterialName(materialName);
        materialBase.setDensity(values.get(0));
        materialBase.setCapacity(values.get(1));
        materialBase.setMelting(values.get(2));
        materialBase.setCoverSpeed(values.get(3));
        materialBase.setCoverTemp(values.get(4));
        materialBase.setConsist(values.get(5));
        materialBase.setViscosity(values.get(6));
        materialBase.setTemp(values.get(7));
        materialBase.setFlow(values.get(8));
        materialBase.setHeatTrans(values.get(9));
        return materialBase;
    }

    public static void updateMaterialName(MaterialBase materialBase) throws ClassNotFoundException, SQLException {
        String updateStmt =
                "UPDATE material\n" +
                        "SET Material_name = '" + materialBase.getMaterialName() + "'\n" +
                        "WHERE Material_ID = " + materialBase.getMaterial_id() + ";\n";
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMaterialValue(MaterialBase materialBase, int value_ID, Double parameter) throws ClassNotFoundException, SQLException {
        String updateStmt =
                "UPDATE parameter_value\n" +
                        "SET Parameter_value = " + parameter + "\n" +
                        "WHERE Material_ID = " + materialBase.getMaterial_id() + " AND Parameter_ID = " + (value_ID) + ";\n";
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static int addMaterialInBase(MaterialBase materialBase, String materialType) {
        String addStmt = "INSERT INTO material (Material_type, Material_name) VALUES ('" + materialType + "', '"
                + materialBase.getMaterialName() + "');\n";
        String idStmt = "Select Material_ID from material where Material_name = '" + materialBase.getMaterialName() + "'";
        int materialId = 0;
        ResultSet rsMatName;
        try {
            DBUtil.dbExecuteUpdate(addStmt);
            rsMatName = DBUtil.dbExecuteQuery(idStmt);
            while (rsMatName.next()) {
                materialId = rsMatName.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return materialId;
    }

    public static void addMaterialInBase(int id, int parameterID, double parameterValue) {
        try {
            String addStmt = "INSERT INTO parameter_value (Material_ID, Parameter_ID, Parameter_value) VALUE (" +
                    id + ", " + parameterID + ", " + parameterValue + ");";

            DBUtil.dbExecuteUpdate(addStmt);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMaterialBase(int materialId) throws SQLException, ClassNotFoundException {
        String updateStmtMaterial =
                "DELETE FROM material\n" +
                        "WHERE Material_ID = " + materialId + ";";
        String updateStmtValues =
                "DELETE FROM parameter_value\n" +
                        "WHERE Material_ID = " + materialId + ";";
        DBUtil.dbExecuteUpdate(updateStmtMaterial);
        DBUtil.dbExecuteUpdate(updateStmtValues);
    }

    public static ObservableList<String> getTypeMaterial() throws SQLException, ClassNotFoundException {
        String getStmt = "SELECT Material_type FROM material_class";
        ObservableList<String> types = FXCollections.observableArrayList();

        ResultSet resultSet = DBUtil.dbExecuteQuery(getStmt);
        while (resultSet.next()){
            types.add(resultSet.getString(1));
        }

        return types;
    }


    public static ObservableList<MaterialBase> searchMaterialBaseLike(String materialName) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmtName = "Select Material_ID, Material_name from material where Material_name LIKE '%" + materialName + "%'";
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> id = new ArrayList<>();
        ObservableList<MaterialBase> materialBases = FXCollections.observableArrayList();
        try {
            ResultSet rsMatName = DBUtil.dbExecuteQuery(selectStmtName);
            while (rsMatName.next()) {
                id.add(rsMatName.getInt(1));
                names.add(rsMatName.getString(2));
            }
            for(int i = 0; i < id.size(); i++){
                String selectStmtValue = "Select Parameter_value from parameter_value WHERE Material_id = " + id.get(i);
                ResultSet rsMatValue = DBUtil.dbExecuteQuery(selectStmtValue);

                //Send ResultSet to the getMaterialFromResultSet method and get material object
                materialBases.add(getMaterialBaseFromResultSet(rsMatValue, id.get(i), names.get(i)));

            }

            //Return materialBase object
            return materialBases;
        } catch (SQLException e) {
            System.out.println("While searching an material with " + materialName + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }
}
