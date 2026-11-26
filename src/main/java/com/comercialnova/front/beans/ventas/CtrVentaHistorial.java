package com.comercialnova.front.beans.ventas;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.VentaApi;
import com.comercialnova.front.core.CtrTemplate;
import com.comercialnova.front.modelo.Venta;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrVentaHistorial")
@ViewScoped
public class CtrVentaHistorial extends CtrTemplate {

    private final transient VentaApi ventaApi;

    private List<Venta> ventas = Collections.emptyList();

    public CtrVentaHistorial(VentaApi ventaApi) {
        super("Historial de ventas");
        this.ventaApi = ventaApi;
    }

    @PostConstruct
    public void init() {
        filtrar();
    }

    public void filtrar() {
        try {
            ventas = ventaApi.listar();
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public List<Venta> getVentas() { return ventas; }
}
