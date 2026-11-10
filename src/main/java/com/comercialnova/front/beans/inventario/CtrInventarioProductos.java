package com.comercialnova.front.beans.inventario;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.ProductoApi;
import com.comercialnova.front.core.CtrTemplate;
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

    public List<Producto> getProductos() { return productos; }
    public ProductoEdicion getEdicion() { return edicion; }
}
