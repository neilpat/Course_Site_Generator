/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.csgApp;
import csg.data.TAData;
import csg.data.student;
import csg.workspace.CourseSiteWorkspace;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author Neil
 */
public class Update_Student_Trans implements jTPS_Transaction{
    student stu;
    String orgFirstName;
    String orgLastName;
    String orgTeamName;
    String orgRole;
    String newFirstName;
    String newLastName;
    String newTeamName;
    String newRole;
    TAData data;
    csgApp app;
    public Update_Student_Trans(csgApp app,student stu, TAData data, String orgFirstName, String orgLastName,
            String orgTeamName, String orgRole) {
        this.stu = stu;
        this.data = data;
        this.orgFirstName = orgFirstName;
        this.orgLastName = orgLastName;
        this.orgTeamName = orgTeamName;
        this.orgRole = orgRole;
        this.newFirstName = stu.getFirstName();
        this.newLastName = stu.getLastName();
        this.newRole = stu.getRole();
        this.newTeamName = stu.getTeam();
        this.app = app;
    }
    @Override
    public void doTransaction() {
        data.updateStudent(stu, orgFirstName, orgLastName, orgTeamName, orgRole);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getTeamTable();
        studentTable.getSelectionModel().select(stu);
        
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        student stu = (student)selectedItem;
        studentTable.refresh();
    }
    @Override
    public void undoTransaction() {
        data.updateStudent(stu, newFirstName, newLastName, newTeamName, newRole);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getTeamTable();
        studentTable.getSelectionModel().select(stu);
        
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        student stu = (student)selectedItem;
        studentTable.refresh();
    }
}
