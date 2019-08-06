/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package head.broken.automobilehelper;

/**
 *
 * @author Broken Head
 * Not done yet! For the future
 */
public class DBRepairCheck {
    
    private DBRepairCheck() {
    }
    
    public static DBRepairCheck getInstance() {
        return DBMultiSelectHolder.INSTANCE;
    }
    
    private static class DBMultiSelectHolder {

        private static final DBRepairCheck INSTANCE = new DBRepairCheck();
    }
}
