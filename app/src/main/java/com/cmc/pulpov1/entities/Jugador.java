package com.cmc.pulpov1.entities;

import java.util.Date;

public class Jugador {
    private String cedula;
    private String primerNombre;
    private String primerApellido;
    private String segundoNombre;
    private String segundoApellido;
    private Date fechaNacimiento;
    private String mailJugador;
    private String estado;

    public Jugador(){

    }

    public Jugador(String cedula, String primerNombre, String primerApellido, String segundoNombre, String segundoApellido, Date fechaNacimiento, String mailJugador, String estado) {
        this.cedula = cedula;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.segundoNombre = segundoNombre;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.mailJugador = mailJugador;
        this.estado = estado;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMailJugador() {
        return mailJugador;
    }

    public void setMailJugador(String mailJugador) {
        this.mailJugador = mailJugador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "cedula='" + cedula + '\'' +
                ", primerNombre='" + primerNombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoNombre='" + segundoNombre + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", mailJugador='" + mailJugador + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

