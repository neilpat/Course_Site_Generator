/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.lang.reflect.Field;
import javafx.scene.paint.Color;

/**
 *
 * @author Neil
 */
public class team {
        String name;
        String red;
        String green;
        String blue;
        String color;
        String textColor;
        String link;
        
        int sizeOfHB = 8;
        int NoInHB = 4;
        int halfByte = 0x0F;
        char[] hexDigits = { 
            '0', '1', '2', '3', '4', '5', '6', '7', 
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
        
    public team(String name, String red, String green, String blue, String textColor, String link) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.textColor = textColor;
        this.link = link;
        Color clr = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        color = getColorName(clr);
    }

    public String getName() {
        return name;
    }

    public String getRed() {
        return red;
    }

    public String getGreen() {
        return green;
    }

    public String getBlue() {
        return blue;
    }

    public String getColor(){
        return color;
    }
    public String getTextColor() {
        return textColor;
    }

    public String getLink() {
        return link;
    }
    public String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfHB);
        hexBuilder.setLength(sizeOfHB);
        for (int i = sizeOfHB - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= NoInHB;
        }
        return hexBuilder.toString(); 
    } 
    public static String getColorName(Color c) {
        for (Field f : Color.class.getFields()) {
            try {
                if (f.getType() == Color.class && f.get(null).equals(c)) {
                    return f.getName().toLowerCase();
                }
            } catch (java.lang.IllegalAccessException e) {
            // it should never get to here
            } 
        }
        return "white";
    }

    public void setRed(String red) {
        this.red = red;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGreen(String green) {
        this.green = green;
    }
    
}
