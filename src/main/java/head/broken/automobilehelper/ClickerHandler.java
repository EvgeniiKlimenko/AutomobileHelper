/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package head.broken.automobilehelper;


import java.util.LinkedList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author evgenij
 */
public class ClickerHandler {

    private final String refreshMileageOK = "OK! Mileage has refreshed!";
    private final String makeRecordOK = "OK! Record created successfully!";
    private final String newElementCreateOK = "OK! New car's element created successfully!";
    private final String elementDescrChangeOK = "OK! Element's description changed successfully!";
    private final String refreshMileageFail = "FAILED! Mileage save failed! Check all fields and use only numbers\n for numeric parameters.";
    private final String makeRecordFail = "FAILED! Can't make a record! Check all fields and use only numbers\n for numeric parameters and fill all the fields correctly.";
    private final String newElementCreateFail = "FAILED! New car's element can't be created. Fill all the fields correctly!";
    private final String elementDescrChangeFail = "FAILED! Can't save new element's description. Text must be less than 200 symbols.";
    private final String filterStr = "[\"\']"; // no braces pass to SQL!
    private final DBMainHandler dbHandle = DBMainHandler.getInstance();

    public void refreshMileageBtn(TextArea mileageArea, Label curMileAgeValue, Text responseOnActionsMain) {
        try {
            int newMileage = Integer.parseInt(mileageArea.getText());
            if (newMileage == 0) {
                responseOnActionsMain.setText(refreshMileageFail);
                return;
            }
            if (dbHandle.saveMileage(newMileage)) {
                curMileAgeValue.setText(Integer.toString(newMileage));
                responseOnActionsMain.setText(refreshMileageOK);
                responseOnActionsMain.setFill(Color.GREEN);
            } else {
                responseOnActionsMain.setText(refreshMileageFail);
                responseOnActionsMain.setFill(Color.RED);
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            responseOnActionsMain.setText(refreshMileageFail);
            responseOnActionsMain.setFill(Color.RED);
        }
    }

    public void makeARecordBtn(ChoiceBox elementsChoiceBox, TextArea comment, Label curMileAgeValue, TextArea elementCostArea, TextArea serviceCostArea, Text responseOnActionsMain) {
        String partName = elementsChoiceBox.getValue().toString();
        String commentary = comment.getText();
        try {
            int mileageStamp = Integer.parseInt(curMileAgeValue.getText());
            double elemCost = Double.parseDouble(elementCostArea.getText());
            double maintanCost = Double.parseDouble(serviceCostArea.getText());
            if (mileageStamp == 0 || elemCost == 0.0 || maintanCost == 0.0) {
                responseOnActionsMain.setText(makeRecordFail);
                responseOnActionsMain.setFill(Color.RED);
                return;
            }
            if (dbHandle.createRecord(partName, mileageStamp, elemCost, maintanCost, commentary.replaceAll(filterStr, ""))) {
                responseOnActionsMain.setText(makeRecordOK);
                responseOnActionsMain.setFill(Color.GREEN);
            } else {
                responseOnActionsMain.setText(makeRecordFail);
                responseOnActionsMain.setFill(Color.RED);
            }
        } catch (NumberFormatException | NullPointerException nfe) {    // if pasrers throws an exeption...
            responseOnActionsMain.setText(makeRecordFail);
            responseOnActionsMain.setFill(Color.RED);
        }
    }

    public void saveElementBtn(ChoiceBox elementsChoiceBox, TextArea elementDiscriptionArea, Text responseOnActionsElements) {
        try {
            String partName = elementsChoiceBox.getValue().toString();
            String elementInfo = elementDiscriptionArea.getText();              // get element description
            if (dbHandle.setNewElementDesc(partName, elementInfo.replaceAll(filterStr, ""))) {
                responseOnActionsElements.setText(elementDescrChangeOK);
                responseOnActionsElements.setFill(Color.GREEN);
            } else {
                responseOnActionsElements.setText(elementDescrChangeFail);
                responseOnActionsElements.setFill(Color.RED);
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            responseOnActionsElements.setText(elementDescrChangeFail);
            responseOnActionsElements.setFill(Color.RED);
        }
    }

    public void createElementBtn(TextArea newElementNameArea, TextArea elementDiscriptionArea, Text responseOnActionsElements) {
        try {
            String partName = newElementNameArea.getText();
            String elementInfo = elementDiscriptionArea.getText();
            if (dbHandle.createNewElement(partName.replaceAll(filterStr, ""), elementInfo.replaceAll(filterStr, ""))) {
                responseOnActionsElements.setText(newElementCreateOK);
                responseOnActionsElements.setFill(Color.GREEN);
            } else{
                responseOnActionsElements.setText(newElementCreateFail);
                responseOnActionsElements.setFill(Color.RED);
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            responseOnActionsElements.setText(newElementCreateFail);
            responseOnActionsElements.setFill(Color.RED);
        }
    }

    public void historyRequestBtn(ChoiceBox selectElemChoiceBox, ChoiceBox selectTypeChoiceBox, TextArea recordsArea) {
        String partName = selectElemChoiceBox.getValue().toString();
        String selectType = selectTypeChoiceBox.getValue().toString();
        if (selectType.equalsIgnoreCase("Last")) {
            String resultString = dbHandle.getLastRecordByName(partName);
            recordsArea.setText(resultString);
        } else {
            LinkedList<String> resList = dbHandle.getAllRecordsByName(partName);
            while(!resList.isEmpty()){
                recordsArea.appendText(resList.pop());
            }
        }
        
    }

    public void getElementDescr(ChoiceBox name, TextArea descr) {
        descr.setText(dbHandle.getElementDesc(name.getValue().toString()));
    }
}
