package com.comercialnova.front.beans.dashboard;

import com.comercialnova.front.modelo.Dashboard;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Component
public class DashboardBean implements Serializable {

    private Dashboard datos;
    private long totalProductos = 0;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "http://localhost:9090/api/dashboard";

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            datos = restTemplate.getForObject(API_URL, Dashboard.class);
            if (datos != null && datos.getNombresProductos() != null) {
                totalProductos = datos.getNombresProductos().size();
            }
        } catch (Exception e) {
            System.out.println("ERROR DE CONEXIÓN: " + e.getMessage());
        }
    }

    public Dashboard getDatos() { return datos; }
    public long getTotalProductos() { return totalProductos; }
}