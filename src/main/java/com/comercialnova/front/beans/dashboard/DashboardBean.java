package com.comercialnova.front.beans.dashboard;

import com.comercialnova.front.modelo.Dashboard;
import org.springframework.beans.factory.annotation.Value;
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

    // Inyecta la URL automáticamente desde el application.properties
    @Value("${api.url}")
    private String apiBaseUrl;

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            // Concatenamos la URL base (http://localhost:8090) con el endpoint
            String endpoint = apiBaseUrl + "/api/dashboard";
            
            datos = restTemplate.getForObject(endpoint, Dashboard.class);
            if (datos != null && datos.getNombresProductos() != null) {
                totalProductos = datos.getNombresProductos().size();
            }
        } catch (Exception e) {
            System.out.println("ERROR DE CONEXIÓN CON EL BACKEND: " + e.getMessage());
        }
    }

    // Getters para que el XHTML pueda leer los datos
    public Dashboard getDatos() { 
        return datos; 
    }
    
    public long getTotalProductos() { 
        return totalProductos; 
    }
}