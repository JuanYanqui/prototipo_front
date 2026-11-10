package com.comercialnova.front.api;

import com.comercialnova.front.modelo.Producto;
import com.comercialnova.front.modelo.ProductoEdicion;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductoApi {

    private final RestCliente cliente;

    public ProductoApi(RestCliente cliente) {
        this.cliente = cliente;
    }

    public List<Producto> listar() {
        return cliente.llamar(() -> Arrays.asList(cliente.rest().getForObject("/api/productos", Producto[].class)));
    }

    public Producto obtener(Long id) {
        return cliente.llamar(() -> cliente.rest().getForObject("/api/productos/{id}", Producto.class, id));
    }

    public void actualizar(Long id, ProductoEdicion datos) {
        cliente.llamar(() -> {
            cliente.rest().put("/api/productos/{id}", datos, id);
            return null;
        });
    }
}
