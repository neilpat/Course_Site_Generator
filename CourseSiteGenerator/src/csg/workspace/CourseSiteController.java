package csg.workspace;

import djf.components.AppFileComponent;
import djf.controller.AppFileController;
import static csg.csgProp.*;
import djf.ui.AppMessageDialogSingleton;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import csg.csgApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.style.CourseSiteStyle;
import csg.transactions.Add_TA_Trans;
import csg.transactions.Remove_TA_Trans;
import csg.transactions.Toggle_TAOfficeHours_Trans;
import csg.transactions.Update_TA_Trans;
import csg.workspace.CourseSiteWorkspace;

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
        // IS A TA SELECTED IN THE TABLE?
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
}
