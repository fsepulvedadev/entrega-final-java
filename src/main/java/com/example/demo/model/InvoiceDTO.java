package com.example.demo.model;



import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class InvoiceDTO {

    private Integer comprobanteid;

    private Integer cantidad;

    private Date fecha;

    private BigDecimal total;

    private Client cliente;

    private Set<LineDTO> lineas;

    public Integer getComprobanteid() {
        return comprobanteid;
    }

    public void setComprobanteid(Integer comprobanteid) {
        this.comprobanteid = comprobanteid;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public Set<LineDTO> getLineas() {
        return lineas;
    }

    public void setLineas(Set<LineDTO> lineas) {
        this.lineas = lineas;
    }



}
