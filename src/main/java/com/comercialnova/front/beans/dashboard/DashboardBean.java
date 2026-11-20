package com.comercialnova.front.beans.dashboard;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.RestCliente;
import com.comercialnova.front.core.FacesUtils;
import com.comercialnova.front.modelo.Dashboard;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Component
public class DashboardBean implements Serializable {

    private final transient RestCliente cliente;

    private Dashboard datos;
    private long totalProductos = 0;

    public DashboardBean(RestCliente cliente) {
        this.cliente = cliente;
    }

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            datos = cliente.llamar(() -> cliente.rest().getForObject("/api/dashboard", Dashboard.class));
            if (datos != null && datos.getNombresProductos() != null) {
                totalProductos = datos.getNombresProductos().size();
            }
        } catch (ApiException e) {
            FacesUtils.msgError("Dashboard", e.getMessage());
        }
    }

    public Dashboard getDatos() {
        return datos;
    }

    public long getTotalProductos() {
        return totalProductos;
    }
}
