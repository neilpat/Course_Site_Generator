package csg.style;

import djf.AppTemplate;
import djf.components.AppStyleComponent;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import csg.data.TeachingAssistant;
import csg.workspace.CourseSiteWorkspace;

/**
 * This class manages all CSS style for this application.
 * 
 * @co-author Niral Patel (1110626877)
 * @version 1.0
 */
public class CourseSiteStyle extends AppStyleComponent {
    // FIRST WE SHOULD DECLARE ALL OF THE STYLE TYPES WE PLAN TO USE
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";
    public static String CLASS_UPDATE_TA_BUTTON = "update_ta_button";
    public static String CLASS_CLEAR_TA_BUTTON = "clear_ta_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE1 = "office_hours_grid_ta_cell_pane1";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";
    
    public static String REQUIRED_FIELD = "required_field";
    public static String CLASS_FINAL_HBOX_IN_TABS = "final_hbox_in_tabs";
    public static String TABS = "tabs";
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public CourseSiteStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initCourseSiteWorkspaceStyle();
    }

    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initCourseSiteWorkspaceStyle() {
        // LEFT SIDE - THE HEADER
        CourseSiteWorkspace workspaceComponent = (CourseSiteWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getTAsHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = workspaceComponent.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE);
        for (TableColumn tableColumn : taTable.getColumns()) {
            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
        }

        // LEFT SIDE - THE TA DATA ENTRY
        workspaceComponent.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE);
        workspaceComponent.getNameTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        workspaceComponent.getAddButton().getStyleClass().add(CLASS_ADD_TA_BUTTON);
        workspaceComponent.getUpdateButton().getStyleClass().add(CLASS_UPDATE_TA_BUTTON);
        workspaceComponent.getClearButton().getStyleClass().add(CLASS_CLEAR_TA_BUTTON);

        // RIGHT SIDE - THE HEADER
        workspaceComponent.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getOfficeHoursSubheaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        
        //COURSE_DETAILS_TAB
        workspaceComponent.returnCourseInfoLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        workspaceComponent.returnSiteTemplateLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        workspaceComponent.getPage_Style_label().getStyleClass().add(CLASS_HEADER_LABEL);
        workspaceComponent.getCourse_details_box().getStyleClass().add(TABS);
        
        workspaceComponent.getNumber_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getExportDirectory().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getSubject_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getSemester_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getYear_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getTitle_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getInstructor_home_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getInstructor_name_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getExport_directory_label().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getBannerImage().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getLeftFooterImage().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getRightFooterImage().getStyleClass().add(REQUIRED_FIELD);
        workspaceComponent.getStyleSheetLabel().getStyleClass().add(REQUIRED_FIELD);
        
        workspaceComponent.getCourseInfoBox().getStyleClass().add(CLASS_FINAL_HBOX_IN_TABS);
        workspaceComponent.getSiteTemplate().getStyleClass().add(CLASS_FINAL_HBOX_IN_TABS);
        workspaceComponent.getPageStyle().getStyleClass().add(CLASS_FINAL_HBOX_IN_TABS);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        CourseSiteWorkspace workspaceComponent = (CourseSiteWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) { 
        CourseSiteWorkspace workspaceComponent = (CourseSiteWorkspace)app.getWorkspaceComponent();
        GridPane gp = workspaceComponent.getOfficeHoursGridPane();
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
            n.setOnMouseEntered(e ->{
                    int row,col;
                    if(GridPane.getRowIndex(n)!=null && GridPane.getRowIndex(n)>0 
                            && GridPane.getColumnIndex(n)!=null && GridPane.getColumnIndex(n)>1){
                        row = GridPane.getRowIndex(n);
                        //System.out.println("ROW: "+row);
                        col = GridPane.getColumnIndex(n);
                        //System.out.println("COL: "+col);
                        for (Node node : gp.getChildren()) {
                            if (GridPane.getColumnIndex(node) < col && GridPane.getColumnIndex(node) > 1
                                    && GridPane.getRowIndex(node) == row) {
                                node.setStyle("-fx-border-color: #FFFF00;");
                            }
                            if (GridPane.getRowIndex(node) < row && GridPane.getRowIndex(node) > 0
                                    && GridPane.getColumnIndex(node) == col){
                                    node.setStyle("-fx-border-color: #FFFF00;");
                                }
                        }
                    } 
            });
            n.setOnMouseExited(e -> {
                for (Node node : gp.getChildren()) {
                                node.setStyle(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
                }
            });
        }
    }
}