

package csg.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.csgApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.data.projects;
import csg.data.recitation;
import csg.data.schedule;
import csg.data.sitePage;
import csg.data.student;
import csg.data.team;
import csg.workspace.CourseSiteController;
import csg.workspace.CourseSiteWorkspace;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @co-author Niral Patel (1110626877)
 */
public class csgFiles implements AppFileComponent{
    // THIS IS THE APP ITSELF
    csgApp app;
    String currentWorkFile = "";
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_UNDERGRADUTE = "undergraduate";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    
    static final String JSON_COURSES = "courses";
    static final String JSON_COURSE_NAME = "course_name";
    static final String JSON_COURSE_NUMBER = "course_number";
    
    static final String JSON_SEMESTERS = "semesters";
    static final String JSON_SEMESTER = "sem";
    static final String JSON_YEAR = "year";
    static final String JSON_PAGE_TITLE = "page_title";
    static final String JSON_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_INSTRUCTOR_HOME = "instructor_home";
    
    static final String JSON_BANNER_IMAGE_PATH = "banner_image_path";
    static final String JSON_LEFT_FOOTER_IMAGE_PATH = "left_footer_image_path";
    static final String JSON_RIGHT_FOOTER_IMAGE_PATH = "right_footer_image_path";
    
    static final String JSON_SITE_PAGE = "site_pages";
    static final String JSON_USE = "site_page_use";
    static final String JSON_NAV_BAR_TITLE = "nav_bar_title";
    static final String JSON_FILE_NAME = "file_name";
    static final String JSON_SCRIPT = "script";
    
    static final String JSON_RECITATION = "recitations";
    static final String JSON_SECTION = "section";
    static final String JSON_INSTUCTOR = "instructor";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";
    
    static final String JSON_SCHEDULE = "schedules";
    static final String JSON_TYPE = "type";
    static final String JSON_DATE = "date";
    static final String JSON_TITLE = "title";
    static final String JSON_TOPIC = "topic";
    static final String JSON_START_YEAR = "startingYear";
    static final String JSON_START_MONTH = "startingMondayMonth";
    static final String JSON_START_DAY = "startingMondayDay";
    static final String JSON_END_YEAR = "endingYear";
    static final String JSON_END_MONTH = "endingFridayMonth";
    static final String JSON_END_DAY = "endingFridayDay";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_LECTURES = "lectures";
    static final String JSON_REFERENCES = "references";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_REC_RECITATIONS = "rec_recitations";
    static final String JSON_SCH_RECITATIONS = "sch_recitations";
    static final String JSON_HWS = "hws";
    static final String JSON_MONTH = "month";
    static final String JSON_CRITERIA = "criteria";
    
    
    static final String JSON_TEAM = "teams";
    static final String JSON_TEAM_NAME = "name";
    static final String JSON_COLOR_RED = "red";
    static final String JSON_COLOR_GREEN = "green";
    static final String JSON_COLOR_BLUE = "blue";
    static final String JSON_TEXT_COLOR = "text_color";
    static final String JSON_LINK = "link";
    
    static final String JSON_STUDENT = "students";
    static final String JSON_FIRST_NAME = "firstName";
    static final String JSON_LAST_NAME = "lastName";
    static final String JSON_ASSIGNED_TEAM = "team";
    static final String JSON_ROLE = "role";
    
    static final String JSON_PROJECT_NAME = "name";
    static final String JSON_STUDENTS = "students";
    static final String JSON_PROJECT_LINK = "link";
    static final String JSON_PROJECT_SEMESTER = "semester";
    static final String JSON_PROEJCTS = "projects";

    
    static final String JSON_WORK = "work";
    
    int sizeOfHB = 8;
    int NoInHB = 4;
    int halfByte = 0x0F;
    char[] hexDigits = { 
        '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    
    public csgFiles(csgApp initApp) {
        app = initApp;
    }
    public String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfHB);
        hexBuilder.setLength(sizeOfHB);
        for (int i = sizeOfHB - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= NoInHB;
        }
        return hexBuilder.toString(); 
    } 
    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
             char c = s.charAt(i);
             int d = digits.indexOf(c);
             val = 16*val + d;
        }
        return val;
    }
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
        
	TAData dataManager = (TAData)data;
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        CourseSiteController controller = new CourseSiteController(app);

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        String begTime;
        String endTime;
        String ampm = "am";
        
        begTime = startHour;
        if(Integer.parseInt(begTime)>11){
            ampm = "pm";
            begTime = (Integer.parseInt(begTime)-12)+"";
            workspace.getBeg_hours().setValue(begTime+":"+"00"+ampm);
        }
        else{
            ampm = "am";
            workspace.getBeg_hours().setValue(begTime+":"+"00"+ampm);
        }
        endTime = endHour;
        if(Integer.parseInt(endTime)>11){
            ampm = "pm";
            endTime = (Integer.parseInt(endTime)-12)+"";
            workspace.getEnd_hours().setValue(endTime+":"+"00"+ampm);
        }
        else{
            ampm = "am";
            workspace.getEnd_hours().setValue(endTime+":"+"00"+ampm);
        }
        

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
//        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());

        // ADD THE COURSE NAME AND NUMBER SEMESTER AND YEAR
        
        String courseName = json.getString(JSON_COURSE_NAME);
        String course_Number = json.getString(JSON_COURSE_NUMBER);
        String semester = json.getString(JSON_SEMESTER);
        String year = json.getString(JSON_YEAR);
        
        dataManager.setCourseName(courseName);
        dataManager.setCourseNumber(course_Number);
        dataManager.setSemester(semester);
        dataManager.setYear(year);
        
        workspace.setSub_name(courseName);
        workspace.setNumber(course_Number);
        workspace.setSemester(semester);
        workspace.setYear(year);
        
        
        // ADD THE PAGE TITLE, INSTRUCTOR NAME AND HOME
        String pageTitle = json.getString(JSON_PAGE_TITLE);
        String instructorName = json.getString(JSON_INSTRUCTOR_NAME);
        String instructorHome = json.getString(JSON_INSTRUCTOR_HOME);
        
        dataManager.setPageTitle(pageTitle);
        dataManager.setInstructorName(instructorName);
        dataManager.setInsturctorHome(instructorHome);
        
        workspace.setTitleField(pageTitle);
        workspace.setInstructorNameField(instructorName);
        workspace.setInstructorHomeField(instructorHome);
        
        
        //ADD THE PATH TO THE IMAGES
        String bannerImagePath = json.getString("ORIGINAL_BANNER_PATH");
        String leftFooterImagePath = json.getString("ORIGINAL_LEFT_PATH");
        String rightFooterImagePath = json.getString("ORIGINAL_RIGHT_PATH");
        
//        dataManager.setBannerImageFilePath(bannerImagePath);
//        dataManager.setLeftFootImageFilePath(leftFooterImagePath);
//        dataManager.setRightFooterImageFilePath(rightFooterImagePath);
//        
//        
//        workspace.setBannerFilePath(bannerImagePath);
//        workspace.setLeftFooterFilePath(leftFooterImagePath);
//        workspace.setRightFooterFilePath(rightFooterImagePath);
//        
//        controller.handleAddBannerImage(bannerImagePath);
//        controller.handleAddLeftFooterImage(leftFooterImagePath);
//        controller.handleAddRightFooterImage(rightFooterImagePath);
        
        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = json.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            Boolean undergrad = jsonTA.getBoolean(JSON_UNDERGRADUTE);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            dataManager.addTA(undergrad,name,email);
            
            workspace.getSupervising_TA_ComboBox1().getItems().add(name);
        }

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(JSON_DAY);
            String time = jsonOfficeHours.getString(JSON_TIME);
            String name = jsonOfficeHours.getString(JSON_NAME);
            dataManager.addOfficeHoursReservation(day, time, name);
        }
        
        // NOW LOAD ALL THE SITE PAGES
        JsonArray jsonSitePageArray = json.getJsonArray(JSON_SITE_PAGE);
        for (int i = 0; i < jsonSitePageArray.size(); i++) {
            JsonObject jsonSitePage = jsonSitePageArray.getJsonObject(i);
            Boolean sitePageUse = jsonSitePage.getBoolean(JSON_USE);
            String nav_bar_title = jsonSitePage.getString(JSON_NAV_BAR_TITLE);
            String file_name = jsonSitePage.getString(JSON_FILE_NAME);
            String script = jsonSitePage.getString(JSON_SCRIPT);
            dataManager.addPage(sitePageUse, nav_bar_title, file_name, script);
        }
        String startYear = json.getString(JSON_START_YEAR);
        String startMonth = json.getString(JSON_START_MONTH);
        String startDay = json.getString(JSON_START_DAY);
        String endYear = json.getString(JSON_END_YEAR);
        String endMonth = json.getString(JSON_END_MONTH);
        String endDay = json.getString(JSON_END_DAY);
        String startDate = startYear+"-"+startMonth+"-"+startDay;
        String endDate = endYear+"-"+endMonth+"-"+endDay;
        
        dataManager.setStartMonth(startMonth);
        dataManager.setStartDay(startDay);
        dataManager.setEndMonth(endMonth);
        dataManager.setEndDay(endDay);
        
        workspace.setStartDate(startDate);
        workspace.setEndDate(endDate);
        
         // NOW LOAD ALL THE RECITATIONS
        JsonArray jsonRecRecitationArray = json.getJsonArray(JSON_REC_RECITATIONS);
        for (int i = 0; i < jsonRecRecitationArray.size(); i++) {
            JsonObject jsonRecitation = jsonRecRecitationArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            String instructor = jsonRecitation.getString(JSON_INSTUCTOR);
            String day_time = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String TA1 = jsonRecitation.getString(JSON_TA1);
            String TA2 = jsonRecitation.getString(JSON_TA2);
            dataManager.addRecitation(section, instructor, day_time, location, TA1, TA2);
            
        }
        
        
        // NOW LOAD ALL THE HOLIDAY SCHEDULE
        JsonArray jsonHolidayArray = json.getJsonArray(JSON_HOLIDAYS);
        for (int i = 0; i < jsonHolidayArray.size(); i++) {
            JsonObject jsonHoliday = jsonHolidayArray.getJsonObject(i);
            String month = jsonHoliday.getString(JSON_MONTH);
            String day = jsonHoliday.getString(JSON_DAY);
            String title = jsonHoliday.getString(JSON_TITLE);
            String link = jsonHoliday.getString(JSON_LINK);
            
            dataManager.addSchedule(JSON_HOLIDAYS, month, day, title, "", link, "","");
        }
        JsonArray jsonLectureArray = json.getJsonArray(JSON_LECTURES);
        for (int i = 0; i < jsonLectureArray.size(); i++) {
            JsonObject jsonLecture = jsonLectureArray.getJsonObject(i);
            String month = jsonLecture.getString(JSON_MONTH);
            String day = jsonLecture.getString(JSON_DAY);
            String title = jsonLecture.getString(JSON_TITLE);
            String topic = jsonLecture.getString(JSON_TOPIC);
            
            dataManager.addSchedule(JSON_LECTURES, month, day, title, topic, "", "","");
        }
        JsonArray jsonReferencesArray = json.getJsonArray(JSON_REFERENCES);
        for (int i = 0; i < jsonReferencesArray.size(); i++) {
            JsonObject jsonReferences = jsonReferencesArray.getJsonObject(i);
            String month = jsonReferences.getString(JSON_MONTH);
            String day = jsonReferences.getString(JSON_DAY);
            String title = jsonReferences.getString(JSON_TITLE);
            String link = jsonReferences.getString(JSON_LINK);
            String topic = jsonReferences.getString(JSON_TOPIC);
            
            dataManager.addSchedule(JSON_REFERENCES, month, day, title, topic, link, "","");
        }
        JsonArray jsonSchRecitationsArray = json.getJsonArray(JSON_SCH_RECITATIONS);
        for (int i = 0; i < jsonSchRecitationsArray.size(); i++) {
            JsonObject jsonSchRecitations = jsonSchRecitationsArray.getJsonObject(i);
            String month = jsonSchRecitations.getString(JSON_MONTH);
            String day = jsonSchRecitations.getString(JSON_DAY);
            String title = jsonSchRecitations.getString(JSON_TITLE);
            String topic = jsonSchRecitations.getString(JSON_TOPIC);
            
            dataManager.addSchedule(JSON_RECITATION, month, day , title, topic, "", "","");
        }
        JsonArray jsonHWsArray = json.getJsonArray(JSON_HWS);
        for (int i = 0; i < jsonHWsArray.size(); i++) {
            JsonObject jsonHws = jsonHWsArray.getJsonObject(i);
            String month = jsonHws.getString(JSON_MONTH);
            String day = jsonHws.getString(JSON_DAY);
            String title = jsonHws.getString(JSON_TITLE);
            String link = jsonHws.getString(JSON_LINK);
            String topic = jsonHws.getString(JSON_TOPIC);
            String time = jsonHws.getString(JSON_TIME);
            String criteria = jsonHws.getString(JSON_CRITERIA);
            
            dataManager.addSchedule(JSON_HWS, month, day, title, topic, link, time, criteria);
        }
         // NOW LOAD ALL THE TEAMS
        JsonArray jsonTeamArray = json.getJsonArray(JSON_TEAM);
        for (int i = 0; i < jsonTeamArray.size(); i++) {
            JsonObject jsonTeam = jsonTeamArray.getJsonObject(i);
            String name = jsonTeam.getString(JSON_NAME);
            String red = jsonTeam.getString(JSON_COLOR_RED);
            String green = jsonTeam.getString(JSON_COLOR_GREEN);
            String blue = jsonTeam.getString(JSON_COLOR_BLUE);
            String textColor = jsonTeam.getString(JSON_TEXT_COLOR);
            String link = jsonTeam.getString(JSON_LINK);
            dataManager.addTeam(name,red,green,blue,textColor,link);
            workspace.getTeamField().getItems().add(name);
            
            Color clr = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
            String color = dataManager.getColorName(clr);
            
            dataManager.setColor(color);
            
        }
        // NOW LOAD ALL THE STUDENTS
        JsonArray jsonStudentArray = json.getJsonArray(JSON_STUDENT);
        for (int i = 0; i < jsonStudentArray.size(); i++) {
            JsonObject jsonStudent = jsonStudentArray.getJsonObject(i);
            String first_name = jsonStudent.getString(JSON_FIRST_NAME);
            String last_name = jsonStudent.getString(JSON_LAST_NAME);
            String assigned_team = jsonStudent.getString(JSON_ASSIGNED_TEAM);
            String role = jsonStudent.getString(JSON_ROLE);
            dataManager.addStudent(first_name,last_name,assigned_team,role);
        }
        
        currentWorkFile = filePath;
        
         app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
    }
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    public void saveOfficeHours(AppDataComponent data, String filePath) throws IOException{
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
	ObservableList<TeachingAssistant> tas = dataManager.getTeachingAssistants();
	for (TeachingAssistant ta : tas) {	    
	    JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_UNDERGRADUTE, ta.getUndergraduate().get())
		    .add(JSON_NAME, ta.getName())
                    .add(JSON_EMAIL,ta.getEmail()).build();
                    
	    taArrayBuilder.add(taJson);
	}
	JsonArray undergradTAsArray = taArrayBuilder.build();
//
	// NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
	JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
	ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
	for (TimeSlot ts : officeHours) {	    
	    JsonObject tsJson = Json.createObjectBuilder()
		    .add(JSON_DAY, ts.getDay())
		    .add(JSON_TIME, ts.getTime())
		    .add(JSON_NAME, ts.getName()).build();
	    timeSlotArrayBuilder.add(tsJson);
	}
	JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_START_HOUR, dataManager.getStartHour())
                .add(JSON_END_HOUR, dataManager.getEndHour())
		.add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray).build();
                
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    public void saveRecitationData(AppDataComponent data, String filePath) throws IOException{
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder recitationBuilder = Json.createArrayBuilder();
        
	ObservableList<recitation> recitations = dataManager.getRecitaitons();
	for (recitation rt : recitations) {	    
	    JsonObject recitationJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, rt.getSection())
                    .add(JSON_INSTUCTOR,rt.getInstructor())
                    .add(JSON_DAY_TIME, rt.getDay_time())
                    .add(JSON_LOCATION,rt.getLocation())
                    .add(JSON_TA1, rt.getTA1())
                    .add(JSON_TA2, rt.getTA2()).build();
                    
	    recitationBuilder.add(recitationJson);
	}
	JsonArray recitationArray = recitationBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_RECITATION, recitationArray).build();
        
       Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    public void saveScheduleData(AppDataComponent data, String filePath) throws IOException{
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder holidayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecturesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsBuilder = Json.createArrayBuilder();
        
        JsonArray holidaysArray;
        JsonArray lecturesArray;
        JsonArray referencesArray;
        JsonArray recitationsArray;
        JsonArray hwsArray;
        
	ObservableList<schedule> schedules = dataManager.getSchedules();
	for (schedule sh : schedules) {	 
            if(sh.getType().equals("holidays")){
                JsonObject holidaysJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_LINK, sh.getLink()).build();
                
                holidayBuilder.add(holidaysJson);
            }
            if(sh.getType().equals("lectures")){
                JsonObject lecturesJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink()).build();
                
                lecturesBuilder.add(lecturesJson);
            }
            if(sh.getType().equals("references")){
                JsonObject referencesJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getLink())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink()).build();
                
                referencesBuilder.add(referencesJson);
            }
            if(sh.getType().equals("recitations")){
                JsonObject recitaionsJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getLink())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic()).build();
                
                recitationsBuilder.add(recitaionsJson);
            }
            if(sh.getType().equals("hws")){
                JsonObject hwsJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink())
                    .add(JSON_TIME, sh.getTime())
                    .add(JSON_CRITERIA, sh.getCriteria()).build();
                
                hwsBuilder.add(hwsJson);
            }
        }
        holidaysArray = holidayBuilder.build();
        lecturesArray = lecturesBuilder.build();
        referencesArray = referencesBuilder.build();
        recitationsArray = recitationsBuilder.build();
        hwsArray = hwsBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_MONTH, dataManager.getStartMonth())
                .add(JSON_START_DAY, dataManager.getStartDay())
                .add(JSON_END_DAY, dataManager.getEndDay())
                .add(JSON_END_MONTH, dataManager.getEndMonth())
                .add(JSON_HOLIDAYS, holidaysArray)
                .add(JSON_LECTURES, lecturesArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_RECITATIONS, recitationsArray)
                .add(JSON_HWS, hwsArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    /**
     *
     * @param data
     * @param filePath
     * @throws IOException
     */
    public void saveTeamsData(AppDataComponent data, String filePath) throws IOException{
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder teamBuilder = Json.createArrayBuilder();
        JsonArrayBuilder studentBuilder = Json.createArrayBuilder();
        
	ObservableList<team> teams = dataManager.getTeams();
	for (team tm : teams) {	    
	    JsonObject teamJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, tm.getName())
                    .add(JSON_COLOR_RED,tm.getRed())
                    .add(JSON_COLOR_GREEN,tm.getGreen())
                    .add(JSON_COLOR_BLUE,tm.getBlue())
                    .add(JSON_TEXT_COLOR, tm.getTextColor())
                    .build();
                    
	    teamBuilder.add(teamJson);
	}
	JsonArray teamArray = teamBuilder.build();
        
        ObservableList<student> students = dataManager.getStudents();
        for(student stu: students){
            JsonObject studentJson = Json.createObjectBuilder()
                    .add(JSON_LAST_NAME, stu.getLastName())
                    .add(JSON_FIRST_NAME, stu.getFirstName())
                    .add(JSON_ASSIGNED_TEAM, stu.getTeam())
                    .add(JSON_ROLE, stu.getRole()).build();
            
            studentBuilder.add(studentJson);
        }
        JsonArray studentArray = studentBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_TEAM, teamArray)
                .add(JSON_STUDENTS, studentArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    public void saveProjectData(AppDataComponent data, String filePath) throws IOException{
       TAData dataManager = (TAData)data;
        
//        JsonArrayBuilder projectBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder projectStudentBuilder = Json.createArrayBuilder();
        JsonObject studentObject = null;
        JsonObject dataManagerJSO = null;
        ArrayList<String> arraylist = new ArrayList<String>();
        ObservableList<team> teams = dataManager.getTeams();
        for(team tm : teams){
            ObservableList<student> students = dataManager.getStudents();
            for (student stu : students) {
                if(stu.getTeam().equals(tm.getName())){
                      arraylist.add("\"" + stu.getFirstName() + "\"");
                   }
                    
                }
            JsonReader jsonreader = Json.createReader(new StringReader(arraylist.toString()));
            JsonArray jo  = jsonreader.readArray();
            jsonreader.close();
            
            studentObject = Json.createObjectBuilder()
                    .add(JSON_NAME, tm.getName())
                    .add(JSON_STUDENTS, jo)
                    .add(JSON_LINK, tm.getLink()).build();
            
        }
        dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_PROJECT_SEMESTER, dataManager.getSemester()+" "+dataManager.getYear())
		.add(JSON_PROEJCTS, studentObject).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	TAData dataManager = (TAData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
	ObservableList<TeachingAssistant> tas = dataManager.getTeachingAssistants();
	for (TeachingAssistant ta : tas) {	    
	    JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_UNDERGRADUTE, ta.getUndergraduate().get())
		    .add(JSON_NAME, ta.getName())
                    .add(JSON_EMAIL,ta.getEmail()).build();
                    
	    taArrayBuilder.add(taJson);
	}
	JsonArray undergradTAsArray = taArrayBuilder.build();

	// NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
	JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
	ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
	for (TimeSlot ts : officeHours) {	    
	    JsonObject tsJson = Json.createObjectBuilder()
		    .add(JSON_DAY, ts.getDay())
		    .add(JSON_TIME, ts.getTime())
		    .add(JSON_NAME, ts.getName()).build();
	    timeSlotArrayBuilder.add(tsJson);
	}
	JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
        //FOR THE SITE PAGE TABLE
        JsonArrayBuilder sitePageBuilder = Json.createArrayBuilder();
        
	ObservableList<sitePage> pages = dataManager.getPages();
	for (sitePage st : pages) {	    
	    JsonObject pageJson = Json.createObjectBuilder()
                    .add(JSON_USE, st.returnUse().get())
                    .add(JSON_NAV_BAR_TITLE,st.getTitle())
                    .add(JSON_FILE_NAME, st.getFileName())
                    .add(JSON_SCRIPT,st.getScript()).build();
                    
	    sitePageBuilder.add(pageJson);
	}
	JsonArray sitePageArray = sitePageBuilder.build();
        
        //FOR THE RECITATIONS TABLE
        JsonArrayBuilder recitationBuilder = Json.createArrayBuilder();
        
	ObservableList<recitation> recitations = dataManager.getRecitaitons();
	for (recitation rt : recitations) {	    
	    JsonObject recitationJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, rt.getSection())
                    .add(JSON_INSTUCTOR,rt.getInstructor())
                    .add(JSON_DAY_TIME, rt.getDay_time())
                    .add(JSON_LOCATION,rt.getLocation())
                    .add(JSON_TA1, rt.getTA1())
                    .add(JSON_TA2, rt.getTA2()).build();
                    
	    recitationBuilder.add(recitationJson);
	}
	JsonArray recitationArray = recitationBuilder.build();
        
        //FOR THE SCHEDULE ITEMS TABLE
        JsonArrayBuilder holidayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecturesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder sch_recitationsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsBuilder = Json.createArrayBuilder();
        
        JsonArray holidaysArray;
        JsonArray lecturesArray;
        JsonArray referencesArray;
        JsonArray sch_recitationsArray;
        JsonArray hwsArray;
        
        
	ObservableList<schedule> schedules = dataManager.getSchedules();
	for (schedule sh : schedules) {	 
            if(sh.getType().equals("holidays")){
                JsonObject holidaysJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_LINK, sh.getLink()).build();
                
                holidayBuilder.add(holidaysJson);
            }
            if(sh.getType().equals("lectures")){
                JsonObject lecturesJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink()).build();
                
                lecturesBuilder.add(lecturesJson);
            }
            if(sh.getType().equals("references")){
                JsonObject referencesJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getLink())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink()).build();
                
                referencesBuilder.add(referencesJson);
            }
            if(sh.getType().equals("recitations")){
                JsonObject recitaionsJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getLink())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic()).build();
                
                sch_recitationsBuilder.add(recitaionsJson);
            }
            if(sh.getType().equals("hws")){
                JsonObject hwsJson = Json.createObjectBuilder()
                    .add(JSON_MONTH, sh.getMonth())
                    .add(JSON_DAY, sh.getDay())
                    .add(JSON_TITLE, sh.getTitle())
                    .add(JSON_TOPIC, sh.getTopic())
                    .add(JSON_LINK, sh.getLink())
                    .add(JSON_TIME, sh.getTime())
                    .add(JSON_CRITERIA, sh.getCriteria()).build();
                
                hwsBuilder.add(hwsJson);
            }
        }
        holidaysArray = holidayBuilder.build();
        lecturesArray = lecturesBuilder.build();
        referencesArray = referencesBuilder.build();
        sch_recitationsArray = sch_recitationsBuilder.build();
        hwsArray = hwsBuilder.build();
        
        //FOR THE TEAM TABLE
        JsonArrayBuilder teamBuilder = Json.createArrayBuilder();
        
	ObservableList<team> teams = dataManager.getTeams();
	for (team tm : teams) {	    
	    JsonObject teamJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, tm.getName())
                    .add(JSON_COLOR_RED,tm.getRed())
                    .add(JSON_COLOR_GREEN,tm.getGreen())
                    .add(JSON_COLOR_BLUE,tm.getBlue())
                    .add(JSON_TEXT_COLOR, tm.getTextColor())
                    .add(JSON_LINK,tm.getLink()).build();
                    
	    teamBuilder.add(teamJson);
	}
	JsonArray teamArray = teamBuilder.build();
        
        //FOR THE STUDENT TABLE
        JsonArrayBuilder studentBuilder = Json.createArrayBuilder();
        
	ObservableList<student> students = dataManager.getStudents();
	for (student st : students) {	    
	    JsonObject studentJson = Json.createObjectBuilder()
                    .add(JSON_FIRST_NAME, st.getFirstName())
                    .add(JSON_LAST_NAME,st.getLastName())
                    .add(JSON_ASSIGNED_TEAM, st.getTeam())
                    .add(JSON_ROLE,st.getRole()).build();
                    
	    studentBuilder.add(studentJson);
	}
	JsonArray studentArray = studentBuilder.build();
        //convert the image to proper name
        String orgBanner = dataManager.getBannerImageFilePath();
        String leftFooter = dataManager.getLeftFootImageFilePath();
        String rightFooter = dataManager.getRightFooterImageFilePath();
        if(!dataManager.getBannerImageFilePath().equals("")||!dataManager.getLeftFootImageFilePath().equals("")||!dataManager.getRightFooterImageFilePath().equals("")){
            String BannerImageName = dataManager.getBannerImageFilePath()
                        .substring(dataManager.getBannerImageFilePath().lastIndexOf("/")+1, dataManager.getBannerImageFilePath().length());
        String LeftFooterImageName = dataManager.getLeftFootImageFilePath()
                        .substring(dataManager.getLeftFootImageFilePath().lastIndexOf("/")+1, dataManager.getLeftFootImageFilePath().length());
        String RightFootImageName = dataManager.getRightFooterImageFilePath()
                        .substring(dataManager.getRightFooterImageFilePath().lastIndexOf("/")+1, dataManager.getRightFooterImageFilePath().length());
        
        String imageExtension = "./images/";
        
        dataManager.setBannerImageFilePath(imageExtension+BannerImageName);
        dataManager.setLeftFootImageFilePath(imageExtension+LeftFooterImageName);
        dataManager.setRightFooterImageFilePath(imageExtension+RightFootImageName);
        }
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_COURSE_NAME, dataManager.getCourseName())
                .add(JSON_COURSE_NUMBER, dataManager.getCourseNumber())
                .add(JSON_SEMESTER, dataManager.getSemester())
                .add(JSON_YEAR, dataManager.getYear())
                .add(JSON_PAGE_TITLE, dataManager.getPageTitle())
                .add(JSON_INSTRUCTOR_NAME, dataManager.getInstructorName())
                .add(JSON_INSTRUCTOR_HOME, dataManager.getInsturctorHome())
                .add(JSON_BANNER_IMAGE_PATH, dataManager.getBannerImageFilePath())
                .add(JSON_LEFT_FOOTER_IMAGE_PATH, dataManager.getLeftFootImageFilePath())
                .add(JSON_RIGHT_FOOTER_IMAGE_PATH, dataManager.getRightFooterImageFilePath())
                .add("ORIGINAL_BANNER_PATH", orgBanner)
                .add("ORIGINAL_LEFT_PATH", leftFooter)
                .add("ORIGINAL_RIGHT_PATH", rightFooter)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
                .add(JSON_SITE_PAGE, sitePageArray)
                .add(JSON_START_YEAR, dataManager.getStartYear())
                .add(JSON_START_MONTH, dataManager.getStartMonth())
                .add(JSON_START_DAY, dataManager.getStartDay())
                .add(JSON_END_YEAR, dataManager.getEndYear())
                .add(JSON_END_MONTH, dataManager.getEndMonth())
                .add(JSON_END_DAY, dataManager.getEndDay())
                .add(JSON_REC_RECITATIONS, recitationArray)
                .add(JSON_HOLIDAYS, holidaysArray )
                .add(JSON_LECTURES, lecturesArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_SCH_RECITATIONS, sch_recitationsArray )
                .add(JSON_HWS, hwsArray)
                .add(JSON_TEAM, teamArray)
                .add(JSON_STUDENT, studentArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
        currentWorkFile = filePath;
    }
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAData dataManager = (TAData)data;
        
	    DirectoryChooser fc = new DirectoryChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle("EXPORT");
		File selectedFile;// = fc.showDialog(app.getGUI().getWindow());
                if(dataManager.getExportDirectoryPath() != null){
                     selectedFile = new File(dataManager.getExportDirectoryPath());
                }
                else{
                    selectedFile = fc.showDialog(app.getGUI().getWindow());
                }
                //keep copy of orginal path of image to export out before changed in save Data
                String bannerImagePath = dataManager.getBannerImageFilePath();
                String leftFooterImagePath = dataManager.getLeftFootImageFilePath();
                String rightFooterImagePath = dataManager.getRightFooterImageFilePath();
                
                
                saveData(data, "../TAManagerTester/public_html/CSE219/js/AppDetailsData.json");
                saveOfficeHours(data, "../TAManagerTester/public_html/CSE219/js/OfficeHoursGridData.json");
                saveRecitationData(data, "../TAManagerTester/public_html/CSE219/js/RecitationsData.json");
                saveScheduleData(data, "../TAManagerTester/public_html/CSE219/js/ScheduleData.json");
                saveData(data, "../TAManagerTester/public_html/CSE308/js/AppDetailsData.json");
                saveOfficeHours(data, "../TAManagerTester/public_html/CSE308/js/OfficeHoursGridData.json");
                saveRecitationData(data, "../TAManagerTester/public_html/CSE308/js/RecitationsData.json");
                saveScheduleData(data, "../TAManagerTester/public_html/CSE308/js/ScheduleData.json");
                saveTeamsData(data, "../TAManagerTester/public_html/CSE308/js/TeamsAndStudents.json");
                saveProjectData(data, "../TAManagerTester/public_html/CSE308/js/ProjectsData.json");
                
                File fileName = new File("../TAManagerTester/public_html"); 
                File imagesFolder219 = new File("../TAManagerTester/public_html/CSE219/images/");
                File imagesFolder308 = new File("../TAManagerTester/public_html/CSE308/images/");
                File imageFile1 = new File(bannerImagePath);
                File imageFile2 = new File(leftFooterImagePath);
                File imageFile3 = new File(rightFooterImagePath);
                
                try{
                    FileUtils.copyFileToDirectory(imageFile1, imagesFolder219);
                    FileUtils.copyFileToDirectory(imageFile1, imagesFolder308);
                    FileUtils.copyFileToDirectory(imageFile2, imagesFolder219);
                    FileUtils.copyFileToDirectory(imageFile2, imagesFolder308);
                    FileUtils.copyFileToDirectory(imageFile3, imagesFolder219);
                    FileUtils.copyFileToDirectory(imageFile3, imagesFolder308);
                }catch(Exception e){
                    System.out.println("Image Not Found To Export");
                }
                
                
                copy(fileName.getAbsolutePath(), selectedFile.getAbsolutePath());
                
//                
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show("SUCCESS","EXPORT COMPLETE");
    }
     public void copy(String oldPath, String newPath) { 
        try { 
            (new File(newPath)).mkdirs();
            File old =new File(oldPath); 
            String[] file = old.list(); 
            File targget = null; 
            for (int i = 0; i < file.length; i++) { 
                if(oldPath.endsWith(File.separator))
                    targget = new File(oldPath+file[i]); 
                else
                    targget = new File(oldPath+File.separator+file[i]); 
                if(targget.isFile()){ 
                    FileInputStream input = new FileInputStream(targget); 
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (targget.getName()).toString()); 
                    byte[] b = new byte[1024 * 5]; 
                    int templength; 
                    while ( (templength = input.read(b)) != -1)
                        output.write(b, 0, templength); 
                    output.flush(); 
                    output.close(); 
                    input.close(); 
                } 
                if(targget.isDirectory())
                    copy(oldPath+"/"+file[i],newPath+"/"+file[i]); 
            } 
        } 
        catch (Exception e) {  
            e.printStackTrace(); 
        } 
    }
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}