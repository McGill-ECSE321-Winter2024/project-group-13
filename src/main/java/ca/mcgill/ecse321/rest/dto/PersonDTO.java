package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Person;


public class PersonDTO {

    private String id;
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
  
  
    // Constructor accepting a Person entity
    public PersonDTO(Person person) {
        if (person != null) {
            this.id = person.getId();
            this.email = person.getEmail();
            this.phoneNumber = person.getPhoneNumber();
            this.name = person.getName();
        }
    }

    public PersonDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
