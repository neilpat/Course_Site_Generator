package csg.workspace;

import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import djf.controller.AppFileController;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import csg.csgApp;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JComboBox;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import csg.csgProp;
import csg.style.CourseSiteStyle;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.file.csgFiles;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * @co-author Niral Patel (1110626877)
 */
public class CourseSiteWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    csgApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    CourseSiteController controller;
    
    jTPS jtps;
    
    
    

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;

    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    TextField emailTextField;
    Button addButton;
    Button updateButton;
    Button clearButton;

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    
    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;

    /**
     * The contstructor initializes the user interface, except for
     * the full office hours grid, since it doesn't yet know what
     * the hours will be until a file is loaded or a new one is created.
     */
    public CourseSiteWorkspace(csgApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
           
        jtps = new jTPS();
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(csgProp.TAS_HEADER_TEXT.toString());
        tasHeaderLabel = new Label(tasHeaderText);
        tasHeaderBox.getChildren().add(tasHeaderLabel);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TAData data = (TAData) app.getDataComponent();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        String nameColumnText = props.getProperty(csgProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(csgProp.EMAIL_COLUMN_TEXT.toString());
        nameColumn = new TableColumn(nameColumnText);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(csgProp.NAME_PROMPT_TEXT.toString());
        String emailPromptText = props.getProperty(csgProp.EMAIL_PROMPT_TEXT.toString());
        String addButtonText = props.getProperty(csgProp.ADD_BUTTON_TEXT.toString());
        String updateButtonText = props.getProperty(csgProp.UPDATE_BUTTON_TEXT.toString());
        String clearButtonText = props.getProperty(csgProp.CLEAR_BUTTON_TEXT.toString());
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        updateButton = new Button(updateButtonText);
        clearButton = new Button(clearButtonText);
        clearButton.setDisable(true);
        addBox = new HBox();
        //updateBox = new HBox();
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        updateButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);
        //updateBox.getChildren().add(nameTextField);
        //updateBox.getChildren().add(emailTextField);
        //updateBox.getChildren().add(updateButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        String officeHoursGridText = props.getProperty(csgProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);        
        leftPane.getChildren().add(taTable);        
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);
        
        //make a combo box
        ObservableList<String> beg_times = FXCollections.observableArrayList();
        ObservableList<String> end_times = FXCollections.observableArrayList();
        
        for(int i = 0; i<24 ;i ++){
            beg_times.add(buildCellText(i, "00"));
            //beg_times.add(buildCellText(i, "30"));
            end_times.add(buildCellText(i, "00"));
            //end_times.add(buildCellText(i, "30"));
        }
        ComboBox beg_hours = new ComboBox();
        beg_hours.setPromptText("Start Time");
        beg_hours.getItems().addAll(beg_times);
        
        ComboBox end_hours = new ComboBox();
        end_hours.setPromptText("End Time");
        end_hours.getItems().addAll(end_times);
        
        HBox final_right = new HBox();
        VBox box2 = new VBox();
        Button submit = new Button("Submit");
        //box2.getChildren().add(rightPane);
        box2.getChildren().add(beg_hours);
        box2.getChildren().add(end_hours);
        box2.getChildren().add(submit);
        rightPane.getChildren().add(box2);
        final_right.getChildren().add(rightPane);
        final_right.getChildren().add(box2);
        //Controls for beg_hours and end_hours, changes end hours to relate with beg hours
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(final_right));
        workspace = new BorderPane();
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(sPane);

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));

        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new CourseSiteController(app);
        
        beg_hours.setOnAction(e ->{
            String start_time = (String)beg_hours.getValue();
            String am_pm = start_time.substring(5,7);
            int temp;
            int beg_time=-1;
            if(am_pm.equals("am")){
                beg_time = Integer.parseInt(start_time.substring(0, 2));
                temp = beg_time;
                start_time = beg_time +"";
            }
            else{
                beg_time = 12 + Integer.parseInt(start_time.substring(0,2));
                temp = beg_time - 12;
                start_time = beg_time +"";
            }
            if(beg_time >=0){
                end_hours.getItems().clear();
                end_times.clear();
                for(int i = beg_time+1; i<24 ;i ++){
                    end_times.add(buildCellText(i, "00"));
                }
                end_hours.getItems().addAll(end_times);
            }
        });
        //adjust grid after end time is selected
        submit.setOnMouseClicked(e ->{
            String start_time = (String)beg_hours.getValue();
            String am_pm = start_time.substring(5,7);
            int temp;
            int beg_time=-1;
            if(am_pm.equals("am")){
                beg_time = Integer.parseInt(start_time.substring(0, 2));
                temp = beg_time;
                start_time = beg_time +"";
            }
            else{
                beg_time = 12 + Integer.parseInt(start_time.substring(0,2));
                temp = beg_time - 12;
                start_time = beg_time +"";
            }
            String end_time = (String)end_hours.getValue();
            am_pm = end_time.substring(5,7);
            int last_time =-1;
            if(am_pm.equals("am")){
                last_time = Integer.parseInt(end_time.substring(0, 2));
                if(Integer.parseInt(end_time.substring(3,4)) == 3){
                    last_time++;
                    
                }
                end_time = last_time +"";
            }
            else{
                last_time = 12 + Integer.parseInt(end_time.substring(0,2));
                end_time = last_time + "";
            }
            if(start_time.equals(null)){
                start_time = data.getStartHour()+"";
                beg_time = Integer.parseInt(start_time);
            }
            if(end_time.equals(null)){
                end_time = data.getEndHour()+"";
                last_time = Integer.parseInt(end_time);
            }
            if(beg_time >= 0 && last_time >= 0){
                //dialog box appears to ask for action
                    
                    HashMap<String, StringProperty> orgMap = data.getOfficeHours();
                    HashMap<String, StringProperty> newMap = new HashMap<String,StringProperty>(orgMap);
                    Set<String> keySet = orgMap.keySet();
                    Iterator<String> keySetIterator = keySet.iterator();
                    //deep copy of the original map
                    while(keySetIterator.hasNext()){
                        String key = keySetIterator.next();
                        StringProperty prop = orgMap.get(key);
                        newMap.put(key, prop);
                    }
                    
                    int org_start_time = data.getStartHour();
                    int org_end_time = data.getEndHour();
                    resetWorkspace();
                    ((TAData)(app.getDataComponent())).initHours(start_time, end_time);
                    reloadOfficeHoursGrid(data);
                    if(Integer.parseInt(start_time)> org_start_time || Integer.parseInt(end_time)< org_end_time){
                        AppYesNoCancelDialogSingleton dialogSingleton = AppYesNoCancelDialogSingleton.getSingleton();
                        dialogSingleton.show("WARNING", "You are deleting some rows in the Grid!");
                        if(dialogSingleton.getSelection().equals(AppYesNoCancelDialogSingleton.YES)){
                            keySet = newMap.keySet();
                            keySetIterator = keySet.iterator();
                            //to find the biggest row in the orgMap
                            int big_row = (Integer.parseInt(end_time) - Integer.parseInt(start_time))*2;
                            while(keySetIterator.hasNext()){
                                String key = keySetIterator.next();
                                StringProperty prop = newMap.get(key);
                                int col = Integer.parseInt(key.substring(0, key.indexOf('_')));
                                int row = Integer.parseInt(key.substring(key.indexOf('_')+1,key.length()));
                                if(Integer.parseInt(start_time)<org_start_time && row>0){
                                    row = (org_start_time - Integer.parseInt(start_time))*2 + row;
                                    key = data.getCellKey(col, row);
                                    if(col>1 && row>0 && row<=big_row){
                                        if(!prop.getValue().equals("")){
                                            String names = prop.getValue();
                                            data.toggleTAOfficeHours_2(key, names);
                                        }
                                        else{
                                            data.toggleTAOfficeHours_2(key, "");
                                        }  
                                    }
                                }   
                                else if(Integer.parseInt(start_time)>org_start_time && row>0){
                                    row = row - (Integer.parseInt(start_time) - org_start_time)*2;
                                    key = data.getCellKey(col, row);
                                    if(col>1 && row>0 && row<=big_row){
                                        if(!prop.getValue().equals("")){
                                            String names = prop.getValue();
                                            data.toggleTAOfficeHours_2(key, names);
                                        }
                                        else{
                                            data.toggleTAOfficeHours_2(key, "");
                                        }  
                                    }
                                }
                            }
                        }
                    }
                    else{
                        keySet = newMap.keySet();
                            keySetIterator = keySet.iterator();
                            while(keySetIterator.hasNext()){
                                String key = keySetIterator.next();
                                StringProperty prop = newMap.get(key);
                                int col = Integer.parseInt(key.substring(0, key.indexOf('_')));
                                int row = Integer.parseInt(key.substring(key.indexOf('_')+1,key.length()));
                                if(Integer.parseInt(start_time)<org_start_time && row>0){
                                    row = (org_start_time - Integer.parseInt(start_time))*2 + row;
                                    key = data.getCellKey(col, row);
                                    if(col>1 && row>0){
                                        if(!prop.getValue().equals("")){
                                            String names = prop.getValue();
                                            data.toggleTAOfficeHours_2(key, names);
                                        }
                                        else{
                                            data.toggleTAOfficeHours_2(key, "");
                                        }  
                                    }
                                }   
                                else if(Integer.parseInt(start_time)>org_start_time && row>0){
                                    row = row - (Integer.parseInt(start_time) - org_start_time)*2;
                                    key = data.getCellKey(col, row);
                                    if(col>1 && row>0){
                                        if(!prop.getValue().equals("")){
                                            String names = prop.getValue();
                                            data.toggleTAOfficeHours_2(key, names);
                                        }
                                        else{
                                            data.toggleTAOfficeHours_2(key, "");
                                        }  
                                    }
                                }
                            }
                        
                    }
            }
        });

        taTable.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.DELETE){
                controller.handleDelteKey();
            }
        });
        // Controls for Update TA
        taTable.setOnMouseClicked(e ->{
            clearButton.setDisable(false);
            controller.handleSelctedTA();
            addBox.getChildren().remove(addButton);
            addBox.getChildren().remove(updateButton);
            addBox.getChildren().remove(clearButton);
            addBox.getChildren().add(updateButton);
            addBox.getChildren().add(clearButton);
        });
        updateButton.setOnAction(e -> {
                controller.handleUpdateTA();
                taTable.refresh(); 
        });
        clearButton.setOnAction(e ->{
            controller.handleClear();
            addBox.getChildren().remove(addButton);
            addBox.getChildren().remove(updateButton);
            addBox.getChildren().remove(clearButton);
            addBox.getChildren().add(addButton);
            addBox.getChildren().add(clearButton);
            taTable.getSelectionModel().clearSelection();
        });
        
        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        addButton.setOnAction(e -> {
            controller.handleAddTA();
        });
        app.getGUI().getPrimaryScene().setOnKeyPressed(e ->{
            if(e.isControlDown()){
                if(e.getCode() == KeyCode.Z){
                    jtps.undoTransaction();
                }
                if(e.getCode() == KeyCode.Y){
                    jtps.doTransaction();
                }
            }
        });
    }
    
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    
    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }
    
    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getAddButton() {
        return addButton;
    }
    public Button getUpdateButton() {
        return updateButton;
    }
    public Button getClearButton() {
        return clearButton;
    }

    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }
    
    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String extra_zero;
        if(hour == 0){
            hour = 12;
        }
        if(hour<10){
           extra_zero = "0"+hour+"";
        }
        else{
            extra_zero = hour+"";
        }
        String cellText = "" + extra_zero + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        TAData taData = (TAData)dataComponent;
        reloadOfficeHoursGrid(taData);
        
    }

    public void reloadOfficeHoursGrid(TAData dataComponent) {        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }
        
        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
            col++;

            
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }
        
        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
                
            });
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        CourseSiteStyle taStyle = (CourseSiteStyle)app.getStyleComponent();
        taStyle.initOfficeHoursGridStyle();
    }
    
    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty()); 
    }
    public jTPS getJTPS(){
        return jtps;
    }
}
