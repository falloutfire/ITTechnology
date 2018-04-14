package sample.Util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    private static final String url = "jdbc:mysql://localhost:3306/isat?&useSSL=false";
    private static final String user = "root";
    private static final String password = "polosin";

    // JDBC переменные для создания и управлением соединения
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    public static void dbConnect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException cnfe) {
            System.err.println("Error: " + cnfe.getMessage());
        }
        conn = DriverManager.getConnection(url, user, password);

    }

    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;

        try {
            dbConnect();
            //Создание выражений для выполнения запросов
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);

            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }

    //For Update/Insert/Delete operations
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException{

        Statement stmt = null;
        try{
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e){
            throw e;
        } finally {
            if(stmt != null){
                stmt.close();
            }
            dbDisconnect();
        }
    }


}

