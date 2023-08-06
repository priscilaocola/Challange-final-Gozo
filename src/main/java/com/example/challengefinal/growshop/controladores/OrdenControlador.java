package com.example.challengefinal.growshop.controladores;

import com.example.challengefinal.growshop.Repositorios.OrdenProductoRepositorio;
import com.example.challengefinal.growshop.Repositorios.OrdenRepositorio;
import com.example.challengefinal.growshop.dto.*;
import com.example.challengefinal.growshop.models.*;
import com.example.challengefinal.growshop.servicios.*;
import com.example.challengefinal.growshop.utils.NumeroOrden;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrdenControlador {

    @Autowired
    private ServicioOrden servicioOrden;
    @Autowired
    private OrdenProductoRepositorio ordenProductoRepositorio;
    @Autowired
    private ServicioProducto servicioProducto;
    @Autowired
    private ServicioCliente servicioCliente;
    @Autowired
    private ServicioFacturacion servicioFacturacion;
    @Autowired
    private ServicioOrdenProducto servicioOrdenProducto;
    @Autowired
    private EmailSend emailSend;

    @GetMapping("/ordenes")
    public Set<OrdenDTO> traerOrdenesDTO(){
        return servicioOrden.traerOrdenesDTO();
    }

    @GetMapping("/ordenes/{id}")
    public OrdenDTO traerOrdenDTO(@PathVariable Long id){
        return servicioOrden.traerOrdenDTO(id);
    }

    @PostMapping("/crear/orden")
    public ResponseEntity<Object> crearOrden(@RequestBody Set<OrdenInfoDTO> productosCarrito, Authentication authentication) throws DocumentException, IOException {
        Cliente cliente = servicioCliente.traerClientePorEmail(authentication.getName());

        double totalCompra = 0;
        for (OrdenInfoDTO producto : productosCarrito) {
            totalCompra += producto.getTotal() * producto.getTotalProductos();
        }

        String numeroDeOrden = NumeroOrden.getRandomNum();
        Orden orden = new Orden();
        do {
            orden.setNumeroDeOrden(numeroDeOrden);
        } while (servicioOrden.traerOrdenPorNumero(numeroDeOrden) != null);

        orden.setFecha(LocalDateTime.now());
        orden.setCliente(cliente);
        orden.setMontoTotal(totalCompra);

        servicioOrden.save(orden);

        for (OrdenInfoDTO producto : productosCarrito) {
            // Crear un nuevo objeto OrdenProducto para cada producto en la orden
            OrdenProducto ordenProducto = new OrdenProducto();
            ordenProducto.setPrecioUnitario(producto.getTotal());
            ordenProducto.setCantidadDeProductos(producto.getTotalProductos());
            ordenProducto.setNombre(producto.getNombre());
            // Asociar el objeto OrdenProducto con la orden y el producto correspondiente
            Producto productoEntity = servicioProducto.traerProductoPorId(producto.getId());
            ordenProducto.setOrden(orden);
            ordenProducto.setProducto(productoEntity);
            // Guardar el objeto OrdenProducto en la base de datos
            servicioOrdenProducto.save(ordenProducto);
        }
        OrdenDTO ordenDTO = new OrdenDTO(orden);
        // Enviar el correo con la factura adjunta
        try {
            // Generar el Correo
            Correo correo = new Correo();
            correo.setRemitente("growshopgozo@gmail.com");
            correo.setDestinatario(cliente.getEmail());
            correo.setAsunto("Factura de compra - Orden #" + orden.getNumeroDeOrden());
            correo.setComentario("Adjuntamos la factura de su compra realizada en nuestro Growshop.");

            // Generar el PDF
            ByteArrayOutputStream pdfData = servicioFacturacion.generarFacturaPDF(ordenDTO, productosCarrito, cliente);

            // Enviar el correo electrónico con la factura adjunta
            ResponseEntity<Object> response = emailSend.sendFactura(correo, pdfData.toByteArray());
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("Orden generada y correo enviado con éxito", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Orden generada, pero hubo un problema al enviar el correo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            // Si ocurre alguna excepción al generar la factura o enviar el correo, manejarla aquí
            e.printStackTrace();
            return new ResponseEntity<>("Error al generar la factura o enviar el correo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
