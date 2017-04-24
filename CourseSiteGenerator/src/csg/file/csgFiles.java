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
import csg.data.courses;
import csg.data.recitation;
import csg.data.schedule;
import csg.data.semesters;
import csg.data.sitePage;
import csg.data.student;
import csg.data.team;
import java.math.BigDecimal;
import javafx.collections.FXCollections;

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
    
    static final String JSON_TEAM = "teams";
    static final String JSON_TEAM_NAME = "name";
    static final String JSON_COLOR = "color";
    static final String JSON_TEXT_COLOR = "text_color";
    static final String JSON_LINK = "link";
    
    static final String JSON_STUDENT = "students";
    static final String JSON_FIRST_NAME = "firstName";
    static final String JSON_LAST_NAME = "lastName";
    static final String JSON_ASSIGNED_TEAM = "team";
    static final String JSON_ROLE = "role";
    
    public csgFiles(csgApp initApp) {
        app = initApp;
    }

  
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	TAData dataManager = (TAData)data;

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());

        // ADD THE COURSE NAME AND NUMBER SEMESTER AND YEAR
        
        String courseName = json.getString(JSON_COURSE_NAME);
        String course_Number = json.getString(JSON_COURSE_NUMBER);
        String semester = json.getString(JSON_SEMESTER);
        String year = json.getString(JSON_YEAR);
        
        dataManager.setCourseName(courseName);
        dataManager.setCourseNumber(course_Number);
        dataManager.setSemester(semester);
        dataManager.setYear(year);
        
        String pageTitle = json.getString(JSON_PAGE_TITLE);
        String instructorName = json.getString(JSON_INSTRUCTOR_NAME);
        String instructorHome = json.getString(JSON_INSTRUCTOR_HOME);
        
        dataManager.setPageTitle(pageTitle);
        dataManager.setInstructorName(instructorName);
        dataManager.setInsturctorHome(instructorHome);
        
        String bannerImagePath = json.getString(JSON_BANNER_IMAGE_PATH);
        String leftFooterImagePath = json.getString(JSON_LEFT_FOOTER_IMAGE_PATH);
        String rightFooterImagePath = json.getString(JSON_RIGHT_FOOTER_IMAGE_PATH);
        
        dataManager.setBannerImageFilePath(bannerImagePath);
        dataManager.setLeftFootImageFilePath(leftFooterImagePath);
        dataManager.setRightFooterImageFilePath(rightFooterImagePath);
        
        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = json.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            dataManager.addTA(true,name,email);
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
        
         // NOW LOAD ALL THE RECITATIONS
        JsonArray jsonRecitationArray = json.getJsonArray(JSON_RECITATION);
        for (int i = 0; i < jsonRecitationArray.size(); i++) {
            JsonObject jsonRecitation = jsonRecitationArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            String instructor = jsonRecitation.getString(JSON_INSTUCTOR);
            String day_time = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String TA1 = jsonRecitation.getString(JSON_TA1);
            String TA2 = jsonRecitation.getString(JSON_TA2);
            dataManager.addRecitation(section, instructor, day_time, location, TA1, TA2);
        }
        
        // NOW LOAD ALL THE SCHEDULES
        JsonArray jsonScheduleArray = json.getJsonArray(JSON_SCHEDULE);
        for (int i = 0; i < jsonScheduleArray.size(); i++) {
            JsonObject jsonSchedule = jsonScheduleArray.getJsonObject(i);
            String type = jsonSchedule.getString(JSON_TYPE);
            String date = jsonSchedule.getString(JSON_DATE);
            String title = jsonSchedule.getString(JSON_TITLE);
            String topic = jsonSchedule.getString(JSON_TOPIC);
            dataManager.addSchedule(type, date, title, topic);
        }
        
         // NOW LOAD ALL THE TEAMS
        JsonArray jsonTeamArray = json.getJsonArray(JSON_TEAM);
        for (int i = 0; i < jsonTeamArray.size(); i++) {
            JsonObject jsonTeam = jsonTeamArray.getJsonObject(i);
            String name = jsonTeam.getString(JSON_NAME);
            String color = jsonTeam.getString(JSON_COLOR);
            String textColor = jsonTeam.getString(JSON_TEXT_COLOR);
            String link = jsonTeam.getString(JSON_LINK);
            dataManager.addTeam(name,color,textColor,link);
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
    
    public void saveOfficeHours(AppDataComponent data, String filePath){
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
		.add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray).build();
    }
    public void saveRecitationData(AppDataComponent data, String filePath){
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
    }
    public void saveScheduleData(AppDataComponent data, String filePath){
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder scheduleBuilder = Json.createArrayBuilder();
        
	ObservableList<schedule> schedules = dataManager.getSchedules();
	for (schedule sh : schedules) {	    
	    JsonObject scheduleJson = Json.createObjectBuilder()
                    .add("type", sh.getType())
                    .add("date",sh.getDate())
                    .add("title", sh.getTitle())
                    .add("topic",sh.getTopic()).build();
                    
	    scheduleBuilder.add(scheduleJson);
	}
	JsonArray scheduleArray = scheduleBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_SCHEDULE, scheduleArray).build();
    }
    public void saveTeamsData(AppDataComponent data, String filePath){
        TAData dataManager = (TAData)data;
        
        JsonArrayBuilder teamBuilder = Json.createArrayBuilder();
        
	ObservableList<team> teams = dataManager.getTeams();
	for (team tm : teams) {	    
	    JsonObject teamJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, tm.getName())
                    .add(JSON_COLOR,tm.getColor())
                    .add(JSON_TEXT_COLOR, tm.getTextColor())
                    .add(JSON_LINK,tm.getLink()).build();
                    
	    teamBuilder.add(teamJson);
	}
	JsonArray teamArray = teamBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_TEAM, teamArray).build();
    }
    public void saveProjectData(AppDataComponent data, String filePath){
        TAData dataManager = (TAData)data;
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
        JsonArrayBuilder scheduleBuilder = Json.createArrayBuilder();
        
	ObservableList<schedule> schedules = dataManager.getSchedules();
	for (schedule sh : schedules) {	    
	    JsonObject scheduleJson = Json.createObjectBuilder()
                    .add("type", sh.getType())
                    .add("date",sh.getDate())
                    .add("title", sh.getTitle())
                    .add("topic",sh.getTopic()).build();
                    
	    scheduleBuilder.add(scheduleJson);
	}
	JsonArray scheduleArray = scheduleBuilder.build();
        
        //FOR THE TEAM TABLE
        JsonArrayBuilder teamBuilder = Json.createArrayBuilder();
        
	ObservableList<team> teams = dataManager.getTeams();
	for (team tm : teams) {	    
	    JsonObject teamJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, tm.getName())
                    .add(JSON_COLOR,tm.getColor())
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
                    
	    teamBuilder.add(studentJson);
	}
	JsonArray studentArray = teamBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_COURSE_NAME, "" + dataManager.getCourseName())
                .add(JSON_COURSE_NUMBER, "" + dataManager.getCourseNumber())
                .add(JSON_SEMESTER, "" + dataManager.getSemester())
                .add(JSON_YEAR, "" + dataManager.getYear())
                .add(JSON_PAGE_TITLE, "" + dataManager.getPageTitle())
                .add(JSON_INSTRUCTOR_NAME, "" + dataManager.getInstructorName())
                .add(JSON_INSTRUCTOR_HOME, "" + dataManager.getInsturctorHome())
                .add(JSON_BANNER_IMAGE_PATH, "" + dataManager.getBannerImageFilePath())
                .add(JSON_LEFT_FOOTER_IMAGE_PATH, "" + dataManager.getLeftFootImageFilePath())
                .add(JSON_RIGHT_FOOTER_IMAGE_PATH, "" + dataManager.getRightFooterImageFilePath())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
                .add(JSON_SITE_PAGE, sitePageArray)
                .add(JSON_RECITATION, recitationArray)
                .add(JSON_SCHEDULE, scheduleArray)
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
    }
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}