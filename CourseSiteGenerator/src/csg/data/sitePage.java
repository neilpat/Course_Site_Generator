/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Neil
 */
public class sitePage <E extends Comparable<E>> implements Comparable<E>{
    BooleanProperty use1;
    String title;
    String fileName;
    String script;
    
    public sitePage(Boolean use, String title, String fileName, 
        String script ){
        use1 = new SimpleBooleanProperty(use);
        this.title = title;
        this.fileName = fileName;
        this.script = script;
    }

    public BooleanProperty returnUse(){
        return use1;
    }
    
    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public String getScript() {
        return script;
    }

    @Override
    public int compareTo(E otherPage) {
       return getTitle().compareTo(((sitePage)otherPage).getTitle());
    }
    
    
}
