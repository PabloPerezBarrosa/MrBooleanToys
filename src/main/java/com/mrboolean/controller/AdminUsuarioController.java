package com.mrboolean.controller;

import com.mrboolean.ejb.ClienteFacadeLocal;
import com.mrboolean.model.Cliente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class AdminUsuarioController implements Serializable{
    @EJB
    private ClienteFacadeLocal clienteEJB;
    
    private List<Cliente> clientes;
    private Cliente cliente = new Cliente();
    
    @PostConstruct
    public void init(){
        System.out.println("-----------------------------INIT ADMINUSUARIOCONTROLLER");
        clientes = new ArrayList<Cliente>();
        listarAllClientes();
        
    }
    public void listarAllClientes(){
        try{
            clientes = clienteEJB.findAll();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error en init AdminUsuarioController...");
        }
    }
//    
//    public void leer(Cliente cliente_selected){
//        
//        this.cliente = cliente_selected;
//        
//    }
   /*Función para editar las filas de la tabla de clientes.*/
    public void modificarUsuario(Cliente cli){
        
        clienteEJB.edit(cli);
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
    
}
