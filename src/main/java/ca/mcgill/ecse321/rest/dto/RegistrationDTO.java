package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Registration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RegistrationDTO {

    @JsonIgnore
    private CustomerDTO customer;
    private CourseDTO course;
    public Integer rating;

    public String id;

    public RegistrationDTO(){
    }



    public RegistrationDTO(Registration registration){

        if (registration!=null) {
            if (registration.getCustomer()!=null){
                this.customer = new CustomerDTO(registration.getCustomer());
            }

            if (registration.getCourse()!=null){

                this.course = new CourseDTO(registration.getCourse());

            }

            System.out.println(registration.getRating());

//            if ((registration.getRating()>= 1) && (registration.getRating()<=5)){
            this.rating = registration.getRating();

            if ((registration.getId()!=null)){
                this.id = registration.getId();}
        }
    }


    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public String getID() {
        return id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setID(String id) {
        this.id = id;
    }

}
