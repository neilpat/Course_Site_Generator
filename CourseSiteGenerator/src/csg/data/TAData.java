package csg.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import csg.csgApp;
import csg.csgProp;
import csg.transactions.Add_TA_Trans;
import csg.transactions.OfficeHours_Grid_Trans;
import csg.workspace.CourseSiteWorkspace;
import java.lang.reflect.Field;
import javafx.scene.paint.Color;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class TAData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    csgApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;
    ObservableList<courses> courses;
    ObservableList<semesters> semesters;
    ObservableList<sitePage> pages;
    ObservableList<recitation> recitaitons;
    ObservableList<schedule> schedules;
    ObservableList<team> teams;
    ObservableList<student> students;
    ObservableList<projects> projects;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 0;
    public static final int MAX_END_HOUR = 23;
    public static final int MIN_START_YEAR = 2017;
    public static final int MIN_START_MONTH = 01;
    public static final int MIN_START_DAY = 01;
    public static final int MIN_END_YEAR = 2020;
    public static final int MIN_END_MONTH = 12;
    public static final int MIN_END_DAY = 26;
    
    public String courseName = "";
    public String courseNumber = "";
    public String semester = "";
    public String year = "";
    
    public String pageTitle = "";
    public String instructorName = "";
    public String insturctorHome = "";
    
    public String bannerImageFilePath = "" ;
    public String leftFootImageFilePath = "";
    public String rightFooterImageFilePath = "";
    public String orgbannerImageFilePath = "" ;
    public String orgleftFootImageFilePath = "";
    public String orgrightFooterImageFilePath = "";
    
    public String exportDirectoryPath;
    
    public String type_textField;
    public String time_textField;
    public String plannedDate;
    public String title_textField;
    public String topic_textField;
    public String link_textField;
    public String criteria_textField;
    
    public String teamNameField;
    public String color;
    public String textColor;
    
    public String startYear = MIN_START_YEAR+"";
    public String startMonth = MIN_START_MONTH+"";
    public String startDay = MIN_START_DAY+"";;
    public String endYear = MIN_END_YEAR+"";;
    public String endMonth = MIN_END_MONTH+"";;
    public String endDay = MIN_END_DAY+"";;
    
    public String studentFirstName;
    public String studentLastName;
    public String studentTeamName;
    public String studentRole;
    public String TeamLink;
    
    private int count = 0;
    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public TAData(csgApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();
        courses = FXCollections.observableArrayList();
        semesters = FXCollections.observableArrayList();
        pages = FXCollections.observableArrayList();
        recitaitons = FXCollections.observableArrayList();
        schedules = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
        projects = FXCollections.observableArrayList();
        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();
        
//         //THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> timeHeaders = props.getPropertyOptionsList(csgProp.OFFICE_HOURS_TABLE_HEADERS);
//        ArrayList<String> dowHeaders = props.getPropertyOptionsList(csgProp.DAYS_OF_WEEK);
//        gridHeaders = new ArrayList();
//        gridHeaders.addAll(timeHeaders);
//        gridHeaders.addAll(dowHeaders);
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        startYear = MIN_START_YEAR+"";
        startMonth = MIN_START_MONTH+"";
        startDay =MIN_START_DAY+"";
        endYear = MIN_END_YEAR+"";
        endMonth = MIN_END_MONTH+"";
        endDay = MIN_END_DAY+"";
        
        teachingAssistants.clear();
        officeHours.clear();
        courses.clear();
        pages.clear();
        recitaitons.clear();
        schedules.clear();
        students.clear();
        teams.clear();
        
        sitePage Home = new sitePage(true, "Home", "index.html", "HomeBuilder.js");
        sitePage Syllabus = new sitePage(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
        sitePage Schedule = new sitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js");
        sitePage HWs = new sitePage(true, "HWs", "hws.html", "HWsBuilder.js");
        sitePage Projects = new sitePage(false, "Projects", "projects.html", "ProjectsBuilder.js");
        
        addSitePage(Home);
        addSitePage(Syllabus);
        addSitePage(Schedule);
        addSitePage(HWs);
        addSitePage(Projects);
    }
    public  void buildGridHeaders(){
          //THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(csgProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(csgProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }

    public ObservableList<projects> getProjects() {
        return projects;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getStartYear() {
        return startYear;
    }
    
    public String getTeamNameField() {
        return teamNameField;
    }

    public String getColor() {
        return color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTeamNameField(String teamNameField) {
        this.teamNameField = teamNameField;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }

    public void setType_textField(String type_textField) {
        this.type_textField = type_textField;
    }

    public void setTime_textField(String time_textField) {
        this.time_textField = time_textField;
    }

    public void setTitle_textField(String title_textField) {
        this.title_textField = title_textField;
    }

    public void setTopic_textField(String topic_textField) {
        this.topic_textField = topic_textField;
    }

    public void setLink_textField(String link_textField) {
        this.link_textField = link_textField;
    }

    public void setCriteria_textField(String criteria_textField) {
        this.criteria_textField = criteria_textField;
    }

    public String getType_textField() {
        return type_textField;
    }

    public String getTime_textField() {
        return time_textField;
    }

    public String getTitle_textField() {
        return title_textField;
    }

    public String getTopic_textField() {
        return topic_textField;
    }

    public String getLink_textField() {
        return link_textField;
    }

    public String getCriteria_textField() {
        return criteria_textField;
    }

    public void setExportDirectoryPath(String exportDirectoryPath) {
        this.exportDirectoryPath = exportDirectoryPath;
    }

    public String getExportDirectoryPath() {
        return exportDirectoryPath;
    }
    
    public String getBannerImageFilePath() {
        return bannerImageFilePath;
    }

    public String getLeftFootImageFilePath() {
        return leftFootImageFilePath;
    }

    // ACCESSOR METHODS
    public String getRightFooterImageFilePath() {
        return rightFooterImageFilePath;
    }

    public void setBannerImageFilePath(String bannerImageFilePath) {
        this.bannerImageFilePath = bannerImageFilePath;
    }

    public void setLeftFootImageFilePath(String leftFootImageFilePath) {
        this.leftFootImageFilePath = leftFootImageFilePath;
    }

    public void setRightFooterImageFilePath(String rightFooterImageFilePath) {
        this.rightFooterImageFilePath = rightFooterImageFilePath;
    }

    public void setOrgbannerImageFilePath(String orgbannerImageFilePath) {
        this.orgbannerImageFilePath = orgbannerImageFilePath;
    }

    public void setOrgleftFootImageFilePath(String orgleftFootImageFilePath) {
        this.orgleftFootImageFilePath = orgleftFootImageFilePath;
    }

    public void setOrgrightFooterImageFilePath(String orgrightFooterImageFilePath) {
        this.orgrightFooterImageFilePath = orgrightFooterImageFilePath;
    }

    public String getOrgbannerImageFilePath() {
        return orgbannerImageFilePath;
    }

    public String getOrgleftFootImageFilePath() {
        return orgleftFootImageFilePath;
    }

    public String getOrgrightFooterImageFilePath() {
        return orgrightFooterImageFilePath;
    }

    
    public int getStartHour() {
        return startHour;
    }
    public void setStartHour(int start_hour){
        this.startHour = start_hour;
    }
    public void setEndHour(int end_hour){
        this.endHour = end_hour;
    }

    public int getEndHour() {
        return endHour;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getInsturctorHome() {
        return insturctorHome;
    }

    public void setPageTitle(String title) {
        this.pageTitle = title;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInsturctorHome(String insturctorHome) {
        this.insturctorHome = insturctorHome;
    }

    public String getTeamLink() {
        return TeamLink;
    }

    public void setTeamLink(String TeamLink) {
        this.TeamLink = TeamLink;
    }
    
    
    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList<TeachingAssistant> getTeachingAssistants() {
        return teachingAssistants;
    }
    
    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }
    
    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    public String getCellKey(String day, String time) {
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
        if (hour < startHour)
            milHour += 12;
        row += (milHour - startHour) * 2;
        if (time.contains("_30"))
            row += 1;
        return getCellKey(col, row);
    }
    
    public TeachingAssistant getTA(String testName, String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
            else if (ta.getEmail().equals(testEmail)) {
                return ta;
            }
        }
        return null;
    }
    
    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }    
    
    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
                                int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }
    
    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        CourseSiteWorkspace workspaceComponent = (CourseSiteWorkspace)app.getWorkspaceComponent();
        count++;            //the first 3 grid changes need to be ignored
        
        int orgStart = startHour;
        int orgEnd = endHour;
        
        startHour = initStartHour;
        endHour = initEndHour;
        
        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();
            
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
//        if((orgStart != 0 || orgEnd != 23) && count>3){
//            jTPS_Transaction trans = new OfficeHours_Grid_Trans(workspaceComponent, this);
//            workspaceComponent.getJTPS().addTransaction(trans);
//        }
        workspaceComponent = (CourseSiteWorkspace)app.getWorkspaceComponent();
        workspaceComponent.reloadOfficeHoursGrid(this); 
        
    }
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= MIN_START_HOUR)
                && (initEndHour <= MAX_END_HOUR)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    public boolean containsTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
        }
        return false;
    }
    public void addTA_trans(Boolean value, String initName, String initEmail){
        TeachingAssistant ta = new TeachingAssistant(value, initName, initEmail);
        if(!containsTA(initName) && !containsTA(initEmail)){
           teachingAssistants.add(ta);
            jTPS_Transaction trans = new Add_TA_Trans(ta, this);
            CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
            workspace.getJTPS().addTransaction(trans); 
        }
        //Collections.sort(teachingAssistants);

    }
    public void addTA(Boolean value, String initName, String initEmail) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(value, initName, initEmail);
        
        // ADD THE TA
        if (!containsTA(initName) && !containsTA(initEmail)) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        //Collections.sort(teachingAssistants);
        
    }
    
    /**
     *
     * @param day
     * @param time
     * @param taName
     */
    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }
    
    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     * @param cellKey
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        
        StringProperty cellProp = officeHours.get(cellKey);
        String cellVal = cellProp.getValue();
        String cellText = cellProp.getValue();
        if(cellText.equals("\n")){
            cellText = "";
        }
        if(cellVal.equals("\n")){
            cellVal = "";
        }
        if(cellVal.contains(taName)){
            removeTAFromCell(cellProp,"\n"+taName);
        }
        else{
            String replace = cellText +"\n"+taName;
            replace.replace("\n\n","");
            cellProp.setValue(replace);
        }
    }
    public void toggleTAOfficeHours_2(String cellKey, String names) {
        StringProperty cellProp = officeHours.get(cellKey);
        //String cellText = cellProp.getValue();
        cellProp.setValue(names);
    }
    
    /**
     * This method removes taName from the office grid cell
     * represented by cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();    
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            cellText = cellText.substring(1);
            int startIndex = cellText.indexOf("\n");
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            int startIndex = cellText.indexOf(taName);
            int startIndex2 = startIndex+taName.length();
            String cellText2 = cellText.substring(startIndex2);
            cellText = cellText.substring(0, startIndex);
            cellText = cellText.concat(cellText2);
            cellProp.setValue(cellText);
        }
    }
    public void deleteTA(TeachingAssistant TA){
        teachingAssistants.remove(TA); 
        Set<String> keySet = officeHours.keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        try{
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                if(officeHours.get(key).getValue().contains(TA.getName())){
                    StringProperty prop = officeHours.get(key);
                    removeTAFromCell(prop, TA.getName());
                } 
            }
        }    catch(NullPointerException e){}
    }
    public void updateTA(TeachingAssistant TA,String name){
        
        Set<String> keySet = officeHours.keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        try{
            while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            String temp = officeHours.get(key).getValue();

            if(temp.contains(TA.getName())){
               StringProperty prop = officeHours.get(key);
               String newName  = prop.getValue();
               newName = newName.replace(TA.getName(), name);
               prop.setValue(newName);
            } 
            }
        }    catch(NullPointerException e){}
    }
    
    public void addSemester(String initName, String initYear) {
        // MAKE THE COURSE
        semesters sem = new semesters(initName, initYear);
        
        // ADD THE COURSE
        if (!containsSemester(initName, initYear)) {
            semesters.add(sem);
        }
    }
    public boolean containsSemester(String testName, String testYear) {
        for (semesters sem : semesters) {
            if (sem.getSemester().equals(testName) && sem.getYear().equals(testYear)) {
                return true;
            }
        }
        return false;
    }
    public ObservableList<semesters> getSemesters() {
        return semesters;
    }
    
    public void addCourse(String initName, String initNumber) {
        // MAKE THE COURSE
        courses course = new courses(initName, initNumber);
        
        // ADD THE COURSE
        if (!containsCourse(initName, initNumber)) {
            courses.add(course);
        }

        
    }
    public boolean containsCourse(String testName, String testNumber) {
        for (courses course : courses) {
            if (course.getName().equals(testName) && course.getNumber().equals(testNumber)) {
                return true;
            }
        }
        return false;
    }
    public ObservableList<courses> getCourses() {
        return courses;
    }

    public ObservableList<sitePage> getPages() {
        return pages;
    }
    
    public void addPage(Boolean use, String NavBarTitle, String FileName, String Script){
        sitePage page = new sitePage(use, NavBarTitle, FileName, Script);
        
        // ADD THE COURSE
        if (!containsPage(NavBarTitle)) {
            pages.add(page);
        }

        // SORT THE TAS
        //Collections.sort(pages);
    }
    public boolean containsPage(String testName) {
        for (sitePage page: pages) {
            if (page.getTitle().equals(testName)) {
                return true;
            }
        }
        return false;
    }
    
    public void addRecitation(String section, String instructor, String day_time, 
            String location, String TA1, String TA2){
            recitation rec = new recitation(section,instructor,day_time,location,TA1,TA2);
        
        // ADD THE COURSE
        if (!containsRecitation(section)) {
            recitaitons.add(rec);
        }

        // SORT THE TAS
        //Collections.sort(pages);
    }
    public void updateRecitation(recitation rec, String section, String instructor, String TA1, String TA2){
        rec.setSection(section);
        rec.setInstructor(instructor);
        rec.setTA1(TA1);
        rec.setTA2(TA2);
    }
    public void deleteRecitation(recitation rec){
        recitaitons.remove(rec);
    }
    public boolean containsRecitation(String testName) {
        for (recitation rec: recitaitons) {
            if (rec.getSection().equals(testName)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<recitation> getRecitaitons() {
        return recitaitons;
    }
    
    public void addSchedule(String type, String month, String day, String title, String topic, String link, String time,String criteria){
        schedule sch = new schedule(type, month,day, title, topic, link, time,criteria);
        
        // ADD THE COURSE
       if(!containsSchedule(month, day)){
            schedules.add(sch);
        }

        // SORT THE TAS
        //Collections.sort(schedules);
    }
    public boolean containsSchedule(String month, String day) {
        for (schedule sch: schedules) {
            if (sch.getMonth().equals(month) && sch.getDay().equals(day)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<schedule> getSchedules() {
        return schedules;
    }
    
    public void addTeam(String name, String red, String green, String blue, String textColor, String link){
        team tm = new team(name, red, green, blue, textColor, link);
        
        // ADD THE COURSE
       if(!containsTeam(name)){
            teams.add(tm);
        }

        // SORT THE TAS
        //Collections.sort(schedules);
    }
    public boolean containsTeam(String name) {
        for (team tm: teams) {
            if (tm.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<team> getTeams() {
        return teams;
    }
    public void addStudent(String firstName, String lastName, String assignedTeam, String role){
       student stu = new student(firstName, lastName, assignedTeam, role);
        
        // ADD THE COURSE
       if(!containsStudent(firstName, lastName)){
            students.add(stu);
        }
        //Collections.sort(students);
    }
    public boolean containsStudent(String firstName, String lastName) {
        for (student stu: students) {
            if (stu.getFirstName().equals(firstName) && stu.getLastName().equals(lastName)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<student> getStudents() {
        return students;
    }
    public void removeStudent(student stu){
        students.remove(stu);
    }
    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public void setStudentTeamName(String studentTeamName) {
        this.studentTeamName = studentTeamName;
    }

    public void setStudentRole(String studentRole) {
        this.studentRole = studentRole;
    }
    public String getColorName(Color c) {
    for (Field f : Color.class.getFields()) {
        try {
            if (f.getType() == Color.class && f.get(null).equals(c)) {
                return f.getName().toLowerCase();
            }
        } catch (java.lang.IllegalAccessException e) {
            // it should never get to here
        } 
    }
    return "white";
    }
    
    public recitation getRecitation(String section){
        for (recitation rec : recitaitons) {
            if (rec.getSection().equals(section)) {
                return rec;
            }
        }
        return null;
    }
    public schedule getSchedule(String type, String date, String title){
        for (schedule sch: schedules) {
            if (sch.getType().equals(type) && sch.getDate().equals(date) && sch.getTitle().equals(title)) {
                return sch;
            }
        }
        return null;
    }
    public void deleteSchedule(schedule sch){
        schedules.remove(sch);
    }
    public void deleteTeam(team tm){
        teams.remove(tm);
    }
    public team getTeam(String name){
        for(team tm: teams){
            if(tm.getName().equals(name)){
                return tm;
            }
        }
        return null;
    }
    public student getStudent(String firstName, String lastName, String team){
        for(student stu: students){
            if(stu.getFirstName().equals(firstName) && stu.getLastName().equals(lastName) && stu.getTeam().equals(team)){
                return stu;
            }
        }
        return null;
    }
    public void deleteStudent(student stu){
        students.remove(stu);
    }
    public void updateTeam(team tm, String name, String color, String textColor, String link){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        tm.setName(name);
        tm.setColor(color);
        tm.setTextColor(textColor);
        tm.setLink(link);
        Color clr = Color.web(color);
        String red = workspace.hexTodecimal(clr.toString().substring(2,4))+"";
        String green= workspace.hexTodecimal(clr.toString().substring(4,6))+"";
        String blue= workspace.hexTodecimal(clr.toString().substring(6,8))+"";
        tm.setRed(red);
        tm.setGreen(green);
        tm.setBlue(blue);
    }
    public void updateStudent(student stu, String firstName, String lastName, String teamName, String role){
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        stu.setFirstName(firstName);
        stu.setLastName(lastName);
        stu.setTeam(teamName);
        stu.setRole(role);
    }
    public void addSite(Boolean use, String title, String fileName, String script){
       sitePage site = new sitePage(use, title, fileName, script);
       pages.add(site);
    }
    public void addSitePage(sitePage page){
        pages.add(page);
    }
}