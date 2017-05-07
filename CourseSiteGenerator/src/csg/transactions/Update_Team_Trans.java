/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.csgApp;
import csg.data.TAData;
import csg.data.team;
import csg.workspace.CourseSiteWorkspace;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;


/**
 *
 * @author Neil
 */
public class Update_Team_Trans implements jTPS_Transaction{
    team tm;
    String orgName;
    String orgRed;
    String orgGreen;
    String orgBlue;
    String orgColor;
    String orgTextColor;
    String orgLink;
    String newName;
    String newRed;
    String newGreen;
    String newBlue;
    String newColor;
    String newTextColor;
    String newLink;
    TAData data;
    csgApp app;
    public Update_Team_Trans(csgApp app,team tm, TAData data, String orgName, 
            String orgRed, String orgGreen, String orgBlue, String orgColor, String orgTextColor, String orgLink) {
        this.tm = tm;
        this.data = data;
        this.orgName = orgName;
        this.orgRed = orgRed;
        this.orgGreen = orgGreen;
        this.orgBlue = orgBlue;
        this.orgTextColor = orgTextColor;
        this.orgColor = orgColor;
        this.orgLink = orgLink;
        this.app = app;
    }
    @Override
    public void doTransaction() {
        data.updateTeam(tm, orgName, orgColor, orgTextColor, orgLink);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        teamTable.getSelectionModel().select(tm);
        
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        team tm = (team)selectedItem;
        tm.setName(newName);
        tm.setColor(newColor);
        tm.setTextColor(newTextColor);
        tm.setLink(newLink);
        tm.setRed(newRed);
        tm.setGreen(newGreen);
        tm.setBlue(newBlue);
        teamTable.refresh();
    }
    @Override
    public void undoTransaction() {
        data.updateTeam(tm, newName, newColor, newTextColor, newLink);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        teamTable.getSelectionModel().select(tm);
        
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        // GET THE REC
        team tm = (team)selectedItem;
        tm.setName(orgName);
        tm.setColor(orgColor);
        tm.setTextColor(orgTextColor);
        tm.setLink(orgLink);
        tm.setRed(orgRed);
        tm.setGreen(orgGreen);
        tm.setBlue(orgBlue);
        teamTable.refresh();
    }
}
