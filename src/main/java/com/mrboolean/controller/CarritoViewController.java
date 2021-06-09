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
import java.util.Arrays;
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
    private List<String> provincias = new ArrayList<String>();

    @PostConstruct
    public void init() {

        items = new ArrayList<CartItem>();
        listarProvincias();
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
                restarStock(ci.getProducto(), ci.getCantidad());
                lp.setProducto(ci.getProducto());
                lp.setPedido(this.pedido);

                linea_pedidoEJB.create(lp);

            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra Realizada", "La compra fue un éxito. Muchas gracias por confiar en Mr Boolean Toys"));
            List<CartItem> list = new ArrayList<CartItem>();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", list);
            PrimeFaces.current().executeScript("PF('wcart').hide();");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/carrito_view_cliente.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo en la compra. Disculpe las molestias"));
        }

    }

    public void restarStock(Producto pr, int cantidad) {

        try {

            pr.setStock(pr.getStock() - cantidad);

            if (pr.getStock() == 0) {
                pr.setEstado("Agotado");
            } else {
                pr.setEstado("Disponible");
            }

            productoEJB.edit(pr);

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Fallo en bajada de stock");

        }

    }

    public void eliminarCartItem(CartItem item) {

        try {
            List<CartItem> itemList = new ArrayList<CartItem>();

            itemList = (List<CartItem>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("carrito");

            for (CartItem i : itemList) {

                if (i.getProducto().getIdproducto() == item.getProducto().getIdproducto()) {

                    itemList.remove(i);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conseguido", "El articulo y todas sus unidades borradas con éxito."));
                    break;

                }

            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", itemList);

            listarCarrito();
            calcularMonto();

            FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/carrito_view_cliente.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error borrando producto en carritoView..................");
        }
    }

    public void editarCartItem(CartItem item) {

        try {

            List<CartItem> itemList = new ArrayList<CartItem>();

            itemList = (List<CartItem>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("carrito");

            for (CartItem i : itemList) {

                if (i.getProducto().getIdproducto() == item.getProducto().getIdproducto()) {

                    if (item.getCantidad() == 0) {

                        itemList.remove(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conseguido", "Borrado con éxito. Al marcar 0 ha eliminado toda la fila relativa a ese artículo."));
                        break;

                    } else {
                        i.setCantidad(item.getCantidad());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conseguido", "Reducción de unidades del articulo exitosa."));
                        break;
                    }

                }

            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", itemList);

            listarCarrito();
            calcularMonto();

            FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/carrito_view_cliente.xhtml");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error editando producto en carritoView..................");
        }

    }
    public void listarProvincias(){
        
        this.provincias = Arrays.asList("A Coruña", "Álava","Albacete","Alicante","Almería","Asturias","Ávila","Badajoz",
                "Baleares","Barcelona", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ciudad Real", "Córdoba", 
                "Cuenca","Girona","Granada","Guadalajara","Gipuzkoa","Huelva","Huesca","Jaén","La Rioja","Las Palmas","León",
                "Lérida","Lugo","Madrid","Málaga","Murcia","Navarra","Ourense","Palencia","Pontevedra","Salamanca","Segovia",
                "Sevilla","Soria","Tarragona","Santa Cruz de Tenerife","Teruel","Toledo","Valencia","Valladolid","Vizcaya",
                "Zamora","Zaragoza");
        
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

    public List<String> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<String> provincias) {
        this.provincias = provincias;
    }
    
}
