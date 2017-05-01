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
public class student<E extends Comparable<E>> implements Comparable<E> {
    String firstName;
    String lastName;
    String team;
    String role;

    public student(String firstName, String lastName, String team, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTeam() {
        return team;
    }

    public String getRole() {
        return role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int compareTo(E otherStudent) {
        return getFirstName().compareTo(((student)otherStudent).getFirstName());
    }
    
    
    
}
