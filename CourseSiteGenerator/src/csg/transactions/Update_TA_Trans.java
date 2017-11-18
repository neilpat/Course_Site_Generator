/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import csg.csgApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.workspace.CourseSiteWorkspace;

/**
 *
 * @author Neil
 */
public class Update_TA_Trans implements jTPS_Transaction{
    TeachingAssistant ta;
    String orgName;
    String newName;
    TAData data;
    csgApp app;
    public Update_TA_Trans(csgApp app,TeachingAssistant ta, TAData data, String orgName) {
        this.ta = ta;
        this.data = data;
        this.orgName = orgName;
        this.newName = ta.getName();
        this.app = app;
    }
    @Override
    public void doTransaction() {
        data.updateTA(ta, newName);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        taTable.getSelectionModel().select(ta);
        
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        ta.setName(newName);
        TextField nameTextField = workspace.getNameTextField();
        nameTextField.setText(newName);
        taTable.refresh();
    }
    @Override
    public void undoTransaction() {
        data.updateTA(ta, orgName);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        taTable.getSelectionModel().select(ta);
        
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        ta.setName(orgName);
        TextField nameTextField = workspace.getNameTextField();
        nameTextField.setText(orgName);
        taTable.refresh();
        
       
    }
}
