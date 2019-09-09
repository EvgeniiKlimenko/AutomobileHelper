package head.broken.automobilehelper;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX App (actually it is an OpenFX)
 */
public class App extends Application {

    private final DBMainHandler dbHandle = DBMainHandler.getInstance();
    public int currentMileage = 0;
    public int currentInterval = 0;

    @Override
    public void start(Stage stage) {
        Text curMileAgeText = new Text("Current mileage:");
        Text curIntervalText = new Text("Maintenance interval:");
        Text elementDicrText = new Text("Element additional info:");
        Text creatRecordWelcome = new Text("Create a new record down below");
        Text elementsChoiceText = new Text("Choose an element:");
        Text mainChoiceText = new Text("Choose an element:");
        Text selectsText1 = new Text("Select");
        Text selectsText2 = new Text("records for ");
        Text recordsAreaText = new Text("Records:");
        Label curMileAgeValue = new Label(currentMileage + " units of length");
        Label elementCostLabel = new Label("Element cost: ");
        Label maintenanceCostLabel = new Label("Maintenance cost: ");

        TextArea curIntervalValueArea = new TextArea(currentInterval + " units of length");
        curIntervalValueArea.setMaxSize(100, 12);
        TextArea mileageArea = new TextArea();
        mileageArea.setMaxSize(100, 12);
        TextArea elementCostArea = new TextArea();
        elementCostArea.setMaxSize(100, 12);
        TextArea maintenanceCostArea = new TextArea();
        maintenanceCostArea.setMaxSize(100, 12);
        TextArea recordsArea = new TextArea();           // output text area in History tab
        recordsArea.setMaxSize(400, 600);
        TextArea elementDiscriptionArea = new TextArea();
        elementDiscriptionArea.setMaxSize(300, 100);
        TextArea comment = new TextArea();
        comment.setPromptText("Type a commentary (max 100 symbols)");
        comment.setMaxSize(350, 50);

        // list of car elements
        ObservableList<String> elementsList = FXCollections.observableArrayList("Engine Oil", "Oil filter", "Spark plug", "Air filter", "Coolant",
                "Transmission Oil", "Fuses", "Wheels beerings", "Brake pads");

        ChoiceBox mainChoiceBox = new ChoiceBox();
        mainChoiceBox.getItems().addAll(elementsList);
        ChoiceBox elementsChoiceBox = new ChoiceBox();
        elementsChoiceBox.getItems().addAll(elementsList);
        ChoiceBox selectTypeChoiceBox = new ChoiceBox();        //type of history selection
        selectTypeChoiceBox.getItems().addAll("All", "Last");
        ChoiceBox selectElemChoiceBox = new ChoiceBox();        // element for history selections
        selectElemChoiceBox.getItems().addAll(elementsList);

        Button refreshMileAge = new Button("Refresh\nmileage");
        Button makeARecordBtn = new Button("Make a\nrecord");
        Button saveElementInfo = new Button("Save changes");
        Button sendHistoryRequest = new Button("Go!");
        //Button saveNewPart = new Button("Save part");  // for the future!

        
        // Try to move logic to another method or class (For the future)
        refreshMileAge.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    int newMileage = Integer.parseInt(mileageArea.getText());
                    dbHandle.saveMileage(newMileage);
                    curMileAgeValue.setText(newMileage + " units of length");
                    System.out.println("-----> Refresh mileage clicked, parameters passed");
                } catch (NumberFormatException | NullPointerException nfe) {
                    System.out.println("-----> Wrong input value");
                    // add warning pop-up message for user here in future
                    return;                                         
                }
                System.out.println("-----> Save the mileage value failed, wrong parameters parsing");
            }
        }));

        // Try to move logic to another method or class (For the future)
        makeARecordBtn.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String partName = elementsChoiceBox.getValue().toString();
                String commentary = comment.getText();
                if(commentary.contains("\"") ||commentary.contains("'")){
                    System.out.println("-----> Make a record failed! Don't use single or double quotes in text, please!");
                    // add warning pop-up message for user here in future
                    return;
                }
                // check correct input with try-catch
                try {       
                    int mileageStamp = Integer.parseInt(mileageArea.getText());
                    double elemCost = Double.parseDouble(elementCostArea.getText());
                    double maintanCost = Double.parseDouble(maintenanceCostArea.getText());
                    dbHandle.createRecord(partName, mileageStamp, elemCost, maintanCost, commentary);
                    System.out.println("-----> Make a record succeed, parameters passed");
                } catch (NumberFormatException | NullPointerException nfe) {    // if pasrers throws an exeption do not send anything
                    System.out.println("-----> Wrong input value");
                    // add warning pop-up message for user here in future
                    return;                                         
                }
                System.out.println("-----> Make a record failed, wrong parameters parsing");

            }
        }));

        // Try to move logic to another method or class (For the future)
        saveElementInfo.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String partName = elementsChoiceBox.getValue().toString();
                    String elementInfo = elementDiscriptionArea.getText();              // get element description
                    if(elementInfo.contains("\"") || elementInfo.contains("'")){
                        System.out.println("-----> Make a record failed! Don't use single or double quotes in text, please!");
                        // add warning pop-up message for user here in future
                    return;
                }
                    //int intVal = Integer.parseInt(curIntervalValueArea.getText());      
                    dbHandle.createNewElement(partName, elementInfo);
                    System.out.println("--------> Save element info clicked, parameters passed");
                } catch (NumberFormatException | NullPointerException nfe) {
                    System.out.println("----> Wrong input value");
                    // add warning pop-up message for user here in future
                    return;      
                }
                System.out.println("--------> Save part info failed, wrong parameters parsing");
            }
        }));

        // Try to move logic to another method or class (For the future)
        sendHistoryRequest.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String resultString = null;
                String partName = selectElemChoiceBox.getValue().toString();
                String selectType = selectTypeChoiceBox.getValue().toString();
                if (selectType.equalsIgnoreCase("Last")) {
                    resultString = dbHandle.getLastRecordByName(partName);
                } else {
                    resultString = dbHandle.getAllRecordsByName(partName);  // get all records does not work yet!
                }
                System.out.println("--------> Go! button clicked, parameters passed");
                recordsArea.setText(resultString);               // put result into output text area
            }
        }));

        // First tab "Main"
        GridPane gridPaneMain = new GridPane();
        gridPaneMain.setPadding(new Insets(10, 10, 10, 10));
        gridPaneMain.setHgap(8);
        gridPaneMain.setVgap(8);
        gridPaneMain.add(curMileAgeText, 0, 0);
        gridPaneMain.add(curMileAgeValue, 1, 0);
        gridPaneMain.add(refreshMileAge, 0, 1);
        gridPaneMain.add(mileageArea, 1, 1);
        gridPaneMain.add(creatRecordWelcome, 0, 2, 2, 1);
        gridPaneMain.add(mainChoiceText, 0, 3);
        gridPaneMain.add(mainChoiceBox, 1, 3);
        gridPaneMain.add(elementCostLabel, 0, 4);
        gridPaneMain.add(elementCostArea, 1, 4);
        gridPaneMain.add(maintenanceCostLabel, 0, 5);
        gridPaneMain.add(maintenanceCostArea, 1, 5);
        gridPaneMain.add(comment, 0, 6, 2, 1);
        gridPaneMain.add(makeARecordBtn, 0, 7);

        // Second tab "Elements info"
        GridPane gridPaneElements = new GridPane();
        gridPaneElements.setPadding(new Insets(10, 10, 10, 10));
        gridPaneElements.setHgap(8);
        gridPaneElements.setVgap(8);
        gridPaneElements.add(elementsChoiceText, 0, 0);
        gridPaneElements.add(elementsChoiceBox, 1, 0);
        gridPaneElements.add(curIntervalText, 0, 1);
        gridPaneElements.add(curIntervalValueArea, 1, 1);
        gridPaneElements.add(elementDicrText, 0, 2);
        gridPaneElements.add(elementDiscriptionArea, 0, 3, 2, 1);
        gridPaneElements.add(saveElementInfo, 0, 4);

        // Third tab "History"
        GridPane gridPaneHistory = new GridPane();
        gridPaneHistory.setPadding(new Insets(10, 10, 10, 10));
        gridPaneHistory.setHgap(8);
        gridPaneHistory.setVgap(8);
        gridPaneHistory.add(selectsText1, 0, 0);
        gridPaneHistory.add(selectsText2, 2, 0);
        gridPaneHistory.add(selectTypeChoiceBox, 1, 0);
        gridPaneHistory.add(selectElemChoiceBox, 3, 0);
        gridPaneHistory.add(sendHistoryRequest, 0, 1);
        gridPaneHistory.add(recordsAreaText, 0, 3);
        gridPaneHistory.add(recordsArea, 0, 4, 4, 1);

        TabPane tabPane = new TabPane();
        Tab tabMain = new Tab();
        tabMain.setText("Main");
        tabMain.setGraphic(new Circle(0, 0, 5));
        tabMain.setContent(gridPaneMain);
        tabMain.setClosable(false);

        Tab tabAutoParts = new Tab();
        tabAutoParts.setGraphic(new Circle(0, 0, 5));
        tabAutoParts.setClosable(false);
        tabAutoParts.setText("Elements info");
        tabAutoParts.setContent(gridPaneElements);

        Tab tabSelections = new Tab();
        tabSelections.setGraphic(new Circle(0, 0, 5));
        tabSelections.setClosable(false);
        tabSelections.setText("History");
        tabSelections.setContent(gridPaneHistory);

        tabPane.getTabs().add(tabMain);
        tabPane.getTabs().add(tabAutoParts);
        tabPane.getTabs().add(tabSelections);

        mileageArea.setText(dbHandle.getLastMilesOnStart());       //set up the last mileage value on start
        //add getter for auto elements list from DB
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
