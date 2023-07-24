package com.example.proyectorestaurante.recycler;

public class Players {
    private int id;
    private String nombre;
    private String apellido;
    private String cargo;
    private String dni;

    public Players(int id, String nombre, String apellido, String cargo, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
        this.dni = dni;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public String getDni() {
        return dni;
    }
}
