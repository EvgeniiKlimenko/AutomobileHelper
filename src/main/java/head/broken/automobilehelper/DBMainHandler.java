/*
 * No lisence!
 */
package head.broken.automobilehelper;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Broken Head.
 *
 */

/*
 This is a singleton class, that handles all DB tasks.
 This app uses MySQL database AutoPartsHelp, that contains three tables:
SELECT partName, dateStamp, mileageStamp, commentText FROM Automobile.autoParameters 
WHERE partName = 'Bearings' AND ID = MAX(ID) ;
 */
public class DBMainHandler {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";     //fresh driver for MySQL
    static final String DB_URL = "jdbc:mysql://localhost/AutomobileHelperDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USER = "root";            // name is specific for user
    static final String PASS = "3107892Mysql";    // password is specific for user
    private Connection conn = null;
    private Statement stmt = null;

    public static DBMainHandler getInstance() {
        return DBHandlerHolder.INSTANCE;
    }

    private static class DBHandlerHolder {

        private static final DBMainHandler INSTANCE = new DBMainHandler();
    }

    private DBMainHandler() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS); //Open a connection, asking DriverManager for that
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();      //Handle errors for Class.forName
        }
    }

    public String getLastMilesOnStart() {
        int lastMileageValue;
        String out = null;
        ResultSet result;

        try {
            String sqlTask = "SELECT MileageValue FROM AutomobileHelperDB.MileageHistory WHERE ID = (SELECT MAX(ID) FROM AutomobileHelperDB.MileageHistory);";
            result = stmt.executeQuery(sqlTask);
            lastMileageValue = result.getInt("MileageValue");
            out = Integer.toString(lastMileageValue);

        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("-----> Get last value of mileage on application start");
        return out;
    }
    
   
   public void getElementList(){}
   
   
   
   

    public void saveMileage(int newMileage) {
        System.out.println("-----> Got new mileage " + newMileage);
        try {
            String sqlTask = "INSERT INTO AutomobileHelperDB.MileageHistory (MileageValue, DateStamp) VALUES (" + newMileage + ", CURDATE());";
            stmt.executeUpdate(sqlTask);
            System.out.println("-----> Mileage saved");
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Add element name to the table
    public void createRecord(String partName, int curMileage, double elemCost, double serviceCost, String commentary) {
        try {
            // commentary text without ' or ". Add deletion of this symbols!!!
            String sqlTask = "INSERT INTO AutomobileHelperDB.ServiceHistory (ServiceCost, ElementName, ElementCost, MileageStamp, DateStamp, Commentary)"
                    + " VALUES ( " + serviceCost + ", '" + partName +"', " + elemCost + ", " + curMileage + ", CURDATE(), '" + commentary + "'); ";
            stmt.executeUpdate(sqlTask);
            System.out.println("-----> Record created!");
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLastRecordByName(String partName) {    // for "History" tab, search last record by ID column
        ResultSet result;
        String lastRecord = null;
        String resultName = null;
        String resultDate = null;
        int resultMileage = 0;
        double resultElCost = 0;
        double resultMaintCost = 0;
        String resultComment = null;
        try {
            String sqlTask = "SELECT ServiceCost, ElementCost, MileageStamp, DateStamp, Commentary FROM AutomobileHelperDB.ServiceHistory"
                    + " WHERE partName = '" + partName + "' AND ID = (SELECT MAX(ID) FROM AutomobileHelperDB.ServiceHistory) ;";
            result = stmt.executeQuery(sqlTask);
            resultName = result.getString("partName");
            resultDate = result.getString("dateStamp");
            resultMileage = result.getInt("mileageStamp");
            resultElCost = result.getInt("elementCost");
            resultMaintCost = result.getInt("maintenanceCost");
            resultComment = result.getString("comment");
            // make a super string with whole result, maybe use a StringBuilder?
            lastRecord = "Name: " + resultName + "; Date:" + resultDate + ", Mileage: " + resultMileage + " km; Element cost: " + resultElCost
                    + "; Maintenance cost:" + resultMaintCost + " \nCommentary: " + resultComment;
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastRecord;
    }

    /**
     * For the future. I need to learn more about SQL quarries.
     * @param partName mean for which part I need to return all records
     * @return super-huge string with all results (maybe i need to return some
     * kind of collection)
     */
    public String getAllRecordsByName(String partName) {
        ResultSet result;
        String oneRecord = null;

        return oneRecord;
    }

    public String getElementDesc(String partName) {
        ResultSet result;
        String elementDesc = new String();
        try {
            String sqlTask = "SELECT ElementDesc FROM AutomobileHelperDB.AutoElements "
                             + "WHERE ElementName = '" + partName + "';";    // single quotes in SQL!
            result = stmt.executeQuery(sqlTask);
            elementDesc = result.getString("ElementDesc");

        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elementDesc;
    }

    
    public void setNewElementDesc(String partName, String newDesc) { // was setIntervalValue
        try {
            String sqlTask = "UPDATE AutomobileHelperDB.AutoElements SET ElementDesc = '" + newDesc + "' WHERE ElementName = '" + partName + "';";
            stmt.executeUpdate(sqlTask);
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createNewElement(String partName, String elementDesc) {
        try {
            String sqlTask = "INSERT INTO AutomobileHelperDB.AutoElements (ElementName, ElementDesc) VALUES" 
                    + "'" + partName + "', '" + elementDesc + "';";
            stmt.executeUpdate(sqlTask);
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
