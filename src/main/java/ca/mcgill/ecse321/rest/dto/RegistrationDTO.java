package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Registration;

public class RegistrationDTO {

    private CustomerDTO customer;
    private CourseDTO course;
    private Integer rating;

    public RegistrationDTO(){}

    public RegistrationDTO(CustomerDTO customer, CourseDTO course, Integer rating){
        this.customer = customer;
        this.course = course;
        this.rating = rating;

    }

    public RegistrationDTO(Registration registration){
        this.customer = new CustomerDTO(registration.getCustomer());
        this.course = new CourseDTO(registration.getCourse());
        this.rating = registration.getRating();

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

}
