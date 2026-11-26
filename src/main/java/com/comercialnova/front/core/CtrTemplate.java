package com.comercialnova.front.core;

import java.io.Serializable;

public abstract class CtrTemplate implements Serializable {

    private final String titulo;

    protected CtrTemplate(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    protected void msgInfo(String detalle) {
        FacesUtils.msgInfo(titulo, detalle);
    }

    protected void msgWarning(String detalle) {
        FacesUtils.msgWarning(titulo, detalle);
    }

    protected void msgError(String detalle) {
        FacesUtils.msgError(titulo, detalle);
    }
}
