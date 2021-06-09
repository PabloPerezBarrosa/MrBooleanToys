package com.mrboolean.mail;

import com.mrboolean.model.Cliente;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    private String userEmail;
    private String password;

    public SendMail(String userEmail, String password) {
        super();
        this.userEmail = userEmail;
        this.password = password;
    }

    public static void sendEmail(Cliente cliente, boolean opcion) {

        final String email = "pablo.mr.boolean.toys@gmail.com";
        final String pass = "mrbooleantoys1983";

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(email, pass);

            }

        });

        try {
            if (opcion) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(cliente.getEmail()));
                message.setSubject("Verificación de cuenta Mr Boolean Toys");
                message.setContent(
                        "<h1 style='color: #6ea5d1;display: flex;align-items: center;justify-content: center;overflow: hidden;'>Mr Boolean Toys</h1><br><p>Buenos días " + cliente.getNombre() + ". Click para terminar su registro: <br><a href='http://localhost:8080/MrBooleanToys/faces/protegido/verificacion/verificacion.xhtml?key1=" + cliente.getIdcliente() + "'>Enlace de activación de cuenta</a></p>",
                        "text/html");
                Transport.send(message);
                
    
    
            }else{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(cliente.getEmail()));
                message.setSubject("Recuperación de clave");
                message.setContent(
                        "<h1 style='color: #6ea5d1'>Recuperación de clave</h1><br><p>Buenos dias " + cliente.getNombre() + " su clave es: " + cliente.getClave()+".<br> No comparta esta información con nadie si quiere mantener su cuenta segura.</p>",
                        "text/html");
                Transport.send(message);
            }

            

        } catch (Exception e) {

            System.out.println("Error en SendMail().......");

        }

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
