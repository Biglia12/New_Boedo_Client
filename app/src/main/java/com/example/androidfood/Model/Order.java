package com.example.androidfood.Model;

public class Order {
    private int ID;
    private String ProductoId;
    private String ProductoNombre;
    private String Cantidad;
    private String Precio;
    private String Descuento;
    private String Imagen;


    public Order() {
    }

    public Order(String productoId, String productoNombre, String cantidad, String precio, String descuento, String imagen) {
        ProductoId = productoId;
        ProductoNombre = productoNombre;
        Cantidad = cantidad;
        Precio = precio;
        Descuento = descuento;
        Imagen = imagen;
    }

    public Order(int ID, String productoId, String productoNombre, String cantidad, String precio, String descuento, String imagen) {
        this.ID = ID;
        ProductoId = productoId;
        ProductoNombre = productoNombre;
        Cantidad = cantidad;
        Precio = precio;
        Descuento = descuento;
        Imagen = imagen;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductoId() {
        return ProductoId;
    }

    public void setProductoId(String productoId) {
        ProductoId = productoId;
    }

    public String getProductoNombre() {
        return ProductoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        ProductoNombre = productoNombre;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}