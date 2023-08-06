package com.example.challengefinal.growshop.servicios;

import com.example.challengefinal.growshop.dto.OrdenDTO;
import com.example.challengefinal.growshop.dto.OrdenInfoDTO;
import com.example.challengefinal.growshop.models.Cliente;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
@Service
public class ServicioFacturacion {

    @Autowired
    private EmailSend emailSend;
    @Autowired
    private ResourceLoader resourceLoader;


    public ByteArrayOutputStream generarFacturaPDF(OrdenDTO orden, Set<OrdenInfoDTO> ordenProductos, Cliente cliente) throws DocumentException, IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);


            document.open();
            // Agregar encabezado de la factura
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph encabezado = new Paragraph("Factura de compra en Gozo Growshop", tituloFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setSpacingBefore(20);
            document.add(encabezado);

            document.add(new Paragraph("\n"));

        // Agregar información de la orden (encabezado)
            Paragraph infoOrden = new Paragraph("Número de orden: " + orden.getNumeroDeOrden());
            Paragraph clienteCompra = new Paragraph("Nombre del cliente: " + cliente.getNombre() + " " + cliente.getApellido());
            Paragraph montoTotal = new Paragraph("Monto total de la compra: $" + calcularTotal(ordenProductos));
            document.add(infoOrden);
            document.add(clienteCompra);
            document.add(montoTotal);

            // Agregar tabla con los productos y sus detalles

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float padding = 12f;
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Nombre"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(padding);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Cantidad"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(padding);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Precio unitario"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(padding);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Precio por cantidad"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(padding);
            table.addCell(cell);


        for (OrdenInfoDTO ordenProducto : ordenProductos) {
            float paddingCell = 9f;
            PdfPCell cellNombre = new PdfPCell(new Phrase(ordenProducto.getNombre()));
            cellNombre.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellNombre.setVerticalAlignment(Element.ALIGN_CENTER);
            cellNombre.setPadding(paddingCell);
            table.addCell(cellNombre);

            PdfPCell cellCantidad = new PdfPCell(new Phrase(String.valueOf(ordenProducto.getTotalProductos())));
            cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCantidad.setVerticalAlignment(Element.ALIGN_CENTER);
            cellCantidad.setPadding(paddingCell);
            table.addCell(cellCantidad);

            PdfPCell cellPrecioUnitario = new PdfPCell(new Phrase("$ " + ordenProducto.getTotal()));
            cellPrecioUnitario.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellPrecioUnitario.setVerticalAlignment(Element.ALIGN_CENTER);
            cellPrecioUnitario.setPadding(paddingCell);
            table.addCell(cellPrecioUnitario);

            PdfPCell cellPrecioTotal = new PdfPCell(new Phrase("$ " + ordenProducto.getTotal() * ordenProducto.getTotalProductos()));
            cellPrecioTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellPrecioTotal.setVerticalAlignment(Element.ALIGN_CENTER);
            cellPrecioTotal.setPadding(paddingCell);
            table.addCell(cellPrecioTotal);
        }

                document.add(table);

            // Agregar total de la factura
            Paragraph total = new Paragraph("Total: $" + calcularTotal(ordenProductos));
            document.add(total);

            document.add(new Paragraph("\n"));

            Resource logoResource = resourceLoader.getResource("classpath:static/assets/images/logo.png");
            Image logo = Image.getInstance(logoResource.getURL());
            logo.setAlignment(Image.ALIGN_CENTER);
            logo.scaleToFit(200, 200);
            document.add(logo);

            document.close();

        return outputStream;
    }

    private double calcularTotal(Set<OrdenInfoDTO> ordenProductos) {
        double total = 0;
        for (OrdenInfoDTO ordenProducto : ordenProductos) {
            total += ordenProducto.getTotalProductos() * ordenProducto.getTotal();
        }
        return total;
    }
}
