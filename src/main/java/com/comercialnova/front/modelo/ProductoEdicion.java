package com.comercialnova.front.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductoEdicion implements Serializable {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Long version;

    public static ProductoEdicion de(Producto p) {
        ProductoEdicion e = new ProductoEdicion();
        e.id = p.getId();
        e.nombre = p.getNombre();
        e.precio = p.getPrecio();
        e.stock = p.getStock();
        e.version = p.getVersion();
        return e;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
