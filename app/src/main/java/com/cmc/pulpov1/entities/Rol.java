package com.cmc.pulpov1.entities;

public class Rol {
    private String idRol;
    private String nombreRol;
    private String idTorneo;
    private String idEquipo;
    public Rol(){

    }

    public Rol(String idRol, String nombreRol, String idTorneo, String idEquipo) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.idTorneo = idTorneo;
        this.idEquipo = idEquipo;
    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(String idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "idRol='" + idRol + '\'' +
                ", nombreRol='" + nombreRol + '\'' +
                ", idTorneo='" + idTorneo + '\'' +
                ", idEquipo='" + idEquipo + '\'' +
                '}';
    }
}
