package com.example.challengefinal.growshop.controladores;

import com.example.challengefinal.growshop.dto.CorreoDTO;
import com.example.challengefinal.growshop.dto.OrdenDTO;
import com.example.challengefinal.growshop.dto.OrdenProductoDTO;
import com.example.challengefinal.growshop.models.Cliente;
import com.example.challengefinal.growshop.servicios.EmailSend;
import com.example.challengefinal.growshop.models.Correo;
import com.example.challengefinal.growshop.servicios.ServicioCliente;
import com.example.challengefinal.growshop.servicios.ServicioFacturacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CorreoControlador {
    @Autowired
    private EmailSend emailSend; // Inyecta el bean EmailSender que configuraste previamente
    @Autowired
    private ServicioCliente servicioCliente;
    @Autowired
    private ServicioFacturacion servicioFacturacion;

    @PostMapping("/enviarEmail")
    public ResponseEntity<String> sendEmail(@RequestBody CorreoDTO correoDTO) {
            // Verificar que se hayan proporcionado los datos necesarios
            if (correoDTO.getRemitente() == null) {
                return new ResponseEntity<>("Falta el remitente en el formulario", HttpStatus.BAD_REQUEST);
            }
            if (correoDTO.getRemitente().isBlank()) {
            return new ResponseEntity<>("Falta ingresar remitente en el formulario", HttpStatus.BAD_REQUEST);
            }
            if ( correoDTO.getComentario() == null) {
                return new ResponseEntity<>("Falta el comentario en el formulario", HttpStatus.BAD_REQUEST);
            }
            if ( correoDTO.getComentario().isBlank()) {
            return new ResponseEntity<>("Falta ingresar el comentario en el formulario", HttpStatus.BAD_REQUEST);
            }
            if (correoDTO.getAsunto().isBlank()){
                return new ResponseEntity<>("Ingrese el asunto", HttpStatus.FORBIDDEN);
            }
            if (correoDTO.getAsunto() == null){
            return new ResponseEntity<>("Falta ingresar el asunto", HttpStatus.FORBIDDEN);
            }

            // Crear un mensaje de correo electrónico
            Correo correo = new Correo();
            correo.setRemitente(correoDTO.getRemitente());
            correo.setDestinatario("admin-gozogrowshop@gmail.com"); // Reemplaza con la dirección de correo destino
            correo.setAsunto(correoDTO.getAsunto());
            correo.setComentario(correoDTO.getComentario());

            // Enviar el correo electrónico utilizando el EmailSender
            emailSend.sendEmail(correo);

            return new ResponseEntity<>("Correo enviado exitosamente", HttpStatus.OK);

    }
}
