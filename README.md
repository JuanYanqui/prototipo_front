# Comercial Nova – Front (JSF)

Interfaz web del prototipo, hecha en **Java con JSF + PrimeFaces**.
No tiene base de datos: todo lo pide a la API REST de **prototipo_back**.

```
Navegador  ──HTTP──▶  prototipo_front (JSF, puerto 8091)  ──REST/JSON──▶  prototipo_back (API, puerto 8090)  ──▶  PostgreSQL
```

## Tecnologías
- Java 8 + Spring Boot 2.7 (Maven)
- JSF 2.3 (Mojarra) + PrimeFaces 11, integrados con **JoinFaces** (Tomcat embebido: no hay que instalar servidor)

## Cómo ejecutar
1. Ejecutar primero **prototipo_back** (http://localhost:8090).
2. Abrir este proyecto en NetBeans → **Run (F6)**.
3. Abrir **http://localhost:8091**

Si el back está en otra máquina o puerto, cambiar `api.url` en `src/main/resources/application.properties`
o usar la variable de entorno `API_URL=http://<host>:8090`.

## Estructura (misma idea que gosap, simplificada)

```
src/main/java/com/comercialnova/front/
├── FrontApplication.java          ← arranque (Run F6)
├── core/                          ← lo común a todas las pantallas
│   ├── CtrTemplate.java           ← clase base de los controladores (título + msgInfo/msgError)
│   └── FacesUtils.java            ← mensajes JSF (growl "msgBox")
├── api/                           ← llamadas al back, una clase por recurso
│   ├── RestCliente.java           ← conexión HTTP (api.url) y manejo de errores
│   ├── ProductoApi.java  ClienteApi.java  VentaApi.java
│   └── ApiException.java
├── modelo/                        ← datos que van y vienen del back (Producto, Venta…)
└── beans/                         ← un controlador por pantalla, agrupados por módulo
    ├── ventas/      CtrVentaNueva.java, CtrVentaHistorial.java
    ├── inventario/  CtrInventarioProductos.java
    └── demo/        CtrDemoConcurrencia.java

src/main/resources/META-INF/resources/      (equivale a "webapp" de gosap)
├── WEB-INF/
│   ├── template.xhtml             ← plantilla: topbar + menú lateral + contenido + growl
│   ├── topbar.xhtml               ← barra superior
│   └── menu.xhtml                 ← menú lateral (aquí se agregan las pantallas)
├── index.xhtml                    ← inicio
├── ventas/      ventaNueva.xhtml, ventaHistorial.xhtml
├── inventario/  inventarioProductos.xhtml
├── demo/        demoConcurrencia.xhtml
└── resources/css/estilos.css      ← colores y layout
```

| Pantalla | Diseño (.xhtml) | Controlador (Java) |
|---|---|---|
| Inicio | `index.xhtml` | — |
| Nueva venta | `ventas/ventaNueva.xhtml` | `beans/ventas/CtrVentaNueva.java` |
| Historial de ventas | `ventas/ventaHistorial.xhtml` | `beans/ventas/CtrVentaHistorial.java` |
| Inventario | `inventario/inventarioProductos.xhtml` | `beans/inventario/CtrInventarioProductos.java` |
| Demo concurrencia | `demo/demoConcurrencia.xhtml` | `beans/demo/CtrDemoConcurrencia.java` |

### Cómo es una pantalla
```xml
<ui:composition template="/WEB-INF/template.xhtml" ...>
    <ui:define name="content">
        <h:form id="frmListado">
            <div class="cabecera">
                <h:outputText value="#{CtrVentaHistorial.titulo}" styleClass="titulo"/>
                <h:panelGroup styleClass="acciones">  ... botones ...  </h:panelGroup>
            </div>
            <p:separator/>
            ... formgrid con filtros / p:dataTable ...
        </h:form>
    </ui:define>
</ui:composition>
```
```java
@Component("CtrVentaHistorial")   // nombre usado en el xhtml: #{CtrVentaHistorial...}
@ViewScoped
public class CtrVentaHistorial extends CtrTemplate {
    public CtrVentaHistorial(VentaApi ventaApi) { super("Historial de ventas"); ... }
    @PostConstruct public void init() { filtrar(); }
    public void filtrar() { ... ventaApi.listar() ... msgError(...) }
}
```

### Agregar una pantalla nueva
1. Crear `<modulo>/<pantalla>.xhtml` copiando una existente.
2. Crear `beans/<modulo>/Ctr<Pantalla>.java` que extienda `CtrTemplate`, con `@Component("Ctr<Pantalla>")` y `@ViewScoped`.
3. Si necesita otro endpoint del back, agregar el método en la `*Api` correspondiente.
4. Agregar un `p:menuitem` en `WEB-INF/menu.xhtml`.

### Cambiar el aspecto
- Colores, menú, tarjetas: `resources/css/estilos.css` (variables `--azul`, `--verde`… al inicio).
- Tema de PrimeFaces (botones, tablas, combos): `joinfaces.primefaces.theme` en `application.properties`
  (ej.: `saga`, `vela`, `arya`).
