package test;


import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.exception.StripeException;
import java.util.HashMap;
import java.util.Map;


public class PaymentService {
    public PaymentService(String apiKey) {
        Stripe.apiKey = apiKey; // Initialize Stripe with your secret key.
    }

    public boolean processPayment(String token, double amount) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (amount * 100)); // amount is in cents
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Guide booking charge");
        chargeParams.put("source", token); // Token created on client side

        try {
            Charge charge = Charge.create(chargeParams);
            return charge.getPaid();
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
