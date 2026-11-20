package com.comercialnova.front.beans.inventario;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.ProductoApi;
import com.comercialnova.front.core.CtrTemplate;
import com.comercialnova.front.modelo.MovimientoInventario;
import com.comercialnova.front.modelo.Producto;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrKardex")
@ViewScoped
public class CtrKardex extends CtrTemplate {

    private final transient ProductoApi productoApi;

    private List<Producto> productos = Collections.emptyList();
    private List<MovimientoInventario> movimientos = Collections.emptyList();
    private Long productoId;
    private Producto producto;

    public CtrKardex(ProductoApi productoApi) {
        super("Kardex de inventario");
        this.productoApi = productoApi;
    }

    @PostConstruct
    public void init() {
        try {
            productos = productoApi.listar();
            String param = FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("productoId");
            if (param != null && param.matches("\\d+")) {
                productoId = Long.valueOf(param);
            } else if (!productos.isEmpty()) {
                productoId = productos.get(0).getId();
            }
            consultar();
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public void consultar() {
        if (productoId == null) {
            movimientos = Collections.emptyList();
            return;
        }
        try {
            producto = productoApi.obtener(productoId);
            movimientos = productoApi.movimientos(productoId);
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public List<Producto> getProductos() { return productos; }
    public List<MovimientoInventario> getMovimientos() { return movimientos; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Producto getProducto() { return producto; }
}
