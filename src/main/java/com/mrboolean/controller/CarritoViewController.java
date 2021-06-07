package com.mrboolean.controller;

import com.mrboolean.ejb.LineaPedidoFacadeLocal;
import com.mrboolean.ejb.PedidoFacadeLocal;
import com.mrboolean.ejb.ProductoFacadeLocal;
import com.mrboolean.model.CartItem;
import com.mrboolean.model.Cliente;
import com.mrboolean.model.LineaPedido;
import com.mrboolean.model.Pedido;
import com.mrboolean.model.Producto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class CarritoViewController implements Serializable {

    @EJB
    private PedidoFacadeLocal pedidoEJB;
    @EJB
    private ProductoFacadeLocal productoEJB;
    @EJB
    private LineaPedidoFacadeLocal linea_pedidoEJB;

    private List<CartItem> items;
    private CartItem item = new CartItem();
    private double monto;
    private Pedido pedido = new Pedido();

    @PostConstruct
    public void init() {

        items = new ArrayList<CartItem>();
        listarCarrito();
        calcularMonto();

    }

    public void leerItem(CartItem ci) {

        this.item = ci;

    }

    public void listarCarrito() {

        this.items = (List<CartItem>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("carrito");

    }

    public void calcularMonto() {

        this.monto = 0;

        for (CartItem ci : this.items) {

            double precio_fila = ci.getCantidad() * (ci.getProducto().getPrecio());
            this.monto = this.monto + precio_fila;

        }
        this.monto = Math.round(this.monto * 100.0) / 100.0;

    }

    public void confirmarCompra() {

        try {
            this.pedido.setTotal_pagado(this.monto);

            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");

            this.pedido.setCliente(cl);

            pedidoEJB.create(this.pedido);

            for (CartItem ci : this.items) {

                LineaPedido lp = new LineaPedido();

                lp.setCantidad(ci.getCantidad());
                restarStock(ci.getProducto(),ci.getCantidad());
                lp.setProducto(ci.getProducto());
                lp.setPedido(this.pedido);

                linea_pedidoEJB.create(lp);

            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra Realizada", "La compra fue un éxito. Muchas gracias por confiar en Mr Boolean Toys"));
            List<CartItem> list = new ArrayList<CartItem>();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", list);
            PrimeFaces.current().executeScript("PF('wcart').hide();");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo en la compra. Disculpe las molestias"));
        }

    }
    public void restarStock(Producto pr, int cantidad){
        
        try{
            
            pr.setStock(pr.getStock()-cantidad);
            productoEJB.edit(pr);
            
            
            
        }catch(Exception e){
            
            e.printStackTrace();
            System.out.println("Fallo en bajada de stock");
            
        }
        
        
        
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public CartItem getItem() {
        return item;
    }

    public void setItem(CartItem item) {
        this.item = item;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

}
