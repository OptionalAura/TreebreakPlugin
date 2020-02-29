/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.treebreaker.plugin.utils;

/**
 *
 * @author Daniel Allen
 */
public class Time {
    private final long originalInputInMillis;
    private final long hours,
            minutes,
            seconds,
            milliseconds;
    public Time(long milliseconds){
        this.originalInputInMillis = milliseconds;
        double millis = milliseconds;
        double msToH = 3600000.;
        double msToM = 60000.;
        double msToS = 1000.;
        //get hours by flooring ms/3600000
        double hours = (long)(millis/msToH);
        //get remaining time by subtracting the floor of ms/3600000 from ms/3600000 and multiplying by 3600000
        double remaining = (long) (((millis/msToH)-hours) * msToH);
        //get minutes by flooring remaining/60000
        double minutes = (long)(remaining/msToM);
        //get remaining time by subtracting the floor of remaining/60000 from remaining/60000
        remaining = (long) (((remaining/msToM)-minutes) * msToM);
        //get seconds by flooring remaining/1000
        double seconds = (long)(remaining/msToS);
        //get remaining time by subtracting the floor of remaining/1000 from remaining/1000
        this.milliseconds = (long) (((remaining/msToS)-seconds) * msToS);
        //milliseconds is what is remaining
        this.seconds = (long)seconds;
        this.minutes = (long)minutes;
        this.hours = (long)hours;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }
    
    private boolean shouldPrintHours = true,
            shouldPrintMinutes = true,
            shouldPrintSeconds = true,
            shouldPrintMillis = true;
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        boolean shouldHaveComma = false;
        if(this.hours != 0 && shouldPrintHours){
            if(this.hours == 1)
                sb.append(this.hours).append(" hour");
            else
                sb.append(this.hours).append(" hours");
            shouldHaveComma = true;
        }
        if(this.minutes != 0 && shouldPrintMinutes){
            if(shouldHaveComma){
                sb.append(',').append(' ');
            }
            if(this.minutes == 1)
                sb.append(this.minutes).append(" minute");
            else
                sb.append(this.minutes).append(" minutes");
            shouldHaveComma = true;
        }
        if(this.seconds != 0 && shouldPrintSeconds){
            if(shouldHaveComma){
                sb.append(',').append(' ');
            }
            if(this.seconds == 1)
                sb.append(this.seconds).append(" second");
            else
                sb.append(this.seconds).append(" seconds");
            shouldHaveComma = true;
        }
        if(this.milliseconds != 0 && shouldPrintMillis){
            if(shouldHaveComma){
                sb.append(',').append(' ');
            }
            if(this.milliseconds == 1)
                sb.append(this.milliseconds).append(" millisecond");
            else
                sb.append(this.milliseconds).append(" milliseconds");
        }
        
        return sb.toString();
    }

    public void setShouldPrintHours(boolean shouldPrintHours) {
        this.shouldPrintHours = shouldPrintHours;
    }

    public void setShouldPrintMinutes(boolean shouldPrintMinutes) {
        this.shouldPrintMinutes = shouldPrintMinutes;
    }

    public void setShouldPrintSeconds(boolean shouldPrintSeconds) {
        this.shouldPrintSeconds = shouldPrintSeconds;
    }

    public void setShouldPrintMilliseconds(boolean shouldPrintMilliseconds) {
        this.shouldPrintMillis = shouldPrintMilliseconds;
    }
}
