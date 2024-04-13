package ca.mcgill.ecse321.rest.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioService {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "AC1b4929596fbd01a9c44d7f9ae59b7e11";
    public static final String AUTH_TOKEN = "08618991c264517d8516320276811da2";

    public static void sendSms(String phoneNumber, String messageText) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+1"+phoneNumber),
                        new com.twilio.type.PhoneNumber("+14388123252"),
                        messageText)
                .create();

        System.out.println(message.getSid());
    }
}