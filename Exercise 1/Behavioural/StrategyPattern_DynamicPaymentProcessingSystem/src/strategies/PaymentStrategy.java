package strategies;

import models.PaymentDetails;
import models.PaymentResult;

// PaymentStrategy.java - Strategy Interface (Dependency Inversion Principle)
public interface PaymentStrategy {
    PaymentResult processPayment(double amount, PaymentDetails details);
    String getPaymentMethodName();
    boolean isAvailable();
}
