/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.math.BigInteger;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;

/**
 *
 * @author Neil
 */
public class schedule <E extends Comparable<E>> implements Comparable<E> {
    String type;
    String date;
    String title;
    String topic;
    String link;
    String time;
    String criteria;

    public schedule(String type, String date, String title, String topic, String link, String time,String criteria) {
        this.type = type;
        this.date = date;
        this.title = title;
        this.topic = topic;
        this.link = link;
        this.time = time;
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }
    public String getLink(){
        return link;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int compareTo(E otherDate) {
       return getDate().compareTo(((schedule)otherDate).getDate());
    }
    
    
}
