/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.TAData;
import csg.workspace.CourseSiteWorkspace;

/**
 *
 * @author Neil
 */
public class OfficeHours_Grid_Trans implements jTPS_Transaction{
    CourseSiteWorkspace workspace;
    TAData data;
    public OfficeHours_Grid_Trans(CourseSiteWorkspace workspace, TAData data){
        this.workspace = workspace;
        this.data = data;
    }
    @Override
    public void doTransaction(){
        workspace.reloadOfficeHoursGrid(data);
    }
    @Override
    public void undoTransaction(){
        workspace.reloadOfficeHoursGrid(data);
    }
    
}
