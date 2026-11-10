package com.comercialnova.front.modelo;

import java.util.ArrayList;
import java.util.List;

public class VentaNueva {
    private final Long clienteId;
    private final String vendedor;
    private final List<Item> items = new ArrayList<>();

    public VentaNueva(Long clienteId, String vendedor) {
        this.clienteId = clienteId;
        this.vendedor = vendedor;
    }

    public VentaNueva agregar(Long productoId, int cantidad) {
        items.add(new Item(productoId, cantidad));
        return this;
    }

    public Long getClienteId() { return clienteId; }
    public String getVendedor() { return vendedor; }
    public List<Item> getItems() { return items; }

    public static class Item {
        private final Long productoId;
        private final int cantidad;

        public Item(Long productoId, int cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }

        public Long getProductoId() { return productoId; }
        public int getCantidad() { return cantidad; }
    }
}
