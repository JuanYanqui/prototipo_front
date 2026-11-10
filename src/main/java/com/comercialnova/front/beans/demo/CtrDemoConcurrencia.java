package com.comercialnova.front.beans.demo;

import com.comercialnova.front.api.ApiException;
import com.comercialnova.front.api.ClienteApi;
import com.comercialnova.front.api.ProductoApi;
import com.comercialnova.front.api.VentaApi;
import com.comercialnova.front.core.CtrTemplate;
import com.comercialnova.front.modelo.Producto;
import com.comercialnova.front.modelo.VentaNueva;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("CtrDemoConcurrencia")
@ViewScoped
public class CtrDemoConcurrencia extends CtrTemplate {

    private final transient ClienteApi clienteApi;
    private final transient ProductoApi productoApi;
    private final transient VentaApi ventaApi;

    private List<Producto> productos = Collections.emptyList();
    private Long productoId;
    private Integer vendedores = 20;

    private boolean hayResultado;
    private int stockAntes;
    private int aceptadas;
    private int rechazadas;
    private int otrosErrores;
    private int stockDespues;

    public CtrDemoConcurrencia(ClienteApi clienteApi, ProductoApi productoApi, VentaApi ventaApi) {
        super("Demostracion de concurrencia");
        this.clienteApi = clienteApi;
        this.productoApi = productoApi;
        this.ventaApi = ventaApi;
    }

    @PostConstruct
    public void init() {
        try {
            productos = productoApi.listar();
            productos.stream().filter(p -> p.getStock() == 1).findFirst()
                    .ifPresent(p -> productoId = p.getId());
        } catch (ApiException e) {
            msgError(e.getMessage());
        }
    }

    public void simular() {
        if (productoId == null || vendedores == null || vendedores < 2 || vendedores > 100) {
            msgWarning("Seleccione un producto e indique entre 2 y 100 vendedores");
            return;
        }
        try {
            final Long clienteId = clienteApi.listar().get(0).getId();
            final Long id = productoId;
            stockAntes = productoApi.obtener(id).getStock();

            AtomicInteger ok = new AtomicInteger();
            AtomicInteger sinStock = new AtomicInteger();
            AtomicInteger otros = new AtomicInteger();
            CountDownLatch largada = new CountDownLatch(1);
            ExecutorService pool = Executors.newFixedThreadPool(vendedores);

            for (int i = 1; i <= vendedores; i++) {
                final String vendedor = "Vendedor simultaneo " + i;
                pool.submit(() -> {
                    try {
                        largada.await();
                        ventaApi.registrar(new VentaNueva(clienteId, vendedor).agregar(id, 1));
                        ok.incrementAndGet();
                    } catch (ApiException e) {
                        if (e.esConflicto()) {
                            sinStock.incrementAndGet();
                        } else {
                            otros.incrementAndGet();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        otros.incrementAndGet();
                    }
                });
            }
            largada.countDown();
            pool.shutdown();
            pool.awaitTermination(60, TimeUnit.SECONDS);

            aceptadas = ok.get();
            rechazadas = sinStock.get();
            otrosErrores = otros.get();
            stockDespues = productoApi.obtener(id).getStock();
            hayResultado = true;
            productos = productoApi.listar();
        } catch (ApiException e) {
            msgError(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            msgError("La simulacion fue interrumpida");
        }
    }

    public List<Producto> getProductos() { return productos; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getVendedores() { return vendedores; }
    public void setVendedores(Integer vendedores) { this.vendedores = vendedores; }
    public boolean isHayResultado() { return hayResultado; }
    public int getStockAntes() { return stockAntes; }
    public int getAceptadas() { return aceptadas; }
    public int getRechazadas() { return rechazadas; }
    public int getOtrosErrores() { return otrosErrores; }
    public int getStockDespues() { return stockDespues; }
}
