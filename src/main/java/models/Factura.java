package models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Factura {
    private int id;
    private String numeroFactura;
    private LocalDate fecha;
    private int idCliente;
    private BigDecimal total;

    public Factura() {}

    public Factura(int id, String numeroFactura, LocalDate fecha, int idCliente, BigDecimal total) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return id + " - " + numeroFactura;
    }
}
