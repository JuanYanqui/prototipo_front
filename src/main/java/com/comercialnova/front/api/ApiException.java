package com.comercialnova.front.api;

public class ApiException extends RuntimeException {

    private final int status;

    public ApiException(int status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean esConflicto() {
        return status == 409;
    }
}
