
package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.sql.Time;
import java.util.*;
import java.sql.Date;

@Entity
public class CourseFrequency
{
    public CourseFrequency() {

    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, unique = true)
    private String id;

    private String mondayStartTime;
    private String mondayEndTime;
    private String tuesdayStartTime;
    private String tuesdayEndTime;
    private String wednesdayStartTime;
    private String wednesdayEndTime;
    private String thursdayStartTime;
    private String thursdayEndTime;
    private String fridayStartTime;
    private String fridayEndTime;
    private String saturdayStartTime;
    private String saturdayEndTime;
    private String sundayStartTime;
    private String sundayEndTime;

    @OneToOne
    private Course course;

}