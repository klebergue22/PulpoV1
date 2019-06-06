package com.cmc.pulpov1.entities;

public class Equipo {
    private String nombreEquipo;
    private String nombreRepresentante;
    private String apellidoRepresentante;
    private String mail;
    private String telefono;
    private String categoria;
    private String id;

    public Equipo(){

    }

    public Equipo(String nombreEquipo, String nombreRepresentante, String apellidoRepresentante, String mail, String telefono, String categoria, String id) {
        this.nombreEquipo = nombreEquipo;
        this.nombreRepresentante = nombreRepresentante;
        this.apellidoRepresentante = apellidoRepresentante;
        this.mail = mail;
        this.telefono = telefono;
        this.categoria = categoria;
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getApellidoRepresentante() {
        return apellidoRepresentante;
    }

    public void setApellidoRepresentante(String apellidoRepresentante) {
        this.apellidoRepresentante = apellidoRepresentante;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "nombreEquipo='" + nombreEquipo + '\'' +
                ", nombreRepresentante='" + nombreRepresentante + '\'' +
                ", apellidoRepresentante='" + apellidoRepresentante + '\'' +
                ", mail='" + mail + '\'' +
                ", telefono='" + telefono + '\'' +
                ", categoria='" + categoria + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
