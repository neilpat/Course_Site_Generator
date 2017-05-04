/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.csgApp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.data.recitation;
import csg.workspace.CourseSiteWorkspace;

/**
 *
 * @author Neil
 */
public class Remove_TA_Trans implements jTPS_Transaction{
    csgApp app;
    TeachingAssistant ta;
    TAData data;
    HashMap<String, StringProperty> officeHours;
    ArrayList<String> keys;
    ArrayList<StringProperty> props;
    recitation rec;
    String TA1;
    String TA2;
    CourseSiteWorkspace workspace;
    
    public Remove_TA_Trans(csgApp app, TeachingAssistant ta, TAData data, ArrayList<String> keys, 
            ArrayList<StringProperty> props, recitation rec, String TA1, String TA2) {
        this.app = app;
        this.ta = ta;
        this.data = data;
        this.officeHours = data.getOfficeHours();
        this.keys = keys;
        this.props = props;
        this.rec = rec;
        this.TA1 = TA1;
        this.TA2 = TA2;
        workspace = (CourseSiteWorkspace)app.getWorkspaceComponent();
    }
    @Override
    public void doTransaction(){
        data.deleteTA(ta);
        for(int i=0;i<data.getRecitaitons().size();i++){
            if(data.getRecitaitons().get(i).getTA1().equals(ta.getName())){
                data.getRecitaitons().get(i).setTA1("TBA");
            }
            if(data.getRecitaitons().get(i).getTA2().equals(ta.getName())){
                data.getRecitaitons().get(i).setTA2("TBA");
            }
        }
        workspace.getRecitationTable().refresh();
    }
    @Override
    public void undoTransaction(){
        data.addTA(Boolean.TRUE,ta.getName(), ta.getEmail());
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
        if(rec!=null){
          for(int i=0;i<data.getRecitaitons().size();i++){
                if(data.getRecitaitons().get(i).getSection().equals(rec.getSection())){
                    data.getRecitaitons().get(i).setTA1(TA1);
                }
                if(data.getRecitaitons().get(i).getSection().equals(rec.getSection())){
                    data.getRecitaitons().get(i).setTA2(TA2);
                }
            }  
        }
        workspace.getRecitationTable().refresh();
    }   
}
