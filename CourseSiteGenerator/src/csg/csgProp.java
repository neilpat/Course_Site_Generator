package csg;

/**
 * This enum provides a list of all the user interface
 * text that needs to be loaded from the XML properties
 * file. By simply changing the XML file we could initialize
 * this application such that all UI controls are provided
 * in another language.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum csgProp {
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OK_PROMPT,
    CANCEL_PROMPT,
    
    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    TAS_HEADER_TEXT,
    NAME_COLUMN_TEXT,
    EMAIL_COLUMN_TEXT,
    NAME_PROMPT_TEXT,
    EMAIL_PROMPT_TEXT,
    CLEAR_BUTTON_TEXT,
    ADD_BUTTON_TEXT,
    UPDATE_BUTTON_TEXT,
    OFFICE_HOURS_SUBHEADER,
    OFFICE_HOURS_TABLE_HEADERS,
    DAYS_OF_WEEK,
    
    // THESE ARE FOR TA DETAILS TAB
    TA_DETAILS_TAB,
    MISSING_TA_NAME_TITLE,
    MISSING_TA_NAME_MESSAGE,
    MISSING_TA_EMAIL_TITLE,
    MISSING_TA_EMAIL_MESSAGE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE,
    
    //THESE ARE FOR COURSE DETAILS TAB
    COURSE_DETAILS_TAB,
    COURSE_INFO_LABEL,
    SITE_TEMPLATE_LABEL,
    PAGE_STYLE_LABEL,
    
    //THESE ARE FOR COURSE DETAILS TAB
    RECITATION_DETAILS_TAB,
    
    //THESE ARE FOR COURSE DETAILS TAB
    SCHEDULE_DETAILS_TAB,
    SCHEDULE_BOUNDARIES_LABEL,
    SCHEUDLE_ITEMS_LABEL,
    SUBJECT_LABEL,
    NUMBER_LABEL,
    SEMESTER_LABEL,
    YEAR_LABEL,
    TITLE_LABEL,
    INSTRUCTOR_NAME_LABEL,
    INSTRUCTOR_HOME_LABEL,
    EXPORT_DIRECTORY_LABEL,
    
    
    //THESE ARE FOR COURSE DETAILS TAB
    PROJECT_DETAILS_TAB
    
}
