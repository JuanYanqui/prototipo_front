package com.comercialnova.front.api;

import com.comercialnova.front.modelo.Cliente;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ClienteApi {

    private final RestCliente cliente;

    public ClienteApi(RestCliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> listar() {
        return cliente.llamar(() -> Arrays.asList(cliente.rest().getForObject("/api/clientes", Cliente[].class)));
    }
}
