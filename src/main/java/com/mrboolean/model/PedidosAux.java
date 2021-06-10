
package com.mrboolean.model;

import java.io.Serializable;

public class PedidosAux implements Serializable{
  
    private Pedido pedido = new Pedido();
    private String dateString;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
    
    
    
}
