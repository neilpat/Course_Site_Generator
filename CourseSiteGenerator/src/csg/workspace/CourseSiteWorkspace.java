package csg.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import csg.csgApp;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jtps.jTPS;
import properties_manager.PropertiesManager;
import csg.csgProp;
import csg.style.CourseSiteStyle;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.data.recitation;
import csg.data.sitePage;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
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
    
    Label course_info_label;
    Label site_template_label;
    Label page_Style_label;
    Label subject_label;
    Label number_label;
    Label semester_label;
    Label year_label;
    Label title_label;
    Label instructor_name_label;
    Label instructor_home_label;
    Label export_directory_label;
    Label exportDirectory;// for the selected directory
    Label templateDirectory;// for the selected directory
    Label bannerImage;
    Label leftFooterImage;
    Label rightFooterImage;
    Label styleSheetLabel;
    Label disclaimer;
    
    ImageView bannerImageView;
    ImageView leftFooterImageView;
    ImageView rightFooterImageView;
          
    HBox siteTemplate;
    VBox course_details_box;
    VBox pageStyle;
    VBox courseInfoBox;
    
    Button changeBannerButton;
    Button changeLeftFooterButton;
    Button changeRightFooterButton;
    
    ComboBox styleSheetComboBox;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    
    TableView<sitePage> siteTable;
    TableColumn<sitePage, Boolean> use;
    TableColumn<sitePage, String> Navbar_title;
    TableColumn<sitePage, String> File_name;
    TableColumn<sitePage, String> Script;
    
    TableView<recitation> recitationTable;
    TableColumn<recitation, String> section;
    TableColumn<recitation, String> instructor;
    TableColumn<recitation, String> day_time;
    TableColumn<recitation, String> location;
    TableColumn<recitation, String> Table_TA1;
    TableColumn<recitation, String> Table_TA2;
    
    
    //FOR THE SITE TABLE
    ObservableList<sitePage> sitePages;
    //FOR THE RECITATION TABLE
    ObservableList<recitation> recitations;
    
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

    TabPane tabPane;
    Tab courseDetailsTab;
    Tab TADetailsTab;
    Tab recitationTab;
    Tab scheduleTab;
    Tab projectTab;
   /**
    * This will be the main workspace. It will contain calls to methods to 
    * generate each pane in the final application.
    * @param initApp 
    */
    public CourseSiteWorkspace(csgApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
           
        jtps = new jTPS();
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        //Make a new tabPane and set it to the frameworktab pane
        //to make is easy to add and remove stuff
        tabPane = new TabPane();
        courseDetailsTab = new Tab();
        TADetailsTab = new Tab();
        recitationTab = new Tab();
        scheduleTab = new Tab();
        projectTab = new Tab();
        
        //set all the tabs title to correct name
        courseDetailsTab.setText(props.getProperty(csgProp.COURSE_DETAILS_TAB.toString()));
        TADetailsTab.setText(props.getProperty(csgProp.TA_DETAILS_TAB.toString()));
        recitationTab.setText(props.getProperty(csgProp.RECITATION_DETAILS_TAB.toString()));
        scheduleTab.setText(props.getProperty(csgProp.SCHEDULE_DETAILS_TAB.toString()));
        projectTab.setText(props.getProperty(csgProp.PROJECT_DETAILS_TAB.toString()));
        
        //set each tab properties
        courseDetailsTab.setClosable(false);
        TADetailsTab.setClosable(false);
        recitationTab.setClosable(false);
        scheduleTab.setClosable(false);
        projectTab.setClosable(false);
        
        
       
        //add all the tabs to the tabPane
        tabPane.getTabs().add(TADetailsTab);
        tabPane.getTabs().add(courseDetailsTab);
        tabPane.getTabs().add(recitationTab);
        tabPane.getTabs().add(scheduleTab);
        tabPane.getTabs().add(projectTab);
        
        workspace = new BorderPane();
        TADetailsTab.setContent(TADetailsPane(app, jtps, props));
        courseDetailsTab.setContent(CourseDetailsPane(app, jtps, props));
        scheduleTab.setContent(ScheduleDetailsPane(app, jtps, props));
        recitationTab.setContent(RecitationDetailsPane(app, jtps, props));

        ((BorderPane)workspace).setCenter(tabPane);
    }
    public SplitPane TADetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
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
        return sPane;
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
    
    public VBox CourseDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        course_details_box = new VBox();
        
        course_info_label = new Label(props.getProperty(csgProp.COURSE_INFO_LABEL.toString()));
        site_template_label = new Label(props.getProperty(csgProp.SITE_TEMPLATE_LABEL.toString()));
        page_Style_label = new Label(props.getProperty(csgProp.PAGE_STYLE_LABEL.toString()));
        subject_label = new Label(props.getProperty(csgProp.SUBJECT_LABEL.toString()));
        number_label = new Label(props.getProperty(csgProp.NUMBER_LABEL.toString()));
        semester_label = new Label(props.getProperty(csgProp.SEMESTER_LABEL.toString()));
        year_label = new Label(props.getProperty(csgProp.YEAR_LABEL.toString()));
        title_label = new Label(props.getProperty(csgProp.TITLE_LABEL.toString()));
        instructor_name_label = new Label(props.getProperty(csgProp.INSTRUCTOR_NAME_LABEL.toString()));
        instructor_home_label = new Label(props.getProperty(csgProp.INSTRUCTOR_HOME_LABEL.toString()));
        export_directory_label = new Label(props.getProperty(csgProp.EXPORT_DIRECTORY_LABEL.toString()));
        exportDirectory = new Label("Please Select A Directory");// for the selected directory
        templateDirectory = new Label("Please Select A Directory");// for the selected directory
        
        Button selectDirectory = new Button("Change");
        Button templateDirectoryButton = new Button("Select Template Directory");
        
        TextField titleField = new TextField();
        titleField.setPrefWidth(375);
        TextField instructorNameField = new TextField();
        instructorNameField.setPrefWidth(375);
        TextField instructorHomeField = new TextField();
        instructorHomeField.setPrefWidth(375);
       
        //begin for course details HBox
        ComboBox Subject = new ComboBox();
        ObservableList<String> subjects = FXCollections.observableArrayList();
        subjects.add("CSE");
        subjects.add("ISE");
        Subject.getItems().addAll(subjects);
        Subject.setPrefWidth(125);
        
        ComboBox Number = new ComboBox();
        ObservableList<String> numbers = FXCollections.observableArrayList();
        numbers.add("219");
        numbers.add("220");
        Number.getItems().addAll(numbers);
        Number.setPrefWidth(125);
        
        ComboBox Semester= new ComboBox();
        ObservableList<String> semesters = FXCollections.observableArrayList();
        semesters.add("Spring");
        semesters.add("Fall");
        Semester.getItems().addAll(semesters);
        Semester.setPrefWidth(125);
        
        ComboBox Year= new ComboBox();
        ObservableList<String> years = FXCollections.observableArrayList();
        years.add("2017");
        years.add("2016");
        Year.getItems().addAll(years);
        Year.setPrefWidth(125);
        
        courseInfoBox = new VBox();
        ColumnConstraints width = new ColumnConstraints(130);
        ColumnConstraints width2 = new ColumnConstraints(80);
        GridPane courseInfo = new GridPane();
        courseInfo.getColumnConstraints().add(width);
        courseInfo.getColumnConstraints().add(width);
        courseInfo.getColumnConstraints().add(width2);
        courseInfo.getColumnConstraints().add(width);
        courseInfo.setVgap(5);
        courseInfo.setHgap(5);
        courseInfo.add(course_info_label, 0, 0);
        courseInfo.add(subject_label, 0, 1);
        courseInfo.add(Subject, 1, 1);
        courseInfo.add(number_label, 2, 1);
        courseInfo.add(Number, 3, 1);
        courseInfo.add(semester_label, 0, 2);
        courseInfo.add(Semester, 1, 2);
        courseInfo.add(year_label, 2, 2);
        courseInfo.add(Year, 3, 2);
        
        GridPane courseInfo2 = new GridPane();
        courseInfo2.getColumnConstraints().add(width);
        ColumnConstraints textWidth = new ColumnConstraints(340);
        courseInfo2.getColumnConstraints().add(textWidth);
        courseInfo2.setVgap(5);
        courseInfo2.setHgap(5);
        courseInfo2.add(title_label, 0, 0);
        courseInfo2.add(titleField, 1, 0);
        courseInfo2.add(instructor_name_label, 0, 1);
        courseInfo2.add(instructorNameField, 1, 1);
        courseInfo2.add(instructor_home_label, 0, 2);
        courseInfo2.add(instructorHomeField, 1, 2);
        
        GridPane DirectoryBox = new GridPane();
        ColumnConstraints widthA = new ColumnConstraints(130);
        ColumnConstraints widthB = new ColumnConstraints(230);
        ColumnConstraints widthC = new ColumnConstraints(150);
        DirectoryBox.getColumnConstraints().addAll(widthA,widthB,widthC);
        DirectoryBox.add(export_directory_label,0,0);
        DirectoryBox.add(exportDirectory,1,0);
        DirectoryBox.add(selectDirectory,2,0);
        DirectoryBox.setPadding(new Insets(5, 300, 5, 0));
        
        selectDirectory.setOnMouseClicked(e->{
            DirectoryChooser dc = new DirectoryChooser();
            File file = dc.showDialog(app.getGUI().getWindow());
            exportDirectory.setText(file.toString());
        });
        
        courseInfoBox.setPadding(new Insets(2, 2, 2, 2));
        courseInfoBox.getChildren().add(courseInfo);
        courseInfoBox.getChildren().add(courseInfo2);
        courseInfoBox.getChildren().add(DirectoryBox);
        
        //courseInfoBox.setStyle("-fx-background-color: #DCDCDC;");   //GRAY COLOR
        //end of course details Hbox
        
        //begin of site Template HBox
        VBox finalSiteTemplateBox = new VBox();
        siteTemplate = new HBox();
        
        finalSiteTemplateBox.getChildren().add(site_template_label);
        VBox informationText = new VBox();
        Text t = new Text("The selected directory should contain the complete"
                + "template, including the HTML file");
        informationText.getChildren().add(t);
        finalSiteTemplateBox.getChildren().add(informationText);
        finalSiteTemplateBox.getChildren().add(templateDirectory);
        finalSiteTemplateBox.getChildren().add(templateDirectoryButton);
        
        templateDirectoryButton.setOnMouseClicked(e->{
            DirectoryChooser dc = new DirectoryChooser();
            File file = dc.showDialog(app.getGUI().getWindow());
            exportDirectory.setText(file.toString());
        });
        finalSiteTemplateBox.setSpacing(10);
        
        sitePage Home = new sitePage(true, "Home", "index.html", "HomeBuilder.js");
        sitePage Syllabus = new sitePage(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
        sitePage Schedule = new sitePage(true, "Schedule", "schedule.html", "scheduleBuilder.js");
        sitePage HWs = new sitePage(true, "HWs", "hws.html", "HWsBuilder.js");
        sitePage Projects = new sitePage(true, "Projects", "projects.html", "ProjectsBuilder.js");
        
        sitePages = FXCollections.observableArrayList();
        sitePages.add(Home);
        sitePages.add(Syllabus);
        sitePages.add(Schedule);
        sitePages.add(HWs);
        sitePages.add(Projects);
        
        siteTable = new TableView<sitePage>();
        siteTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        use = new TableColumn("Use");
        use.setCellValueFactory(
                new PropertyValueFactory<sitePage, Boolean>("use")
        );
        Navbar_title = new TableColumn("NavBar Title");
        Navbar_title.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("title")
        );
        File_name = new TableColumn("File Name");
        File_name.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("fileName")
        );
        Script = new TableColumn("Script");
        Script.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("script")
        );
        
        siteTable.getColumns().add(use);
        siteTable.getColumns().add(Navbar_title);
        siteTable.getColumns().add(File_name);
        siteTable.getColumns().add(Script);
        
        siteTable.setItems(sitePages);
        
        finalSiteTemplateBox.getChildren().add(siteTable);
        
        siteTemplate.getChildren().add(finalSiteTemplateBox);
        siteTemplate.setPadding(new Insets(3, 3, 3, 3));
         
        pageStyle = new VBox();
       
        pageStyle.getChildren().add(page_Style_label);
        GridPane logoImagePane = new GridPane();
        RowConstraints rows = new RowConstraints(30);
        ColumnConstraints column = new ColumnConstraints(190);
        logoImagePane.getRowConstraints().add(rows);
        logoImagePane.getRowConstraints().add(rows);
        logoImagePane.getRowConstraints().add(rows);
        logoImagePane.getColumnConstraints().add(column);
        logoImagePane.getColumnConstraints().add(column);
        logoImagePane.getColumnConstraints().add(column);
        
        bannerImage = new Label(props.getProperty(csgProp.BANNER_IMAGE_LABEL.toString()));
        leftFooterImage = new Label(props.getProperty(csgProp.LEFT_FOOTER_IMAGE_LABEL.toString()));
        rightFooterImage = new Label(props.getProperty(csgProp.RIGHT_FOOTER_IMAGE_LABEL.toString()));
        
        bannerImageView = new ImageView();
        leftFooterImageView = new ImageView();
        rightFooterImageView = new ImageView();
        
        changeBannerButton = new Button("Change");
        changeLeftFooterButton = new Button("Change");
        changeRightFooterButton = new Button("Change");
        
        logoImagePane.add(bannerImage, 0, 0);
        logoImagePane.add(bannerImageView, 1, 0);
        logoImagePane.add(changeBannerButton, 2, 0);
        logoImagePane.add(leftFooterImage, 0, 1);
        logoImagePane.add(leftFooterImageView, 1, 1);
        logoImagePane.add(changeLeftFooterButton, 2, 1);
        logoImagePane.add(rightFooterImage, 0, 2);
        logoImagePane.add(rightFooterImageView,1,2);
        logoImagePane.add(changeRightFooterButton, 2, 2);
        
        HBox styleSheetBox = new HBox();
        styleSheetComboBox = new ComboBox();
        
        ObservableList<String> stylesheets = FXCollections.observableArrayList();
        stylesheets.add("sea_wolf.css");
        stylesheets.add("island_gator.css");
        styleSheetComboBox.getItems().addAll(stylesheets);
        styleSheetComboBox.setPrefWidth(200);
        
        styleSheetLabel = new Label(props.getProperty(csgProp.STYLE_SHEET_LABEL.toString()));
        styleSheetBox.getChildren().add(styleSheetLabel);
        styleSheetBox.getChildren().add(styleSheetComboBox);
        
        disclaimer = new Label(props.getProperty(csgProp.DISCLAIMER_LABEL.toString()));
        
        pageStyle.getChildren().add(logoImagePane);
        pageStyle.getChildren().add(styleSheetBox);
        pageStyle.getChildren().add(disclaimer);
        pageStyle.setPadding(new Insets(5, 5, 5, 5));
        pageStyle.setSpacing(10);
        
        course_details_box.getChildren().add(courseInfoBox);
        course_details_box.getChildren().add(siteTemplate);
        course_details_box.getChildren().add(pageStyle);
        course_details_box.setAlignment(Pos.TOP_CENTER);
        course_details_box.setSpacing(10);
        course_details_box.setPadding(new Insets(10, 300, 10, 300));
        course_details_box.setStyle("-fx-background-color: #FDCE99;");
        return course_details_box;
    }
    public Label returnCourseInfoLabel(){
        return course_info_label;
    }
    public Label returnSiteTemplateLabel(){
        return site_template_label;
    }

    public Label getPage_Style_label() {
        return page_Style_label;
    }
    

    public Label getSite_template_label() {
        return site_template_label;
    }

    public Label getSemester_label() {
        return semester_label;
    }

    public Label getSubject_label() {
        return subject_label;
    }

    public Label getNumber_label() {
        return number_label;
    }

    public Label getYear_label() {
        return year_label;
    }

    public Label getTitle_label() {
        return title_label;
    }

    public Label getInstructor_name_label() {
        return instructor_name_label;
    }

    public Label getInstructor_home_label() {
        return instructor_home_label;
    }

    public Label getExport_directory_label() {
        return export_directory_label;
    }

    public Label getExportDirectory() {
        return exportDirectory;
    }

    public HBox getSiteTemplate() {
        return siteTemplate;
    }
    
    public VBox getCourseInfoBox(){
        return courseInfoBox;
    }

    public VBox getPageStyle() {
        return pageStyle;
    }
    public VBox getCourse_details_box() {
        return course_details_box;
    }

    public Label getTemplateDirectory() {
        return templateDirectory;
    }

    public Label getBannerImage() {
        return bannerImage;
    }

    public Label getLeftFooterImage() {
        return leftFooterImage;
    }

    public Label getRightFooterImage() {
        return rightFooterImage;
    }

    public Label getStyleSheetLabel() {
        return styleSheetLabel;
    }
    public VBox RecitationDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        VBox recitation_details_box = new VBox();
        
        TAData data = (TAData) app.getDataComponent();
        TeachingAssistant teachingAssistant_1 = data.getTA("Bryan Robicheau", "bryan.robicheau@stonybrook.edu");
        TeachingAssistant teachingAssistant_2 = data.getTA("Calvin Cheng", "calvin.cheng@stonybrook.edu");
        String TA1 = "Jarry Jone";//teachingAssistant_1.getName();
        String TA2 = "Hellen Corpac";//teachingAssistant_2.getName();
        
        recitation R02 = new recitation("R02", "McKenna", "Wed 3:30pm-4:23pm", "Old CS2114", TA1, TA2);
        recitation R05 = new recitation("R05", "Fodor", "Thu 1:30pm-2:53pm", "New CS1014", TA2, TA1);
        
        recitations = FXCollections.observableArrayList();
        recitations.add(R02);
        recitations.add(R05);
        
        recitationTable = new TableView<recitation>();
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        section = new TableColumn("Section");
        section.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("section")
        );
        instructor = new TableColumn("Instructor");
        instructor.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("instructor")
        );
        day_time = new TableColumn("Day/Time");
        day_time.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("day_time")
        );
        location = new TableColumn("Section");
        location.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("location")
        );
        Table_TA1 = new TableColumn("TA");
        Table_TA1.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("TA1")
        );
        Table_TA2 = new TableColumn("TA");
        Table_TA2.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("TA2")
        );
        
        recitationTable.getColumns().add(section);
        recitationTable.getColumns().add(instructor);
        recitationTable.getColumns().add(day_time);
        recitationTable.getColumns().add(location);
        recitationTable.getColumns().add(Table_TA1);
        recitationTable.getColumns().add(Table_TA2);
        recitationTable.setMaxHeight(100);
        recitationTable.setItems(recitations);
        
        recitation_details_box.getChildren().add(recitationTable);
        recitation_details_box.setAlignment(Pos.TOP_CENTER);
        recitation_details_box.setSpacing(10);
        recitation_details_box.setPadding(new Insets(10, 300, 10, 300));
        recitation_details_box.setStyle("-fx-background-color: #FDCE99;");
        
        return recitation_details_box;
       
    }
    
    
    
    public Pane ScheduleDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        Pane pane = new Pane();
        VBox main = new VBox();
        HBox Calendar_boundaries_box = new HBox();
        HBox Schedule_items_box = new HBox();
        
        Label schedule_boundaries_label = new Label(props.getProperty(csgProp.SCHEDULE_BOUNDARIES_LABEL.toString()));
        Label schedule_items_label = new Label(props.getProperty(csgProp.SCHEUDLE_ITEMS_LABEL.toString()));
        
        DatePicker start = new DatePicker();
        DatePicker end = new DatePicker();
        
        GridPane calendar_boundaries_pane = new GridPane();
        calendar_boundaries_pane.getChildren().add(schedule_boundaries_label);
        //calendar_boundaries_pane.add(schedule_boundaries_label, 0, 0);
        //calendar_boundaries_pane.add(start, 0, 1);
        //calendar_boundaries_pane.add(end, 1, 1);
        Calendar_boundaries_box.getChildren().add(calendar_boundaries_pane);
        
        GridPane schedule_items_pane = new GridPane();
        schedule_items_pane.add(schedule_items_label, 0, 0);
        Schedule_items_box.getChildren().add(schedule_items_pane);
        
        main.getChildren().add(Calendar_boundaries_box);
        main.getChildren().add(Schedule_items_box);
        pane.getChildren().add(main);
        return pane;
    }
}
