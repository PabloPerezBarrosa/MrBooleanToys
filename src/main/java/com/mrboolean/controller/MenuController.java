package com.mrboolean.controller;

import com.mrboolean.ejb.ClienteFacadeLocal;
import com.mrboolean.model.Cliente;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Named(value = "menuController")
@SessionScoped
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

    /*Función para el inicio de sesión, hace así mismo la redirección en función de que en que página nos encontramos antes de 
    logear y de el nuevo usuario logeado.*/
    public void iniciarSesion() {

        Cliente cl = new Cliente();
        String url = "";

        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            String url1 = "http://localhost:8080/MrBooleanToys/faces/principal.xhtml";
            String url2 = "http://localhost:8080/MrBooleanToys/faces/categoria.xhtml";

            System.out.println(url);

            cl = clienteEJB.iniciarSesion(this.cliente);
            if (cl != null) {

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", cl);

                if (url.equals(url1)) {
                    if (cl.getTipo().equals("a")) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/principal.xhtml_admin");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/principal_cliente.xhtml");
                    }
                } else {
                    if (cl.getTipo().equals("a")) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/categoria_admin.xhtml");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/categoria_cliente.xhtml");
                    }
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Datos incorrectos"));
            }

        } catch (Exception e) {

            System.out.println("Fallo en iniciarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public void verificarSesion() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");

            if (cl == null) {
                context.getExternalContext().redirect("/MrBooleanToys/");
            } else {
                if (cl.getTipo().equals("c")) {
                    context.getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/principal_cliente.xhtml");
                }
            }

        } catch (Exception e) {
            System.out.println("Fallo en verificarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public void cerrarSesion() {

        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/");

        } catch (Exception e) {

            System.out.println("Fallo en cerrarSesion() MenuController..");
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
