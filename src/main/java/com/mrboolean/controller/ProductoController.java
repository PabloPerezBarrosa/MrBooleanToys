package com.mrboolean.controller;

import com.mrboolean.ejb.CategoriaFacadeLocal;
import com.mrboolean.ejb.ProductoFacadeLocal;
import com.mrboolean.model.Categoria;
import com.mrboolean.model.Producto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

@Named
@ViewScoped
public class ProductoController implements Serializable {

    @EJB
    private CategoriaFacadeLocal categoriaEJB;
    @EJB
    private ProductoFacadeLocal productoEJB;

    private String ruta = "../../../../../Pablo/Mis_Proyectos/MrBooleanToys/src/main/webapp/resources/images/productos/";
    private UploadedFile file;
    private Producto producto;
    private List<Categoria> categorias = new ArrayList<Categoria>();
    private int cat_id;

    @PostConstruct
    public void init() {
        this.producto = new Producto();
        loadCategorias();

    }

    public void loadCategorias() {

        this.categorias = categoriaEJB.findAll();

    }

    public void transferFile(String fileName, InputStream in) {

        try {

            OutputStream out = new FileOutputStream(new File(this.ruta + fileName));
            int reader = 0;
            byte[] bytes = new byte[(int) getFile().getSize()];
            while ((reader = in.read(bytes)) != -1) {

                out.write(bytes, 0, reader);

            }
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void upload() {

        String extValidate;
        if (getFile() != null) {

            String ext = getFile().getFileName();

            if (ext != null) {
                extValidate = ext.substring(ext.indexOf(".") + 1);
            } else {
                extValidate = null;
            }
            try {

                transferFile(getFile().getFileName(), getFile().getInputstream());

            } catch (Exception e) {
                Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, e);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Fallo", "Error subiendo el archivo"));
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Conseguido", getFile().getFileName() + " se subió " + getFile().getContentType() + " tamaño " + getFile().getSize()));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "Seleccione un archivo"));
        }
    }

    public void añadirProducto() {

        upload();

        Categoria categoria = new Categoria();

        this.producto.setUrl(getFile().getFileName());

        categoria = categoriaEJB.find(this.cat_id);

        this.producto.setCategoria(categoria);

        productoEJB.create(this.producto);

    }

    public void leerProducto(Producto pro){
        
        this.producto = pro;
   
    }
    
    public void modificarImagen(){
    
        upload();
        this.producto.setUrl(getFile().getFileName());
        productoEJB.edit(this.producto);
        
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

}
