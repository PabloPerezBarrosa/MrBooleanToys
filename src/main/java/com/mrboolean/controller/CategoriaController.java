package com.mrboolean.controller;

import com.mrboolean.ejb.CategoriaFacadeLocal;
import com.mrboolean.ejb.ProductoFacadeLocal;
import com.mrboolean.model.Categoria;
import com.mrboolean.model.Cliente;
import com.mrboolean.model.Producto;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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
        
        try{
            categorias = categoriaEJB.findAll();
            String cat = (String)(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("categoria_sesion"));
            cargarCategoria(cat);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error en init CategoriaController...");
        }
        
    }
    public void redirectCategorias(String cat){
        
        try{
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("categoria_sesion",cat);
            Cliente cliente = (Cliente)(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente"));
            
            if(cliente == null){
                FacesContext.getCurrentInstance().getExternalContext().redirect("categoria.xhtml");
            }else{
                if(cliente.getTipo().equals("a")){
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/categoria_admin.xhtml");
                }else{
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/categoria_cliente.xhtml");
                }
            }
            
        }catch(Exception e){
            System.out.println("Error en el redirect" + e);
        }
        
    }
    public void sacarCatId(String cat_name){
  
        for(Categoria categoria : categorias){
            
            if(categoria.getNombre().equals(cat_name)){
                this.setCodigo_categoria(categoria.getIdcategoria());
            }
        }
    }
    public void cargarCategoria(String cat) throws Exception{
        
        if(cat != null){
            sacarCatId(cat);
        }
        
        try{
            productos = productoEJB.listarProductos(codigo_categoria);
            
            for(Producto prod : productos){
                
                System.out.println(prod.getNombre());
                
            }
                      
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
