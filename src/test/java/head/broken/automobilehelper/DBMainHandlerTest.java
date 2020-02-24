/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package head.broken.automobilehelper;

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author brokenhead
 */
public class DBMainHandlerTest {
    private DBMainHandler instance;
    
    public DBMainHandlerTest() {
        System.out.println("---Running tests!---");
        instance = DBMainHandler.getInstance();
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetLastMilesOnStart() {
        System.out.println("-----> getLastMilesOnStart() test");
        String result = instance.getLastMilesOnStart();
        System.out.println("Result: "+result);
        assertNotNull(result);
    }

    @Test
    public void testGetElementList() {
        System.out.println("-----> getElementList() test");
        System.out.println("    Element list:");
        ArrayList resultArray = instance.getElementList();
        int size = resultArray.size();
           for(int i=0; i<size; i++){
               System.out.println(resultArray.get(i));
           }
        System.out.println("Just pass into sout: " + resultArray);
    }
    
    @Test()
    public void testSaveMileage() {
        System.out.println("-----> saveMileage() test");
        int newMileage = 50555;
        boolean result = instance.saveMileage(newMileage);
        System.out.println("Try assertTrue()...");
        assertTrue(result);
        System.out.println("Try assertDoesNotThrow()...");
        assertDoesNotThrow(() -> {instance.saveMileage(newMileage);});
    }

    @Test
    public void testCreateRecord() {
        System.out.println("-----> createRecord()");
        String partName = "TestPart";
        int curMileage = 50555;
        double elemCost = 1000.0;
        double serviceCost = 1000.0;
        String commentary = "Testing tests";
        boolean result = instance.createRecord(partName, curMileage, elemCost, serviceCost, commentary);
        String testStr = instance.getLastRecordByName(partName);
        System.out.println("Test record: " + testStr);
        assertTrue(result);
    }
    
    @Test
    public void testCreateNewElement() {
        System.out.println("----> createNewElement()");
        String partName = "TestPart";
        String elementDesc = "Test part description.";
        boolean result = instance.createNewElement(partName, elementDesc);
        assertTrue(result);
    }

    @Test
    public void testGetLastRecordByName() {
        System.out.println("-----> getLastRecordByName()");
        String partName = "TestPart";
        String result = instance.getLastRecordByName(partName);
        assertNotNull(result);
    }

    @Test
    public void testGetAllRecordsByName() {
        System.out.println("-----> getAllRecordsByName()");
        String partName = "Tyres";
        LinkedList<String> result = instance.getAllRecordsByName(partName);
        boolean check = result.isEmpty();
        System.out.println("Size of records list: " + result.size());
        assertFalse(check); //check if result is not empty
    }

    @Test
    public void testGetElementDesc() {
        System.out.println("-----> getElementDesc()");
        String partName = "TestPart";
        String result = instance.getElementDesc(partName);
        assertNotNull(result);
        
    }

    @Test
    public void testSetNewElementDesc() {
        System.out.println("-----> setNewElementDesc()");
        String partName = "TestPart";
        String newDesc = "New test description.";
        boolean result = instance.setNewElementDesc(partName, newDesc);
        if(result){
            String desc = instance.getElementDesc(partName);
            assertEquals(desc, newDesc);
        } else {
            fail("The test failed."); // use fail() if test fails with some results
        }
    }

    
  
}
