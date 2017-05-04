/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.TAData;
import csg.data.recitation;
import jtps.jTPS_Transaction;

/**
 *
 * @author Neil
 */
public class Remove_Rec_Trans implements jTPS_Transaction{
    recitation rec;
    TAData data;
    public Remove_Rec_Trans(recitation rec, TAData data) {
        this.rec = rec;
        this.data = data;
    }
    @Override
    public void doTransaction(){
        data.deleteRecitation(rec);
    }
    @Override
    public void undoTransaction(){
        data.addRecitation(rec.getSection(), rec.getInstructor(), rec.getDay_time(), rec.getLocation(), rec.getTA1(), rec.getTA2());
        rec = data.getRecitation(rec.getSection());
    }
}
