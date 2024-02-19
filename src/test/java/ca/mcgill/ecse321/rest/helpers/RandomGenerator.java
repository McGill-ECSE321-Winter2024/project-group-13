package ca.mcgill.ecse321.rest.helpers;

import java.util.Random;

public class RandomGenerator {
  public static String generateRandomEmail() {
    String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder email = new StringBuilder();
    Random random = new Random();
    int length = 10; // You can change the length of the email username

    for (int i = 0; i < length; i++) {
      int index = random.nextInt(characters.length());
      email.append(characters.charAt(index));
    }

    email.append("@example.com");
    return email.toString();
  }

  public static String generateRandomPhoneNumber() {
    StringBuilder phoneNumber = new StringBuilder();
    Random random = new Random();
    int length = 10; // Length of the phone number

    // Assuming the first digit should not be 0
    phoneNumber.append(random.nextInt(9) + 1);

    for (int i = 1; i < length; i++) {
      phoneNumber.append(random.nextInt(10));
    }

    return phoneNumber.toString();
  }
}
