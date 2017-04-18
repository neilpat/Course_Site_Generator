/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.math.BigInteger;

/**
 *
 * @author Neil
 */
public class schedule <E extends Comparable<E>> implements Comparable<E> {
    String type;
    String date;
    String title;
    String topic;

    public schedule(String type, String date, String title, String topic) {
        this.type = type;
        this.date = date;
        this.title = title;
        this.topic = topic;
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

    @Override
    public int compareTo(E otherDate) {
       return getDate().compareTo(((schedule)otherDate).getDate());
    }
    
    
}
