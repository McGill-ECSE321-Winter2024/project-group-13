package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Person;


public class PersonDTO {

    private String id;
    private String email;
    private String phoneNumber;
    private String name;
    private String password;


  
  
    // Constructor accepting a Person entity
    public PersonDTO(){}
    public PersonDTO(Person person) {
        if (person != null) {
            this.id = person.getId();
            this.email = person.getEmail();
            this.phoneNumber = person.getPhoneNumber();
            this.name = person.getName();
            this.password = person.getPassword();
        }
    }



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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PersonDTO personDTO)) {
            return false;
        }
        return (id==personDTO.getId() || id.equals(personDTO.getId()))&&
                (email==personDTO.getEmail() || email.equals(personDTO.getEmail()))&&
                (phoneNumber==personDTO.getPhoneNumber() || phoneNumber.equals(personDTO.getPhoneNumber()))&&
                (name==personDTO.getName() || name.equals(personDTO.getName()))&&
                (password==personDTO.getPassword() || password.equals(personDTO.getPassword()));
    }
}
