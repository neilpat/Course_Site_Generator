/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;
/**
 * @author Neil
 */
public class projects {
    public String name;
    public String[] students;
    public String link;

    public projects(String name, String[] students, String link) {
        this.name = name;
        this.students = students;
        this.link = link;
    }
    public String getName() {
        return name;
    }
    public String[] getStudents() {
        return students;
    }
    public String getLink() {
        return link;
    }  
}