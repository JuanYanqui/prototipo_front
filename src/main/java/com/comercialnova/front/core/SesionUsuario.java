package com.comercialnova.front.core;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.AuthApi;
import com.comercialnova.front.modelo.SesionInfo;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component("SesionUsuario")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SesionUsuario implements Serializable {

    private final transient AuthApi authApi;

    private String sessionId;
    private Long usuarioId;
    private String username;
    private String nombre;
    private String rol;

    public SesionUsuario(AuthApi authApi) {
        this.authApi = authApi;
    }

    public void iniciar(SesionInfo info) {
        this.sessionId = info.getSessionId();
        this.usuarioId = info.getUsuarioId();
        this.username = info.getUsername();
        this.nombre = info.getNombre();
        this.rol = info.getRol();
    }

    public String cerrar() {
        try {
            if (sessionId != null) {
                authApi.logout();
            }
        } catch (ApiException ignorada) {
            // Si el back ya no tiene la sesion, igual limpiamos el front.
        }
        limpiar();
        return "/login/login?faces-redirect=true";
    }

    public void limpiar() {
        sessionId = null;
        usuarioId = null;
        username = null;
        nombre = null;
        rol = null;
    }

    public boolean isAutenticado() {
        return sessionId != null && !sessionId.isEmpty();
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }

    public boolean isVendedor() {
        return "VENDEDOR".equalsIgnoreCase(rol);
    }

    public void verificarAcceso() {
        FacesContext faces = FacesContext.getCurrentInstance();
        if (faces == null || faces.getViewRoot() == null) {
            return;
        }
        String viewId = faces.getViewRoot().getViewId();
        if (viewId != null && viewId.contains("/login/")) {
            return;
        }
        if (!isAutenticado()) {
            faces.getApplication().getNavigationHandler()
                    .handleNavigation(faces, null, "/login/login?faces-redirect=true");
            return;
        }
        boolean zonaAdmin = viewId != null
                && (viewId.contains("/inventario/") || viewId.contains("/demo/"));
        if (zonaAdmin && !isAdmin()) {
            FacesUtils.msgWarning("Acceso", "Su perfil no tiene permiso para esa pantalla");
            faces.getApplication().getNavigationHandler()
                    .handleNavigation(faces, null, "/index?faces-redirect=true");
        }
    }

    public String getSessionId() { return sessionId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
}
