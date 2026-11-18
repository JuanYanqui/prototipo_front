package com.comercialnova.front.beans.login;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.AuthApi;
import com.comercialnova.front.core.CtrTemplate;
import com.comercialnova.front.core.SesionUsuario;
import com.comercialnova.front.modelo.SesionInfo;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrLogin")
@ViewScoped
public class CtrLogin extends CtrTemplate {

    private final transient AuthApi authApi;
    private final transient SesionUsuario sesionUsuario;

    private String username;
    private String password;

    public CtrLogin(AuthApi authApi, SesionUsuario sesionUsuario) {
        super("Iniciar sesion");
        this.authApi = authApi;
        this.sesionUsuario = sesionUsuario;
    }

    public String ingresar() {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            msgWarning("Ingrese usuario y contraseña");
            return null;
        }
        try {
            SesionInfo info = authApi.login(username.trim(), password);
            sesionUsuario.iniciar(info);
            password = null;
            return "/index?faces-redirect=true";
        } catch (ApiException e) {
            msgError(e.getMessage());
            return null;
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
