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
public class team {
        String name;
        String color;
        String textColor;
        String link;

    public team(String name, String color, String textColor, String link) {
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getLink() {
        return link;
    }
        
        
}
