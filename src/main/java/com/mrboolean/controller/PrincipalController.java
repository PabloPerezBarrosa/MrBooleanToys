package com.mrboolean.controller;

import com.mrboolean.ejb.CategoriaFacadeLocal;
import com.mrboolean.ejb.ProductoFacadeLocal;
import com.mrboolean.model.Categoria;
import com.mrboolean.model.Producto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;


@Named
@ViewScoped
public class PrincipalController implements Serializable{

    @EJB
    private CategoriaFacadeLocal categoriaEJB;
   
    @EJB
    private ProductoFacadeLocal productoEJB;
    
    private Categoria categoria;
    
    private List<Producto> productos = new ArrayList();
    
    @PostConstruct
    public void init(){
        
        categoria = new Categoria();
        
    }
    
    public void registrar(){
        
        try{
            
            categoriaEJB.create(categoria);
            
        }catch(Exception e){
            
            //Mensaje Prime con growl
            
        }
        
    }
    
    public void cargarCategoria() throws Exception{
       
        try{
            productos = productoEJB.findAll();
//            String link = "categoria.xhtml";
//            FacesContext.getCurrentInstance().getExternalContext().redirect(link);
                      
        }catch(Exception e){
            
            throw e;
            
        }
    }
   
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
}
