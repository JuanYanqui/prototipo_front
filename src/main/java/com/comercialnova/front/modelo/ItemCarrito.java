package com.comercialnova.front.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemCarrito implements Serializable {
    private final Long productoId;
    private final String nombre;
    private final BigDecimal precio;
    private int cantidad;

    public ItemCarrito(Producto p, int cantidad) {
        this.productoId = p.getId();
        this.nombre = p.getNombre();
        this.precio = p.getPrecio();
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() { return precio.multiply(BigDecimal.valueOf(cantidad)); }
    public void sumar(int mas) { this.cantidad += mas; }

    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
}
