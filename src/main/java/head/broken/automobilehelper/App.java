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
    ClickerHandler click = new ClickerHandler();
    

    @Override
    public void start(Stage stage) {
        Text curMileAgeText = new Text("Current mileage (units of length):");
        Text elementDicrText = new Text("Element description:");
        Text creatRecordWelcome = new Text("Create a new record down below:");
        Text elementsChoiceText = new Text("Choose an element:");
        Text mainChoiceText = new Text("Choose an element:");
        Text newElementDicrText = new Text("New element description:");
        
        Text selectsText1 = new Text("Select");
        Text selectsText2 = new Text("records for ");
        Text recordsAreaText = new Text("Records:");
        Text responseOnActionsMain = new Text("Tip: refresh mileage first before any other actions.");
        Text responseOnActionsElements = new Text("Tip: don't forget to fill all the fields");
        
        Label curMileAgeValue = new Label(Integer.toString(currentMileage)); // BINGO!
        Label elementCostLabel = new Label("Element cost: ");
        Label maintenanceCostLabel = new Label("Service cost: ");
        Label newElementWelcomeLabel = new Label("Create a new car's element down below: ");
        Label newElementNameLabel = new Label("New element name: ");
        
        
        TextArea mileageArea = new TextArea();
        mileageArea.setPromptText("Only numbers");
        mileageArea.setMaxSize(110, 12);
        
        TextArea elementCostArea = new TextArea();
        elementCostArea.setMaxSize(110, 12);
        elementCostArea.setPromptText("Only numbers");
        
        TextArea serviceCostArea = new TextArea();
        serviceCostArea.setMaxSize(110, 5);
        serviceCostArea.setPromptText("Only numbers");
        
        TextArea recordsArea = new TextArea();  // output text area in History tab
        recordsArea.setMaxSize(400, 600);
        
        TextArea newElementNameArea = new TextArea();
        newElementNameArea.setMaxSize(180, 12);
        newElementNameArea.setPromptText("Enter name of a part");
        
        TextArea newElementDiscrArea = new TextArea();
        newElementDiscrArea.setMaxSize(300, 100);
        newElementDiscrArea.setPromptText("Model, specific information, where you bought it, etc.");
        
        
        TextArea elementDiscriptionArea = new TextArea();
        elementDiscriptionArea.setMaxSize(300, 100);
        elementDiscriptionArea.setPromptText("Model, specific information, where you bought it, etc.");
        
        TextArea comment = new TextArea();
        comment.setPromptText("Type a commentary (max 200 symbols)");
        comment.setMaxSize(350, 100);

        ObservableList<String> elementsList = FXCollections.observableArrayList(dbHandle.getElementList());
        
        ChoiceBox mainChoiceBox = new ChoiceBox();
        mainChoiceBox.getItems().addAll(elementsList);
        ChoiceBox elementsChoiceBox = new ChoiceBox();
        elementsChoiceBox.getItems().addAll(elementsList);
        ChoiceBox selectTypeChoiceBox = new ChoiceBox();        
        selectTypeChoiceBox.getItems().addAll("All", "Last");
        ChoiceBox selectElemChoiceBox = new ChoiceBox();        // element for history selections
        selectElemChoiceBox.getItems().addAll(elementsList);

        Button refreshMileAge = new Button("Refresh\nmileage");
        Button makeARecordBtn = new Button("Make a\nrecord");
        Button saveElementInfo = new Button("Save updated info");
        Button createElementBtn = new Button("Create new element");
        Button sendHistoryRequest = new Button("Go!");
        Button getElementInfoBtn = new Button("Get info!");
        
        refreshMileAge.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.refreshMileageBtn(mileageArea, curMileAgeValue, responseOnActionsMain);
            }
        }));

        makeARecordBtn.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.makeARecordBtn(mainChoiceBox, comment, curMileAgeValue, elementCostArea, serviceCostArea, responseOnActionsMain);
            }
        }));

        saveElementInfo.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.saveElementBtn(elementsChoiceBox, elementDiscriptionArea, responseOnActionsElements);
            }
        }));
        
        createElementBtn.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.createElementBtn(newElementNameArea, newElementDiscrArea, responseOnActionsElements);
            }
        }));
        
        sendHistoryRequest.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.historyRequestBtn(selectElemChoiceBox, selectTypeChoiceBox, recordsArea);
            }
        }));
        
        getElementInfoBtn.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click.getElementDescr(elementsChoiceBox, elementDiscriptionArea);
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
        gridPaneMain.add(serviceCostArea, 1, 5);
        gridPaneMain.add(comment, 0, 6, 2, 1);
        gridPaneMain.add(makeARecordBtn, 0, 7);
        gridPaneMain.add(responseOnActionsMain, 0, 8, 2, 1);

        // Second tab "Elements info"
        GridPane gridPaneElements = new GridPane();
        gridPaneElements.setPadding(new Insets(10, 10, 10, 10));
        gridPaneElements.setHgap(8);
        gridPaneElements.setVgap(8);
        gridPaneElements.add(elementsChoiceText, 0, 0);
        gridPaneElements.add(elementsChoiceBox, 1, 0);
        gridPaneElements.add(getElementInfoBtn, 2, 0);
        gridPaneElements.add(elementDicrText, 0, 1);
        gridPaneElements.add(elementDiscriptionArea, 0, 2, 2, 1);
        gridPaneElements.add(saveElementInfo, 0, 3);
        gridPaneElements.add(newElementWelcomeLabel, 0, 4, 2, 1);
        gridPaneElements.add(newElementNameLabel, 0, 5);
        gridPaneElements.add(newElementNameArea, 1, 5, 2, 1);
        gridPaneElements.add(newElementDicrText, 0, 6);
        gridPaneElements.add(newElementDiscrArea, 0, 7, 2, 1);
        gridPaneElements.add(createElementBtn, 0, 8);
        gridPaneElements.add(responseOnActionsElements, 0, 9, 2, 1);

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

        curMileAgeValue.setText(dbHandle.getLastMilesOnStart());       //set up the last mileage value on start
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.setTitle("Automobile helper");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
