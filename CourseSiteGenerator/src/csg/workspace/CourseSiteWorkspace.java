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
import csg.data.courses;
import csg.data.recitation;
import csg.data.schedule;
import csg.data.sitePage;
import csg.data.student;
import csg.data.team;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.imageio.ImageIO;

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
    
    Label recitation_mainLabel;
    Label add_edit_label;
    Label section_label;
    Label instructor_label;
    Label day_time_label;
    Label location_label;
    Label supervising_TA_label;
    
    Label schedule_mainLabel;
    Label minimize_schedulesButtonLabel;
    Label type_label;
    Label date_label;
    Label time_label;
    Label topic_label;
    Label link_label;
    Label criteria_label;
    Label startDateLabel;
    Label endDateLabel;
    Label scheduleItemsLabel;
    Label calendarBoundariesLabel;
    
    Label projectHeaderLabel;
    Label teamsLabel;
    Label AddEditLabel;
    Label projectLinkLabel;
    Label firstNameLabel;
    Label lastNameLabel;
    Label roleLabel;
    Label teamLabel;
    Label nameTeamLabel;
    Label colorLabel;
    Label textColorLabel;
    Label addEditStudentLabel;
    
    TextField section_textField;
    TextField instructor_textField;
    TextField day_time_textField;
    TextField location_textField;
    TextField supervising_TA_textField;
    
    TextField type_textField;
    TextField time_textField;
    TextField title_textField;
    TextField topic_textField;
    TextField link_textField;
    TextField criteria_textField;
    
    ImageView bannerImageView;
    ImageView leftFooterImageView;
    ImageView rightFooterImageView;
          
    HBox siteTemplate;
    VBox course_details_box;
    VBox pageStyle;
    VBox courseInfoBox;
    
    VBox recitation_details_box;
    VBox add_edit_box;
    
    VBox finalCalBoundariesBox;
    HBox scheduleHeaderBox;
    GridPane calendarBoundariesBox;
    VBox schedule_details_box;
    VBox add_edit_schedule_box;
    VBox final_scheduleItemsBox;
    
    HBox projectHeaderBox;
    VBox teamsBox;
    HBox teamsHeaderLabelBox;
    VBox addEditTeamBox;
    HBox addEditNameBox;
    HBox colorBox;
    HBox studentsHeaderLabelBox;
    VBox studentsBox;
    VBox projectDetailsBox;
    
    //COURSE DETAILS BUTTON
    Button selectDirectory;
    Button templateDirectoryButton; 
    Button changeBannerButton;
    Button changeLeftFooterButton;
    Button changeRightFooterButton;
    ScrollPane finalprojectDetailsBox;
    
    //RECITATIONS DETAILS BUTTON
    Button minimize_reciationsButton;
    Button addRecitationButton;
    Button updateReciationButton;
    Button clearRecitationButton;
    String add_recText;
    String update_recText;
    String clear_recText;
    
    //SCHEDULE DETAILS BUTTON
    Button minimize_schedulesButton;
    Button addScheduleButton;
    Button clearScheduleButton;
    Button updateScheduleButton;
    
    Button teamsMinimizeButton;
    Button addLinkButton;
    Button clearLinkButton;
    Button addStudentButton;
    Button clearStudeButton;
    
    ComboBox styleSheetComboBox;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, Boolean> undergraduate;
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
    
    TableView<schedule> scheduleTable;
    TableColumn<schedule, String> type;
    TableColumn<schedule, BigInteger> date;
    TableColumn<schedule, String> title;
    TableColumn<schedule, String> topic;
    
    TableView teamTable = new TableView<schedule>();
    TableColumn<team, String> name;
    TableColumn<team, String> color;
    TableColumn<team, String> textColor;
    TableColumn<team, String> link;
    
    TableView studentTable = new TableView<student>();
    TableColumn<student, String> firstName;
    TableColumn<student, String> lastName;
    TableColumn<student, String> team;
    TableColumn<student, String> role;
    
    DatePicker startDate;
    DatePicker endDate;
    DatePicker plannedDate;   
       
    //FOR THE SITE TABLE
    ObservableList<sitePage> sitePages;
    //FOR THE RECITATION TABLE
    ObservableList<recitation> recitations;
    //FOR THE SCHEDULE TABLE
    ObservableList<schedule>schedules;
    //FOR PROJECTS TABLE
    ObservableList<team> teams;
    //FOR STUDENTS TABLE
    ObservableList<student> students;
    
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
    
    TableColumn undergraduateColumn;
    ScrollPane finalCourseDetailsBox;
    HBox sPane;
    ComboBox beg_hours;
    ComboBox end_hours;
    ScrollPane finalTAPane;
    String undergraduateColumnText;
    String nameColumnText;
    String emailColumnText;
    
    
    
    
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
        recitationTab.setContent(RecitationDetailsPane(app, jtps, props));
        scheduleTab.setContent(ScheduleDetailsPane(app, jtps, props));
        projectTab.setContent(ProjectDetailsPane(app, jtps, props));
        
        tabPane.prefWidthProperty().bind(((BorderPane)workspace).widthProperty().multiply(.4));
        BorderPane.setMargin(tabPane, new Insets(15, 15, 15, 15));
        ((BorderPane)workspace).setStyle("-fx-background-color: #FFE8CC");
        ((BorderPane)workspace).setCenter(tabPane);
    }
    public ScrollPane TADetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
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
        undergraduateColumnText = props.getProperty(csgProp.UNDERGRADUATE_COLUMN_TEXT.toString());
        nameColumnText = props.getProperty(csgProp.NAME_COLUMN_TEXT.toString());
        emailColumnText = props.getProperty(csgProp.EMAIL_COLUMN_TEXT.toString());
        undergraduateColumn = new TableColumn("Undergraduate");
        undergraduateColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(0.2));
        undergraduateColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, Boolean>("undergraduate"));
        undergraduateColumn.setCellValueFactory(
        new Callback<CellDataFeatures<TeachingAssistant,Boolean>,ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<TeachingAssistant, Boolean> param)
            {   
                return param.getValue().getUndergraduate();
            }   
        });
        undergraduateColumn.setCellFactory(CheckBoxTableCell.forTableColumn(undergraduateColumn));
        nameColumn = new TableColumn(nameColumnText);
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(0.4));
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(0.4));
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        taTable.getColumns().add(undergraduateColumn);
        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);
        taTable.setPrefHeight(500);
        taTable.setEditable(true);
        
        

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

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        String officeHoursGridText = props.getProperty(csgProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridPane.setPrefWidth(700);
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
        rightPane.setPrefWidth(730);
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
        beg_hours = new ComboBox();
        beg_hours.setPromptText(props.getProperty(csgProp.START_TIME_LABEL));
        beg_hours.getItems().addAll(beg_times);
        beg_hours.setPrefWidth(115);
        
        end_hours = new ComboBox();
        end_hours.setPromptText(props.getProperty(csgProp.END_TIME_LABEL));
        end_hours.getItems().addAll(end_times);
        end_hours.setPrefWidth(115);
        
        //HBox final_right = new HBox();
        HBox box2 = new HBox();
        Button submit = new Button(props.getProperty(csgProp.SUBMIT_BUTTON_LABEL));
        //box2.getChildren().add(rightPane);
        box2.getChildren().add(beg_hours);
        box2.getChildren().add(end_hours);
        box2.getChildren().add(submit);
        box2.setSpacing(2);
        officeHoursHeaderBox.getChildren().add(box2);
        officeHoursHeaderBox.setSpacing(60);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        leftPane.setPrefWidth(458);
        leftPane.setPrefHeight(200);
        sPane = new HBox();
        sPane.getChildren().add(leftPane);
        sPane.getChildren().add(rightPane);
        sPane.setSpacing(20);
        sPane.setPadding(new Insets(10, 5, 10, 5));
        
        //sPane = new SplitPane(leftPane, new ScrollPane(rightPane));

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
                    if(Integer.parseInt(start_time)> org_start_time || Integer.parseInt(end_time)< org_end_time){
                        AppYesNoCancelDialogSingleton dialogSingleton = AppYesNoCancelDialogSingleton.getSingleton();
                        dialogSingleton.show("WARNING", "You are deleting some rows in the Grid!");
                        if(dialogSingleton.getSelection().equals(AppYesNoCancelDialogSingleton.YES)){
                    resetWorkspace();
                    ((TAData)(app.getDataComponent())).initHours(start_time, end_time);
                    reloadOfficeHoursGrid(data);
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
            resetWorkspace();
            ((TAData)(app.getDataComponent())).initHours(start_time, end_time);
            reloadOfficeHoursGrid(data);
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
            if(e.getCode() == KeyCode.BACK_SPACE){
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
        finalTAPane = new ScrollPane();
        finalTAPane.setContent(sPane);
        sPane.setStyle("-fx-background-color: #FDCE99");
        finalTAPane.setPadding(new Insets(10, 10, 10, 10));
        finalTAPane.setStyle("-fx-background-color: #FDCE99");
        return finalTAPane;
    }
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT

    public ScrollPane getFinalTAPane() {
        return finalTAPane;
    }

    public HBox getsPane() {
        return sPane;
    }
    
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

    public String getUndergraduateColumnText() {
        return undergraduateColumnText;
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
    
    public ScrollPane CourseDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        finalCourseDetailsBox = new ScrollPane();
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
        exportDirectory = new Label(props.getProperty(csgProp.SELECT_DIRECTORY_LABEL.toString()));// for the selected directory
        templateDirectory = new Label(props.getProperty(csgProp.SELECT_DIRECTORY_LABEL.toString()));// for the selected directory
        
        selectDirectory = new Button(props.getProperty(csgProp.CHANGE_BUTTON));
        templateDirectoryButton = new Button(props.getProperty(csgProp.SELECT_TEMPLATE_BUTTON));
        
        TextField titleField = new TextField();
        titleField.setPrefWidth(375);
        TextField instructorNameField = new TextField();
        instructorNameField.setPrefWidth(375);
        TextField instructorHomeField = new TextField();
        instructorHomeField.setPrefWidth(375);
       
        
        
        //begin for course details HBox
        ComboBox sub_name = new ComboBox();
//        ObservableList<String> subjects = FXCollections.observableArrayList();
//        subjects.add("CSE");
//        subjects.add("ISE");
//        Subject.getItems().addAll(subjects);
//        Subject.setPrefWidth(125);

        TAData data = (TAData) app.getDataComponent();
        ObservableList<courses> courseData = data.getCourses();
        sub_name.setItems(courseData);
        ComboBox Number = new ComboBox();
        
//        ObservableList<String> numbers = FXCollections.observableArrayList();
//        numbers.add("219");
//        numbers.add("220");
//        Number.getItems().addAll(numbers);
//        Number.setPrefWidth(125);
        
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
        courseInfo.add(sub_name, 1, 1);
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
        
        courseInfoBox.setPadding(new Insets(5, 5, 5, 10));
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
        Text t = new Text(props.getProperty(csgProp.STYLE_DISCALIMER_LABEL));
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
        
          sitePages = FXCollections.observableArrayList();
        
        
        siteTable = new TableView<sitePage>();
        siteTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sitePages = data.getPages();
        siteTable.setItems(sitePages);
        
        use = new TableColumn(props.getProperty(csgProp.USE_LABEL));
        use.setCellValueFactory(
                new PropertyValueFactory<sitePage, Boolean>("use"));
        use.setCellValueFactory(
        new Callback<CellDataFeatures<sitePage,Boolean>,ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<sitePage, Boolean> param)
            {   
                return param.getValue().returnUse();
            }   
        });
        use.setCellFactory(CheckBoxTableCell.forTableColumn(use));
        siteTable.setEditable(true);
            
        Navbar_title = new TableColumn(props.getProperty(csgProp.NAVBAR_TITLE_LABEL));
        Navbar_title.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("title")
        );
        File_name = new TableColumn(props.getProperty(csgProp.FILE_NAME_LABEL));
        File_name.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("fileName")
        );
        Script = new TableColumn(props.getProperty(csgProp.SCRIPT_LABEL));
        Script.setCellValueFactory(
                new PropertyValueFactory<sitePage, String>("script")
        );
        
        siteTable.getColumns().add(use);
        siteTable.getColumns().add(Navbar_title);
        siteTable.getColumns().add(File_name);
        siteTable.getColumns().add(Script);
        
        finalSiteTemplateBox.getChildren().add(siteTable);
        
        siteTemplate.getChildren().add(finalSiteTemplateBox);
        siteTemplate.setPadding(new Insets(5, 5, 5, 10));
         
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
        
        changeBannerButton = new Button(props.getProperty(csgProp.CHANGE_BUTTON));
        changeLeftFooterButton = new Button(props.getProperty(csgProp.CHANGE_BUTTON));
        changeRightFooterButton = new Button(props.getProperty(csgProp.CHANGE_BUTTON));
        
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
        stylesheets.add("roadrunner.css");
        stylesheets.add("eagle.css");
        styleSheetComboBox.getItems().addAll(stylesheets);
        styleSheetComboBox.setPrefWidth(200);
        styleSheetComboBox.setPromptText(stylesheets.get(0).toString());
        
        styleSheetLabel = new Label(props.getProperty(csgProp.STYLE_SHEET_LABEL.toString()));
        styleSheetBox.getChildren().add(styleSheetLabel);
        styleSheetBox.getChildren().add(styleSheetComboBox);
        
        disclaimer = new Label(props.getProperty(csgProp.DISCLAIMER_LABEL.toString()));
        
        pageStyle.getChildren().add(logoImagePane);
        pageStyle.getChildren().add(styleSheetBox);
        pageStyle.getChildren().add(disclaimer);
        pageStyle.setPadding(new Insets(5, 5, 5, 10));
        pageStyle.setSpacing(10);
        
        course_details_box.getChildren().add(courseInfoBox);
        course_details_box.getChildren().add(siteTemplate);
        course_details_box.getChildren().add(pageStyle);
        course_details_box.setAlignment(Pos.TOP_CENTER);
        course_details_box.setSpacing(10);
        finalCourseDetailsBox.setPadding(new Insets(10, 300, 10, 300));
        finalCourseDetailsBox.setContent(course_details_box);
        
        return finalCourseDetailsBox;
    }

    public ScrollPane getFinalCourseDetailsBox() {
        return finalCourseDetailsBox;
    }

    public Label getCourse_info_label() {
        return course_info_label;
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
        recitation_details_box = new VBox();
        
        HBox recitationHeaderBox = new HBox();
        recitation_mainLabel = new Label(props.getProperty(csgProp.RECITATION_MAIN_LABEL.toString()));
        minimize_reciationsButton = new Button(props.getProperty(csgProp.MINIMIZE_BUTTON.toString()));
        recitationHeaderBox.getChildren().add(recitation_mainLabel);
        recitationHeaderBox.getChildren().add(minimize_reciationsButton);
        
        TAData data = (TAData) app.getDataComponent();
        
        recitations = FXCollections.observableArrayList();
        
        recitationTable = new TableView<recitation>();
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recitations = data.getRecitaitons();
        recitationTable.setItems(recitations);
        section = new TableColumn(props.getProperty(csgProp.SECTION_LABEL));
        section.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("section")
        );
        instructor = new TableColumn(props.getProperty(csgProp.INSTRUCTOR_LABEL));
        instructor.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("instructor")
        );
        day_time = new TableColumn(props.getProperty(csgProp.DAY_TIME_LABEL));
        day_time.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("day_time")
        );
        location = new TableColumn(props.getProperty(csgProp.LOCATION_LABEL));
        location.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("location")
        );
        Table_TA1 = new TableColumn(props.getProperty(csgProp.TA_LABEL));
        Table_TA1.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("TA1")
        );
        Table_TA2 = new TableColumn(props.getProperty(csgProp.TA_LABEL));
        Table_TA2.setCellValueFactory(
                new PropertyValueFactory<recitation, String>("TA2")
        );
        
        recitationTable.getColumns().add(section);
        recitationTable.getColumns().add(instructor);
        recitationTable.getColumns().add(day_time);
        recitationTable.getColumns().add(location);
        recitationTable.getColumns().add(Table_TA1);
        recitationTable.getColumns().add(Table_TA2);
        recitationTable.setMaxHeight(200);
        
        
        add_edit_box = new VBox();
        add_edit_label = new Label(props.getProperty(csgProp.ADD_EDIT_LABEL.toString()));
        section_label = new Label(props.getProperty(csgProp.SECTION_LABEL.toString()));
        instructor_label = new Label(props.getProperty(csgProp.INSTRUCTOR_LABEL.toString()));
        day_time_label = new Label(props.getProperty(csgProp.DAY_TIME_LABEL.toString()));
        location_label = new Label(props.getProperty(csgProp.LOCATION_LABEL.toString()));
        supervising_TA_label = new Label(props.getProperty(csgProp.SUPERVISING_TA_LABEL.toString()));
        
        section_textField = new TextField("R01");
        instructor_textField = new TextField("McKenna");
        day_time_textField = new TextField("Mon/Tue");
        location_textField = new TextField("New CS");
        supervising_TA_textField = new TextField("Andrew");
        
        addButton = new Button(props.getProperty(csgProp.ADD_BUTTON_TEXT.toString()));
        addButton.setPrefWidth(130);
        clearButton = new Button(props.getProperty(csgProp.CLEAR_BUTTON_TEXT.toString()));
        clearButton.setPrefWidth(130);
        updateButton = new Button(props.getProperty(csgProp.UPDATE_BUTTON_TEXT.toString()));
        
        GridPane add_edit_input = new GridPane();
        RowConstraints rowHeight = new RowConstraints(35);
        ColumnConstraints columnWidth1 = new ColumnConstraints(150);
        ColumnConstraints columnWidth2 = new ColumnConstraints(250);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getRowConstraints().add(rowHeight);
        add_edit_input.getColumnConstraints().add(columnWidth1);
        add_edit_input.getColumnConstraints().add(columnWidth2);
        add_edit_input.setPadding(new Insets(5, 5, 5, 15));
        
        add_edit_input.add(section_label, 0, 0);
        add_edit_input.add(section_textField, 1, 0);
        add_edit_input.add(instructor_label, 0, 1);
        add_edit_input.add(instructor_textField, 1, 1);
        add_edit_input.add(day_time_label, 0, 2);
        add_edit_input.add(day_time_textField, 1, 2);
        add_edit_input.add(location_label, 0, 3);
        add_edit_input.add(location_textField, 1, 3);
        add_edit_input.add(supervising_TA_label, 0, 4);
        add_edit_input.add(supervising_TA_textField, 1, 4);
        add_edit_input.add(addButton, 0, 5);
        add_edit_input.add(clearButton, 1, 5);
        
        add_edit_box.getChildren().add(add_edit_label);
        add_edit_box.getChildren().add(add_edit_input);
        add_edit_box.setPadding(new Insets(5, 0, 0, 5));
        
        recitationTable.prefWidthProperty().bind(recitation_details_box.widthProperty().multiply(.2));
        recitationTable.prefHeightProperty().bind(recitation_details_box.heightProperty().multiply(.2));
        add_edit_box.prefWidthProperty().bind(recitation_details_box.widthProperty().multiply(.2));
        add_edit_box.prefHeightProperty().bind(recitation_details_box.heightProperty().multiply(.2));
        recitation_details_box.getChildren().add(recitationHeaderBox);
        recitation_details_box.getChildren().add(recitationTable);
        recitation_details_box.getChildren().add(add_edit_box);
        recitation_details_box.setAlignment(Pos.TOP_CENTER);
        recitation_details_box.setSpacing(10);
        recitation_details_box.setPadding(new Insets(10, 300, 10, 300));
        recitation_details_box.setStyle("-fx-background-color: #FDCE99;");
        
        
        return recitation_details_box;
       
    }

    public Label getRecitation_mainLabel() {
        return recitation_mainLabel;
    }

    public VBox getRecitation_details_box() {
        return recitation_details_box;
    }

    public Label getAdd_edit_label() {
        return add_edit_label;
    }

    public Label getDay_time_label() {
        return day_time_label;
    }

    public Label getSection_label() {
        return section_label;
    }

    public Label getInstructor_label() {
        return instructor_label;
    }

    public Label getLocation_label() {
        return location_label;
    }

    public Label getSupervising_TA_label() {
        return supervising_TA_label;
    }

    public VBox getAdd_edit_box() {
        return add_edit_box;
    }

    public Button getAddRecitationButton() {
        return addRecitationButton;
    }

    public Button getUpdateReciationButton() {
        return updateReciationButton;
    }

    public Button getClearRecitationButton() {
        return clearRecitationButton;
    }

    public String getAdd_recText() {
        return add_recText;
    }

    public String getUpdate_recText() {
        return update_recText;
    }

    public String getClear_recText() {
        return clear_recText;
    }
    
    
    
    public VBox ScheduleDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        schedule_details_box = new VBox();
        
        scheduleHeaderBox = new HBox();
        schedule_mainLabel = new Label(props.getProperty(csgProp.SCHEDULE_MAIN_LABEL.toString()));
        minimize_schedulesButton = new Button(props.getProperty(csgProp.MINIMIZE_BUTTON.toString()));
        scheduleHeaderBox.getChildren().add(schedule_mainLabel);
        scheduleHeaderBox.getChildren().add(minimize_schedulesButton);
        
        finalCalBoundariesBox = new VBox();
        calendarBoundariesBox = new GridPane();
        ColumnConstraints colmnWidth1 = new ColumnConstraints(100);
        ColumnConstraints colmnWidth2 = new ColumnConstraints(290);
        calendarBoundariesBox.getColumnConstraints().add(colmnWidth1);
        calendarBoundariesBox.getColumnConstraints().add(colmnWidth2);
        calendarBoundariesBox.getColumnConstraints().add(colmnWidth1);
        calendarBoundariesBox.getColumnConstraints().add(colmnWidth2);
        startDate = new DatePicker();
        startDate.setPrefWidth(200);
        endDate = new DatePicker();
        endDate.setPrefWidth(200);
        calendarBoundariesLabel = new Label(props.getProperty(csgProp.CALENDARBOUNDARIES_LABEL.toString()));
        startDateLabel = new Label(props.getProperty(csgProp.STARTDATE_LABEL.toString()));
        endDateLabel = new Label(props.getProperty(csgProp.ENDDATE_LABEL.toString()));
        
        calendarBoundariesBox.add(startDate, 1, 0);
        calendarBoundariesBox.add(startDateLabel, 0, 0);
        calendarBoundariesBox.add(endDate, 3, 0);
        calendarBoundariesBox.add(endDateLabel, 2, 0);
        calendarBoundariesBox.setPadding(new Insets(15, 5, 15, 5));
        finalCalBoundariesBox.getChildren().add(calendarBoundariesLabel);
        finalCalBoundariesBox.getChildren().add(calendarBoundariesBox);
        finalCalBoundariesBox.setPadding(new Insets(5, 5, 5, 5));
        
        startDate.setOnAction(e->{
            if(startDate.getValue() != null){
                startDateLabel.setText( props.getProperty(csgProp.STARTDATE_LABEL.toString())
                        +startDate.getValue().getDayOfWeek().toString());
            }
            
        });
        endDate.setOnAction(e->{
            if(endDate.getValue() != null){
                endDateLabel.setText( props.getProperty(csgProp.ENDDATE_LABEL.toString())
                        +endDate.getValue().getDayOfWeek().toString());
            }
        });
        
        TAData data = (TAData) app.getDataComponent();
        
        schedules = FXCollections.observableArrayList();
        schedules = data.getSchedules();
        scheduleTable = new TableView<schedule>();
        scheduleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        scheduleTable.setItems(schedules);
        type = new TableColumn(props.getProperty(csgProp.TYPE_LABEL));
        type.setCellValueFactory(
                new PropertyValueFactory<schedule, String>("type")
        );
        type.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.2));
        date = new TableColumn(props.getProperty(csgProp.DATE_LABEL));
        date.setCellValueFactory(
                new PropertyValueFactory<schedule, BigInteger>("date")
        );
        date.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.2));
        title = new TableColumn(props.getProperty(csgProp.TITLE_LABEL));
        title.setCellValueFactory(
                new PropertyValueFactory<schedule, String>("title")
        );
        title.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.2));
        topic = new TableColumn(props.getProperty(csgProp.TOPIC_LABEL));
        topic.setCellValueFactory(
                new PropertyValueFactory<schedule, String>("topic")
        );
        topic.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.38));
        
        scheduleTable.getColumns().add(type);
        scheduleTable.getColumns().add(date);
        scheduleTable.getColumns().add(title);
        scheduleTable.getColumns().add(topic);
        
        scheduleTable.setMaxHeight(100);
        
        add_edit_schedule_box = new VBox();
        add_edit_label = new Label(props.getProperty(csgProp.ADD_EDIT_LABEL.toString()));
        date_label = new Label(props.getProperty(csgProp.DATE_LABEL.toString()));
        type_label = new Label(props.getProperty(csgProp.TYPE_LABEL.toString()));
        time_label = new Label(props.getProperty(csgProp.TIME_LABEL.toString()));
        title_label = new Label(props.getProperty(csgProp.TITLE_LABEL.toString()));
        topic_label = new Label(props.getProperty(csgProp.TOPIC_LABEL.toString()));
        link_label = new Label(props.getProperty(csgProp.LINK_LABEL.toString()));
        criteria_label = new Label(props.getProperty(csgProp.CRITERIA_LABEL.toString()));
        
        plannedDate = new DatePicker();
       
        type_textField = new TextField();
        time_textField = new TextField();
        title_textField = new TextField();
        topic_textField = new TextField();
        link_textField = new TextField();
        criteria_textField = new TextField();
        
        
        addScheduleButton = new Button(props.getProperty(csgProp.ADD_BUTTON_TEXT.toString()));
        addScheduleButton.setPrefWidth(130);
        clearScheduleButton = new Button(props.getProperty(csgProp.CLEAR_BUTTON_TEXT.toString()));
        clearScheduleButton.setPrefWidth(130);
        updateScheduleButton = new Button(props.getProperty(csgProp.UPDATE_BUTTON_TEXT.toString()));
        
        final_scheduleItemsBox = new VBox();
        GridPane add_edit_input_schedule = new GridPane();
        RowConstraints rowHeight = new RowConstraints(35);
        ColumnConstraints columnWidth1 = new ColumnConstraints(150);
        ColumnConstraints columnWidth2 = new ColumnConstraints(250);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getRowConstraints().add(rowHeight);
        add_edit_input_schedule.getColumnConstraints().add(columnWidth1);
        add_edit_input_schedule.getColumnConstraints().add(columnWidth2);
        add_edit_input_schedule.setPadding(new Insets(5, 5, 5, 15));
        
        add_edit_input_schedule.add(add_edit_label, 0, 0);
        add_edit_input_schedule.add(type_label, 0, 1);
        add_edit_input_schedule.add(type_textField, 1, 1);
        add_edit_input_schedule.add(date_label, 0, 2);
        add_edit_input_schedule.add(plannedDate, 1, 2);
        add_edit_input_schedule.add(time_label, 0, 3);
        add_edit_input_schedule.add(time_textField, 1, 3);
        add_edit_input_schedule.add(title_label, 0, 4);
        add_edit_input_schedule.add(title_textField, 1, 4);
        add_edit_input_schedule.add(topic_label, 0, 5);
        add_edit_input_schedule.add(topic_textField, 1, 5);
        add_edit_input_schedule.add(link_label, 0, 6);
        add_edit_input_schedule.add(link_textField, 1, 6);
        add_edit_input_schedule.add(criteria_label, 0, 7);
        add_edit_input_schedule.add(criteria_textField, 1, 7);
        add_edit_input_schedule.add(addScheduleButton, 0, 8);
        add_edit_input_schedule.add(clearScheduleButton, 1, 8);
        
        scheduleItemsLabel = new Label(props.getProperty(csgProp.SCHEDULE_ITEMS_LABEL.toString()));
        final_scheduleItemsBox.getChildren().add(scheduleItemsLabel);
        final_scheduleItemsBox.getChildren().add(scheduleTable);
        final_scheduleItemsBox.getChildren().add(add_edit_input_schedule);
        final_scheduleItemsBox.setPadding(new Insets(5, 5, 0, 5));
        add_edit_schedule_box.getChildren().add(final_scheduleItemsBox);
        add_edit_schedule_box.setPadding(new Insets(5, 0, 0, 5));
        
        schedule_details_box.getChildren().add(scheduleHeaderBox);
        schedule_details_box.getChildren().add(finalCalBoundariesBox);
        schedule_details_box.getChildren().add(add_edit_schedule_box);
        schedule_details_box.setAlignment(Pos.TOP_CENTER);
        schedule_details_box.setSpacing(10);
        schedule_details_box.setPadding(new Insets(10, 300, 10, 300));
        
        return schedule_details_box;
    }

    public VBox getFinalCalBoundariesBox() {
        return finalCalBoundariesBox;
    }
    
    public VBox getAdd_edit_schedule_box() {
        return add_edit_schedule_box;
    }

    public VBox getFinal_scheduleItemsBox() {
        return final_scheduleItemsBox;
    }

    public Label getType_label() {
        return type_label;
    }

    public Label getDate_label() {
        return date_label;
    }

    public Label getTime_label() {
        return time_label;
    }

    public Label getTopic_label() {
        return topic_label;
    }

    public Label getLink_label() {
        return link_label;
    }

    public Label getCriteria_label() {
        return criteria_label;
    }

    public Label getStartDateLabel() {
        return startDateLabel;
    }

    public Label getEndDateLabel() {
        return endDateLabel;
    }

    public Label getCalendarBoundariesLabel() {
        return calendarBoundariesLabel;
    }

    public Label getScheduleItemsLabel() {
        return scheduleItemsLabel;
    }
    public ScrollPane ProjectDetailsPane(csgApp app, jTPS jtps, PropertiesManager props){
        finalprojectDetailsBox = new ScrollPane();
        projectDetailsBox = new VBox();
        projectHeaderBox = new HBox();
        projectHeaderLabel = new Label("Projects");
        projectHeaderBox.getChildren().add(projectHeaderLabel);
        
        teamsBox = new VBox();
        
        teamsHeaderLabelBox = new HBox();
        teamsLabel = new Label("Teams");
        teamsMinimizeButton = new Button(props.getProperty(csgProp.MINIMIZE_BUTTON));
        teamsHeaderLabelBox.getChildren().addAll(teamsLabel,teamsMinimizeButton);
        
        TAData data = (TAData) app.getDataComponent();
        teams = FXCollections.observableArrayList();
        teams = data.getTeams();
        teamTable = new TableView<team>();
        teamTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        teamTable.setItems(teams);
        
        name = new TableColumn(props.getProperty(csgProp.NAME_COLUMN_TEXT));
        name.setCellValueFactory(
                new PropertyValueFactory<team, String>("name")
        );
        name.prefWidthProperty().bind(teamTable.widthProperty().multiply(0.2));
        color = new TableColumn(props.getProperty(csgProp.COLOR_COLUMN_TEXT));
        color.setCellValueFactory(
                new PropertyValueFactory<team, String>("color")
        );
        color.prefWidthProperty().bind(teamTable.widthProperty().multiply(0.2));
        textColor = new TableColumn(props.getProperty(csgProp.TEXT_COLOR_TEXT));
        textColor.setCellValueFactory(
                new PropertyValueFactory<team, String>("textColor")
        );
        textColor.prefWidthProperty().bind(teamTable.widthProperty().multiply(0.2));
        link = new TableColumn(props.getProperty(csgProp.LINK_LABEL));
        link.setCellValueFactory(
                new PropertyValueFactory<team, String>("link")
        );
        link.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.38));
        
        
        teamTable.getColumns().add(name);
        teamTable.getColumns().add(color);
        teamTable.getColumns().add(textColor);
        teamTable.getColumns().add(link);
        
        teamTable.setMaxHeight(150);
        teamTable.setMaxWidth(600);
        
        teamsBox.getChildren().add(teamsHeaderLabelBox);
        teamsBox.getChildren().add(teamTable);
        teamsBox.setPadding(new Insets(5, 5, 5, 5));
        teamsBox.setSpacing(10);
        
            addEditTeamBox = new VBox();
            AddEditLabel = new Label(props.getProperty(csgProp.ADD_EDIT_LABEL));
                addEditNameBox = new HBox();
                    nameTeamLabel = new Label("Name");
                    TextField nameTeamField = new TextField();
                    addEditNameBox.getChildren().addAll(nameTeamLabel,nameTeamField);
                    addEditNameBox.setPadding(new Insets(5, 5, 5, 5));
                    addEditNameBox.setSpacing(50);
            
                colorBox = new HBox();
                colorBox.setPadding(new Insets(5, 5, 5, 5));
                    colorLabel = new Label("Color");
                    textColorLabel = new Label("Text Color");
                    StackPane color1Pane  = new StackPane();
                    Circle color1 = new Circle(50);
                    //color1.setFill(Color.WHITE);
                    color1.setFill(Color.WHITE);
                    TextField inputColor1Field = new TextField();
                    inputColor1Field.setAlignment(Pos.CENTER);
                    inputColor1Field.setPrefWidth(40);
                    color1Pane.getChildren().add(color1);
                    color1Pane.getChildren().add(inputColor1Field);
                    StackPane color2Pane = new StackPane();
                    Circle color2 = new Circle(50);
                    color2.setFill(Color.WHITE);
                    TextField inputColor2Field = new TextField();
                    inputColor2Field.setAlignment(Pos.CENTER);
                    inputColor2Field.setPrefWidth(40);
                    color2Pane.getChildren().add(color2);
                    color2Pane.getChildren().add(inputColor2Field);
                    colorBox.getChildren().add(colorLabel);
                    colorBox.getChildren().add(color1Pane);
                    colorBox.getChildren().add(textColorLabel);
                    colorBox.getChildren().add(color2Pane);
                    colorBox.setSpacing(50);
                    
                    inputColor1Field.setOnKeyReleased(e->{
                        try{
                            color1.setStyle("-fx-fill: "+inputColor1Field.getText().toString());
                            inputColor1Field.setOpacity(0.2);
                            inputColor1Field.setStyle("-fx-control-inner-background: white");
                        }catch(Exception g){
                            g.printStackTrace();
                        }
                        
                    });
                    inputColor2Field.setOnKeyReleased(e->{
                        try{
                            color2.setStyle("-fx-fill: "+inputColor2Field.getText().toString());
                            inputColor2Field.setOpacity(0.2);
                            inputColor2Field.setStyle("-fx-control-inner-background: white");
                        }catch(Exception g){
                            g.printStackTrace();
                        }
                    });
                    
                    GridPane linkBox = new GridPane();
                    linkBox.setPadding(new Insets(15, 5, 5, 5));
                    RowConstraints hight = new RowConstraints(50);
                    ColumnConstraints width = new ColumnConstraints(130);
                    linkBox.getColumnConstraints().add(width);
                    linkBox.getColumnConstraints().add(width);
                    linkBox.getRowConstraints().add(hight);
                    projectLinkLabel = new Label("Link");
                    TextField linkField = new TextField();
                    addLinkButton = new Button("Add Team");
                    clearLinkButton = new Button(props.getProperty(csgProp.CLEAR_BUTTON_TEXT.toString()));
                    linkBox.add(projectLinkLabel, 0, 0);
                    linkBox.add(linkField, 1, 0);
                    linkBox.add(addLinkButton, 0, 1);
                    linkBox.add(clearLinkButton, 1, 1);
                    
            addEditTeamBox.setPadding(new Insets(55, 5, 5, 5));
            addEditTeamBox.setSpacing(10);
            addEditTeamBox.getChildren().add(AddEditLabel);
            addEditTeamBox.getChildren().add(addEditNameBox);
            addEditTeamBox.getChildren().add(colorBox);
            addEditTeamBox.getChildren().add(linkBox);
        
        teamsBox.getChildren().add(addEditTeamBox);
        ////////////////////////////////////////////////////////////
        studentsBox = new VBox();
        studentsHeaderLabelBox = new HBox();
            Label studentsHeaderLabel = new Label("Students");
            Button minimizeStudentButton = new Button(props.getProperty(csgProp.MINIMIZE_BUTTON));
            studentsHeaderLabelBox.getChildren().addAll(studentsHeaderLabel,minimizeStudentButton);
        
        studentTable = new TableView<student>();
        studentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        firstName = new TableColumn(props.getProperty(csgProp.FIRST_NAME_LABEL));
        firstName.setCellValueFactory(
                new PropertyValueFactory<student, String>("firstName")
        );
        firstName.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.2));
        lastName = new TableColumn(props.getProperty(csgProp.LAST_NAME_LABEL));
        lastName.setCellValueFactory(
                new PropertyValueFactory<student, String>("lastName")
        );
        lastName.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.2));
        team = new TableColumn(props.getProperty(csgProp.TEAM_NAME_LABEL));
        team.setCellValueFactory(
                new PropertyValueFactory<student, String>("team")
        );
        team.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.2));
        role = new TableColumn(props.getProperty(csgProp.ROLE_LABEL));
        role.setCellValueFactory(
                new PropertyValueFactory<student, String>("role")
        );
        role.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(0.38));
        
        
        studentTable.getColumns().add(firstName);
        studentTable.getColumns().add(lastName);
        studentTable.getColumns().add(team);
        studentTable.getColumns().add(role);
        studentTable.setMaxHeight(100);
        studentTable.setMaxWidth(600);
        
        students = FXCollections.observableArrayList();
        students = data.getStudents();
        studentTable.setItems(students);;
        
        studentTable.setMaxHeight(150);
        
        studentsBox.getChildren().add(studentsHeaderLabelBox);
        studentsBox.getChildren().add(studentTable);
        studentsBox.setPadding(new Insets(5, 5, 5, 5));
        studentsBox.setSpacing(10);
        
            GridPane addEditStudentBox = new GridPane();
            ColumnConstraints width1 = new ColumnConstraints(80);
            ColumnConstraints width2 = new ColumnConstraints(200);
            RowConstraints height = new RowConstraints(50);
            addEditStudentBox.getColumnConstraints().add(width1);
            addEditStudentBox.getColumnConstraints().add(width2);
            addEditStudentBox.getRowConstraints().add(height);
            addEditStudentLabel = new Label(props.getProperty(csgProp.ADD_EDIT_LABEL.toString()));
            firstNameLabel = new Label(props.getProperty(csgProp.FIRST_NAME_LABEL));
            lastNameLabel = new Label(props.getProperty(csgProp.LAST_NAME_LABEL));
            teamLabel = new Label(props.getProperty(csgProp.TEAM_NAME_LABEL));
            roleLabel = new Label(props.getProperty(csgProp.ROLE_LABEL));
            
            TextField firstNameField = new TextField();
            TextField lastNameField = new TextField();
            TextField teamField = new TextField();
            TextField roleField = new TextField();
            
            addStudentButton = new Button(props.getProperty(csgProp.ADD_BUTTON_TEXT));
            clearStudeButton = new Button(props.getProperty(csgProp.CLEAR_BUTTON_TEXT));
            
            addEditStudentBox.add(addEditStudentLabel, 0, 0);
            addEditStudentBox.add(firstNameLabel, 0, 1);
            addEditStudentBox.add(firstNameField, 1, 1);
            addEditStudentBox.add(lastNameLabel, 0, 2);
            addEditStudentBox.add(lastNameField, 1, 2);
            addEditStudentBox.add(teamLabel, 0, 3);
            addEditStudentBox.add(teamField, 1, 3);
            addEditStudentBox.add(roleLabel, 0, 4);
            addEditStudentBox.add(roleField, 1, 4);
            addEditStudentBox.add(addStudentButton, 0, 5);
            addEditStudentBox.add(clearStudeButton, 1, 5);
            addEditStudentBox.setPadding(new Insets(25, 5, 5, 5));
        
        studentsBox.getChildren().add(addEditStudentBox);
        
        projectDetailsBox.getChildren().add(projectHeaderBox);
        projectDetailsBox.getChildren().add(teamsBox);
        projectDetailsBox.getChildren().add(studentsBox);
       
        projectDetailsBox.setPrefWidth(700);
        finalprojectDetailsBox.setContent(projectDetailsBox);
        finalprojectDetailsBox.setPadding(new Insets(10, 267, 10, 266));
        finalprojectDetailsBox.setStyle("-fx-background-color: #FDCE99");
        
        return finalprojectDetailsBox;
    }

    public Label getAddEditStudentLabel() {
        return addEditStudentLabel;
    }

    public ScrollPane getFinalprojectDetailsBox() {
        return finalprojectDetailsBox;
    }

    public VBox getProjectDetailsBox() {
        return projectDetailsBox;
    }
    
    public HBox getProjectHeaderBox() {
        return projectHeaderBox;
    }

    public VBox getTeamsBox() {
        return teamsBox;
    }

    public VBox getStudentsBox() {
        return studentsBox;
    }

    public csgApp getApp() {
        return app;
    }

    public CourseSiteController getController() {
        return controller;
    }

    public jTPS getJtps() {
        return jtps;
    }

    public HBox getTasHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTasHeaderLabel() {
        return tasHeaderLabel;
    }

    public Label getDisclaimer() {
        return disclaimer;
    }

    public Label getSchedule_mainLabel() {
        return schedule_mainLabel;
    }

    public Label getMinimize_schedulesButtonLabel() {
        return minimize_schedulesButtonLabel;
    }

    public Label getProjectHeaderLabel() {
        return projectHeaderLabel;
    }

    public Label getTeamsLabel() {
        return teamsLabel;
    }

    public Label getAddEditLabel() {
        return AddEditLabel;
    }

    public Label getProjectLinkLabel() {
        return projectLinkLabel;
    }

    public Label getFirstNameLabel() {
        return firstNameLabel;
    }

    public Label getAstNameLabel() {
        return lastNameLabel;
    }

    public Label getRoleLabel() {
        return roleLabel;
    }

    public Label getTeamLabel() {
        return teamLabel;
    }

    public Label getLastNameLabel() {
        return lastNameLabel;
    }

    public TextField getSection_textField() {
        return section_textField;
    }

    public TextField getInstructor_textField() {
        return instructor_textField;
    }

    public TextField getDay_time_textField() {
        return day_time_textField;
    }

    public TextField getLocation_textField() {
        return location_textField;
    }

    public TextField getSupervising_TA_textField() {
        return supervising_TA_textField;
    }

    public TextField getType_textField() {
        return type_textField;
    }

    public TextField getTime_textField() {
        return time_textField;
    }

    public TextField getTitle_textField() {
        return title_textField;
    }

    public TextField getTopic_textField() {
        return topic_textField;
    }

    public TextField getLink_textField() {
        return link_textField;
    }

    public TextField getCriteria_textField() {
        return criteria_textField;
    }

    public ImageView getBannerImageView() {
        return bannerImageView;
    }

    public ImageView getLeftFooterImageView() {
        return leftFooterImageView;
    }

    public ImageView getRightFooterImageView() {
        return rightFooterImageView;
    }

    public HBox getScheduleHeaderBox() {
        return scheduleHeaderBox;
    }

    public GridPane getCalendarBoundariesBox() {
        return calendarBoundariesBox;
    }

    public VBox getSchedule_details_box() {
        return schedule_details_box;
    }

    public HBox getTeamsHeaderLabelBox() {
        return teamsHeaderLabelBox;
    }

    public VBox getAddEditTeamBox() {
        return addEditTeamBox;
    }

    public HBox getAddEditNameBox() {
        return addEditNameBox;
    }

    public HBox getColorBox() {
        return colorBox;
    }

    public HBox getStudentsHeaderLabelBox() {
        return studentsHeaderLabelBox;
    }

    public Button getSelectDirectory() {
        return selectDirectory;
    }

    public Button getTemplateDirectoryButton() {
        return templateDirectoryButton;
    }

    public Button getChangeBannerButton() {
        return changeBannerButton;
    }

    public Button getChangeLeftFooterButton() {
        return changeLeftFooterButton;
    }

    public Button getChangeRightFooterButton() {
        return changeRightFooterButton;
    }

    public Button getMinimize_reciationsButton() {
        return minimize_reciationsButton;
    }

    public Button getMinimize_schedulesButton() {
        return minimize_schedulesButton;
    }

    public Button getAddScheduleButton() {
        return addScheduleButton;
    }

    public Button getClearScheduleButton() {
        return clearScheduleButton;
    }

    public Button getUpdateScheduleButton() {
        return updateScheduleButton;
    }

    public Button getTeamsMinimizeButton() {
        return teamsMinimizeButton;
    }

    public Button getAddLinkButton() {
        return addLinkButton;
    }

    public Button getClearLinkButton() {
        return clearLinkButton;
    }

    public ComboBox getStyleSheetComboBox() {
        return styleSheetComboBox;
    }

    public TableView<TeachingAssistant> getTaTable() {
        return taTable;
    }

    public TableColumn<TeachingAssistant, String> getNameColumn() {
        return nameColumn;
    }

    public TableColumn<TeachingAssistant, String> getEmailColumn() {
        return emailColumn;
    }

    public TableView<sitePage> getSiteTable() {
        return siteTable;
    }

    public TableColumn<sitePage, Boolean> getUse() {
        return use;
    }

    public TableColumn<sitePage, String> getNavbar_title() {
        return Navbar_title;
    }

    public TableColumn<sitePage, String> getFile_name() {
        return File_name;
    }

    public TableColumn<sitePage, String> getScript() {
        return Script;
    }

    public TableView<recitation> getRecitationTable() {
        return recitationTable;
    }

    public TableColumn<recitation, String> getSection() {
        return section;
    }

    public TableColumn<recitation, String> getInstructor() {
        return instructor;
    }

    public TableColumn<recitation, String> getDay_time() {
        return day_time;
    }

    public TableColumn<recitation, String> getLocation() {
        return location;
    }

    public TableColumn<recitation, String> getTable_TA1() {
        return Table_TA1;
    }

    public TableColumn<recitation, String> getTable_TA2() {
        return Table_TA2;
    }

    public TableView<schedule> getScheduleTable() {
        return scheduleTable;
    }

    public TableColumn<schedule, String> getType() {
        return type;
    }

    public TableColumn<schedule, BigInteger> getDate() {
        return date;
    }

    public TableColumn<schedule, String> getTitle() {
        return title;
    }

    public TableColumn<schedule, String> getTopic() {
        return topic;
    }

    public TableView getTeamTable() {
        return teamTable;
    }

    public TableColumn<team, String> getName() {
        return name;
    }

    public TableColumn<team, String> getColor() {
        return color;
    }

    public TableColumn<team, String> getTextColor() {
        return textColor;
    }

    public TableColumn<team, String> getLink() {
        return link;
    }

    public TableView getStudentTable() {
        return studentTable;
    }

    public TableColumn<student, String> getFirstName() {
        return firstName;
    }

    public TableColumn<student, String> getLastName() {
        return lastName;
    }

    public TableColumn<student, String> getTeam() {
        return team;
    }

    public TableColumn<student, String> getRole() {
        return role;
    }

    public DatePicker getStartDate() {
        return startDate;
    }

    public DatePicker getEndDate() {
        return endDate;
    }

    public DatePicker getPlannedDate() {
        return plannedDate;
    }

    public ObservableList<sitePage> getSitePages() {
        return sitePages;
    }

    public ObservableList<recitation> getRecitations() {
        return recitations;
    }

    public ObservableList<schedule> getSchedules() {
        return schedules;
    }

    public ObservableList<team> getTeams() {
        return teams;
    }

    public ObservableList<student> getStudents() {
        return students;
    }

    public HBox getOfficeHoursHeaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursHeaderLabel() {
        return officeHoursHeaderLabel;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Tab getCourseDetailsTab() {
        return courseDetailsTab;
    }

    public Tab getTADetailsTab() {
        return TADetailsTab;
    }

    public Tab getRecitationTab() {
        return recitationTab;
    }

    public Tab getScheduleTab() {
        return scheduleTab;
    }

    public Tab getProjectTab() {
        return projectTab;
    }

    public Label getNameTeamLabel() {
        return nameTeamLabel;
    }

    public Label getColorLabel() {
        return colorLabel;
    }

    public Label getTextColorLabel() {
        return textColorLabel;
    }
}
