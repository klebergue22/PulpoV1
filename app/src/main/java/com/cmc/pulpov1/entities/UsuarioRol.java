package com.cmc.pulpov1.entities;

import java.util.HashMap;

public class UsuarioRol {
    private String nombre;
    private String apellido;
    private HashMap<String,Rol> roles;

    public HashMap<String, Rol> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<String, Rol> roles) {
        this.roles = roles;
    }

    public UsuarioRol(){

    }
    public UsuarioRol(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "UsuarioRol{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", roles=" + roles +
                '}';
    }
}
