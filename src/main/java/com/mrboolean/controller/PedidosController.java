/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrboolean.controller;

import com.mrboolean.ejb.LineaPedidoFacadeLocal;
import com.mrboolean.ejb.PedidoFacadeLocal;
import com.mrboolean.model.LineaPedido;
import com.mrboolean.model.Pedido;
import com.mrboolean.model.PedidosAux;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "pedidosController")
@ViewScoped
public class PedidosController implements Serializable {

    @EJB
    private PedidoFacadeLocal pedidoEJB;
    @EJB
    private LineaPedidoFacadeLocal lineaPedidoEJB;

    private List<PedidosAux> pedidos_aux = new ArrayList<PedidosAux>();
    private List<Pedido> pedidos = new ArrayList<Pedido>();
    private List<String> estados = new ArrayList<String>();
    private String estado_comparator;

    @PostConstruct
    public void init() {
        listarEstados();
        listarPedidos();
    }

    public void listarPedidos() {

        try {
            System.out.print(this.estado_comparator);
            System.out.print(this.estado_comparator);
            System.out.print(this.estado_comparator);
            if (this.estado_comparator == null || this.estado_comparator.equals("t") || this.estado_comparator.equals("")) {
                this.pedidos = pedidoEJB.findAll();
            } else {
                this.pedidos = pedidoEJB.findByEstado(this.estado_comparator);
            }

            this.pedidos_aux.clear();

            for (Pedido p : this.pedidos) {

                PedidosAux p_aux = new PedidosAux();
                String dateString = "Fecha no actualizada";
                if (p.getFecha_pedido() != null) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    dateString = format.format(p.getFecha_pedido());

                }

                System.out.println(dateString);

                p_aux.setPedido(p);
                p_aux.setDateString(dateString);

                this.pedidos_aux.add(p_aux);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fallo en Listar Pedidos....................");
        }

    }

    public void listarEstados() {

        this.estados = Arrays.asList("En espera", "En entrega", "Entregado");

    }

    public void editarEstado(Pedido p) {

        try {
            pedidoEJB.edit(p);
            listarEstados();
            listarPedidos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conseguido", "Pedido editado correctamente."));
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Fallo en Editar Pedidos....................");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo editando pedido."));

        }

    }

    public void eliminarPedido(Pedido p) {

        try {
            if (p.getEstado().equals("Entregado")) {
                
                List<LineaPedido> lineas = new ArrayList<LineaPedido>();
                
                lineas = lineaPedidoEJB.findByPedidoId(p.getIdpedido());
                
                for(LineaPedido l : lineas){
                    
                    lineaPedidoEJB.remove(l);
                    
                }
                
                pedidoEJB.remove(p);
                listarEstados();
                listarPedidos();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conseguido", "Eliminado pedido correctamente"));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Sólo se puede borrar pedidos ya entregados."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fallo en Eliminar Pedidos....................");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo eliminado pedido."));
        }

    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<PedidosAux> getPedidos_aux() {
        return pedidos_aux;
    }

    public void setPedidos_aux(List<PedidosAux> pedidos_aux) {
        this.pedidos_aux = pedidos_aux;
    }

    public List<String> getEstados() {
        return estados;
    }

    public void setEstados(List<String> estados) {
        this.estados = estados;
    }

    public String getEstado_comparator() {
        return estado_comparator;
    }

    public void setEstado_comparator(String estado_comparator) {
        this.estado_comparator = estado_comparator;
    }

}
