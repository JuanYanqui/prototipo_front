package com.comercialnova.front.beans.inventario;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.ProductoApi;
import com.comercialnova.front.core.CtrTemplate;
import org.primefaces.PrimeFaces;
import com.comercialnova.front.modelo.MovimientoInventario;
import com.comercialnova.front.modelo.Producto;
import com.comercialnova.front.modelo.ProductoEdicion;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrInventarioProductos")
@ViewScoped
public class CtrInventarioProductos extends CtrTemplate {

    private final transient ProductoApi productoApi;

    private List<Producto> productos = Collections.emptyList();
    private ProductoEdicion edicion;

    private Producto productoIngreso;
    private Integer cantidadIngreso = 1;
    private String motivoIngreso;

    public CtrInventarioProductos(ProductoApi productoApi) {
        super("Inventario de productos");
        this.productoApi = productoApi;
    }

    @PostConstruct
    public void init() {
        filtrar();
    }

    public void filtrar() {
        try {
            productos = productoApi.listar();
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public void editar(Producto p) {
        edicion = ProductoEdicion.de(p);
    }

    public void guardar() {
        try {
            productoApi.actualizar(edicion.getId(), edicion);
            Producto actualizado = productoApi.obtener(edicion.getId());
            edicion = ProductoEdicion.de(actualizado);
            msgInfo("Producto guardado (nueva version " + actualizado.getVersion() + ")");
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
        filtrar();
    }

    public void cancelar() {
        edicion = null;
    }

    public boolean isEditando() {
        return edicion != null;
    }

    public void prepararIngreso(Producto p) {
        productoIngreso = p;
        cantidadIngreso = 1;
        motivoIngreso = null;
    }

    public void registrarIngreso() {
        if (productoIngreso == null || cantidadIngreso == null || cantidadIngreso <= 0) {
            msgWarning("Indique una cantidad mayor a cero");
            return;
        }
        try {
            MovimientoInventario m = productoApi.ingresar(productoIngreso.getId(), cantidadIngreso, motivoIngreso);
            msgInfo("Ingreso registrado: " + productoIngreso.getNombre() + " pasa de "
                    + m.getStockAnterior() + " a " + m.getStockNuevo() + " unidades");
            PrimeFaces.current().ajax().addCallbackParam("guardado", true);
            edicion = null;
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
        filtrar();
    }

    public long getTotalBajoMinimo() {
        return productos.stream().filter(Producto::isBajoMinimo).count();
    }

    public List<Producto> getProductos() { return productos; }
    public ProductoEdicion getEdicion() { return edicion; }
    public Producto getProductoIngreso() { return productoIngreso; }
    public Integer getCantidadIngreso() { return cantidadIngreso; }
    public void setCantidadIngreso(Integer cantidadIngreso) { this.cantidadIngreso = cantidadIngreso; }
    public String getMotivoIngreso() { return motivoIngreso; }
    public void setMotivoIngreso(String motivoIngreso) { this.motivoIngreso = motivoIngreso; }
}
