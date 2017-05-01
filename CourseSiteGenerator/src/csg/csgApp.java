package csg;

import java.util.Locale;
import djf.AppTemplate;
import static javafx.application.Application.launch;
import csg.data.TAData;
import csg.file.csgFiles;
import csg.style.CourseSiteStyle;
//import csg.test_bed.testSave;
import csg.workspace.CourseSiteWorkspace;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class serves as the application class for our CSG App program. 
 * Note that much of its behavior is inherited from AppTemplate, as defined in
 * the Desktop Java Framework. This app starts by loading all the UI-specific
 * settings like icon files and tooltips and other things, then the full 
 * User Interface is loaded using those settings. Note that this is a 
 * JavaFX application.
 * 
 * @author Niral Patel
 * @version 1.0
 */
public class csgApp extends AppTemplate {
    /**
     * This hook method must initialize all four components in the
     * proper order ensuring proper dependencies are respected, meaning
     * all proper objects are already constructed when they are needed
     * for use, since some may need others for initialization.
     */
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL FOUR COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, SO BE CAREFUL OF THE ORDER
        dataComponent = new TAData(this);
        try {
            workspaceComponent = new CourseSiteWorkspace(this);
        } catch (IOException ex) {
        }
        ((TAData)dataComponent).buildGridHeaders();
        fileComponent = new csgFiles(this);
        //fileComponent = new testSave(this);
        styleComponent = new CourseSiteStyle(this);
    }
    
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     * @param args
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}