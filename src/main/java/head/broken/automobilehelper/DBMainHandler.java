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
HistoryRecords - save records, when car's element changed or repaired;
Mileage - save mileage values and dates, when this value achived;
PartsIntervals - save maintanance intervals and names for car elements;
In this app I use a plane JDBC.
SELECT partName, dateStamp, mileageStamp, commentText FROM Automobile.autoParameters 
WHERE partName = 'Bearings' AND ID = MAX(ID) ;
*/

public class DBMainHandler {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";           //fresh driver for MySQL
    static final String DB_URL = "jdbc:mysql://localhost/AutoPartsHelp";
    static final String USER = "root";                                      // name is specific for user
    static final String PASS = "3107892Mysql";                              // password is specific for user
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
    
    public String getValuesOnStart(){
    int lastMileageValue;
    String out = null;
    ResultSet result;
    System.out.println("----> Get last value of mileage on application start");
        try {
            String sqlTask = "SELECT mileageValue FROM AutoPartsHelp.Mileage WHERE ID = (SELECT MAX(ID) FROM AutoPartsHelp.Mileage);";
            result = stmt.executeQuery(sqlTask);
            lastMileageValue = result.getInt("mileageValue");
            out = Integer.toString(lastMileageValue);
           
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    return out;
    }

    public void saveMileage(int newMileage) {
        System.out.println("-----> Got new mileage " + newMileage);
        try {
            String sqlTask = "INSERT INTO AutoPartsHelp.Mileage (mileageValue, dateStamp) VALUES (" + newMileage +  ", CURDATE());";
            stmt.executeUpdate(sqlTask);
            System.out.println("-----> Mileage saved");
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createRecord(String partName, int curMileage , double elemCost, double maintanCost, String commentary) {
        try {
            // comment text without ' or "
            //System.out.println(commentary);
            String sqlTask = "INSERT INTO AutoPartsHelp.HistoryRecords (partName, dateStamp, mileageStamp, elementCost, maintenanceCost, commentText)"  
                            + " VALUES ( '" + partName + "', CURDATE()," + curMileage + ", "+ elemCost +", "+ maintanCost + ", '" + commentary + "'); " ;
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
            String sqlTask = "SELECT partName, dateStamp, mileageStamp, elementCost, maintenanceCost, commentText FROM AutoPartsHelp.HistoryRecords"
                    + " WHERE partName = '" + partName + "' AND ID = (SELECT MAX(ID) FROM AutoPartsHelp.HistoryRecords) ;";
            result = stmt.executeQuery(sqlTask);
            resultName = result.getString("partName");
            resultDate = result.getString("dateStamp");
            resultMileage = result.getInt("mileageStamp");
            resultElCost = result.getInt("elementCost");
            resultMaintCost = result.getInt("maintenanceCost");
            resultComment = result.getString("comment");
            // make a super string with whole result, maybe use a StringBuilder?
            lastRecord = "Name: " + resultName + "; Date:" + resultDate + ", Mileage: " + resultMileage + " km; Element cost: " + resultElCost +   
                 "; Maintenance cost:" + resultMaintCost + " \nCommentary: " + resultComment;   
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastRecord;
    }
    
    /** 
     * For the future. I need to learn more about SQL quarries.
     * @param partName mean for which part I need to return all records
     * @return super-huge string with all results (maybe i need to return some kind of collection)  */
    public String getAllRecordsByName(String partName) {
        ResultSet result;
        String oneRecord = null;

        return oneRecord;
    }
    
    public int getIntervalValue(String partName) {      // I dont know where i can use it
        ResultSet result;
        int intervalValue = 0;
        try {
            String sqlTask = "SELECT intervalValue  FROM PartsIntervals " 
                            + "WHERE elementName = '" + partName + "';";
            result = stmt.executeQuery(sqlTask);
            intervalValue = result.getInt("intervalValue");     //get value from sql result set
            
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return intervalValue;   
    }
    
    public String getIntervalAddInfo(String partName) {
        ResultSet result;
        String intervaInfo = new String();
        try {
            String sqlTask = "SELECT elementInfo FROM PartsIntervals " 
                            + "WHERE elementName = '" + partName + "';";    // quotes in SQL!
            result = stmt.executeQuery(sqlTask);
            intervaInfo = result.getString("elementInfo");     
            
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return intervaInfo;   
    }
    
    public void setIntervalValue(String partName, int intVal) {
        try {
            String sqlTask = "UPDATE AutoPartsHelp.PartsIntervals SET intervalValue = "+ intVal +" WHERE elementName = '" + partName + "';";
            stmt.executeUpdate(sqlTask);
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setIntervalAddInfo(String partName, String elementInfo) {
        try {
            String sqlTask = "UPDATE AutoPartsHelp.PartsIntervals SET elementInfo = '"+ elementInfo +"' WHERE elementName = '" + partName + "';";
            stmt.executeUpdate(sqlTask);
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void createNewAutoPart(String partName, long intervalValue) {
        
        try {
            String sqlTask = "INSERT partName, dateStamp, mileageStamp, commentText FROM autoIntervals";
            stmt.executeUpdate(sqlTask);
        } catch (SQLException ex) {
            Logger.getLogger(DBMainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
