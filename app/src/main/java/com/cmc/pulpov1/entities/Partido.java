package com.cmc.pulpov1.entities;

public class Partido {
    private String id;
    private String equipoUno;
    private String equipoDos;
    private String puntosEquipoUno;
    private String puntosEquiDos;
    private String hora;
    private String minuto;
    public Partido(){

    }

    public Partido(String id, String equipoUno, String equipoDos, String puntosEquipoUno, String puntosEquiDos, String hora, String minuto) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Partido{" +
                "id='" + id + '\'' +
                ", equipoUno='" + equipoUno + '\'' +
                ", equipoDos='" + equipoDos + '\'' +
                ", puntosEquipoUno='" + puntosEquipoUno + '\'' +
                ", puntosEquiDos='" + puntosEquiDos + '\'' +
                ", hora='" + hora + '\'' +
                ", minuto='" + minuto + '\'' +
                '}';
    }
}