package com.mrboolean.controller;

import com.mrboolean.model.CartItem;
import com.mrboolean.model.Cliente;
import com.mrboolean.model.Producto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named(value = "carritoController")
@ViewScoped
public class CarritoController implements Serializable {

    private Producto producto;
    private int cantidad;

    @PostConstruct
    public void init() {
        
        producto = new Producto();

    }

    public void leerProducto(Producto pro) {
        this.producto = pro;
    }

    public void añadirCartItem() {
        List<CartItem> itemList = new ArrayList<CartItem>();
        try {
           
            boolean match = false;
            itemList = (List<CartItem>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("carrito");
            CartItem item = new CartItem();
            
            item.setProducto(this.producto);
            item.setCantidad(this.cantidad);

            if (itemList != null && !itemList.isEmpty()) {
                System.out.println("NO SOY NULL" + itemList.size());
                for (CartItem ci : itemList) {

                    if (ci.getProducto().getIdproducto() == item.getProducto().getIdproducto()) {

                        ci.setCantidad(ci.getCantidad() + item.getCantidad());
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    itemList.add(item);
                }
            } else {
                System.out.println("SOY NULL");
                itemList.add(item);
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", itemList);

        for(CartItem i : itemList){
            
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(i.getProducto().getNombre() + "cantidad: " + i.getCantidad());
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
            
        }
        System.out.println("SIGGUIENTE VUELTA");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Articulo añadido al carrito"));
            PrimeFaces.current().executeScript("PF('wcart').hide();");
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo añadiendo al carrito"));
            e.printStackTrace();
            System.out.println("Fallo añadiendo item .............");
        }

    }

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
