package com.comercialnova.front.api;

import com.comercialnova.front.modelo.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCliente {

    private final RestTemplate rest;
    private final ObjectMapper mapper;
    private final String apiUrl;

    public RestCliente(RestTemplateBuilder builder, ObjectMapper mapper, @Value("${api.url}") String apiUrl) {
        this.rest = builder.rootUri(apiUrl).build();
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    public RestTemplate rest() {
        return rest;
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
