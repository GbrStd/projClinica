package com.example.clinica.mail;

public interface EmailService {

    void sendSimpleMail(String to, String subject, String text);

}
