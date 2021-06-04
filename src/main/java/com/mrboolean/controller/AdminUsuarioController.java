package com.mrboolean.controller;

import com.mrboolean.ejb.ClienteFacadeLocal;
import com.mrboolean.model.entities.Cliente;
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
public class AdminUsuarioController implements Serializable{
    @EJB
    private ClienteFacadeLocal clienteEJB;
    
    private List<Cliente> clientes;
    private Cliente cliente = new Cliente();
    private String tipo_user;
    
    @PostConstruct
    public void init(){
        System.out.println("-----------------------------INIT ADMINUSUARIOCONTROLLER");
        clientes = new ArrayList<Cliente>();
        listarAllClientes();
        
    }
    public void listarAllClientes(){
        try{
            clientes = clienteEJB.findAll();
            this.tipo_user = "t";
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error en init AdminUsuarioController...");
        }
    }
    public void listarClientesByTipo(){
        
        if(this.tipo_user.equals("t")){
            
            listarAllClientes();
            
        }else{
            
            this.clientes = clienteEJB.findByTipo(this.tipo_user);
            
        } 
    }
    public void eliminarCliente(Cliente cli){
        
        clienteEJB.remove(cli);
        listarClientesByTipo();
        
        
    }

   /*Función para editar las filas de la tabla de clientes.*/
    public void modificarUsuario(Cliente cli){
        
        clienteEJB.edit(cli);
    }
    
    /*Función para añadir usuario*/
    public void añadirUsuario() {

        try {
            clienteEJB.create(this.cliente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Usuario añadido correctamente"));
            listarClientesByTipo();
            PrimeFaces.current().executeScript("PF('wregusu').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo tratando de añadir el usuario"));
            System.out.println("Error en registrarCliente..");
            PrimeFaces.current().executeScript("PF('wregusu').hide();");
            e.printStackTrace();
        }

    }

    /*GET AND SET*/
    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(String tipo_user) {
        this.tipo_user = tipo_user;
    }
   
}
