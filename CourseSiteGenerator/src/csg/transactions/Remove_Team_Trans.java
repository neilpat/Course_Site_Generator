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
public class Remove_Team_Trans implements jTPS_Transaction{
    team tm;
    TAData data;
    public Remove_Team_Trans(team tm, TAData data) {
        this.tm = tm;
        this.data = data;
    }
    @Override
    public void doTransaction(){
        data.deleteTeam(tm);
    }
    @Override
    public void undoTransaction(){
        data.addTeam(tm.getName(), tm.getRed(), tm.getGreen(), tm.getBlue(), tm.getTextColor(), tm.getLink());
        tm = data.getTeam(tm.getName());
    }
}
