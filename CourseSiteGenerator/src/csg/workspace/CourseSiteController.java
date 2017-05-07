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
import csg.transactions.Add_Rec_Trans;
import csg.transactions.Add_Schedule_Trans;
import csg.transactions.Add_Student_Trans;
import csg.transactions.Add_TA_Trans;
import csg.transactions.Add_Team_Trans;
import csg.transactions.Remove_Rec_Trans;
import csg.transactions.Remove_Schedule_Trans;
import csg.transactions.Remove_Student_Trans;
import csg.transactions.Remove_TA_Trans;
import csg.transactions.Remove_Team_Trans;
import csg.transactions.Toggle_TAOfficeHours_Trans;
import csg.transactions.Update_TA_Trans;
import csg.transactions.Update_Team_Trans;
import csg.transactions.Update_rec_Trans;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import static jdk.nashorn.internal.objects.NativeObject.keys;
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
            workspace.getSupervising_TA_ComboBox1().getItems().add(name);
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
            
            TeachingAssistant ta = data.getTA(name, email);
            jTPS_Transaction trans = new Add_TA_Trans(ta, data);
            workspace.getJTPS().addTransaction(trans);
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
        
        // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
        
        jTPS_Transaction trans = new Toggle_TAOfficeHours_Trans(cellKey,taName, data);
        workspace.getJTPS().addTransaction(trans);
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        }catch(NullPointerException e){
            
        }
    }
    public void handleDeleteKey(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TeachingAssistant focused = (TeachingAssistant) taTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
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
       recitation rec = null;
        for(int i=0;i<workspace.getRecitations().size();i++){
            if(workspace.getRecitations().get(i).getTA1().equals(focused.getName())){
                rec = workspace.getRecitations().get(i);
            }
            if(workspace.getRecitations().get(i).getTA2().equals(focused.getName())){
                rec = workspace.getRecitations().get(i);
            }
        }
        String TA1;
        String TA2;
        if(rec != null){
            TA1 = rec.getTA1();
            TA2 = rec.getTA2();
        }
        else{
            TA1 = null;
            TA2 = null;
        }

        jTPS_Transaction trans = new Remove_TA_Trans(app,focused, dataComponent, keys, props, rec, TA1, TA2);
        //workspace.getRecitationTable().refresh();
        workspace.getJTPS().addTransaction(trans);
        
        //dataComponent.deleteTA(focused);                      //CHECK
        handleClear();
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
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(app.getGUI().getWindow());
        
        app.getGUI().updateToolbarControls(false);
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        
        return file;
    }
    
    public void handleDeleteRecitationKey(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView RecitationTable = workspace.getRecitationTable();
        recitation focused = (recitation) RecitationTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        dataComponent.getRecitaitons().remove(focused);
        
        workspace.getSupervising_TA_ComboBox1().getItems().clear();
        workspace.getSupervising_TA_ComboBox2().getItems().clear();
        workspace.getSupervising_TA_ComboBox1().setItems(workspace.availableTAs());
        workspace.getSupervising_TA_ComboBox2().setItems(workspace.availableTAs());
        
        handleClearRecitation();
        
        jTPS_Transaction trans = new Remove_Rec_Trans(focused, dataComponent);
        workspace.getJTPS().addTransaction(trans);
        
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
            ComboBox TA1ComboBox = workspace.getSupervising_TA_ComboBox1();
            ComboBox TA2ComboBox = workspace.getSupervising_TA_ComboBox2();
            
            sectionTextField.setText(section);
            instructorTextField.setText(instructor);
            dayTimeTextField.setText(day_time);
            locationTextField.setText(location);
            TA1ComboBox.setValue(TA1);
            TA2ComboBox.setValue(TA2);
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
        
        String TA1 = "TBA";
        if(workspace.getSupervising_TA_ComboBox1().getSelectionModel().getSelectedIndex()>=0){
            TA1 = (String)workspace.getSupervising_TA_ComboBox1().getValue();
        }
        String TA2 = "TBA";
        if(workspace.getSupervising_TA_ComboBox2().getSelectionModel().getSelectedIndex()>=0){
              TA2 = (String)workspace.getSupervising_TA_ComboBox2().getValue();
        }
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        Boolean validTAs = true;
        for(int i=0;i<data.getRecitaitons().size();i++){
            if(TA1.equals(data.getRecitaitons().get(i).getTA1()) || TA1.equals(data.getRecitaitons().get(i).getTA2())){
                if(!TA1.equals("TBA")){
                    validTAs = false;
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show("INVALID TA", "Duplicate TA, Please Select TA1 Again");
                }
            }
            if(TA2.equals(data.getRecitaitons().get(i).getTA2())|| TA2.equals(data.getRecitaitons().get(i).getTA1())){
                if(!TA2.equals("TBA")){
                    validTAs = false;
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show("INVALID TA", "Duplicate TA, Please Select TA2 Again");
                }
            }
        }
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
        else if(validTAs == false){
//            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
//            dialog.show("INVALID INFORMATION", "PLEASE SELECT TA AGAIN");
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addRecitation(section, instructor, day_time, location, TA1, TA2);
            
            workspace.getSupervising_TA_ComboBox1().getItems().clear();
            workspace.getSupervising_TA_ComboBox2().getItems().clear();
            workspace.getSupervising_TA_ComboBox1().setItems(workspace.availableTAs());
            workspace.getSupervising_TA_ComboBox2().setItems(workspace.availableTAs());
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            sectionTextField.requestFocus();
            
            recitation rec = data.getRecitation(section);
            jTPS_Transaction trans = new Add_Rec_Trans(rec, data);
            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
    }
    public boolean handleUpdateRecitation(){
        Boolean complete = true;
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView recTable = workspace.getRecitationTable();
        Object selectedItem = recTable.getSelectionModel().getSelectedItem();
        // GET THE RECITATION
        recitation rec = (recitation)selectedItem;
        
        TextField sectionTextField = workspace.getSection_textField();
        TextField instructorTextField = workspace.getInstructor_textField();
        TextField locationTextField = workspace.getLocation_textField();
        TextField dayTimeTextField = workspace.getDay_time_textField();
        ComboBox TA1ComboxBox = workspace.getSupervising_TA_ComboBox1();
        ComboBox TA2ComboxBox = workspace.getSupervising_TA_ComboBox2();
        String section = sectionTextField.getText();
        String instructor = instructorTextField.getText();
        String day_time = dayTimeTextField.getText();
        String location = locationTextField.getText();
        String TA1 = "TBA";
        String TA2 = "TBA";
        if(TA1ComboxBox.getSelectionModel().getSelectedIndex()>=0){
             TA1 = (String)TA1ComboxBox.getValue();
        }
        if (TA2ComboxBox.getSelectionModel().getSelectedIndex()>=0) {
            TA2 = (String)TA2ComboxBox.getValue();
        }
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (section.isEmpty() || section.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));            
            complete = false;
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (instructor.isEmpty()|| instructor.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));            
            complete = false;
        }
        else if (location.isEmpty()|| location.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));            
            complete = false;
        }
        else if (day_time.isEmpty()|| day_time.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DAYTIME_TITLE), props.getProperty(MISSING_DAYTIME_MESSAGE));            
            complete = false;
        }
        // EVERYTHING IS FINE, CHANGE THE RECITATION
        else {
            // ADD THE NEW TA TO THE DATA
           rec.setSection(section);
           rec.setInstructor(instructor);
           rec.setDay_time(day_time);
           rec.setLocation(location);
           rec.setTA1(TA1);
           rec.setTA2(TA2);
        
           jTPS_Transaction trans = new Update_rec_Trans(app,rec, data,section, instructor, TA1, TA2);
           workspace.getJTPS().addTransaction(trans);
           
           app.getGUI().updateToolbarControls(false);
           AppFileController appFileController = app.getGUI().getAppfileController();
           appFileController.markFileAsNotSaved();
           
        }  
        return complete;
    }
    public void handleClearRecitation(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField sectionTextField = workspace.getSection_textField();
        TextField instructorTextField = workspace.getInstructor_textField();
        TextField locationTextField = workspace.getLocation_textField();
        TextField dayTimeTextField = workspace.getDay_time_textField();
        ComboBox TA1ComboBox = workspace.getSupervising_TA_ComboBox1();
        ComboBox TA2ComboBox = workspace.getSupervising_TA_ComboBox2();
        
        sectionTextField.setText("");
        instructorTextField.setText("");
        locationTextField.setText("");
        dayTimeTextField.setText("");
        TA1ComboBox.setValue(null);
        TA2ComboBox.setValue(null);
        
    }
    public Boolean handleAddSchedule(){
        Boolean valid = true;
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
	    dialog.show(props.getProperty(MISSING_TYPE_TITLE), props.getProperty(MISSING_TYPE_MESSAGE));            
            valid = false;
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (date.isEmpty()|| date.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_DATE_TITLE), props.getProperty(MISSING_DATE_MESSAGE));            
            valid = false;
        }
        else if (title.isEmpty()|| title.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TITLE_TITLE), props.getProperty(MISSING_TITLE_MESSAGE));            
            valid = false;
        }
        else if ((topic.isEmpty()|| topic.startsWith(" ")) && !(type.equals("holidays"))) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TOPIC_TITLE), props.getProperty(MISSING_TOPIC_MESSAGE));            
            valid = false;
        }
        else if ((link.isEmpty()|| link.startsWith(" ")) && !(type.equals("recitations"))) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LINK_TITLE), props.getProperty(MISSING_LINK_MESSAGE));            
            valid = false;
        }
        else if ((time.isEmpty()|| time.startsWith(" ")) && (type.equals("hws"))) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TIME_TITLE), props.getProperty(MISSING_TIME_MESSAGE));            
            valid = false;
        }
        else if ((criteria.isEmpty()|| criteria.startsWith(" ")) && (type.equals("hws"))) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_CRITERIA_TITLE), props.getProperty(MISSING_CRITERIA_MESSAGE));            
            valid = false;
        }
        // EVERYTHING IS FINE, ADD A NEW SCHEDULE
        else {
            // ADD THE NEW TA TO THE DATA
            data.addSchedule(type, month, day , title, topic, link, time,criteria);
            
            // CLEAR THE TEXT FIELDS
            handleClearSchedule();
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            title_textField.requestFocus();
            
            schedule sch = data.getSchedule(type, date, title);
            jTPS_Transaction trans = new Add_Schedule_Trans(sch, data);
            workspace.getJTPS().addTransaction(trans);
            
            return valid;
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
        
        return valid;
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
        handleClearSchedule();
        
        jTPS_Transaction trans = new Remove_Schedule_Trans(focused, dataComponent);
        workspace.getJTPS().addTransaction(trans);
        
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
            String color = tm.getColor();
            String textColor = tm.getTextColor();
            String link = tm.getLink();
        
            TextField teamName = workspace.getNameTeamField();
            TextField colorField = workspace.getInputColor1Field();
            TextField textColorField = workspace.getInputColor2Field();
            TextField linkField = workspace.getLinkField();
            
            //plannedDate.setValue(LocalDate.parse(date));
            teamName.setText(name);
            colorField.setText(color);
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
        handleClearTeam();
        
        jTPS_Transaction trans = new Remove_Team_Trans(focused, dataComponent);
        workspace.getJTPS().addTransaction(trans);
        
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
        Boolean validColor = true;
        Color clr = Color.WHITE;
        String red = "255";
        String green = "255";
        String blue = "255";
        try{
            clr = Color.web(color);
        }catch(IllegalArgumentException e){
            validColor = false;
        }
        if(validColor==true){
            red = workspace.hexTodecimal(clr.toString().substring(2,4))+"";
            green= workspace.hexTodecimal(clr.toString().substring(4,6))+"";
            blue= workspace.hexTodecimal(clr.toString().substring(6,8))+"";
        }
        String textColor = textColorTextField.getText();
        String link = linkTextField.getText();
        
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
            
            
            workspace.getTeamField().getItems().add(teamName);
            
            // CLEAR THE TEXT FIELDS
            teamNameField.setText("");
            colorTextField.setText("");
            colorTextField.setStyle("-fx-background-color: white");
            textColorTextField.setText("");
            textColorTextField.setStyle("-fx-background-color: white");
            linkTextField.setText("");
            workspace.getColor1().setFill(Color.WHITE);
            workspace.getColor2().setFill(Color.WHITE);
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            teamNameField.requestFocus();
            
            team tm = data.getTeam(teamName);
            jTPS_Transaction trans = new Add_Team_Trans(tm, data);
            workspace.getJTPS().addTransaction(trans);
        }
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
        appFileController.checkFile();
    }
    public void handleUpdateTeam(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        team tm = (team)selectedItem;
        
        TextField teamNameField = workspace.getNameTeamField();
        TextField colorTextField = workspace.getInputColor1Field();
        TextField textColorTextField = workspace.getInputColor2Field();
        TextField linkTextField = workspace.getLinkField();
        
        String teamName = teamNameField.getText();
        String color = colorTextField.getText();
        String red = "255";
        String green = "255";
        String blue = "255";
        Color clr = Color.WHITE;
        Boolean validColor = true;
        try{
            clr = Color.web(color);
        }catch(IllegalArgumentException e){
            validColor = false;
        }
        if(validColor==true){
            red = workspace.hexTodecimal(clr.toString().substring(2,4))+"";
            green= workspace.hexTodecimal(clr.toString().substring(4,6))+"";
            blue= workspace.hexTodecimal(clr.toString().substring(6,8))+"";
        }
        String textColor = textColorTextField.getText();
        String link = linkTextField.getText();
        // WE'LL NEED TO ASKå THE DATA SOME QUESTIONS TOO
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (teamName.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TEAM_TITLE), props.getProperty(MISSING_TEAM_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (color.isEmpty()|| color.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_COLOR_TITLE), props.getProperty(MISSING_COLOR_MESSAGE));            
        }
        else if (textColor.isEmpty()|| textColor.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TEXT_COLOR_TITLE), props.getProperty(MISSING_TEXT_COLOR_MESSAGE));            
        }
        else if (link.isEmpty()|| link.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LINK_TITLE), props.getProperty(MISSING_LINK_MESSAGE));            
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            teamNameField.requestFocus();
           TAData data = (TAData)app.getDataComponent();
           
           jTPS_Transaction trans = new Update_Team_Trans(app, tm, data, teamName, red, green, blue, color, textColor, link);
           workspace.getJTPS().addTransaction(trans);
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
        TextField linkTextField = workspace.getLinkField();
        
            teamNameField.setText("");
            colorTextField.setText("");
            colorTextField.setStyle("-fx-background-color: white");
            textColorTextField.setText("");
            textColorTextField.setStyle("-fx-background-color: white");
            linkTextField.setText("");
            workspace.getColor1().setFill(Color.WHITE);
            workspace.getColor2().setFill(Color.WHITE);
    }
    public Boolean handleAddStudentButton(){
        Boolean valid = true;
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        ComboBox studentTeamName = workspace.getTeamField();
        ComboBox studentRole = workspace.getRoleField();
        
        String firstName = studentFirstName.getText();
        String lastName = studentLastName.getText();
        String team = (String)studentTeamName.getValue();
        String role = (String)studentRole.getValue();
        
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (firstName.isEmpty() || firstName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_FIRST_NAME_TITLE), props.getProperty(MISSING_FIRST_NAME_MESSAGE));            
            valid = false;
        }
        else if (lastName.isEmpty()|| lastName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LAST_NAME_TITLE), props.getProperty(MISSING_LAST_NAME_MESSAGE));            
            valid = false;
        }
        else if (team.isEmpty()|| team.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TEAM_TITLE), props.getProperty(MISSING_TEAM_MESSAGE));            
            valid = false;
        }
        else if (role.isEmpty()|| role.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_ROLE_TITLE), props.getProperty(MISSING_ROLE_MESSAGE));            
            valid = false;
        }
        // EVERYTHING IS FINE, ADD A NEW STUDENT
        else {
            for(int i=0;i<workspace.getStudents().size();i++){
                if(workspace.getStudents().get(i).getTeam().equals(team)){
                    if(workspace.getStudents().get(i).getRole().equals(role)){
                        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                        dialog.show(props.getProperty(DUPLICATE_ROLE_TITLE), props.getProperty(DUPLICATE_ROLE_MESSAGE));            
                        valid = false;

                    }
                }
            }
            if(valid){
                data.addStudent(firstName, lastName, team, role);
            
                // CLEAR THE TEXT FIELDS
                handleClearStudentButton();
            
                // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
                studentFirstName.requestFocus();
            
                student stu = data.getStudent(firstName, lastName, team);
                jTPS_Transaction trans = new Add_Student_Trans(stu, data);
                workspace.getJTPS().addTransaction(trans);
                
                app.getGUI().updateToolbarControls(false);
        
                AppFileController appFileController = app.getGUI().getAppfileController();
                appFileController.markFileAsNotSaved();
                appFileController.checkFile();
            }
        }
        return valid;
    }
    public boolean handleUpdateStudentButton(){
        boolean valid = true;
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        student stu = (student)selectedItem;
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        ComboBox studentTeamName = workspace.getTeamField();
        ComboBox studentRole = workspace.getRoleField();
        
        String firstName = studentFirstName.getText();
        String lastName = studentLastName.getText();
        String team = (String)studentTeamName.getValue();
        String role = (String)studentRole.getValue();
        
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (firstName.isEmpty() || firstName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_FIRST_NAME_TITLE), props.getProperty(MISSING_FIRST_NAME_MESSAGE));            
            valid = false;
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (lastName.isEmpty()|| lastName.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_LAST_NAME_TITLE), props.getProperty(MISSING_LAST_NAME_MESSAGE));            
            valid = false;
        }
        else if (team.isEmpty()|| team.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TEAM_TITLE), props.getProperty(MISSING_TEAM_MESSAGE));            
            valid = false;
        }
        else if (role.isEmpty()|| role.startsWith(" ")) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_ROLE_TITLE), props.getProperty(MISSING_ROLE_MESSAGE));            
            valid = false;
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            for(int i=0;i<workspace.getStudents().size();i++){
                if(workspace.getStudents().get(i).getTeam().equals(team)){
                    if(workspace.getStudents().get(i).getRole().equals(role) && !(workspace.getStudents().get(i).getFirstName().equals(stu.getFirstName()))){
                        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                        dialog.show(props.getProperty(DUPLICATE_ROLE_TITLE), props.getProperty(DUPLICATE_ROLE_MESSAGE));            
                        valid = false;
                    }
                }
            }
            if(valid){
                stu.setFirstName(firstName);
                stu.setLastName(lastName);
                stu.setTeam(team);
                stu.setRole(role);
                
                handleClearStudentButton();
                studentFirstName.requestFocus();
//                TeachingAssistant ta = data.getTA(name, email);
//                jTPS_Transaction trans = new Add_TA_Trans(ta, data);
//                workspace.getJTPS().addTransaction(trans);

                app.getGUI().updateToolbarControls(false);
        
                AppFileController appFileController = app.getGUI().getAppfileController();
                appFileController.markFileAsNotSaved();
                appFileController.checkFile();
            }
        }
        return valid;
    }
    public void handleSelectedStudent(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        student stu = (student)selectedItem;
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        ComboBox studentTeamName = workspace.getTeamField();
        ComboBox studentRole = workspace.getRoleField();
        
        studentFirstName.setText(stu.getFirstName());
        studentLastName.setText(stu.getLastName());
        studentTeamName.setValue(stu.getTeam());
        studentRole.setValue(stu.getRole());
        
    }
    public void handleDeleteStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        student focused = (student) studentTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        handleClearStudentButton();
        
        jTPS_Transaction trans = new Remove_Student_Trans(focused, dataComponent);
        workspace.getJTPS().addTransaction(trans);
        
        dataComponent.removeStudent(focused);
        app.getGUI().updateToolbarControls(false);
        
        AppFileController appFileController = app.getGUI().getAppfileController();
        appFileController.markFileAsNotSaved();
    }
    public void handleClearStudentButton(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        
        TextField studentFirstName = workspace.getFirstNameField();
        TextField studentLastName = workspace.getLastNameField();
        ComboBox studentTeamName = workspace.getTeamField();
        ComboBox studentRole = workspace.getRoleField();
        
        studentFirstName.setText("");
        studentLastName.setText("");
        studentTeamName.setValue(null);
        studentRole.setValue(null);
    }
//    public void handleTabChanged(){
//        jTPS_Transaction trans = new Tab_Changed_Trans(rec, data);
//        workspace.getJTPS().addTransaction(trans);
//    }
}
