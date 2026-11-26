package com.comercialnova.front.api;

import com.comercialnova.front.core.SesionUsuario;
import com.comercialnova.front.modelo.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Supplier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCliente {

    private static final ThreadLocal<String> SESION_HILO = new ThreadLocal<>();

    private final RestTemplate rest;
    private final ObjectMapper mapper;
    private final String apiUrl;
    private final SesionUsuario sesionUsuario;

    public RestCliente(RestTemplateBuilder builder,
                       ObjectMapper mapper,
                       @Value("${api.url}") String apiUrl,
                       @Lazy SesionUsuario sesionUsuario) {
        this.mapper = mapper;
        this.apiUrl = apiUrl;
        this.sesionUsuario = sesionUsuario;
        this.rest = builder
                .rootUri(apiUrl)
                .requestFactory(() -> {
                    SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
                    fabrica.setOutputStreaming(false);
                    return fabrica;
                })
                .additionalInterceptors(new ClientHttpRequestInterceptor() {
                    @Override
                    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                        ClientHttpRequestExecution execution) throws IOException {
                        String sessionId = sesionDelHilo();
                        if (sessionId != null && !sessionId.isEmpty()) {
                            request.getHeaders().set("X-Session-Id", sessionId);
                        }
                        return execution.execute(request, body);
                    }
                })
                .build();
    }

    public RestTemplate rest() {
        return rest;
    }

    public String sesionActual() {
        return sesionUsuario.getSessionId();
    }

    public <T> T conSesion(String sessionId, Supplier<T> llamada) {
        SESION_HILO.set(sessionId);
        try {
            return llamada.get();
        } finally {
            SESION_HILO.remove();
        }
    }

    private String sesionDelHilo() {
        String explicita = SESION_HILO.get();
        if (explicita != null) {
            return explicita;
        }
        try {
            return sesionUsuario.getSessionId();
        } catch (RuntimeException sinPeticionWeb) {
            return null;
        }
    }

    public <T> T llamar(Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (HttpStatusCodeException e) {
            throw new ApiException(e.getRawStatusCode(), leerMensaje(e));
        } catch (ResourceAccessException e) {
            throw new ApiException(0, "No se pudo conectar con el back en " + apiUrl
                    + ". Verifique que prototipo_back este ejecutandose.");
        }
    }

    private String leerMensaje(HttpStatusCodeException e) {
        try {
            ApiError error = mapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            if (error.getMensaje() != null) {
                return error.getMensaje();
            }
        } catch (Exception ignorada) {
        }
        return "Error " + e.getRawStatusCode() + " al llamar a la API";
    }
}
