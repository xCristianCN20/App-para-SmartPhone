package com.miguel.mexiwear.Modelo;

public class clsPostulacion {
    String nombre;
    String direccion;
    int idPostulacion;

    public clsPostulacion(String  nombre, String dirección, int idPostulacion) {
        this.nombre = nombre;
        this.direccion = dirección;
        this.idPostulacion = idPostulacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(int idPostulacion) {
        this.idPostulacion = idPostulacion;
    }
}
