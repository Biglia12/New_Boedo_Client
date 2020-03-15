package com.example.androidfood.Model;

public class Food {

    private String nombre, imagen , descripcion, precio, descuento, menuId;

    public Food() {
    }

    public Food (String nombre, String imagen, String descripcion, String precio, String descuento, String menuId) {
        this.nombre = nombre;
        this.imagen= imagen;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.menuId = menuId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() { return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
