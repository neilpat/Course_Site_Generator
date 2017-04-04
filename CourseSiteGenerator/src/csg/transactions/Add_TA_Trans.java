/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.workspace.CourseSiteController;

/**
 *
 * @author Neil
 */
public class Add_TA_Trans implements jTPS_Transaction{
    TeachingAssistant ta;
    TAData data;
    public Add_TA_Trans(TeachingAssistant ta, TAData data){
        this.ta = ta;
        this.data = data;
    }
    @Override
    public void doTransaction() {
        data.addTA(ta.getName(), ta.getEmail());
        ta = data.getTA(ta.getName(), ta.getEmail());
    }
    @Override
    public void undoTransaction() {
        data.deleteTA(ta);
    }
    
}
