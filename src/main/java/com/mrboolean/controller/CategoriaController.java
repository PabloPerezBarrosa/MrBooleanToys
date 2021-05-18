package com.mrboolean.controller;

import com.mrboolean.ejb.CategoriaFacadeLocal;
import com.mrboolean.ejb.ProductoFacadeLocal;
import com.mrboolean.model.Categoria;
import com.mrboolean.model.Producto;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class CategoriaController implements Serializable{
    
    @EJB
    private CategoriaFacadeLocal categoriaEJB;
    
    @EJB
    private ProductoFacadeLocal productoEJB;
    
    private List<Categoria> categorias;
    private List<Producto> productos;
    private int codigo_categoria;
    
    @PostConstruct
    public void init(){
        
        categorias = categoriaEJB.findAll();
        
    }
    public void cargarCategoria() throws Exception{
       
        try{
            
            productos = productoEJB.listarProductos(codigo_categoria);
                      
        }catch(Exception e){
            
            throw e;
            
        }
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public int getCodigo_categoria() {
        return codigo_categoria;
    }

    public void setCodigo_categoria(int codigo_categoria) {
        this.codigo_categoria = codigo_categoria;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
     
}
