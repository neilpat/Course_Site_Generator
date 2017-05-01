package csg.workspace;

import djf.controller.AppFileController;
import static csg.csgProp.*;
import djf.ui.AppMessageDialogSingleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import csg.csgApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.data.recitation;
import csg.data.schedule;
import csg.data.student;
import csg.data.team;
import csg.transactions.Add_TA_Trans;
import csg.transactions.Remove_TA_Trans;
import csg.transactions.Toggle_TAOfficeHours_Trans;
import csg.transactions.Update_TA_Trans;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import jtps.jTPS_Transaction;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @co-author Niral Patel (1110626877)
 * @version 1.0
 */
public class CourseSiteController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    csgApp app;

    /**
     * Constructor, note that the app must already be constructed.
     */
    public CourseSiteController(csgApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        
        Pattern pattern;
        Matcher made_match;
        
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // Lets Validate the Email The User Enters
        String Email_patterString = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(Email_patterString);
        made_match = pattern.matcher(email);
        
        if (name.isEmpty()||name.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (email.isEmpty()|| !made_match.matches()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name) || data.containsTA(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addTA(true,name,email);
            
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
    }

    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */     
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        try{
            CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
       
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        TAData data = (TAData)app.getDataComponent();
        String cellKey = pane.getId();
       
        // set the name and email fields with the selected TA
        
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        
        
        // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
        
        data.toggleTAOfficeHours(cellKey, taName);
        
       
//        jTPS_Transaction trans = new Toggle_TAOfficeHours_Trans(cellKey,taName, data);
//        workspace.getJTPS().addTransaction(trans);
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        }catch(NullPointerException e){
            
        }
    }
    public void handleDelteKey(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TeachingAssistant focused = (TeachingAssistant) taTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
//        dataComponent.deleteTA(focused);
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<StringProperty> props = new ArrayList<StringProperty>();
        
        Set<String> keySet = dataComponent.getOfficeHours().keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        try{
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                if(dataComponent.getOfficeHours().get(key).getValue().contains(focused.getName())){
                    keys.add(key);
                    props.add(dataComponent.getOfficeHours().get(key));
                } 
            }
        }    catch(NullPointerException e){}
//       
        jTPS_Transaction trans = new Remove_TA_Trans(focused, dataComponent, keys, props);
        workspace.getJTPS().addTransaction(trans);
        
        dataComponent.deleteTA(focused);
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleSelctedTA(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
       
        // GET THE TA
        if(selectedItem!=null){
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            String taEmail = ta.getEmail();
       
        // set the name and email fields with the selected TA
        
            TextField nameTextField = workspace.getNameTextField();
            TextField emailTextField = workspace.getEmailTextField();
            nameTextField.setText(taName);
            emailTextField.setText(taEmail);
        }
    }
    public void handleUpdateTA(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        
        Pattern pattern;
        Matcher made_match;
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // Lets Validate the Email The User Enters
        String Email_patterString = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(Email_patterString);
        made_match = pattern.matcher(email);
        
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (email.isEmpty()|| !made_match.matches()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        // EVERYTHING IS FINE, Change the TA
        else {
            // ADD THE NEW TA TO THE DATA
           String orgName = ta.getName();
           
           nameTextField.requestFocus();
           TAData dataComponent = (TAData)app.getDataComponent();
           TeachingAssistant focused = (TeachingAssistant) taTable.getFocusModel().getFocusedItem();
           dataComponent.updateTA(focused, name);
           ta.setName(name);
           ta.setEmail(email);
           String newName = name;
        
           jTPS_Transaction trans = new Update_TA_Trans(app,focused, dataComponent,orgName);
           workspace.getJTPS().addTransaction(trans);
           
           app.getGUI().updateToolbarControls(false);
           AppFileController appFileController = app.getGUI().getAppfileController();
           appFileController.markFileAsNotSaved();
        }     
    }
    public void handleClear(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        
        nameTextField.setText("");
        emailTextField.setText("");
    }
    public String handleAddBannerImage(String path) throws IOException{
        File selectedFile;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle("Banner Image");
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        
        if (path.equals("") || path == null) {
            selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
        }
        else{
            selectedFile = new File(path);
        }
        BufferedImage bufferedImage = ImageIO.read(selectedFile);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        ImageView bannerImageView = workspace.getBannerImageView();
        bannerImageView.setFitHeight(40);
        bannerImageView.setFitWidth(250);
        bannerImageView.setImage(image);
        
        String imagePath = selectedFile.getAbsolutePath();
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        
        return  imagePath;
    }
    public String handleAddLeftFooterImage(String path) throws IOException{
        File selectedFile;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle("Banner Image");
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        
        if (path.equals("") || path == null) {
            selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
        }
        else{
            selectedFile = new File(path);
        }
        
        BufferedImage bufferedImage = ImageIO.read(selectedFile);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        ImageView bannerImageView = workspace.getLeftFooterImageView();
        bannerImageView.setFitHeight(40);
        bannerImageView.setFitWidth(250);
        bannerImageView.setImage(image);
        
        String imagePath = selectedFile.getAbsolutePath();
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        
        return  imagePath;
        
    }
    public String handleAddRightFooterImage(String path) throws IOException{
        File selectedFile;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle("Banner Image");
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        
        if (path.equals("") || path == null) {
            selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
        }
        else{
            selectedFile = new File(path);
        }
        
        BufferedImage bufferedImage = ImageIO.read(selectedFile);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        ImageView bannerImageView = workspace.getRightFooterImageView();
        bannerImageView.setFitHeight(40);
        bannerImageView.setFitWidth(250);
        bannerImageView.setImage(image);
        
        String imagePath = selectedFile.getAbsolutePath();
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        
        return  imagePath;
    }
    public File handleExportDirectoryChooser(){
        String exportDirectory;
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(app.getGUI().getWindow());
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        
        return file;
    }
    
    public void handleDelteRecitationKey(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView RecitationTable = workspace.getRecitationTable();
        recitation focused = (recitation) RecitationTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        dataComponent.getRecitaitons().remove(focused);
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleSelectedRecitation(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView recitaitonTable = workspace.getRecitationTable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = recitaitonTable.getSelectionModel().getSelectedItem();
       
        // GET THE TA
        if(selectedItem!=null){
            recitation rec = (recitation)selectedItem;
            String section = rec.getSection();
            String instructor = rec.getInstructor();
            String day_time = rec.getDay_time();
            String location = rec.getLocation();
            String TA1 = rec.getTA1();
            String TA2 = rec.getTA2();
       
        // set the name and email fields with the selected TA
        
            TextField sectionTextField = workspace.getSection_textField();
            TextField instructorTextField = workspace.getInstructor_textField();
            TextField locationTextField = workspace.getLocation_textField();
            TextField dayTimeTextField = workspace.getDay_time_textField();
            //TextField TA1TextField = workspace.get();
            //TextField TA2TextField = workspace.getInstructor_textField();
            
            sectionTextField.setText(section);
            instructorTextField.setText(instructor);
            dayTimeTextField.setText(day_time);
            locationTextField.setText(location);
            //TA1TextField.setText(instructor);
            //TA2TextField.setText(instructor);
        }
    }
    public void handleAddRecitaiton(){
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TextField sectionTextField = workspace.getSection_textField();
        TextField instructorTextField = workspace.getInstructor_textField();
        TextField locationTextField = workspace.getLocation_textField();
        TextField dayTimeTextField = workspace.getDay_time_textField();
        
        
        String section = sectionTextField.getText();
        String instructor = instructorTextField.getText();
        String day_time = dayTimeTextField.getText();
        String location = locationTextField.getText();
        String TA1 = ((TeachingAssistant)workspace.getSupervising_TA_ComboBox1().getValue()).getName();
        if(TA1 == null || TA1.equals("")){
            TA1 = "TBA";
        }
        String TA2 = ((TeachingAssistant)workspace.getSupervising_TA_ComboBox2().getValue()).getName();
        if(TA2 == null  || TA2.equals("")){
            TA2 = "TBA";
        }
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (section.isEmpty() || section.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (instructor.isEmpty()|| instructor.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (location.isEmpty()|| location.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (day_time.isEmpty()|| day_time.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addRecitation(section, instructor, day_time, location, TA1, TA2);
            
            // CLEAR THE TEXT FIELDS
            sectionTextField.setText("");
            instructorTextField.setText("");
            locationTextField.setText("");
            dayTimeTextField.setText("");
            workspace.getSupervising_TA_ComboBox1().setValue(null);
            workspace.getSupervising_TA_ComboBox2().setValue(null);
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            sectionTextField.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
    }
    public void handleUpdateRecitation(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView recTable = workspace.getRecitationTable();
        Object selectedItem = recTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        recitation rec = (recitation)selectedItem;
        
        TextField sectionTextField = workspace.getSection_textField();
        TextField instructorTextField = workspace.getInstructor_textField();
        TextField locationTextField = workspace.getLocation_textField();
        TextField dayTimeTextField = workspace.getDay_time_textField();
        String section = sectionTextField.getText();
        String instructor = instructorTextField.getText();
        String day_time = dayTimeTextField.getText();
        String location = locationTextField.getText();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (section.isEmpty() || section.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (instructor.isEmpty()|| instructor.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (location.isEmpty()|| location.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (day_time.isEmpty()|| day_time.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, Change the TA
        else {
            // ADD THE NEW TA TO THE DATA
           rec.setSection(section);
           rec.setInstructor(instructor);
           rec.setDay_time(day_time);
           rec.setLocation(location);
        
//           jTPS_Transaction trans = new Update_TA_Trans(app,focused, dataComponent,orgName);
//           workspace.getJTPS().addTransaction(trans);
           
           app.getGUI().updateToolbarControls(false);
           AppFileController appFileController = app.getGUI().getAppfileController();
           appFileController.markFileAsNotSaved();
        }     
    }
    public void handleClearRecitation(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField sectionTextField = workspace.getSection_textField();
        TextField instructorTextField = workspace.getInstructor_textField();
        TextField locationTextField = workspace.getLocation_textField();
        TextField dayTimeTextField = workspace.getDay_time_textField();
        
        sectionTextField.setText("");
        instructorTextField.setText("");
        locationTextField.setText("");
        dayTimeTextField.setText("");
    }
    public void handleAddSchedule(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        ComboBox type_textField = workspace.getType_textField();
        TextField time_textField = workspace.getTime_textField();
        DatePicker plannedDate = workspace.getPlannedDate();
        TextField title_textField = workspace.getTitle_textField();
        TextField topic_textField = workspace.getTopic_textField();
        TextField link_textField = workspace.getLink_textField();
        TextField criteria_textField = workspace.getCriteria_textField();
        
        
        String type = (String)type_textField.getValue();
        String time = time_textField.getText();
        String title = title_textField.getText();
        String date = plannedDate.getValue().toString();
        String[] dateArray = date.split("-",5);
        String year = dateArray[0];
        String month = dateArray[1];
        String day = dateArray[2];
        String topic = topic_textField.getText();
        String link = link_textField.getText();
        String criteria = criteria_textField.getText();
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (type.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (date.isEmpty()|| date.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (title.isEmpty()|| title.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (topic.isEmpty()|| topic.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addSchedule(type, month, day , title, topic, link, time,criteria);
            
            // CLEAR THE TEXT FIELDS
            type_textField.setValue("");
            title_textField.setText("");
            topic_textField.setText("");
            plannedDate.setValue(null);
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            title_textField.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
    }
    public void handleSelectedSchedule(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView scheduleTable = workspace.getScheduleTable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = scheduleTable.getSelectionModel().getSelectedItem();
       
        // GET THE TA
        if(selectedItem!=null){
            schedule sch = (schedule)selectedItem;
            String type = sch.getType();
            String title = sch.getTitle();
            String topic = sch.getTopic();
            String date = sch.getDate();
        
            DatePicker plannedDate = workspace.getPlannedDate();
            TextField title_textField = workspace.getTitle_textField();
            TextField topic_textField = workspace.getTopic_textField();
            ComboBox type_TextField = workspace.getType_textField();
            
            plannedDate.setValue(LocalDate.parse(date));
            title_textField.setText(title);
            topic_textField.setText(topic);
            type_TextField.setValue(type);
        }
    }
    public void handleUpdateSchedule(){
        
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView scheduleTable = workspace.getScheduleTable();
        Object selectedItem = scheduleTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        schedule sch = (schedule)selectedItem;
        ComboBox type_textField = workspace.getType_textField();
        TextField time_textField = workspace.getTime_textField();
        DatePicker plannedDate = workspace.getPlannedDate();
        TextField title_textField = workspace.getTitle_textField();
        TextField topic_textField = workspace.getTopic_textField();
        TextField link_textField = workspace.getLink_textField();
        TextField criteria_textField = workspace.getCriteria_textField();
        
        String type = (String)type_textField.getValue();
        String time = time_textField.getText();
        String title = title_textField.getText();
        String date = plannedDate.getValue().toString();
        String topic = topic_textField.getText();
        String link = link_textField.getText();
        String criteria = criteria_textField.getText();
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
       if (type.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (date.isEmpty()|| date.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (title.isEmpty()|| title.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (topic.isEmpty()|| topic.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, Change the TA
        else {
            // ADD THE NEW TA TO THE DATA
            sch.setType(type);
            sch.setTime(time);
            sch.setTitle(title);
            sch.setTopic(topic);
            sch.setLink(link);
            sch.setCriteria(criteria);
            sch.setMonth(plannedDate.getValue().getMonthValue()+"");
            sch.setDay(plannedDate.getValue().getDayOfMonth()+"");
        
//           jTPS_Transaction trans = new Update_TA_Trans(app,focused, dataComponent,orgName);
//           workspace.getJTPS().addTransaction(trans);
           
           app.getGUI().updateToolbarControls(false);
           AppFileController appFileController = app.getGUI().getAppfileController();
           appFileController.markFileAsNotSaved();
        }
    }
    public void handleDeleteSchedule(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView scheduleTable = workspace.getScheduleTable();
        schedule focused = (schedule) scheduleTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        dataComponent.getSchedules().remove(focused);
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleSelectedTeam(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
       
        // GET THE TA
        if(selectedItem!=null){
            team tm = (team)selectedItem;
            String name = tm.getName();
            String red = tm.getRed();
            String green = tm.getGreen();
            String blue = tm.getBlue();
            String textColor = tm.getTextColor();
            String link = tm.getLink();
            //String color = ;
        
            TextField teamName = workspace.getNameTeamField();
            TextField colorField = workspace.getInputColor1Field();
            TextField textColorField = workspace.getInputColor2Field();
            TextField linkField = workspace.getLink_textField();
            
            //plannedDate.setValue(LocalDate.parse(date));
            teamName.setText(name);
            //colorField.setText(color);
            textColorField.setText(textColor);
            linkField.setText(link);
            
            try{
                workspace.getColor1().setStyle("-fx-fill: "+colorField.getText().toString());
                colorField.setOpacity(0.2);
                colorField.setStyle("-fx-control-inner-background: white");
            }catch(Exception g){
                g.printStackTrace();
            }
            try{
                workspace.getColor2().setStyle("-fx-fill: "+textColorField.getText().toString());
                textColorField.setOpacity(0.2);
                textColorField.setStyle("-fx-control-inner-background: white");
            }catch(Exception g){
                g.printStackTrace();
            }
        }
    }
    public void handleClearSchedule(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
         
        ComboBox type_textField = workspace.getType_textField();
        TextField time_textField = workspace.getTime_textField();
        DatePicker plannedDate = workspace.getPlannedDate();
        TextField title_textField = workspace.getTitle_textField();
        TextField topic_textField = workspace.getTopic_textField();
        TextField link_textField = workspace.getLink_textField();
        TextField criteria_textField = workspace.getCriteria_textField();
        
            type_textField.setValue("");
            time_textField.setText("");
            title_textField.setText("");
            topic_textField.setText("");
            plannedDate.setValue(null);
            topic_textField.setText("");
            link_textField.setText("");
            criteria_textField.setText("");
    }
    public void handleDeleteTeam(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        team focused = (team) teamTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        dataComponent.getTeams().remove(focused);
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleAddTeam(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TAData data = (TAData)app.getDataComponent();
        
        TextField teamNameField = workspace.getNameTeamField();
        TextField colorTextField = workspace.getInputColor1Field();
        TextField textColorTextField = workspace.getInputColor2Field();
        TextField linkTextField = workspace.getLinkField();
        
        String teamName = teamNameField.getText();
        String color = colorTextField.getText();
        String textColor = textColorTextField.getText();
        String link = linkTextField.getText();
        String red = "255";
        String green= "255";
        String blue= "255";
        
        Pattern colorPattern = Pattern.compile("([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
        Matcher m = colorPattern.matcher(color);
        boolean isColor = m.matches();
        if(isColor){
            red =  Integer.valueOf(color.substring( 0, 2 ), 16 )+"";
            green =  Integer.valueOf(color.substring( 2, 4 ), 16 )+"";
            blue =  Integer.valueOf(color.substring( 4, 6 ), 16 )+"";
            
            
        }
        else{
//            Color clr = Color.valueOf(color);
//            red =  clr.toString().substring(2,5);
//            green =  clr.toString().substring(5, 8);
//            blue =  clr.toString().substring(8, 10);
//            
//            System.out.println("red: "+ red);
//            System.out.println("green: "+ green);
//            System.out.println("blue: "+ blue);
        }
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (teamName.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (color.isEmpty()|| color.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (textColor.isEmpty()|| textColor.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (link.isEmpty()|| link.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addTeam(teamName, red, green, blue, textColor, link);
            
            // CLEAR THE TEXT FIELDS
            teamNameField.setText("");
            colorTextField.setText("");
            textColorTextField.setText("");
            linkTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            teamNameField.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
    }
    public void handleClearTeam(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
         
        TextField teamNameField = workspace.getNameTeamField();
        TextField colorTextField = workspace.getInputColor1Field();
        TextField textColorTextField = workspace.getInputColor2Field();
        TextField linkTextField = workspace.getLink_textField();
        
        teamNameField.setText("");
        colorTextField.setText("");
        textColorTextField.setText("");
        linkTextField.setText("");
    }
    public void handleAddStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        TextField studentTeamName = workspace.getTeamField();
        TextField studentRole = workspace.getRoleField();
        
        String firstName = studentFirstName.getText();
        String lastName = studentLastName.getText();
        String team = studentTeamName.getText();
        String role = studentRole.getText();
        
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (firstName.isEmpty() || firstName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (lastName.isEmpty()|| lastName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (team.isEmpty()|| team.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (role.isEmpty()|| role.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
           data.addStudent(firstName, lastName, team, role);
            
            // CLEAR THE TEXT FIELDS
            studentFirstName.setText("");
            studentLastName.setText("");
            studentTeamName.setText("");
            studentRole.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            studentFirstName.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
        
    }
    public void handleUpdateStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        student stu = (student)selectedItem;
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        TextField studentTeamName = workspace.getTeamField();
        TextField studentRole = workspace.getRoleField();
        
        String firstName = studentFirstName.getText();
        String lastName = studentLastName.getText();
        String team = studentTeamName.getText();
        String role = studentRole.getText();
        
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (firstName.isEmpty() || firstName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (lastName.isEmpty()|| lastName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
        }
        else if (team.isEmpty()|| team.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
        }
        else if (role.isEmpty()|| role.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            stu.setFirstName(firstName);
            stu.setLastName(lastName);
            stu.setTeam(team);
            stu.setRole(role);
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            studentFirstName.requestFocus();
            
//            TeachingAssistant ta = data.getTA(name, email);
//            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
    }
    public void handleSelectedStudent(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        student stu = (student)selectedItem;
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        TextField studentTeamName = workspace.getTeamField();
        TextField studentRole = workspace.getRoleField();
        
        studentFirstName.setText(stu.getFirstName());
        studentLastName.setText(stu.getLastName());
        studentTeamName.setText(stu.getTeam());
        studentRole.setText(stu.getRole());
        
    }
    public void handleDeleteStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        student focused = (student) studentTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        
//        jTPS_Transaction trans = new Remove_TA_Trans(focused, dataComponent, keys, props);
//        workspace.getJTPS().addTransaction(trans);
        
        dataComponent.removeStudent(focused);
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleClearStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        TextField studentTeamName = workspace.getTeamField();
        TextField studentRole = workspace.getRoleField();
        
        studentFirstName.setText("");
        studentLastName.setText("");
        studentTeamName.setText("");
        studentRole.setText("");
    }
}
