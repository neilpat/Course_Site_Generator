package djf.components;

import java.io.IOException;

/**
 * This interface provides the structure for file components in
 * our applications. This lets us call save and load methods from
 * the framework.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public interface AppFileComponent {

    /**
     * This function must be overridden in the actual component and would
     * write app data to a file in the necessary format.
     */
    public void saveData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would
     * read app data from a file in the necessary format.
     */
    public void loadData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would
     * be used for exporting app data into another format.
     */
    public void exportData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would
     * be used for importing app data from another format.
     */
    public void importData(AppDataComponent data, String filePath) throws IOException;

    public void saveOfficeHours(AppDataComponent dataComponent, String taManagerTesterpublic_htmljsOfficeHoursGr);

    public void saveScheduleData(AppDataComponent dataComponent, String taManagerTesterpublic_htmljsTAsDatajson);

    public void saveRecitationData(AppDataComponent dataComponent, String taManagerTesterpublic_htmljsRecitationsDa);

    public void saveTeamsData(AppDataComponent dataComponent, String taManagerTesterpublic_htmljsTeamsAndStude);

    public void saveProjectData(AppDataComponent dataComponent, String taManagerTesterpublic_htmljsProjectDatajs);
}
