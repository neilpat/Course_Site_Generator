/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.StringProperty;

/**
 *
 * @author Neil
 */
public class sitePage {
    boolean use;
    String title;
    String fileName;
    String script;
    
    public sitePage(boolean use, String title, String fileName, 
            String script ){
        this.use = use;
        this.title = title;
        this.fileName = fileName;
        this.script = script;
    }
    
    public boolean returnUse(){
        return use;
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
    
    
}