/*
 * No lisence at all!
 */
package head.broken.automobilehelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Broken Head.
 *  This is a singleton class, that handles all DB tasks.
 *  This study app uses MySQL database, OpenFX 11 and plane JDBC.
 */

public class DBMainHandler {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";     //fresh driver for MySQL
    static final String DB_URL = "jdbc:mysql://localhost/AutomobileHelperDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USER = "root";            // name is specific for user
    static final String PASS = "3107892Mysql";    // password is specific for user
    private Connection conn = null;
    private Statement stmt = null;
    //private PreparedStatement createNewElementStmt = null; // try it for study

    public static DBMainHandler getInstance() {
        return DBHandlerHolder.INSTANCE;
    }

    private static class DBHandlerHolder {
        private static final DBMainHandler INSTANCE = new DBMainHandler();
    }

    // constructor!
    private DBMainHandler() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS); //Open a connection, asking DriverManager for that
            stmt = conn.createStatement();
            
            System.out.println("-----> Connection's done");
        } catch (Exception e) {
            e.printStackTrace();      //Handle errors for Class.forName
        }
    }

 /**
 *
 * On the very first start I have one value in DB, it's 0; 
 * @return this value on the very first start, and the actual last value on next starts.
 */
    public String getLastMilesOnStart() {
        String out = null;
        try {
            String sqlTask = "SELECT MileageValue FROM AutomobileHelperDB.MileageHistory WHERE ID = (SELECT MAX(ID) FROM AutomobileHelperDB.MileageHistory);";
            ResultSet result = stmt.executeQuery(sqlTask);
            result.next();  // this method is important before getting a value!!!
            int lastMileageValue = result.getInt("MileageValue");
            out = Integer.toString(lastMileageValue);
            result.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("-----> Get last mileage value on start has done.");
        return out;
    }
    
/**
 *
 * On the very first start I have the list of several car's elements in DB. 
 * @return  ArrayList of all elements from BD on start of app. 
 */
    public ArrayList getElementList() {
        ArrayList elementsFromDB = new ArrayList();
        try {
            String sqlTask = "SELECT ElementName FROM AutomobileHelperDB.AutoElements;";
            ResultSet result = stmt.executeQuery(sqlTask);
            while (result.next()) {
                String elementName = result.getString("ElementName");
                elementsFromDB.add(elementName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("-----> Element list ready.");
        return elementsFromDB;
    }

    public boolean saveMileage(int newMileage) {
        try {
            String sqlTask = "INSERT INTO AutomobileHelperDB.MileageHistory (MileageValue, DateStamp) VALUES (" + newMileage + ", CURDATE());";
            stmt.executeUpdate(sqlTask);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean createRecord(String partName, int curMileage, double elemCost, double serviceCost, String commentary) {
        System.out.println("-----> Entered in createRecord()");
        try {
            String sqlTask = "INSERT INTO AutomobileHelperDB.ServiceHistory (ServiceCost, ElementName, ElementCost, MileageStamp, DateStamp, Commentary)"
                    + " VALUES ( " + serviceCost + ", '" + partName + "', " + elemCost + ", " + curMileage + ", CURDATE(), '" + commentary + "'); ";
            stmt.executeUpdate(sqlTask);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // '" + partName + "'
    
    
    public String getLastRecordByName(String partName) {    // for "History" tab, search last record by ID column
        String lastRecord = null;
        try {
            String sqlTask = "SELECT ServiceCost, ElementName, ElementCost, MileageStamp, DateStamp, Commentary FROM AutomobileHelperDB.ServiceHistory"
                    + " WHERE ID = (SELECT MAX(ID) FROM AutomobileHelperDB.ServiceHistory WHERE ElementName = '" + partName + "') ;";
            ResultSet result = stmt.executeQuery(sqlTask);
            
            result.next();
            String resultName = result.getString("ElementName");
            String resultDate = result.getString("DateStamp");
            int resultMileage = result.getInt("MileageStamp");
            double resultElCost = result.getDouble("ElementCost");
            double resultMaintCost = result.getDouble("ServiceCost");
            String resultComment = result.getString("Commentary");
            // make a super string with whole result, maybe use a StringBuilder?
            lastRecord = "Name: " + resultName + "; \nDate:" + resultDate + ", \nMileage: " + resultMileage + "; \nElement cost: " + resultElCost
                    + "; \nService cost: " + resultMaintCost + " \nCommentary: " + resultComment;
            result.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastRecord;
    }

    public LinkedList<String> getAllRecordsByName(String partName) {
        LinkedList<String> resList = new LinkedList();
        ResultSet result;
        StringBuilder superString = new StringBuilder();
        String sqlTask = "SELECT ServiceCost, ElementCost, MileageStamp, DateStamp, Commentary FROM AutomobileHelperDB.ServiceHistory "
                + "WHERE ElementName = '" + partName + "';";
        try {
            result = stmt.executeQuery(sqlTask);
            while (result.next()) {
                double serviceCost = result.getDouble("ServiceCost");
                double elementCost = result.getDouble("ElementCost");
                int miles = result.getInt("MileageStamp");
                String date = result.getString("DateStamp");
                String comment = result.getString("Commentary");
                superString.append("\n---> Record: ").append("\nService cost:").append(serviceCost)
                            .append("\nElement cost: ").append(elementCost)
                            .append("\nMileage stamp: ").append(miles)
                            .append("\nDate stamp: ").append(date)
                            .append("\nCommentary: ").append(comment);
                resList.add(superString.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!resList.isEmpty()) {
            return resList;
        } else {
            resList.add("No results!");
            return resList;
        }
    }

    public String getElementDesc(String partName) {
        ResultSet result;
        String elementDesc = new String();
        try {
            String sqlTask = "SELECT ElementDesc FROM AutomobileHelperDB.AutoElements"
                    + " WHERE ElementName = '" + partName + "';";    // single quotes in SQL!
            result = stmt.executeQuery(sqlTask);
            result.next();
            elementDesc = result.getString("ElementDesc");

        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elementDesc;
    }

    public boolean setNewElementDesc(String partName, String newDesc) {
        try {
            String sqlTask = "UPDATE AutomobileHelperDB.AutoElements SET ElementDesc = '" + newDesc + "' WHERE ElementName = '" + partName + "';";
            stmt.executeUpdate(sqlTask);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    
    // doesn't work! check App.java 
    public boolean createNewElement(String partName, String elementDesc) {
        System.out.println("-----> Entered in createNewElement()");
        try {
            String sqlTask = "INSERT INTO AutomobileHelperDB.AutoElements (ElementName, ElementDesc) VALUES "
                    + "('" + partName + "', '" + elementDesc + "' );";
            stmt.executeUpdate(sqlTask);
            
            //String sqlLine = "";
            //createNewElementStmt = conn.prepareStatement(USER); // prepared statement create here!
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
