package com.comercialnova.front.core;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class FacesUtils {

    private FacesUtils() { }

    public static void msgInfo(String titulo, String detalle) {
        addMessage(FacesMessage.SEVERITY_INFO, titulo, detalle);
    }

    public static void msgWarning(String titulo, String detalle) {
        addMessage(FacesMessage.SEVERITY_WARN, titulo, detalle);
    }

    public static void msgError(String titulo, String detalle) {
        addMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle);
    }

    public static void addMessage(FacesMessage.Severity tipo, String titulo, String detalle) {
        titulo = titulo == null ? "Mensaje" : titulo;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, titulo, detalle));
    }
}
