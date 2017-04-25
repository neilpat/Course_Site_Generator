/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.test_bed;

import csg.csgApp;
import csg.data.TAData;
import static csg.test_bed.testSave.JSON_FIRST_NAME;
import static csg.test_bed.testSave.JSON_NAME;
import static csg.test_bed.testSave.JSON_SECTION;
import static csg.test_bed.testSave.JSON_TYPE;
import static csg.test_bed.testSave.JSON_USE;
import djf.components.AppDataComponent;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil
 */
public class testSaveTest{
    csgApp app;
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
    static final String JSON_START_MONTH = "startingMondayMonth";
    static final String JSON_START_DAY = "startingMondayDay";
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
    static final String JSON_COLOR = "color";
    static final String JSON_TEXT_COLOR = "text_color";
    static final String JSON_LINK = "link";
    
    static final String JSON_STUDENT = "students";
    static final String JSON_FIRST_NAME = "firstName";
    static final String JSON_LAST_NAME = "lastName";
    static final String JSON_ASSIGNED_TEAM = "team";
    static final String JSON_ROLE = "role";
    
    public testSaveTest() {
        app = new csgApp();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of loadData method, of class testSave.
     */
    @Test
    public void testLoadDataPages() throws Exception {
        System.out.println("loadDataPages");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_SITE_PAGE);

        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject jsonSitePage = getArray.getJsonObject(i);
            //Boolean sitePageUse = jsonSitePage.getBoolean(JSON_USE);
            String nav_bar_title = jsonSitePage.getString(JSON_NAV_BAR_TITLE);
            String file_name = jsonSitePage.getString(JSON_FILE_NAME);
            String script = jsonSitePage.getString(JSON_SCRIPT);
            //assertEquals(sitePageUse, ((TAData)dataManager).getPages().get(i).returnUse().get());
            assertEquals(nav_bar_title, ((TAData)dataManager).getPages().get(i).getTitle());
            assertEquals(file_name, ((TAData)dataManager).getPages().get(i).getFileName());
            assertEquals(script, ((TAData)dataManager).getPages().get(i).getScript());
        }
    }
    @Test
    public void testLoadDataTAS() throws Exception {
        System.out.println("loadDataTAS");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_UNDERGRAD_TAS);

        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject jsonTA = getArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            assertEquals(name, ((TAData)dataManager).getTeachingAssistants().get(i).getName());
        }
    }
    @Test
    public void testLoadDataRecitations() throws Exception {
        System.out.println("loadDataRecitation");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_REC_RECITATIONS);

        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject jsonTA = getArray.getJsonObject(i);
            JsonObject jsonRecitation = getArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            String instructor = jsonRecitation.getString(JSON_INSTUCTOR);
            String day_time = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String TA1 = jsonRecitation.getString(JSON_TA1);
            String TA2 = jsonRecitation.getString(JSON_TA2);
            assertEquals(section, ((TAData)dataManager).getRecitaitons().get(i).getSection());
            assertEquals(instructor, ((TAData)dataManager).getRecitaitons().get(i).getInstructor());
            assertEquals(day_time, ((TAData)dataManager).getRecitaitons().get(i).getDay_time());
            assertEquals(location, ((TAData)dataManager).getRecitaitons().get(i).getLocation());
            assertEquals(TA1, ((TAData)dataManager).getRecitaitons().get(i).getTA1());
            assertEquals(TA2, ((TAData)dataManager).getRecitaitons().get(i).getTA2());
            
        }
    }
    @Test
    public void testLoadDataScheduleHolidays() throws Exception {
        System.out.println("loadDataScheduleHolidays");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_HOLIDAYS);

        for(int i = 0; i < getArray.size(); i++)
        {
           JsonObject jsonSchedule = getArray.getJsonObject(i);
           String month = jsonSchedule.getString(JSON_MONTH);
           String day = jsonSchedule.getString(JSON_DAY);
           String title = jsonSchedule.getString(JSON_TITLE);
           String link = jsonSchedule.getString(JSON_LINK);
            //assertEquals(month, ((TAData)dataManager).getSchedules().get(i).getDate().);
            //assertEquals(day, ((TAData)dataManager).getSchedules().get(i).getDate());
            //assertEquals(title, ((TAData)dataManager).getSchedules().get(i).getTitle());
            //assertEquals(link, ((TAData)dataManager).getSchedules().get(i).getLink());
        }
    }
    @Test
    public void testLoadDataScheduleLectures() throws Exception {
        System.out.println("loadDataScheduleLectures");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_LECTURES);

        for(int i = 0; i < getArray.size(); i++)
        {
           JsonObject jsonSchedule = getArray.getJsonObject(i);
           String month = jsonSchedule.getString(JSON_MONTH);
           String day = jsonSchedule.getString(JSON_DAY);
           String title = jsonSchedule.getString(JSON_TITLE);
           String topic = jsonSchedule.getString(JSON_TOPIC);
            //assertEquals(month, ((TAData)dataManager).getSchedules().get(i).getDate().);
            //assertEquals(day, ((TAData)dataManager).getSchedules().get(i).getDate());
            //assertEquals(title, ((TAData)dataManager).getSchedules().get(i).getTitle());
            //assertEquals(topic, ((TAData)dataManager).getSchedules().get(i).getTopic());
        }
    }
    @Test
    public void testLoadDataScheduleReferences() throws Exception {
        System.out.println("loadDataScheduleReferences");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_REFERENCES);

        for(int i = 0; i < getArray.size(); i++)
        {
           JsonObject jsonReferences = getArray.getJsonObject(i);
           String month = jsonReferences.getString(JSON_MONTH);
           String day = jsonReferences.getString(JSON_DAY);
           String title = jsonReferences.getString(JSON_TITLE);
           String topic = jsonReferences.getString(JSON_TOPIC);
           String link = jsonReferences.getString(JSON_LINK);
            //assertEquals(month, ((TAData)dataManager).getSchedules().get(i).getDate().);
            //assertEquals(day, ((TAData)dataManager).getSchedules().get(i).getDate());
            //assertEquals(title, ((TAData)dataManager).getSchedules().get(i).getTitle());
            //assertEquals(topic, ((TAData)dataManager).getSchedules().get(i).getTopic());
            //assertEquals(link, ((TAData)dataManager).getSchedules().get(i).getLink());
        }
    }
     @Test
    public void testLoadDataScheduleSchRecitations() throws Exception {
        System.out.println("loadDataScheduleSchRecitations");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_SCH_RECITATIONS);

        for(int i = 0; i < getArray.size(); i++)
        {
           JsonObject jsonRecitation = getArray.getJsonObject(i);
           String month = jsonRecitation.getString(JSON_MONTH);
           String day = jsonRecitation.getString(JSON_DAY);
           String title = jsonRecitation.getString(JSON_TITLE);
           String topic = jsonRecitation.getString(JSON_TOPIC);
            //assertEquals(title, ((TAData)dataManager).getSchedules().get(i).getTitle());
            //assertEquals(topic, ((TAData)dataManager).getSchedules().get(i).getTopic());
        }
    }
     @Test
    public void testLoadDataScheduleHws() throws Exception {
        System.out.println("loadDataScheduleHws");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_HWS);

        for(int i = 0; i < getArray.size(); i++)
        {
           JsonObject jsonSchedule = getArray.getJsonObject(i);
           String month = jsonSchedule.getString(JSON_MONTH);
           String day = jsonSchedule.getString(JSON_DAY);
           String title = jsonSchedule.getString(JSON_TITLE);
           String topic = jsonSchedule.getString(JSON_TOPIC);
           String link = jsonSchedule.getString(JSON_LINK);
           String time = jsonSchedule.getString(JSON_TIME);
           String criteria = jsonSchedule.getString(JSON_CRITERIA);
            //assertEquals(month, ((TAData)dataManager).getSchedules().get(i).getDate().);
            //assertEquals(day, ((TAData)dataManager).getSchedules().get(i).getDate());
            //assertEquals(title, ((TAData)dataManager).getSchedules().get(i).getTitle());
            //assertEquals(topic, ((TAData)dataManager).getSchedules().get(i).getTopic());
            //assertEquals(link, ((TAData)dataManager).getSchedules().get(i).getLink());
            //assertEquals(criteria, ((TAData)dataManager).getSchedules().get(i).getCriteria());
            //assertEquals(time, ((TAData)dataManager).getSchedules().get(i).getTime());
        }
    }
    @Test
    public void testLoadDataTeams() throws Exception {
        System.out.println("loadDataTeams");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_TEAM);

        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject jsonTA = getArray.getJsonObject(i);
            JsonObject jsonTeam = getArray.getJsonObject(i);
            String name = jsonTeam.getString(JSON_NAME);
            String color = jsonTeam.getString(JSON_COLOR);
            String textColor = jsonTeam.getString(JSON_TEXT_COLOR);
            String link = jsonTeam.getString(JSON_LINK);
            assertEquals(name, ((TAData)dataManager).getTeams().get(i).getName());
            assertEquals(color, ((TAData)dataManager).getTeams().get(i).getColor());
            assertEquals(textColor, ((TAData)dataManager).getTeams().get(i).getTextColor());
            assertEquals(link, ((TAData)dataManager).getTeams().get(i).getLink());
        }
    }
    @Test
    public void testLoadDataStudent() throws Exception {
        System.out.println("loadDataStudent");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        JsonArray getArray = json.getJsonArray(JSON_STUDENT);

        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject jsonStudent = getArray.getJsonObject(i);
            String first_name = jsonStudent.getString(JSON_FIRST_NAME);
            String last_name = jsonStudent.getString(JSON_LAST_NAME);
            String assigned_team = jsonStudent.getString(JSON_ASSIGNED_TEAM);
            String role = jsonStudent.getString(JSON_ROLE);
            assertEquals(first_name, ((TAData)dataManager).getStudents().get(i).getFirstName());
            assertEquals(last_name, ((TAData)dataManager).getStudents().get(i).getLastName());
            assertEquals(assigned_team, ((TAData)dataManager).getStudents().get(i).getTeam());
            assertEquals(role, ((TAData)dataManager).getStudents().get(i).getRole());
        }
    }
    @Test
    public void testLoadDataCourses() throws Exception {
        System.out.println("loadDataCourses");
        AppDataComponent dataManager = new TAData(app);
        String filePath = "/Users/Neil/Desktop/CSE_219/HW4/CourseSiteGenerator/work/csgFile.json";
        testSave ts = new testSave(app);
        ts.loadData(dataManager, filePath);
        
        InputStream is = new FileInputStream(filePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        
        String courseName = json.getString(JSON_COURSE_NAME);
        String courseNumber = json.getString(JSON_COURSE_NUMBER);
        String semester = json.getString(JSON_SEMESTER);
        String year = json.getString(JSON_YEAR);
        
        assertEquals(courseName, ((TAData)dataManager).getCourseName());
        assertEquals(courseNumber, ((TAData)dataManager).getCourseNumber());
        assertEquals(semester, ((TAData)dataManager).getSemester());
        assertEquals(year, ((TAData)dataManager).getYear());
        
        
        
    }
}
