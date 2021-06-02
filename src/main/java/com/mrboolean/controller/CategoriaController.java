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
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaFacadeLocal categoriaEJB;

    @EJB
    private ProductoFacadeLocal productoEJB;

    private List<Categoria> categorias;
    private List<Producto> productos;
    private int codigo_categoria;
    private String nombre_categoria;

    @PostConstruct
    public void init() {

        try {
            categorias = categoriaEJB.findAll();
            this.nombre_categoria = (String) (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("categoria_sesion"));
            cargarCategoria(this.nombre_categoria);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en init CategoriaController...");
        }

    }

    public void redirectCategorias(String cat) {

        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("categoria_sesion", cat);
            Cliente cliente = (Cliente) (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente"));

            if (cliente == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("categoria.xhtml");
            } else {
                if (cliente.getTipo().equals("a")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/categoria_admin.xhtml");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/categoria_cliente.xhtml");
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el redirect" + e);
        }

    }

    public void sacarCatId(String cat_name) {

        for (Categoria categoria : categorias) {

            if (categoria.getNombre().equals(cat_name)) {
                this.setCodigo_categoria(categoria.getIdcategoria());
            }
        }
    }

    public void cargarCategoria(String cat) throws Exception {
        
        Categoria categoria_aux = new Categoria();
        
        if (cat != null) {
            sacarCatId(cat);
            this.nombre_categoria = cat;
        }else{
             categoria_aux= categoriaEJB.find(this.codigo_categoria);
             this.nombre_categoria = categoria_aux.getNombre();
        }

        try {
            if (cat.equals("Todo")) {
                listarAllProductos();
            } else {

                this.productos = productoEJB.listarProductos(this.codigo_categoria);

            }

        } catch (Exception e) {
            throw e;
        }
    }

    public void listarAllProductos() {

        this.productos = productoEJB.findAll();

    }

    public void eliminarProducto(Producto pro) {

        try {
            productoEJB.remove(pro);
            cargarCategoria(this.nombre_categoria);
        }catch(Exception e){
            
            e.printStackTrace();
            System.out.println("Error en eliminarProducto()...");
        }

    }
    public void modificarProducto(Producto pro){
        
        try{
             productoEJB.edit(pro);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error en modificarProducto()...");
        }
    }

    public CategoriaFacadeLocal getCategoriaEJB() {
        return categoriaEJB;
    }

    public void setCategoriaEJB(CategoriaFacadeLocal categoriaEJB) {
        this.categoriaEJB = categoriaEJB;
    }

    public ProductoFacadeLocal getProductoEJB() {
        return productoEJB;
    }

    public void setProductoEJB(ProductoFacadeLocal productoEJB) {
        this.productoEJB = productoEJB;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getCodigo_categoria() {
        return codigo_categoria;
    }

    public void setCodigo_categoria(int codigo_categoria) {
        this.codigo_categoria = codigo_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    
    
}
