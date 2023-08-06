package com.example.challengefinal.growshop.servicios.Implement;

import com.example.challengefinal.growshop.Repositorios.OrdenProductoRepositorio;
import com.example.challengefinal.growshop.models.OrdenProducto;
import com.example.challengefinal.growshop.servicios.ServicioOrdenProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioOrdenProductoImplement implements ServicioOrdenProducto {

    @Autowired
    private OrdenProductoRepositorio ordenProductoRepositorio;

    @Override
    public void save(OrdenProducto ordenProducto) {
        ordenProductoRepositorio.save(ordenProducto);
    }
}