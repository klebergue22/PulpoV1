package com.cmc.pulpov1.entities;

public class AdminPerfil {
    private Jugador principal;
    private Jugador adicional1;
    private Jugador adicional2;

    public AdminPerfil(){

    }

    public AdminPerfil(Jugador principal, Jugador adicional1, Jugador adicional2) {
        this.principal = principal;
        this.adicional1 = adicional1;
        this.adicional2 = adicional2;
    }

    public Jugador getPrincipal() {
        return principal;
    }

    public void setPrincipal(Jugador principal) {
        this.principal = principal;
    }

    public Jugador getAdicional1() {
        return adicional1;
    }

    public void setAdicional1(Jugador adicional1) {
        this.adicional1 = adicional1;
    }

    public Jugador getAdicional2() {
        return adicional2;
    }

    public void setAdicional2(Jugador adicional2) {
        this.adicional2 = adicional2;
    }

    @Override
    public String toString() {
        return "AdminPerfil{" +
                "principal=" + principal +
                ", adicional1=" + adicional1 +
                ", adicional2=" + adicional2 +
                '}';
    }
}
