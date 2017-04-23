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
public class semesters {
    public String semester;
    public String year;

    public semesters(String semester, String year) {
        this.semester = semester;
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }
    
    
    
}
