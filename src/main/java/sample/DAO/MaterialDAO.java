package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
            MaterialBase materialBase = getMaterialBaseFromResultSet(rsMatValue, materialId, name);

            //Return materialBase object
            return materialBase;
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

    public static void updateMaterial(MaterialBase materialBase) throws ClassNotFoundException, SQLException {
        String updateStmt =
                //"BEGIN;\n" +
                        "   UPDATE material\n" +
                        "      SET Material_name = '" + materialBase.getMaterialName() + "'\n" +
                        "    WHERE Material_ID = " + materialBase.getMaterial_id() + ";\n"; //+
                        //"   COMMIT;\n";
                        //"END;";
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    private static MaterialBase getMaterialBaseFromResultSet(ResultSet rsMat, int materialId, String materialName) throws SQLException {
        MaterialBase materialBase = null;
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

}
