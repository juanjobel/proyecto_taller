package com.juanj.prueba_taller.Entidades;

import java.io.Serializable;

public class Pieza implements Serializable {


    private int id;
    private String nombre_pieza;
    private String marca_pieza;
    private String modelo_pieza;
    private String referencia;
    private String precio;
    private String foto_pieza;
    private String nombre_proveedor;


    public Pieza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_pieza() {
        return nombre_pieza;
    }

    public void setNombre_pieza(String nombre_pieza) {
        this.nombre_pieza = nombre_pieza;
    }

    public String getMarca_pieza() {
        return marca_pieza;
    }

    public void setMarca_pieza(String marca_pieza) {
        this.marca_pieza = marca_pieza;
    }

    public String getModelo_pieza() {
        return modelo_pieza;
    }

    public void setModelo_pieza(String modelo_pieza) {
        this.modelo_pieza = modelo_pieza;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFoto_pieza() {
        return foto_pieza;
    }

    public void setFoto_pieza(String foto_pieza) {
        this.foto_pieza = foto_pieza;
    }

    public String getNombre_proveedor() {
        return nombre_proveedor;
    }

    public void setNombre_proveedor(String nombre_proveedor) {
        this.nombre_proveedor = nombre_proveedor;
    }
}
