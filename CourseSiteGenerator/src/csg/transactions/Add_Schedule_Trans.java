/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;


import jtps.jTPS_Transaction;
import csg.data.TAData;
import csg.data.schedule;

/**
 *
 * @author Neil
 */
public class Add_Schedule_Trans implements jTPS_Transaction{
    schedule sch;
    TAData data;
    public Add_Schedule_Trans(schedule sch, TAData data){
        this.sch = sch;
        this.data = data;
    }
    @Override
    public void doTransaction() {
        data.addSchedule(sch.getType(), sch.getMonth(), sch.getDay(), sch.getTitle(), sch.getTopic(), 
                sch.getLink(), sch.getTime(), sch.getCriteria());
        sch = data.getSchedule(sch.getType(), sch.getDate(), sch.getTitle());
    }
    @Override
    public void undoTransaction() {
        data.deleteSchedule(sch);
    }
}
