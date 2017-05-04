/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.csgApp;
import csg.data.TAData;
import csg.data.recitation;
import csg.workspace.CourseSiteWorkspace;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Neil
 */
public class Update_rec_Trans implements jTPS_Transaction{
    recitation rec;
    String orgSection;
    String orgInstructorName;
    String oldTA1;
    String oldTA2;
    String newSection;
    String newInstructorName;
    String newTA1;
    String newTA2;
    TAData data;
    csgApp app;
    public Update_rec_Trans(csgApp app,recitation rec, TAData data, String orgSection, String orgInstructor, String orgTA1, String orgTA2) {
        this.rec = rec;
        this.data = data;
        this.orgSection = orgSection;
        this.orgInstructorName = orgInstructor;
        this.oldTA1 = orgTA1;
        this.oldTA2 = orgTA2;
        this.newSection = rec.getSection();
        this.newInstructorName = rec.getInstructor();
        this.newTA1 = rec.getTA1();
        this.newTA2 = rec.getTA2();
        this.app = app;
    }
    @Override
    public void doTransaction() {
        data.updateRecitation(rec, newSection, newInstructorName, newTA1, newTA2);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView recTable = workspace.getRecitationTable();
        recTable.getSelectionModel().select(rec);
        
        Object selectedItem = recTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        recitation rec = (recitation)selectedItem;
        rec.setSection(newSection);
        rec.setInstructor(newInstructorName);
        rec.setTA1(newTA1);
        rec.setTA2(newTA2);
        TextField sectionTextField = workspace.getSection_textField();
        sectionTextField.setText(newSection);
        recTable.refresh();
    }
    @Override
    public void undoTransaction() {
        data.updateRecitation(rec, newSection, newInstructorName, newTA1, newTA2);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView recTable = workspace.getRecitationTable();
        recTable.getSelectionModel().select(rec);
        
        Object selectedItem = recTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        recitation rec = (recitation)selectedItem;
        rec.setSection(orgSection);
        rec.setInstructor(orgInstructorName);
        rec.setTA1(oldTA1);
        rec.setTA2(oldTA2);
        TextField sectionTextField = workspace.getSection_textField();
        sectionTextField.setText(newSection);
        recTable.refresh();
        
       
    }
    
}
