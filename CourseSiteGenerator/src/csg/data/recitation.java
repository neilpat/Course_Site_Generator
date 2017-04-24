/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

/**
 *
 * @author Neil
 */
public class recitation {
    String section;
    String instructor;
    String day_time;
    String location;
    String TA1;
    String TA2;

    public recitation(String section, String instructor, String day_time, String location, String TA1, String TA2) {
        this.section = section;
        this.instructor = instructor;
        this.day_time = day_time;
        this.location = location;
        this.TA1 = TA1;
        this.TA2 = TA2;
    }
    
    public String getSection() {
        return section;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDay_time() {
        return day_time;
    }

    public String getLocation() {
        return location;
    }

    public String getTA1() {
        return TA1;
    }

    public String getTA2() {
        return TA2;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTA1(String TA1) {
        this.TA1 = TA1;
    }

    public void setTA2(String TA2) {
        this.TA2 = TA2;
    }
    
    
}
