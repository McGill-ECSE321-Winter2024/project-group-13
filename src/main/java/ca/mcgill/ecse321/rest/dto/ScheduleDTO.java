package ca.mcgill.ecse321.rest.dto;

import java.sql.Time;
import ca.mcgill.ecse321.rest.models.Schedule;

public class ScheduleDTO {

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

    public ScheduleDTO() {}

    // Constructor that accepts a Schedule object
    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.mondayStart = schedule.getMondayStart();
        this.mondayEnd = schedule.getMondayEnd();
        this.tuesdayStart = schedule.getTuesdayStart();
        this.tuesdayEnd = schedule.getTuesdayEnd();
        this.wednesdayStart = schedule.getWednesdayStart();
        this.wednesdayEnd = schedule.getWednesdayEnd();
        this.thursdayStart = schedule.getThursdayStart();
        this.thursdayEnd = schedule.getThursdayEnd();
        this.fridayStart = schedule.getFridayStart();
        this.fridayEnd = schedule.getFridayEnd();
        this.saturdayStart = schedule.getSaturdayStart();
        this.saturdayEnd = schedule.getSaturdayEnd();
        this.sundayStart = schedule.getSundayStart();
        this.sundayEnd = schedule.getSundayEnd();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Time getMondayStart() {
        return mondayStart;
    }

    public void setMondayStart(Time mondayStart) {
        this.mondayStart = mondayStart;
    }

    public Time getMondayEnd() {
        return mondayEnd;
    }

    public void setMondayEnd(Time mondayEnd) {
        this.mondayEnd = mondayEnd;
    }

    public Time getTuesdayStart() {
        return tuesdayStart;
    }

    public void setTuesdayStart(Time tuesdayStart) {
        this.tuesdayStart = tuesdayStart;
    }

    public Time getTuesdayEnd() {
        return tuesdayEnd;
    }

    public void setTuesdayEnd(Time tuesdayEnd) {
        this.tuesdayEnd = tuesdayEnd;
    }

    public Time getWednesdayStart() {
        return wednesdayStart;
    }

    public void setWednesdayStart(Time wednesdayStart) {
        this.wednesdayStart = wednesdayStart;
    }

    public Time getWednesdayEnd() {
        return wednesdayEnd;
    }

    public void setWednesdayEnd(Time wednesdayEnd) {
        this.wednesdayEnd = wednesdayEnd;
    }

    public Time getThursdayStart() {
        return thursdayStart;
    }

    public void setThursdayStart(Time thursdayStart) {
        this.thursdayStart = thursdayStart;
    }

    public Time getThursdayEnd() {
        return thursdayEnd;
    }

    public void setThursdayEnd(Time thursdayEnd) {
        this.thursdayEnd = thursdayEnd;
    }

    public Time getFridayStart() {
        return fridayStart;
    }

    public void setFridayStart(Time fridayStart) {
        this.fridayStart = fridayStart;
    }

    public Time getFridayEnd() {
        return fridayEnd;
    }

    public void setFridayEnd(Time fridayEnd) {
        this.fridayEnd = fridayEnd;
    }

    public Time getSaturdayStart() {
        return saturdayStart;
    }

    public void setSaturdayStart(Time saturdayStart) {
        this.saturdayStart = saturdayStart;
    }

    public Time getSaturdayEnd() {
        return saturdayEnd;
    }

    public void setSaturdayEnd(Time saturdayEnd) {
        this.saturdayEnd = saturdayEnd;
    }

    public Time getSundayStart() {
        return sundayStart;
    }

    public void setSundayStart(Time sundayStart) {
        this.sundayStart = sundayStart;
    }

    public Time getSundayEnd() {
        return sundayEnd;
    }

    public void setSundayEnd(Time sundayEnd) {
        this.sundayEnd = sundayEnd;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "id='" + id + '\'' +
                ", mondayStart=" + mondayStart +
                ", mondayEnd=" + mondayEnd +
                ", tuesdayStart=" + tuesdayStart +
                ", tuesdayEnd=" + tuesdayEnd +
                ", wednesdayStart=" + wednesdayStart +
                ", wednesdayEnd=" + wednesdayEnd +
                ", thursdayStart=" + thursdayStart +
                ", thursdayEnd=" + thursdayEnd +
                ", fridayStart=" + fridayStart +
                ", fridayEnd=" + fridayEnd +
                ", saturdayStart=" + saturdayStart +
                ", saturdayEnd=" + saturdayEnd +
                ", sundayStart=" + sundayStart +
                ", sundayEnd=" + sundayEnd +
                '}';
    }
}

