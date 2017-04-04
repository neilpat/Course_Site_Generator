/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import csg.data.TAData;

/**
 *
 * @author Neil
 */
public class Toggle_TAOfficeHours_Trans implements jTPS_Transaction{
        String cellKey;
        String name;
        TAData data;
        
    public Toggle_TAOfficeHours_Trans(String cellKey, String name, TAData data){
        this.cellKey = cellKey;
        this.name = name;
        this.data = data;
    }
    
    @Override
    public void doTransaction() {
        data.toggleTAOfficeHours(cellKey, name);
        
    }
    @Override
    public void undoTransaction() {
        data.toggleTAOfficeHours(cellKey, name);
    }
}
