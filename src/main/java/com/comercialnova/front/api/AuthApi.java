package com.comercialnova.front.api;

import com.comercialnova.front.modelo.LoginRequest;
import com.comercialnova.front.modelo.SesionInfo;
import org.springframework.stereotype.Component;

@Component
public class AuthApi {

    private final RestCliente cliente;

    public AuthApi(RestCliente cliente) {
        this.cliente = cliente;
    }

    public SesionInfo login(String username, String password) {
        return cliente.llamar(() -> cliente.rest().postForObject(
                "/api/auth/login", new LoginRequest(username, password), SesionInfo.class));
    }

    public void logout() {
        cliente.llamar(() -> {
            cliente.rest().postForObject("/api/auth/logout", null, Void.class);
            return null;
        });
    }
}
