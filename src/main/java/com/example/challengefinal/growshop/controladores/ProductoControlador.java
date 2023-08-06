package com.example.challengefinal.growshop.controladores;

import ch.qos.logback.core.net.server.Client;
import com.example.challengefinal.growshop.dto.ProductoDTO;
import com.example.challengefinal.growshop.dto.ProductoModificarDTO;
import com.example.challengefinal.growshop.models.Cliente;
import com.example.challengefinal.growshop.models.Producto;
import com.example.challengefinal.growshop.servicios.ServicioCliente;
import com.example.challengefinal.growshop.servicios.ServicioProducto;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductoControlador {

    @Autowired
    private ServicioProducto servicioProducto;

    @Autowired
    private ServicioCliente servicioCliente;

    @GetMapping("/productos")
    public List<ProductoDTO> traerProductosDTO() {
        return servicioProducto.traerProductosDTO();
    }

    @GetMapping("/productos/activos")
    public List<ProductoDTO> traerProductosDTOactivos() {
        return servicioProducto.traerProductosDTO().stream().filter(ProductoDTO::isActivo).collect(Collectors.toList());
    }


    @GetMapping("/productos/{id}")
    public ProductoDTO traerProductoDTO(@PathVariable Long id) {
        return servicioProducto.traerProductoDTO(id);
    }



    @PatchMapping("/productos/{id}/deactivate")
    public ResponseEntity<Object> borrarProducto(@PathVariable Long id, Authentication authentication) {
        Cliente admin = servicioCliente.traerClientePorEmail(authentication.getName());

        if (admin == null) {
            return new ResponseEntity<>("No estas autenticado", HttpStatus.BAD_REQUEST);
        }
        Optional<Producto> productoOptional = Optional.ofNullable(servicioProducto.traerProductoPorId(id));

        if (productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoOptional.get();

        producto.setActivo(false);
        servicioProducto.save(producto);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/productos/{Id}/activate")
    public ResponseEntity<Object> activarProductos(@PathVariable Long Id, Authentication authentication) {
        Cliente admin = servicioCliente.traerClientePorEmail(authentication.getName());

        if (admin == null) {
            return new ResponseEntity<>("No estas autenticado", HttpStatus.BAD_REQUEST);
        }

        Optional<Producto> productoOptional = Optional.ofNullable(servicioProducto.traerProductoPorId(Id));

        List<ProductoDTO> productoDTOS = servicioProducto.traerProductosDTO().stream().filter(ProductoDTO::isActivo).collect(Collectors.toList());

        if (productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoOptional.get();

        producto.setActivo(true);
        servicioProducto.save(producto);
        return ResponseEntity.ok().build();

    }

    @PatchMapping("/productos/{id}/modificar")
    public ResponseEntity<Object> modificarProductos(@PathVariable long id, @RequestBody ProductoModificarDTO modificacion, Authentication authentication){
        Cliente admin = servicioCliente.traerClientePorEmail(authentication.getName());

        if (admin == null) {
            return new ResponseEntity<>("No estas autenticado", HttpStatus.BAD_REQUEST);
        }

        Optional<Producto> productoOptional = Optional.ofNullable(servicioProducto.traerProductoPorId(id));
        if (productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (modificacion.getNombre() == null) {
            return new ResponseEntity<>("El producto no existe", HttpStatus.BAD_REQUEST);
        }
        if (modificacion.getCantidad() <= 0) {
            return new ResponseEntity<>("Asigne una cantidad de stock", HttpStatus.BAD_REQUEST);
        }
        if (modificacion.getDescripcion().isBlank()) {
            return new ResponseEntity<>("Defina una descripcion del producto", HttpStatus.BAD_REQUEST);
        }
        if (modificacion.getPrecio() <= 0) {
            return new ResponseEntity<>("Defina un precio para el producto", HttpStatus.BAD_REQUEST);
        }
        if (modificacion.getMarca() == null) {
            return new ResponseEntity<>("Defina una marca para el producto", HttpStatus.BAD_REQUEST);
        }

        Producto producto = productoOptional.get();

        producto.setNombre(modificacion.getNombre());
        producto.setMarca(modificacion.getMarca());
        producto.setDescripcion(modificacion.getDescripcion());
        producto.setCantidad(modificacion.getCantidad());
        producto.setPrecio(modificacion.getPrecio());

        servicioProducto.save(producto);

        return ResponseEntity.ok().build();
    }

}


