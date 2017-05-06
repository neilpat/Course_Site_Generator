/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.TAData;
import csg.data.team;
import jtps.jTPS_Transaction;

/**
 *
 * @author Neil
 */
public class Add_Team_Trans implements jTPS_Transaction{
    team tm;
    TAData data;
    public Add_Team_Trans(team tm, TAData data){
        this.tm = tm;
        this.data = data;
    }
    @Override
    public void doTransaction() {
        data.addTeam(tm.getName(), tm.getRed(), tm.getGreen(), tm.getBlue(), tm.getTextColor(), tm.getLink());
        tm = data.getTeam(tm.getName());
    }
    @Override
    public void undoTransaction() {
        data.deleteTeam(tm);
    }
    
}
