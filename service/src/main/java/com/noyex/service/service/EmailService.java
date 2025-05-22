package com.noyex.service.service;

import com.noyex.data.enums.PaymentMethod;
import com.noyex.data.model.order.Orders;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${admin.email}")
    private String adminEmail;

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }

    @Override
    public void sendOrderConfirmation(Orders orders) {
        String subject = "BURGERONI - Potwierdzenie zamówienia " + orders.getOrderNumber();
        String orderNumber = orders.getOrderNumber();
        String orderDate = orders.getCreatedAt().toString();
        String paymentMethod = "nw";
        if(orders.getPaymentMethod() == PaymentMethod.CARD) {
            paymentMethod = "Karta płatnicza";
        } else {
            paymentMethod = "Gotówka";
        }
        String delivery = "";
        if(orders.isDelivery()) {
            delivery = "Dostawa";
        } else {
            delivery = "Odbiór osobisty";
        }

        String nameAndSurname = orders.getFirstName() + " " + orders.getLastName();
        String phoneNumber = orders.getPhone();

        // reszta todo
        String htmlMessage = "<!DOCTYPE html>"
                + "<html lang=\"pl\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                + "</head>"
                + "<body style=\"font-family: 'Lato', 'Playfair Display', sans-serif;\">"
                + "<div style=\"background: #f8f5f0; padding: 30px; color: #333; border-radius: 8px;\">"
                + "<div style=\"background-color: white; padding: 30px; border-radius: 6px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08); border: 1px solid #e0d5c1; position: relative;\">"
                + "<div style=\"position: absolute; top: 20px; right: 20px; background-color: #b8292f; color: white; padding: 8px 16px; border-radius: 30px; font-weight: 600; font-size: 1.1rem; letter-spacing: 0.5px; box-shadow: 0 3px 10px rgba(184, 41, 47, 0.3);\">" + orderNumber + "</div>"
                + "<div style=\"width: 90px; height: 90px; background-color: #b8292f; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 45px; margin: 0 auto 25px; position: relative; box-shadow: 0 4px 15px rgba(184, 41, 47, 0.2);\">✓</div>"
                + "<h2 style=\"color: #b8292f; font-weight: 700; font-size: 28px; font-family: 'Playfair Display', serif; text-align: center;\">Dziękujemy za złożenie zamówienia!</h2>"
                + "<p style=\"font-size: 16px; color: #666; font-family: 'Lato', sans-serif; text-align: center; margin-bottom: 30px;\">Twoje zamówienie zostało przyjęte do realizacji. Nasz zespół już pracuje nad jego przygotowaniem.</p>"

                + "<div style=\"margin-bottom: 25px;\">"
                + "<h3 style=\"font-size: 1.3rem; margin-bottom: 15px; color: #b8292f; font-family: 'Playfair Display', serif; border-bottom: 1px solid #e0d5c1; padding-bottom: 8px;\">Szczegóły zamówienia</h3>"
                + "<div style=\"background-color: #f9f9f9; border-radius: 10px; padding: 20px;\">"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Numer zamówienia:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + orderNumber + "</span>"
                + "</div>"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Data zamówienia:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + orderDate + "</span>"
                + "</div>"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Metoda płatności:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + paymentMethod + "</span>"
                + "</div>"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Sposób dostawy:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + delivery + "</span>"
                + "</div>"
                + "</div>"
                + "</div>"

                + "<div style=\"margin-bottom: 25px;\">"
                + "<h3 style=\"font-size: 1.3rem; margin-bottom: 15px; color: #b8292f; font-family: 'Playfair Display', serif; border-bottom: 1px solid #e0d5c1; padding-bottom: 8px;\">Dane kontaktowe</h3>"
                + "<div style=\"background-color: #f9f9f9; border-radius: 10px; padding: 20px;\">"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Imię i nazwisko:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + nameAndSurname + "</span>"
                + "</div>"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Email:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + orders.getUserEmail() + "</span>"
                + "</div>"
                + "<div style=\"margin-bottom: 12px; display: flex; justify-content: space-between; padding-bottom: 12px; border-bottom: 1px solid #e0d5c1;\">"
                + "<span style=\"font-weight: 600; color: #333; margin-right: 10px;\">Telefon:</span>"
                + "<span style=\"color: #b8292f; font-weight: 600; text-align: right;\">" + phoneNumber + "</span>"
                + "</div>"
                + "</div>"
                + "</div>"

                + "<div style=\"margin-bottom: 25px;\">"
                + "<h3 style=\"font-size: 1.3rem; margin-bottom: 15px; color: #b8292f; font-family: 'Playfair Display', serif; border-bottom: 1px solid #e0d5c1; padding-bottom: 8px;\">Zamówione produkty</h3>"
                + "<div style=\"background-color: #f9f9f9; border-radius: 10px; padding: 20px;\">"
                + "<div style=\"margin-bottom: 0px;\">do uzupełnienia</div>"
                + "<div style=\"display: flex; justify-content: space-between; align-items: center; margin-top: 20px; padding-top: 15px; border-top: 2px solid #e0d5c1;\">"
                + "<span style=\"font-size: 1.1rem; font-weight: 700; color: #333;\">Razem do zapłaty:</span>"
                + "<span style=\"font-size: 1.3rem; font-weight: 700; color: #b8292f;\">do uzupełnienia</span>"
                + "</div>"
                + "</div>"
                + "</div>"

                + "<div style=\"margin-top: 30px; text-align: center;\">"
                + "<a href=\"#\" style=\"display: inline-block; background-color: #b8292f; color: white; padding: 14px 25px; border-radius: 8px; text-decoration: none; font-weight: 600; font-family: 'Lato', sans-serif; margin-right: 20px;\">Wróć do strony głównej</a>"
                + "<a href=\"#\" style=\"display: inline-block; background-color: transparent; color: #333; padding: 14px 25px; border-radius: 8px; text-decoration: none; font-weight: 500; font-family: 'Lato', sans-serif; border: 1px solid #e0d5c1;\">Przejdź do menu</a>"
                + "</div>"

                + "<div style=\"margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0d5c1; text-align: center;\">"
                + "<p style=\"font-size: 14px; color: #666; font-family: 'Lato', sans-serif;\">© " + java.time.Year.now().getValue() + " BURGERONI. Wszelkie prawa zastrzeżone.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            System.out.println("Email z potwierdzeniem zamówienia wysłany do " + orders.getUserEmail());
            sendEmail(orders.getUserEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUserRegistrationConfirmation(String email, String username) {
        String subject = "Potwierdzenie utworzenia konta w BURGERONI";
        String htmlMessage = "<!DOCTYPE html>"
                + "<html lang=\"pl\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                + "<title>Potwierdzenie utworzenia konta</title>"
                + "</head>"
                + "<body style=\"font-family: 'Lato', 'Playfair Display', sans-serif; margin: 0; padding: 0; background-color: #f8f5f0;\">"
                + "<div style=\"background-color: white; padding: 30px; border-radius: 6px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08); border: 1px solid #e0d5c1; max-width: 600px; margin: 0 auto;\">"

                + "<h2 style=\"color: #b8292f; font-weight: 700; font-size: 24px; font-family: 'Playfair Display', serif; margin-top: 0; margin-bottom: 20px; text-align: center;\">Potwierdzenie utworzenia konta</h2>"

                + "<p style=\"font-size: 16px; color: #666; font-family: 'Lato', sans-serif; line-height: 1.5; text-align: center;\">Potwierdzenie utworzenia konta użytkownika</p>"
                + "<p style=\"font-size: 16px; color: #666; font-family: 'Lato', sans-serif; line-height: 1.5; text-align: center;\"> " + username + " </p>"
                + "<div style=\"margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0d5c1; text-align: center;\">"
                + "<p style=\"font-size: 14px; color: #666; font-family: 'Lato', sans-serif;\">© " + java.time.Year.now().getValue() + " BURGERONI. Wszelkie prawa zastrzeżone.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";


        try {
            sendEmail(adminEmail, subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
