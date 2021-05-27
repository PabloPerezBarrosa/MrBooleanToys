package com.mrboolean.controller;

import com.mrboolean.ejb.ClienteFacadeLocal;
import com.mrboolean.model.Cliente;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named(value = "menuController")
@ViewScoped
public class MenuController implements Serializable {

    @EJB
    private ClienteFacadeLocal clienteEJB;

    private Cliente cliente;

    @PostConstruct
    public void init() {
        cliente = new Cliente();
    }

    public void registrarCliente() {

        try {
            this.cliente.setTipo("c");
            clienteEJB.create(this.cliente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Registro Exitoso"));
            FacesContext.getCurrentInstance().getExternalContext().redirect("principal.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo en el Registro"));
            System.out.println("Error en registrarCliente..");
            e.printStackTrace();
        }

    }

    public void iniciarSesion() {

        Cliente cl = new Cliente();

        try {

            cl = clienteEJB.iniciarSesion(this.cliente);
            if (cl != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Login Exitoso"));
                FacesContext.getCurrentInstance().getExternalContext().redirect("correcto.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Datos incorrectos"));
            }

        } catch (Exception e) {

            System.out.println("Fallo en iniciarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    

}
