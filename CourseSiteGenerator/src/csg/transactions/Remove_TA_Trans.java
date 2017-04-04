/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jtps.jTPS_Transaction;
import csg.data.TAData;
import csg.data.TeachingAssistant;

/**
 *
 * @author Neil
 */
public class Remove_TA_Trans implements jTPS_Transaction{
    TeachingAssistant ta;
    TAData data;
    HashMap<String, StringProperty> officeHours;
    ArrayList<String> keys;
    ArrayList<StringProperty> props;
    public Remove_TA_Trans(TeachingAssistant ta, TAData data, ArrayList<String> keys, ArrayList<StringProperty> props) {
        this.ta = ta;
        this.data = data;
        this.officeHours = data.getOfficeHours();
        this.keys = keys;
        this.props = props;
    }
    @Override
    public void doTransaction(){
        data.deleteTA(ta);
    }
    @Override
    public void undoTransaction(){
        data.addTA(ta.getName(), ta.getEmail());
        ta = data.getTA(ta.getName(), ta.getEmail());
        Set<String> keySet = officeHours.keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        try{
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                if(keys.contains(key)){
                    StringProperty prop = officeHours.get(key);
                    //officeHours.put(key, prop);
                    data.toggleTAOfficeHours(key, ta.getName());
                }
            }
        }    catch(NullPointerException e){}
    }
    
}
