package com.juanj.prueba_taller.Entidades;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    private int id;
    private String matricula;
    private String num_bastidor;
    private String marca_vehiculo;
    private String modelo_vehiculo;
    private String foto_vehiculo;
    private String tipo_combustible;
    private String nombre_cliente;
    private String apellido1_cliente;
    private String apellido2_cliente;
    private String documento;
    private String movil;
    private String direccion;

    public Vehiculo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNum_bastidor() {
        return num_bastidor;
    }

    public void setNum_bastidor(String num_bastidor) {
        this.num_bastidor = num_bastidor;
    }

    public String getMarca_vehiculo() {
        return marca_vehiculo;
    }

    public void setMarca_vehiculo(String marca_vehiculo) {
        this.marca_vehiculo = marca_vehiculo;
    }

    public String getModelo_vehiculo() {
        return modelo_vehiculo;
    }

    public void setModelo_vehiculo(String modelo_vehiculo) {
        this.modelo_vehiculo = modelo_vehiculo;
    }

    public String getFoto_vehiculo() {
        return foto_vehiculo;
    }

    public void setFoto_vehiculo(String foto_vehiculo) {
        this.foto_vehiculo = foto_vehiculo;
    }

    public String getTipo_combustible() {
        return tipo_combustible;
    }

    public void setTipo_combustible(String tipo_combustible) {
        this.tipo_combustible = tipo_combustible;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getApellido1_cliente() {
        return apellido1_cliente;
    }

    public void setApellido1_cliente(String apellido1_cliente) {
        this.apellido1_cliente = apellido1_cliente;
    }

    public String getApellido2_cliente() {
        return apellido2_cliente;
    }

    public void setApellido2_cliente(String apellido2_cliente) {
        this.apellido2_cliente = apellido2_cliente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
