package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Time;

@Entity
public class Schedule {
    @Id
    private String id;
    private Time mondayStart;
    private Time mondayEnd;
    private Time tuesdayStart;
    private Time tuesdayEnd;
    private Time wednesdayStart;
    private Time wednesdayEnd;
    private Time thursdayStart;
    private Time thursdayEnd;
    private Time fridayStart;
    private Time fridayEnd;
    private Time saturdayStart;
    private Time saturdayEnd;
    private Time sundayStart;
    private Time sundayEnd;


    public Schedule() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMondayStart(Time mondayStart) {
        this.mondayStart = mondayStart;
    }

    public Time getMondayStart() {
        return mondayStart;
    }

    public void setMondayEnd(Time mondayEnd) {
        this.mondayEnd = mondayEnd;
    }

    public Time getMondayEnd() {
        return mondayEnd;
    }

    public void setTuesdayStart(Time tuesdayStart) {
        this.tuesdayStart = tuesdayStart;
    }

    public Time getTuesdayStart() {
        return tuesdayStart;
    }

    public void setTuesdayEnd(Time tuesdayEnd) {
        this.tuesdayEnd = tuesdayEnd;
    }

    public Time getTuesdayEnd() {
        return tuesdayEnd;
    }

    public void setWednesdayStart(Time wednesdayStart) {
        this.wednesdayStart = wednesdayStart;
    }

    public Time getWednesdayStart() {
        return wednesdayStart;
    }

    public void setWednesdayEnd(Time wednesdayEnd) {
        this.wednesdayEnd = wednesdayEnd;
    }

    public Time getWednesdayEnd() {
        return wednesdayEnd;
    }



    public void setThursdayStart(Time thursdayStart) {
        this.thursdayStart = thursdayStart;
    }

    public Time getThursdayStart() {
        return thursdayStart;
    }

    public void setThursdayEnd(Time thursdayEnd) {
        this.thursdayEnd = thursdayEnd;
    }

    public Time getThursdayEnd() {
        return thursdayEnd;
    }

    public void setFridayStart(Time fridayStart) {
        this.fridayStart = fridayStart;
    }

    public Time getFridayStart() {
        return fridayStart;
    }

    public void setFridayEnd(Time fridayEnd) {
        this.fridayEnd = fridayEnd;
    }

    public Time getFridayEnd() {
        return fridayEnd;
    }

    public void setSaturdayStart(Time saturdayStart) {
        this.saturdayStart = saturdayStart;
    }

    public Time getSaturdayStart() {
        return saturdayStart;
    }

    public void setSaturdayEnd(Time saturdayEnd) {
        this.saturdayEnd = saturdayEnd;
    }

    public Time getSaturdayEnd() {
        return saturdayEnd;
    }

    public void setSundayStart(Time sundayStart) {
        this.sundayStart = sundayStart;
    }

    public Time getSundayStart() {
        return sundayStart;
    }

    public void setSundayEnd(Time sundayEnd) {
        this.sundayEnd = sundayEnd;
    }


}
