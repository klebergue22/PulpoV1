package com.cmc.pulpov1.entities;

import java.util.Date;

public class Torneo {
    private String id;
    private String nombreTorneo;
    private String nombreOrganizador;
    private String apellidoOrganizador;
    private String correoOrganizador;
    private String telefonoOrganizador;
    private int anio;
    private Date fechaInicio;
 public Torneo(){

 }

    public Torneo(String id, String nombreTorneo, String nombreOrganizador, String apellidoOrganizador, String correoOrganizador, String telefonoOrganizador, int anio, Date fechaInicio) {
        this.id = id;
        this.nombreTorneo = nombreTorneo;
        this.nombreOrganizador = nombreOrganizador;
        this.apellidoOrganizador = apellidoOrganizador;
        this.correoOrganizador = correoOrganizador;
        this.telefonoOrganizador = telefonoOrganizador;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public String getNombreOrganizador() {
        return nombreOrganizador;
    }

    public void setNombreOrganizador(String nombreOrganizador) {
        this.nombreOrganizador = nombreOrganizador;
    }

    public String getApellidoOrganizador() {
        return apellidoOrganizador;
    }

    public void setApellidoOrganizador(String apellidoOrganizador) {
        this.apellidoOrganizador = apellidoOrganizador;
    }

    public String getCorreoOrganizador() {
        return correoOrganizador;
    }

    public void setCorreoOrganizador(String correoOrganizador) {
        this.correoOrganizador = correoOrganizador;
    }

    public String getTelefonoOrganizador() {
        return telefonoOrganizador;
    }

    public void setTelefonoOrganizador(String telefonoOrganizador) {
        this.telefonoOrganizador = telefonoOrganizador;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Override
    public String toString() {
        return "Torneo{" +
                "id='" + id + '\'' +
                ", nombreTorneo='" + nombreTorneo + '\'' +
                ", nombreOrganizador='" + nombreOrganizador + '\'' +
                ", apellidoOrganizador='" + apellidoOrganizador + '\'' +
                ", correoOrganizador='" + correoOrganizador + '\'' +
                ", telefonoOrganizador='" + telefonoOrganizador + '\'' +
                ", anio=" + anio +
                ", fechaInicio=" + fechaInicio +
                '}';
    }
}

