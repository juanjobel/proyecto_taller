package com.juanj.prueba_taller.Entidades;

public class RecibirMensaje extends Chat{

    private Long hora;

    public RecibirMensaje() {
    }

    public RecibirMensaje(Long hora) {
        this.hora = hora;
    }

    public RecibirMensaje(String mensaje, String nombre, String type_mensaje, Long hora) {
        super(mensaje, nombre, type_mensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
