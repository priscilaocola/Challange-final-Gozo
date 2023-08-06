package com.example.challengefinal.growshop.controladores;


import com.example.challengefinal.growshop.Repositorios.ClienteRepositorio;
import com.example.challengefinal.growshop.dto.ClienteDTO;
import com.example.challengefinal.growshop.dto.ClienteRegistroDTO;
import com.example.challengefinal.growshop.models.Cliente;
import com.example.challengefinal.growshop.servicios.ServicioCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClienteControlador {

    @Autowired
    private ServicioCliente servicioCliente;

    @Autowired
    private PasswordEncoder codificadorDeContraseña;

    @GetMapping("/clientes")
    public List<ClienteDTO> traerClientesDTO(){
        return servicioCliente.traerClientesDTO();
    }
    @GetMapping("/clientes/{id}")
    public  ClienteDTO traerClientePorId(@PathVariable Long id){
        return servicioCliente.traerClientePorId(id);
    }

    @PostMapping("/clientes")
    public ResponseEntity<Object> registrarCliente(@RequestBody ClienteRegistroDTO clienteRegistroDTO){

        if (clienteRegistroDTO.getNombre().isBlank() || clienteRegistroDTO.getApellido().isBlank() || clienteRegistroDTO.getEmail().isBlank() || clienteRegistroDTO.getContraseña().isBlank()|| clienteRegistroDTO.getDireccion().isBlank() || clienteRegistroDTO.getEdad() <= 0 || clienteRegistroDTO.getTelefono().isBlank() ){
            return new ResponseEntity<>("Falta informacion", HttpStatus.FORBIDDEN);
        }

        if (servicioCliente.traerClientePorEmail(clienteRegistroDTO.getEmail()) != null){

            return new ResponseEntity<>("El email esta en uso", HttpStatus.FORBIDDEN);
        }
        if (clienteRegistroDTO.getEdad() < 18){
            return new ResponseEntity<>( "Tenes que ser mayor para ingresar", HttpStatus.FORBIDDEN);
        }
        Cliente cliente = new Cliente(clienteRegistroDTO.getNombre(),clienteRegistroDTO.getApellido(), clienteRegistroDTO.getEmail(), codificadorDeContraseña.encode(clienteRegistroDTO.getContraseña()), clienteRegistroDTO.getDireccion(), clienteRegistroDTO.getTelefono(), clienteRegistroDTO.getEdad());
        servicioCliente.save(cliente);

        return new ResponseEntity<>("El usuario fue registrado exitosamente", HttpStatus.CREATED);

    }

    @GetMapping("/cliente/actual")
    public ResponseEntity<Object> getClientCurrent(Authentication authentication) {
        if (authentication.getName() == null){
            return new ResponseEntity<>("Cliente no autenticado", HttpStatus.BAD_REQUEST);
        }
        Cliente cliente = servicioCliente.traerClientePorEmail(authentication.getName());
        if (cliente != null) {
            ClienteDTO clientDTO = new ClienteDTO(cliente);
            return new ResponseEntity<>(clientDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/cliente/actual/editar")
    public ResponseEntity<Object> editarPerfil(@RequestBody ClienteDTO clienteDTO, Authentication authentication){
        Cliente clienteActual = servicioCliente.traerClientePorEmail(authentication.getName());

        if (clienteActual == null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
        if(clienteDTO.getNombre().isBlank()){
            return new ResponseEntity<>("Ingrese un nombre válido", HttpStatus.BAD_REQUEST);
        }
        if(clienteDTO.getApellido().isBlank()){
            return new ResponseEntity<>("Ingrese un apellido válido", HttpStatus.BAD_REQUEST);
        }
        if(clienteDTO.getEmail().isBlank()){
            return new ResponseEntity<>("Ingrese un email válido", HttpStatus.BAD_REQUEST);
        }
        if(clienteDTO.getEdad() < 18){
            return new ResponseEntity<>("Ingresa tu edad real", HttpStatus.BAD_REQUEST);
        }
        if(clienteDTO.getTelefono().isBlank()){
            return new ResponseEntity<>("Ingrese un telefono válido", HttpStatus.BAD_REQUEST);
        }
        if(clienteDTO.getDireccion().isBlank()){
            return new ResponseEntity<>("Ingrese una direccion válida", HttpStatus.BAD_REQUEST);
        }
        clienteActual.setNombre(clienteDTO.getNombre());
        clienteActual.setApellido(clienteDTO.getApellido());
        clienteActual.setEmail(clienteDTO.getEmail());
        clienteActual.setEdad(clienteDTO.getEdad());
        clienteActual.setTelefono(clienteDTO.getTelefono());
        clienteActual.setDireccion(clienteDTO.getDireccion());

        servicioCliente.save(clienteActual);
        return new ResponseEntity<>("Has actualizado tu perfil", HttpStatus.ACCEPTED);
    }
}