package com.comercialnova.front.modelo;

public class SesionInfo {

    private String sessionId;
    private Long usuarioId;
    private String username;
    private String nombre;
    private String rol;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }
}
