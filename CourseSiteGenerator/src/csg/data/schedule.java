/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

/**
 *
 * @author Neil
 */
public class schedule <E extends Comparable<E>> implements Comparable<E> {
    String type;
    String year = "2017";
    String month;
    String day;
    String title;
    String topic;
    String link;
    String time;
    String criteria;
    String date;

    public schedule(String type, String month, String day, String title, 
            String topic, String link, String time,String criteria) {
         if(month.startsWith("0")){
             month = month.substring(1);
         }
         if(day.startsWith("0")){
             day = day.substring(1);
         }
        this.type = type;
        this.month = month;
        this.day = day;
        this.title = title;
        this.topic = topic;
        this.link = link;
        this.time = time;
        this.criteria = criteria;
        this.date = year+"-"+getMonth()+"-"+getDay();
    }

    public String getCriteria() {
        return criteria;
    }
    public String getType() {
        return type;
    }
    public String getMonth() {
        if(month.length()==1){
            month = "0"+month;
        }
        return month;
    }
    public String getDay() {
        if(day.length()==1){
            day = "0"+day;
        }
        return day;
    }
    public String getExportMonth(){
        if(month.startsWith("0")){
            return month.substring(1);
        }
        return month;
    }
    public String getExportDay(){
        if(date.startsWith("0")){
            return day.substring(1);
        }
        return day;
    }
    public String getTitle() {
        String tempTitle = title;
        if(title.contains("<")){
            tempTitle  = title.substring(0, title.indexOf("<"));
        }
        return tempTitle;
    }
    public String getTopic() {
        String tempTopic = topic;
        if(topic.contains("<")){
            tempTopic = topic.replaceAll("\\<.*?\\>", " ");
        }
        return tempTopic;
    }
    public String getLink(){
        return link;
    }
    public String getTime() {
        return time;
    }
    public String getDate(){
        return date;
    }
    public void setYear(String year){
        this.year = year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMonth(String month) {
        if(month.length()==1){
            this.month = "0"+month;
        }   
    }
    public void setDay(String day) {
        if(day.length()==1){
            this.day = "0"+day;
        } 
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(E otherDate) {
       return getMonth().compareTo(((schedule)otherDate).getMonth());
    }
    
    
}
