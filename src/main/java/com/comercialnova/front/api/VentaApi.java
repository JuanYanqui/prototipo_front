package com.comercialnova.front.api;

import com.comercialnova.front.modelo.Venta;
import com.comercialnova.front.modelo.VentaNueva;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VentaApi {

    private final RestCliente cliente;

    public VentaApi(RestCliente cliente) {
        this.cliente = cliente;
    }

    public List<Venta> listar() {
        return cliente.llamar(() -> Arrays.asList(cliente.rest().getForObject("/api/ventas", Venta[].class)));
    }

    public Venta registrar(VentaNueva venta) {
        return cliente.llamar(() -> cliente.rest().postForObject("/api/ventas", venta, Venta.class));
    }
}
