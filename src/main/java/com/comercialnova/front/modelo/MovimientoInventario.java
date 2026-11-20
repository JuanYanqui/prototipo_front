package com.comercialnova.front.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MovimientoInventario implements Serializable {
    private Long id;
    private Long productoId;
    private String producto;
    private String tipo;
    private int cantidad;
    private int stockAnterior;
    private int stockNuevo;
    private String motivo;
    private String responsable;
    private LocalDateTime fecha;

    public boolean isEntrada() { return "ENTRADA".equals(tipo); }
    public boolean isSalida() { return "SALIDA".equals(tipo); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public int getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(int stockAnterior) { this.stockAnterior = stockAnterior; }
    public int getStockNuevo() { return stockNuevo; }
    public void setStockNuevo(int stockNuevo) { this.stockNuevo = stockNuevo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
