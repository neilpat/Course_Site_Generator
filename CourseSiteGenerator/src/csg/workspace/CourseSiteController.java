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
import csg.workspace.CourseSiteWorkspace;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

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
            
            TeachingAssistant ta = data.getTA(name, email);
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
//        jTPS_Transaction trans = new Remove_TA_Trans(focused, dataComponent, keys, props);
//        workspace.getJTPS().addTransaction(trans);
        
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
        
//           jTPS_Transaction trans = new Update_TA_Trans(app,focused, dataComponent,orgName);
//           workspace.getJTPS().addTransaction(trans);
           
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
        return  imagePath;
    }
    public File handleExportDirectoryChooser(){
        String exportDirectory;
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(app.getGUI().getWindow());
        return file;
    }
    
    public void handleDelteRecitationKey(){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView RecitationTable = workspace.getRecitationTable();
        recitation focused = (recitation) RecitationTable.getFocusModel().getFocusedItem();
        TAData dataComponent = (TAData)app.getDataComponent();
        dataComponent.getRecitaitons().remove(focused);
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
        String TA1 = (String)workspace.getSupervising_TA_ComboBox1().getValue();
        if(TA1 == null || TA1.equals("")){
            TA1 = "TBA";
        }
        String TA2 = (String)workspace.getSupervising_TA_ComboBox2().getValue();
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
}
