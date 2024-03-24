package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.sql.Time;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Schedule {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
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

  public Schedule() {}

  public Schedule(Time standardOpeningHour, Time standardClosingHour) {
    this.mondayStart = standardOpeningHour;
    this.mondayEnd = standardClosingHour;
    this.tuesdayStart = standardOpeningHour;
    this.tuesdayEnd = standardClosingHour;
    this.wednesdayStart = standardOpeningHour;
    this.wednesdayEnd = standardClosingHour;
    this.thursdayStart = standardOpeningHour;
    this.thursdayEnd = standardClosingHour;
    this.fridayStart = standardOpeningHour;
    this.fridayEnd = standardClosingHour;
    this.saturdayStart = standardOpeningHour;
    this.saturdayEnd = standardClosingHour;
    this.sundayStart = standardOpeningHour;
    this.sundayEnd = standardClosingHour;
  }

  public Schedule(Time standardOpeningHour, Time standardClosingHour,String id) {
    this.id = id;
    this.mondayStart = standardOpeningHour;
    this.mondayEnd = standardClosingHour;
    this.tuesdayStart = standardOpeningHour;
    this.tuesdayEnd = standardClosingHour;
    this.wednesdayStart = standardOpeningHour;
    this.wednesdayEnd = standardClosingHour;
    this.thursdayStart = standardOpeningHour;
    this.thursdayEnd = standardClosingHour;
    this.fridayStart = standardOpeningHour;
    this.fridayEnd = standardClosingHour;
    this.saturdayStart = standardOpeningHour;
    this.saturdayEnd = standardClosingHour;
    this.sundayStart = standardOpeningHour;
    this.sundayEnd = standardClosingHour;
  }

  public Schedule(
      Time weekDaysOpeningHour,
      Time weekDaysClosingHour,
      Time weekEndsOpeningHour,
      Time weekEndClosingHour) {
    this.mondayStart = weekDaysOpeningHour;
    this.mondayEnd = weekDaysClosingHour;
    this.tuesdayStart = weekDaysOpeningHour;
    this.tuesdayEnd = weekDaysClosingHour;
    this.wednesdayStart = weekDaysOpeningHour;
    this.wednesdayEnd = weekDaysClosingHour;
    this.thursdayStart = weekDaysOpeningHour;
    this.thursdayEnd = weekDaysClosingHour;
    this.fridayStart = weekDaysOpeningHour;
    this.fridayEnd = weekDaysClosingHour;
    this.saturdayStart = weekEndsOpeningHour;
    this.saturdayEnd = weekEndClosingHour;
    this.sundayStart = weekEndsOpeningHour;
    this.sundayEnd = weekEndClosingHour;
  }

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

  public String toString() {
    return "["
        + "id"
        + ":"
        + getId()
        + "mondayStart"
        + ":"
        + getMondayStart()
        + "mondayEnd"
        + ":"
        + getMondayEnd()
        + "tuesdayStart"
        + ":"
        + getTuesdayStart()
        + "tuesdayEnd"
        + ":"
        + getTuesdayEnd()
        + "wednesdayStart"
        + ":"
        + getWednesdayStart()
        + "wednesdayEnd"
        + ":"
        + getWednesdayEnd()
        + "thursdayStart"
        + ":"
        + getThursdayStart()
        + "thursdayEnd"
        + ":"
        + getThursdayEnd()
        + "fridayStart"
        + ":"
        + getFridayStart()
        + "fridayEnd"
        + ":"
        + getFridayEnd()
        + "saturdayStart"
        + ":"
        + getSaturdayStart()
        + "saturdayEnd"
        + ":"
        + getSaturdayEnd()
        + "sundayStart"
        + ":"
        + getSundayStart()
        + "sundayEnd"
        + ":"
        + getSundayEnd()
        + "]";
  }
}
