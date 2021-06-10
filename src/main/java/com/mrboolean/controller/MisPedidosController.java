package com.mrboolean.controller;

import com.mrboolean.ejb.PedidoFacadeLocal;
import com.mrboolean.model.Cliente;
import com.mrboolean.model.Pedido;
import com.mrboolean.model.PedidosAux;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "misPedidosController")
@ViewScoped
public class MisPedidosController implements Serializable {

    @EJB
    private PedidoFacadeLocal pedidoEJB;

    private List<PedidosAux> pedidos_aux = new ArrayList<PedidosAux>();
    private List<Pedido> pedidos = new ArrayList<Pedido>();
    private String estado_comparator;

    @PostConstruct
    public void init() {
        listarPedidosCliente();
    }

    public void listarPedidosCliente() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");
            if (this.estado_comparator == null || this.estado_comparator.equals("t") || this.estado_comparator.equals("")) {
                this.pedidos = pedidoEJB.findByIdCliente(cl.getIdcliente());
            } else {
                this.pedidos = pedidoEJB.findByEstadoAndIdCliente(this.estado_comparator,cl.getIdcliente());
            }
            this.pedidos_aux.clear();
            
            for (Pedido p : this.pedidos) {

                PedidosAux p_aux = new PedidosAux();
                String dateString = "Fecha no actualizada";
                if (p.getFecha_pedido() != null) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    dateString = format.format(p.getFecha_pedido());

                }
                p_aux.setPedido(p);
                p_aux.setDateString(dateString);

                this.pedidos_aux.add(p_aux);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fallo en Listar Pedidos Cliente....................");
        }

    }

    public List<PedidosAux> getPedidos_aux() {
        return pedidos_aux;
    }

    public void setPedidos_aux(List<PedidosAux> pedidos_aux) {
        this.pedidos_aux = pedidos_aux;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getEstado_comparator() {
        return estado_comparator;
    }

    public void setEstado_comparator(String estado_comparator) {
        this.estado_comparator = estado_comparator;
    }
}
