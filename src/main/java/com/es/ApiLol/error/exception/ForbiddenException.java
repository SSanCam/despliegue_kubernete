package com.es.ApiLol.error.exception;

public class ForbiddenException extends RuntimeException {
  private static final String DESCRIPCION = "Forbidden (409)";

  public ForbiddenException(String mensaje) {
    super(DESCRIPCION +". "+ mensaje);
  }
}
