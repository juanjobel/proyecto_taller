package com.juanj.prueba_taller.Entidades;

import java.util.Map;

public class Chat {

    private String mensaje;
    private String nombre;
    private String type_mensaje;

    public Chat() {
    }

    public Chat(String mensaje, String nombre, String type_mensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.type_mensaje = type_mensaje;
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }


}

