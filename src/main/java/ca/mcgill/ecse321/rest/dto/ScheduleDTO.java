package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Schedule;

import java.sql.Time;

public class ScheduleDTO {
    private String id;
    private String mondayStart;
    private String mondayEnd;
    private String tuesdayStart;
    private String tuesdayEnd;
    private String wednesdayStart;
    private String wednesdayEnd;
    private String thursdayStart;
    private String thursdayEnd;
    private String fridayStart;
    private String fridayEnd;
    private String saturdayStart;
    private String saturdayEnd;
    private String sundayStart;
    private String sundayEnd;


    public ScheduleDTO(Schedule schedule) {
        if (schedule != null) {
            this.id = schedule.getId();
            this.mondayStart = schedule.getMondayStart().toString();
            this.mondayEnd = schedule.getMondayEnd().toString();
            this.tuesdayStart = schedule.getTuesdayStart().toString();
            this.tuesdayEnd = schedule.getTuesdayEnd().toString();
            this.wednesdayStart = schedule.getWednesdayStart().toString();
            this.wednesdayEnd = schedule.getWednesdayEnd().toString();
            this.thursdayStart = schedule.getThursdayStart().toString();
            this.thursdayEnd = schedule.getThursdayEnd().toString();
            this.fridayStart = schedule.getFridayStart().toString();
            this.fridayEnd = schedule.getFridayEnd().toString();
            this.saturdayStart = schedule.getSaturdayStart().toString();
            this.saturdayEnd = schedule.getSaturdayEnd().toString();
            this.sundayStart = schedule.getSundayStart().toString();
            this.sundayEnd = schedule.getSundayEnd().toString();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMondayStart() {
        return mondayStart;
    }

    public void setMondayStart(String mondayStart) {
        this.mondayStart = mondayStart;
    }

    public String getMondayEnd() {
        return mondayEnd;
    }

    public void setMondayEnd(String mondayEnd) {
        this.mondayEnd = mondayEnd;
    }

    public String getTuesdayStart() {
        return tuesdayStart;
    }

    public void setTuesdayStart(String tuesdayStart) {
        this.tuesdayStart = tuesdayStart;
    }

    public String getTuesdayEnd() {
        return tuesdayEnd;
    }

    public void setTuesdayEnd(String tuesdayEnd) {
        this.tuesdayEnd = tuesdayEnd;
    }

    public String getWednesdayStart() {
        return wednesdayStart;
    }

    public void setWednesdayStart(String wednesdayStart) {
        this.wednesdayStart = wednesdayStart;
    }

    public String getWednesdayEnd() {
        return wednesdayEnd;
    }

    public void setWednesdayEnd(String wednesdayEnd) {
        this.wednesdayEnd = wednesdayEnd;
    }

    public String getThursdayStart() {
        return thursdayStart;
    }

    public void setThursdayStart(String thursdayStart) {
        this.thursdayStart = thursdayStart;
    }

    public String getThursdayEnd() {
        return thursdayEnd;
    }

    public void setThursdayEnd(String thursdayEnd) {
        this.thursdayEnd = thursdayEnd;
    }

    public String getFridayStart() {
        return fridayStart;
    }

    public void setFridayStart(String fridayStart) {
        this.fridayStart = fridayStart;
    }

    public String getFridayEnd() {
        return fridayEnd;
    }

    public void setFridayEnd(String fridayEnd) {
        this.fridayEnd = fridayEnd;
    }

    public String getSaturdayStart() {
        return saturdayStart;
    }

    public void setSaturdayStart(String saturdayStart) {
        this.saturdayStart = saturdayStart;
    }

    public String getSaturdayEnd() {
        return saturdayEnd;
    }

    public void setSaturdayEnd(String saturdayEnd) {
        this.saturdayEnd = saturdayEnd;
    }

    public String getSundayStart() {
        return sundayStart;
    }

    public void setSundayStart(String sundayStart) {
        this.sundayStart = sundayStart;
    }

    public String getSundayEnd() {
        return sundayEnd;
    }

    public void setSundayEnd(String sundayEnd) {
        this.sundayEnd = sundayEnd;
    }

}