/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package head.broken.automobilehelper;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author evgenij
 */
public class ClickerHandler {

    private final String filterStr = "[^a-zA-Z0-9\"\']";
    private final DBMainHandler dbHandle = DBMainHandler.getInstance();

    public void refreshMileageBtn(TextArea mileageArea, Label curMileAgeValue) {
        try {
            int newMileage = Integer.parseInt(mileageArea.getText());
            System.out.println("-----> Refresh mileage clicked.");
            dbHandle.saveMileage(newMileage);
            curMileAgeValue.setText(newMileage + " units of length");
            System.out.println("-----> Refresh mileage successfully done.");
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("-----> Wrong input value, use only numbers");
            // add warning pop-up message for user here in future                                          
        }
    }

    public void makeARecordBtn(ChoiceBox elementsChoiceBox, TextArea comment, TextArea mileageArea, TextArea elementCostArea, TextArea serviceCostArea) {
        String partName = elementsChoiceBox.getValue().toString();
        String commentary = comment.getText();
        //commentary.replaceAll(filterStr, ""); // remove from text bad symbols, that makes SQL anger! (single and double quotes)
        // check correct input with try-catch
        try {
            int mileageStamp = Integer.parseInt(mileageArea.getText());
            double elemCost = Double.parseDouble(elementCostArea.getText());
            double maintanCost = Double.parseDouble(serviceCostArea.getText());
            dbHandle.createRecord(partName, mileageStamp, elemCost, maintanCost, commentary.replaceAll(filterStr, ""));
            System.out.println("-----> Make a record succeed, data passed");
        } catch (NumberFormatException | NullPointerException nfe) {    // if pasrers throws an exeption do not send anything
            System.out.println("-----> Wrong input value, use only numbers. Make a record failed.");
            // add warning pop-up message for user here in future
        }
    }

    public void saveElementBtn(ChoiceBox elementsChoiceBox, TextArea elementDiscriptionArea) {
        try {
            String partName = elementsChoiceBox.getValue().toString();
            String elementInfo = elementDiscriptionArea.getText();              // get element description
            //partName.replaceAll(filterStr, "");
            //elementInfo.replaceAll(filterStr, "");
            dbHandle.createNewElement(partName.replaceAll(filterStr, ""), elementInfo.replaceAll(filterStr, ""));
            System.out.println("--------> Save element info clicked, parameters passed");
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("-----> Wrong input value, use only numbers");
            // add warning pop-up message for user here in future  
        }
    }

    public void historyRequestBtn(ChoiceBox selectElemChoiceBox, ChoiceBox selectTypeChoiceBox, TextArea recordsArea) {
        String resultString = null;
        String partName = selectElemChoiceBox.getValue().toString();
        String selectType = selectTypeChoiceBox.getValue().toString();
        if (selectType.equalsIgnoreCase("Last")) {
            resultString = dbHandle.getLastRecordByName(partName);
        } else {
            resultString = dbHandle.getAllRecordsByName(partName);
        }
        System.out.println("--------> Go! button clicked, parameters passed");
        recordsArea.setText(resultString);
    }
}
