package sg.edu.nus.iss.team1.service;

public interface EmailService {
    Boolean SendEmail(String[] receivers, String subject, String text);
}
