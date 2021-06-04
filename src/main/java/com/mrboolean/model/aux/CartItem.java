package com.mrboolean.model.aux;

import com.mrboolean.model.entities.Producto;
import java.io.Serializable;

public class CartItem implements Serializable{
        
    private Producto producto = new Producto();
    private int cantidad;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
}
