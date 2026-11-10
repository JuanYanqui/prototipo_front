package com.comercialnova.front.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Producto implements Serializable {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private Long version;

    public boolean isAgotado() { return stock == 0; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
