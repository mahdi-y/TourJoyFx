package Controller;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SMS {
    public static final String ACCOUNT_SID = "AC441edf7b702f12f3f72805d081f44d5a";
    public static final String AUTH_TOKEN = "55190770e65f7ce693a803c79d08b8a5";
    public static final String TWILIO_NUMBER = "+14134183448";

    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(recipientPhoneNumber),
                        new com.twilio.type.PhoneNumber(TWILIO_NUMBER),
                        messageBody)
                .create();

        System.out.println("SMS envoyé avec succès : " + message.getSid());
    }
}
