package com.juanj.prueba_taller.Entidades;

import java.util.Map;

public class EnviarMensaje extends Chat {

    private Map hora;

    public EnviarMensaje() {
    }

    public EnviarMensaje(String s, String toString, String s1, String s2, Map hora) {
        this.hora = hora;
    }

    public EnviarMensaje(String mensaje, String nombre, String type_mensaje, Map hora) {
        super(mensaje, nombre, type_mensaje);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
