package com.comercialnova.front.beans.ventas;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.ClienteApi;
import com.comercialnova.front.api.ProductoApi;
import com.comercialnova.front.api.VentaApi;
import com.comercialnova.front.core.CtrTemplate;
import com.comercialnova.front.modelo.Cliente;
import com.comercialnova.front.modelo.ItemCarrito;
import com.comercialnova.front.modelo.Producto;
import com.comercialnova.front.modelo.Venta;
import com.comercialnova.front.modelo.VentaNueva;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrVentaNueva")
@ViewScoped
public class CtrVentaNueva extends CtrTemplate {

    private final transient ClienteApi clienteApi;
    private final transient ProductoApi productoApi;
    private final transient VentaApi ventaApi;

    private List<Cliente> clientes = Collections.emptyList();
    private List<Producto> productos = Collections.emptyList();
    private final List<ItemCarrito> carrito = new ArrayList<>();

    private Long clienteId;
    private String vendedor = "Vendedor 1";
    private Long productoId;
    private Integer cantidad = 1;

    public CtrVentaNueva(ClienteApi clienteApi, ProductoApi productoApi, VentaApi ventaApi) {
        super("Nueva venta");
        this.clienteApi = clienteApi;
        this.productoApi = productoApi;
        this.ventaApi = ventaApi;
    }

    @PostConstruct
    public void init() {
        try {
            clientes = clienteApi.listar();
            if (!clientes.isEmpty()) {
                clienteId = clientes.get(0).getId();
            }
            productos = productoApi.listar();
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public void agregar() {
        Producto p = productos.stream().filter(x -> x.getId().equals(productoId)).findFirst().orElse(null);
        if (p == null) {
            msgWarning("Seleccione un producto");
            return;
        }
        if (cantidad == null || cantidad <= 0) {
            msgWarning("La cantidad debe ser mayor a cero");
            return;
        }
        for (ItemCarrito item : carrito) {
            if (item.getProductoId().equals(p.getId())) {
                item.sumar(cantidad);
                return;
            }
        }
        carrito.add(new ItemCarrito(p, cantidad));
    }

    public void quitar(ItemCarrito item) {
        carrito.remove(item);
    }

    public void nuevo() {
        carrito.clear();
        cantidad = 1;
    }

    public void registrar() {
        if (carrito.isEmpty()) {
            msgWarning("Agregue al menos un producto");
            return;
        }
        VentaNueva venta = new VentaNueva(clienteId, vendedor);
        carrito.forEach(i -> venta.agregar(i.getProductoId(), i.getCantidad()));
        try {
            Venta registrada = ventaApi.registrar(venta);
            msgInfo("Venta #" + registrada.getId() + " registrada. Total $" + registrada.getTotal());
            carrito.clear();
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
        productos = productoApi.listar();
    }

    public BigDecimal getTotal() {
        return carrito.stream().map(ItemCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Cliente> getClientes() { return clientes; }
    public List<Producto> getProductos() { return productos; }
    public List<ItemCarrito> getCarrito() { return carrito; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
