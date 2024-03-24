package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Schedule;

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

    // No-argument constructor
    public ScheduleDTO() {
    }
    public ScheduleDTO(Schedule schedule) {
        if (schedule != null) {
            this.id = schedule.getId();
            this.mondayStart = schedule.getMondayStart() != null ? schedule.getMondayStart().toString() : null;
            this.mondayEnd = schedule.getMondayEnd() != null ? schedule.getMondayEnd().toString() : null;
            this.tuesdayStart = schedule.getTuesdayStart() != null ? schedule.getTuesdayStart().toString() : null;
            this.tuesdayEnd = schedule.getTuesdayEnd() != null ? schedule.getTuesdayEnd().toString() : null;
            this.wednesdayStart = schedule.getWednesdayStart() != null ? schedule.getWednesdayStart().toString() : null;
            this.wednesdayEnd = schedule.getWednesdayEnd() != null ? schedule.getWednesdayEnd().toString() : null;
            this.thursdayStart = schedule.getThursdayStart() != null ? schedule.getThursdayStart().toString() : null;
            this.thursdayEnd = schedule.getThursdayEnd() != null ? schedule.getThursdayEnd().toString() : null;
            this.fridayStart = schedule.getFridayStart() != null ? schedule.getFridayStart().toString() : null;
            this.fridayEnd = schedule.getFridayEnd() != null ? schedule.getFridayEnd().toString() : null;
            this.saturdayStart = schedule.getSaturdayStart() != null ? schedule.getSaturdayStart().toString() : null;
            this.saturdayEnd = schedule.getSaturdayEnd() != null ? schedule.getSaturdayEnd().toString() : null;
            this.sundayStart = schedule.getSundayStart() != null ? schedule.getSundayStart().toString() : null;
            this.sundayEnd = schedule.getSundayEnd() != null ? schedule.getSundayEnd().toString() : null;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScheduleDTO scheduleDTO = (ScheduleDTO) obj;
        return (id == scheduleDTO.getId() || id.equals(scheduleDTO.getId())) &&
                (mondayStart == scheduleDTO.getMondayStart() || mondayStart.equals(scheduleDTO.getMondayStart())) &&
                (mondayEnd == scheduleDTO.getMondayEnd() || mondayEnd.equals(scheduleDTO.getMondayEnd())) &&
                (tuesdayStart == scheduleDTO.getTuesdayStart() || tuesdayStart.equals(scheduleDTO.getTuesdayStart())) &&
                (tuesdayEnd == scheduleDTO.getTuesdayEnd() || tuesdayEnd.equals(scheduleDTO.getTuesdayEnd())) &&
                (wednesdayStart == scheduleDTO.getWednesdayStart() || wednesdayStart.equals(scheduleDTO.getWednesdayStart())) &&
                (wednesdayEnd == scheduleDTO.getWednesdayEnd() || wednesdayEnd.equals(scheduleDTO.getWednesdayEnd())) &&
                (thursdayStart == scheduleDTO.getThursdayStart() || thursdayStart.equals(scheduleDTO.getThursdayStart())) &&
                (thursdayEnd == scheduleDTO.getThursdayEnd() || thursdayEnd.equals(scheduleDTO.getThursdayEnd())) &&
                (fridayStart == scheduleDTO.getFridayStart() || fridayStart.equals(scheduleDTO.getFridayStart())) &&
                (fridayEnd == scheduleDTO.getFridayEnd() || fridayEnd.equals(scheduleDTO.getFridayEnd())) &&
                (saturdayStart == scheduleDTO.getSaturdayStart() || saturdayStart.equals(scheduleDTO.getSaturdayStart())) &&
                (saturdayEnd == scheduleDTO.getSaturdayEnd() || saturdayEnd.equals(scheduleDTO.getSaturdayEnd())) &&
                (sundayStart == scheduleDTO.getSundayStart() || sundayStart.equals(scheduleDTO.getSundayStart())) &&
                (sundayEnd == scheduleDTO.getSundayEnd() || sundayEnd.equals(scheduleDTO.getSundayEnd()));
    }
}
