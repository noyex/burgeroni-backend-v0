package com.noyex.service.service;

import com.noyex.data.enums.PaymentMethod;
import com.noyex.data.model.MenuItem;
import com.noyex.data.model.order.OrderItems;
import com.noyex.data.model.order.Orders;
import com.noyex.data.repository.MenuItemRepository;
import com.noyex.data.repository.OrderItemRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${admin.email}")
    private String adminEmail;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

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
        String orderDate = orders.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String paymentMethod = (orders.getPaymentMethod() == PaymentMethod.CARD) ? "Karta płatnicza" : "Gotówka";
        String delivery = orders.isDelivery() ? "Dostawa" : "Odbiór osobisty";
        String nameAndSurname = orders.getFirstName() + " " + orders.getLastName();
        String phoneNumber = orders.getPhone();

        List<OrderItems> orderedItems = orderItemRepository.findAllByOrders(orders);
        List<MenuItem> menuItems = menuItemRepository.findAllById(orderedItems.stream()
                .map(OrderItems::getMenuItemId)
                .toList());

        Map<Long, MenuItem> menuItemsMap = menuItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, item -> item));

        StringBuilder productsHtmlBuilder = new StringBuilder();
        productsHtmlBuilder.append("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");


        for (OrderItems item : orderedItems) {
            MenuItem menuItem = menuItemsMap.get(item.getMenuItemId());
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal price = menuItem.getPrice();
            BigDecimal totalPrice = quantity.multiply(price);
            if (menuItem != null) {
                productsHtmlBuilder.append("<tr>")
                        .append("<td style='padding: 8px 0; color: #555555; border-bottom: 1px solid #F3F1ED;'>")
                        .append(item.getQuantity()).append(" x ").append(menuItem.getName())
                        .append("</td>")
                        .append("<td style='padding: 8px 0; color: #1A3C34; font-weight: 500; text-align: right; border-bottom: 1px solid #F3F1ED;'>")
                        .append(String.format(Locale.forLanguageTag("pl-PL"), "%.2f zł", totalPrice))
                        .append("</td>")
                        .append("</tr>");
            }
        }
        productsHtmlBuilder.append("</table>");
        String orderedProductsHtmlList = productsHtmlBuilder.toString();

        String htmlMessage = "<!DOCTYPE html>"
                + "<html lang=\"pl\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">"
                + "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>"
                + "<link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap\" rel=\"stylesheet\">"
                + "<title>Potwierdzenie Zamówienia</title>"
                + "</head>"
                + "<body style=\"margin: 0; padding: 0; background-color: #F3F1ED; font-family: 'Montserrat', sans-serif;\">"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"
                + "<tr>"
                + "<td style=\"padding: 20px 0;\">"
                + "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse; background-color: #ffffff; border-radius: 8px; overflow: hidden;\">"
                + "<tr>"
                + "<td align=\"center\" style=\"background-color: #1A3C34; padding: 20px 0;\">"
                + "<h1 style=\"color: #FFFFFF; font-size: 24px; margin: 0; font-weight: 700; letter-spacing: 1px;\">BURGERONI</h1>"
                + "<p style=\"color: #E2DACC; font-size: 14px; margin: 5px 0 0; font-weight: 500;\">BURGERS & PASTA</p>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 40px 30px;\">"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"
                + "<tr>"
                + "<td align=\"center\">"
                + "<h2 style=\"font-size: 28px; font-weight: 700; color: #1A3C34; margin: 0 0 15px;\">Dziękujemy za zamówienie!</h2>"
                + "<p style=\"font-size: 16px; color: #555555; margin: 0 0 30px; line-height: 1.6;\">Otrzymaliśmy Twoje zamówienie i już zabieramy się do pracy. Poniżej znajdziesz jego podsumowanie.</p>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 20px 0;\">"
                + "<h3 style=\"font-size: 20px; color: #1A3C34; margin: 0 0 15px; border-bottom: 2px solid #E2DACC; padding-bottom: 10px;\">Szczególy Zamówienia</h3>"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-size: 15px;\">"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555;\">Numer zamówienia:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 700; text-align: right;\">" + orderNumber + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; border-top: 1px solid #F3F1ED;\">Data zamówienia:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 700; text-align: right; border-top: 1px solid #F3F1ED;\">" + orderDate + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; border-top: 1px solid #F3F1ED;\">Metoda płatności:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 700; text-align: right; border-top: 1px solid #F3F1ED;\">" + paymentMethod + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; border-top: 1px solid #F3F1ED;\">Sposób dostawy:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 700; text-align: right; border-top: 1px solid #F3F1ED;\">" + delivery + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 20px 0;\">"
                + "<h3 style=\"font-size: 20px; color: #1A3C34; margin: 0 0 15px; border-bottom: 2px solid #E2DACC; padding-bottom: 10px;\">Zamówione produkty</h3>"
                + orderedProductsHtmlList
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin-top: 20px; border-top: 2px solid #E2DACC; padding-top: 15px;\">"
                + "<tr>"
                + "<td style=\"font-size: 18px; font-weight: 700; color: #1A3C34;\">Razem do zapłaty:</td>"
                + "<td style=\"font-size: 22px; font-weight: 700; color: #1A3C34; text-align: right;\">" + String.format(Locale.forLanguageTag("pl-PL"), "%.2f zł", orders.getTotalPrice()) + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 20px 0;\">"
                + "<h3 style=\"font-size: 20px; color: #1A3C34; margin: 0 0 15px; border-bottom: 2px solid #E2DACC; padding-bottom: 10px;\">Dane do dostawy</h3>"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-size: 15px;\">"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; width: 150px;\">Imię i nazwisko:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 500; text-align: left;\">" + nameAndSurname + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; border-top: 1px solid #F3F1ED;\">Email:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 500; text-align: left; border-top: 1px solid #F3F1ED;\">" + orders.getUserEmail() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"padding: 10px 0; color: #555555; border-top: 1px solid #F3F1ED;\">Telefon:</td>"
                + "<td style=\"padding: 10px 0; color: #1A3C34; font-weight: 500; text-align: left; border-top: 1px solid #F3F1ED;\">" + phoneNumber + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"center\" style=\"padding: 30px 0 10px;\">"
                + "<a href=\"http://burgeroni.pl\" target=\"_blank\" style=\"background-color: #1A3C34; color: #FFFFFF; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-size: 16px; font-weight: 700;\">Przejdź do strony</a>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"center\" style=\"padding: 20px; font-size: 12px; color: #999999;\">"
                + "<p style=\"margin: 0;\">BURGERONI &copy; " + java.time.Year.now().getValue() + " | Wszelkie prawa zastrzeżone.</p>"
                + "<p style=\"margin: 5px 0 0;\">Jeśli masz pytania, skontaktuj się z nami pod adresem: kontakt@burgeroni.pl</p>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</body>"
                + "</html>";
        try {
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
