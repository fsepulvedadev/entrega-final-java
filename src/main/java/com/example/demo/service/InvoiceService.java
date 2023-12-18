package com.example.demo.service;

import com.example.demo.exception.InvoiceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InvoiceService {

    @Autowired
   private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestTemplate restTemplate;


    public InvoiceDTO create (Invoice newInvoice) {




        System.out.println(newInvoice.getLineas());

        // VALIDACION DE CLIENTE EXISTENTE

        Boolean existeClient = existeClient(newInvoice.getClient());

        // VALIDACION DE PRODUCTO EXISTENTE

        Boolean existenProducts = existeProducts(newInvoice.getLineas());

        // VALIDACION DE QUE HAYA STOCK DISPONIBLE

        Boolean existeStock = existeStock(newInvoice.getLineas());

        if( existeClient && existenProducts && existeStock) {

            Invoice invoiceAGuardar = armarInvoice(newInvoice);

            actualizarStock(invoiceAGuardar.getLineas());

            return crearInvoiceDTO(this.invoiceRepository.save(invoiceAGuardar));
        } else {
            return new InvoiceDTO();
        }

    }

    public List<InvoiceDTO> findAll () {return crearInvoicesDTO(this.invoiceRepository.findAll());}

    public InvoiceDTO findById(Integer id) {
        Optional<Invoice> optionalInvoice = this.invoiceRepository.findById(id);
        if(optionalInvoice.isPresent()) {
            return crearInvoiceDTO(optionalInvoice.get());
        } else {
            return new InvoiceDTO();
        }
    }



    public void delete (Integer id) throws InvoiceNotFoundException {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);

        if(invoiceOptional.isPresent()) {
            this.invoiceRepository.deleteById((id));
        } else {
            throw new InvoiceNotFoundException("No se encontro una factura con ese id.");
        }
    }



    private Invoice armarInvoice(Invoice invoice) {

        Invoice invoiceAGuardar = new Invoice();

        invoiceAGuardar.setClient(this.clientRepository.findById(invoice.getClient().getClientid()).get());

        TimeApi timeApi = this.restTemplate.getForObject("https://timeapi.io/api/TimeZone/zone?timeZone=America/Argentina/Buenos_Aires", TimeApi.class);

       // WorldClock worldClock = this.restTemplate.getForObject("http://worldclockapi.com/api/json/utc/now", WorldClock.class);

        String currentDateTime = timeApi.getCurrentLocalTime();




        try {

            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").parse(currentDateTime);

            invoiceAGuardar.setCreatedAt(date1);

        } catch (ParseException e) {

            e.printStackTrace();

            invoiceAGuardar.setCreatedAt(new Date());

        }

        invoiceAGuardar.setLineas(new HashSet<Line>());
        for(Line linea : invoice.getLineas()) {
            invoiceAGuardar.addLine(crearLine(linea));
        }

        invoiceAGuardar.setTotal(calcularTotal(invoiceAGuardar.getLineas()));
        invoiceAGuardar.setQuantity(invoice.getLineas().size());

        return invoiceAGuardar;
    }

    private Line crearLine(Line linea) {
        Line lineAGuardar = new Line();

        Product productDB = this.productRepository.findById(linea.getProduct().getProductid()).get();


        lineAGuardar.setQuantity(linea.getQuantity());
        lineAGuardar.setDescription(productDB.getDescription());
        lineAGuardar.setPrice(productDB.getPrice());

        lineAGuardar.setProduct(productDB);

        return lineAGuardar;
    }

    private BigDecimal calcularTotal(Set<Line> lineas) {
        BigDecimal total = new BigDecimal("0");

        for(Line linea : lineas) {
            total = total.add(linea.getPrice());
            System.out.println(total);
                 //   total.add( linea.getPrice().multiply(BigDecimal.valueOf(linea.getQuantity())) );
        }

        return total;
    }

    private void actualizarStock(Set<Line> lineas) {
        for (Line linea : lineas) {
            Integer cantidadVendida = linea.getQuantity();
            Product product = linea.getProduct();

            Product productDB = this.productRepository.getReferenceById(product.getProductid());
            Integer stock = productDB.getStock();
            Integer nuevoStock = stock - cantidadVendida;
            productDB.setStock(nuevoStock);

            this.productRepository.save(productDB);

        }
    }

    private List<InvoiceDTO> crearInvoicesDTO(List<Invoice> comprobantes) {
        List<InvoiceDTO> comprobantesDTOs = new ArrayList<>();
        for (Invoice comprobante : comprobantes) {
            comprobantesDTOs.add(this.crearInvoiceDTO(comprobante));
        }
        return comprobantesDTOs;
    }

    private InvoiceDTO crearInvoiceDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();

        dto.setComprobanteid(invoice.getInvoiceid());
        dto.setCantidad(invoice.getQuantity());
        dto.setFecha(invoice.getCreatedAt());
        dto.setTotal(invoice.getTotal());
        dto.setCliente(invoice.getClient());
        dto.setLineas(crearLineasDTO(invoice.getLineas()));

        return dto;

    }

    private Set<LineDTO> crearLineasDTO(Set<Line> lineas) {
        Set<LineDTO> dtos = new HashSet<>();

        for (Line linea : lineas) {
            LineDTO dto = new LineDTO();
            dto.setLineaid(linea.getLineid());
            dto.setCantidad(linea.getQuantity());
            dto.setDescripcion(linea.getDescription());
            dto.setPrecio(linea.getPrice());

            dtos.add(dto);
        }
        return dtos;
    }


    // VALIDATORS

    private Boolean existeClient(Client cliente) {
        Optional<Client> opClient = this.clientRepository.findById(cliente.getClientid());

        return opClient.isPresent();
    }

    private Boolean existeProducts(Set<Line> lineas) {
        for (Line linea : lineas) {
            Integer productId = linea.getProduct().getProductid();
            Optional<Product> opProduct = this.productRepository.findById(productId);
            if(opProduct.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Boolean existeStock(Set<Line> lineas) {
        for(Line linea : lineas) {
            Integer productId = linea.getProduct().getProductid();
            Optional<Product> optionalProduct = this.productRepository.findById(productId);
            if(optionalProduct.isEmpty()) {
                return false;
            }
            if(linea.getQuantity() <= optionalProduct.get().getStock()) {
                return true;
            }
        }
        return false;

    }

}
