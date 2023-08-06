package com.example.challengefinal.growshop.servicios;

import com.example.challengefinal.growshop.models.Correo;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.util.Properties;
@Service
public class EmailSend {

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(Correo mensajeCorreo) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String username = "growshopgozo@gmail.com";
        String password = "xfiuncmddpjgregt";
        String remitente = mensajeCorreo.getRemitente();
        String asunto= mensajeCorreo.getAsunto();
        String comentario = mensajeCorreo.getComentario();
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Creación del mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mensajeCorreo.getRemitente()));
            message.setSubject(mensajeCorreo.getAsunto());

            // Construcción del contenido del mensaje con el comentario y la imagen
            Multipart multipart = new MimeMultipart();
            BodyPart comentarioPart = new MimeBodyPart();
            comentarioPart.setText("Asunto: Respuesta a tu mensaje de contacto\n" +
                    "\n" +
                    "Estimado" + " " + remitente +
                    "\n" +
                    "¡Saludos cordiales! Queremos agradecerte sinceramente por tomarte el tiempo para contactarnos a través de nuestro formulario. Valoramos tu interés en Gozo GrowShop y estamos emocionados de poder atenderte.\n" +
                    "\n" +
                    "Hemos recibido tu mensaje y nos complace saber que estás interesado/a en nuestros productos/servicios. Tu opinión y preguntas son fundamentales para nosotros, y haremos nuestro mejor esfuerzo para brindarte una respuesta detallada y oportuna.\n" +
                    "\n" +
                    "Nuestro equipo de soporte revisará tu mensaje cuidadosamente y te responderá lo antes posible. Si tu consulta requiere una atención inmediata, te recomendamos que te comuniques con nosotros a través de nuestro  correo electrónico a somosgozo@gmail.com.\n" +
                    "\n" +
                    "Mientras tanto, te invitamos a explorar nuestro sitio web para obtener más información sobre nuestros productos/servicios y mantenernos en contacto con las últimas novedades.\n" +
                    "\n" +
                    "Una vez más, gracias por tu interés en Gozo GrowShop. Estamos aquí para ayudarte en todo lo que necesites y esperamos brindarte una experiencia satisfactoria.\n" +
                    "\n" +
                    "Atentamente,\n" +
                    "\n" +
                    "Gozo GrowShop\n" +
                    "somosgozo@gmail.com\n");

            // Agrega la imagen al correo (asegúrate de que la ruta sea correcta)
           /* BodyPart imagen = new MimeBodyPart();
            ClassLoader classLoader = getClass().getClassLoader();
            URL imageUrl = classLoader.getResource("static/assets/images/gozoSmokeShop.JPEG");
            DataSource dataSource = new URLDataSource(imageUrl);
            imagen.setDataHandler(new DataHandler(dataSource));
            imagen.setFileName("imagen.jpg");*/


            // Agregar las partes al cuerpo del mensaje
            multipart.addBodyPart(comentarioPart);
           /* multipart.addBodyPart(imagen);*/
            message.setContent(multipart);

            // Enviar el correo al remitente
            Transport.send(message);

            // Enviar un segundo correo al destinatario (username) con el contenido del comentario
            Message messageToUsername = new MimeMessage(session);
            messageToUsername.setFrom(new InternetAddress(username));
            messageToUsername.addRecipient(Message.RecipientType.TO, new InternetAddress(username));
            messageToUsername.setSubject("Correo enviado por: " + remitente + "sobre el asunto:" + " "+ asunto + ".");
            messageToUsername.setText("El usuario (" + remitente + ") envió este comentario:" + " " + comentario + ".");
            Transport.send(messageToUsername);
            Transport transport = session.getTransport("smtp");
            transport.connect(username,password);
            transport.sendMessage(messageToUsername, messageToUsername.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo.", e);
        }
    }


    public ResponseEntity<Object> sendFactura(Correo mensajeCorreo, byte[] pdfData) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String username = "growshopgozo@gmail.com";
        String password = "xfiuncmddpjgregt";
        String remitente = mensajeCorreo.getRemitente();
        String destinatario = mensajeCorreo.getDestinatario();
        String asunto= mensajeCorreo.getAsunto();
        String comentario = mensajeCorreo.getComentario();
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(username));
            helper.setTo(new InternetAddress(destinatario));
            helper.setSubject(asunto);

            helper.setText(comentario);


            MimeMessage messageGozo = javaMailSender.createMimeMessage();
            MimeMessageHelper helperGozo = new MimeMessageHelper(messageGozo, true);
            helperGozo.setFrom(new InternetAddress(username));
            helperGozo.setTo(new InternetAddress(remitente));
            helperGozo.setSubject(asunto);

            String mensajeParaGozo = "Aquí el comprobante de la factura de compra";

            helperGozo.setText(mensajeParaGozo);

            // Agregar el archivo PDF adjunto
            helper.addAttachment("factura.pdf", new ByteArrayResource(pdfData));
            helperGozo.addAttachment("factura.pdf", new ByteArrayResource(pdfData));
            javaMailSender.send(messageGozo);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo.", e);
        }
        return new ResponseEntity<>("Email enviado", HttpStatus.OK);
    }

}