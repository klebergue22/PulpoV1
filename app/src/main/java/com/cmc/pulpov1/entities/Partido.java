package com.cmc.pulpov1.entities;

public class Partido {
    private String id;
    private String fecha;
    private String categoria;
    private String equipoUno;
    private String equipoDos;
    private String puntosEquipoUno;
    private String puntosEquiDos;
    private String hora;
    private String minuto;

    public Partido(){

    }

    public Partido(String id, String fecha, String categoria, String equipoUno, String equipoDos, String puntosEquipoUno, String puntosEquiDos, String hora, String minuto) {
        this.id = id;
        this.fecha = fecha;
        this.categoria = categoria;
        this.equipoUno = equipoUno;
        this.equipoDos = equipoDos;
        this.puntosEquipoUno = puntosEquipoUno;
        this.puntosEquiDos = puntosEquiDos;
        this.hora = hora;
        this.minuto = minuto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEquipoUno() {
        return equipoUno;
    }

    public void setEquipoUno(String equipoUno) {
        this.equipoUno = equipoUno;
    }

    public String getEquipoDos() {
        return equipoDos;
    }

    public void setEquipoDos(String equipoDos) {
        this.equipoDos = equipoDos;
    }

    public String getPuntosEquipoUno() {
        return puntosEquipoUno;
    }

    public void setPuntosEquipoUno(String puntosEquipoUno) {
        this.puntosEquipoUno = puntosEquipoUno;
    }

    public String getPuntosEquiDos() {
        return puntosEquiDos;
    }

    public void setPuntosEquiDos(String puntosEquiDos) {
        this.puntosEquiDos = puntosEquiDos;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }
}