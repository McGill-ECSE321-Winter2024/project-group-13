package ca.mcgill.ecse321.rest.dto.auth;

public class RegisterDTO {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    public RegisterDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
