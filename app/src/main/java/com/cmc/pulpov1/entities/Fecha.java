package com.cmc.pulpov1.entities;

import java.util.Date;

public class Fecha {

    private Date dia1;
    private Date dia2;
    private Date dia3;
    private String estado;
    private String cancha1;
    private String cancha2;
     public Fecha (){

     }

    public Fecha(Date dia1, Date dia2, Date dia3, String estado, String cancha1, String cancha2) {
        this.dia1 = dia1;
        this.dia2 = dia2;
        this.dia3 = dia3;
        this.estado = estado;
        this.cancha1 = cancha1;
        this.cancha2 = cancha2;
    }

    public Date getDia1() {
        return dia1;
    }

    public void setDia1(Date dia1) {
        this.dia1 = dia1;
    }

    public Date getDia2() {
        return dia2;
    }

    public void setDia2(Date dia2) {
        this.dia2 = dia2;
    }

    public Date getDia3() {
        return dia3;
    }

    public void setDia3(Date dia3) {
        this.dia3 = dia3;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCancha1() {
        return cancha1;
    }

    public void setCancha1(String cancha1) {
        this.cancha1 = cancha1;
    }

    public String getCancha2() {
        return cancha2;
    }

    public void setCancha2(String cancha2) {
        this.cancha2 = cancha2;
    }

    @Override
    public String toString() {
        return "Fecha{" +
                "dia1=" + dia1 +
                ", dia2=" + dia2 +
                ", dia3=" + dia3 +
                ", estado='" + estado + '\'' +
                ", cancha1='" + cancha1 + '\'' +
                ", cancha2='" + cancha2 + '\'' +
                '}';
    }
}
