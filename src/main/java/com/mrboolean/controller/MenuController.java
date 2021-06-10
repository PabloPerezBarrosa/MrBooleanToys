package com.mrboolean.controller;

import com.mrboolean.ejb.ClienteFacadeLocal;
import com.mrboolean.mail.SendMail;
import com.mrboolean.model.CartItem;
import com.mrboolean.model.Cliente;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

@Named(value = "menuController")
@ViewScoped
public class MenuController implements Serializable {

    @EJB
    private ClienteFacadeLocal clienteEJB;

    private Cliente cliente;
    private Cliente cliente_sesion;
    private String email_recu;
    private String ruta = "../../../../../Pablo/Mis_Proyectos/MrBooleanToys/src/main/webapp/resources/images/productos/";
    private UploadedFile file;
    private String clave;
    private int id_recu;

    @PostConstruct
    public void init() {
        cliente = new Cliente();
        cliente_sesion = new Cliente();
    }

    public void registrarCliente() {

        List<Cliente> clientes = new ArrayList<Cliente>();
        boolean match_email = false;
        boolean match_name = false;

        try {
         
            clientes = clienteEJB.findAll();

            for (Cliente cl : clientes) {

                if (cl.getNombre().equals(this.cliente.getNombre())) {
                    match_name = true;
                } else if (cl.getEmail().equals(this.cliente.getEmail())) {
                    match_email = true;
                }

            }

            if (!match_name && !match_email) {

                this.cliente.setTipo("c");
                this.cliente.setEstado(0);

                this.cliente.setClave(encriptar(this.cliente.getClave()));

                clienteEJB.create(this.cliente);

                SendMail.sendEmail(this.cliente, 0);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Registro Exitoso"));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Revise su email para verificación de cuenta"));
                PrimeFaces.current().executeScript("PF('wreg').hide();");

            } else {
                if (match_name && match_email) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Nombre y email ya están en uso"));
                } else if (true) {
                    if (match_email) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Email ya en uso"));
                    }
                    if (match_name) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Nombre ya en uso"));
                    }
                }

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo en el Registro"));
            System.out.println("Error en registrarCliente..");
            PrimeFaces.current().executeScript("PF('wreg').hide();");
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

            this.cliente.setClave(encriptar(this.cliente.getClave()));

            cl = clienteEJB.iniciarSesion(this.cliente);
            if (cl != null) {

                if (cl.getEstado() == 1) {
                    List<CartItem> items = new ArrayList<CartItem>();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", cl);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("carrito", items);

                    if (url.equals(url1)) {
                        if (cl.getTipo().equals("a")) {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/principal_admin.xhtml");
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
                }else{
                   PrimeFaces.current().executeScript("PF('wlog').hide();");
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Sus datos son correctos, pero aún no ha confirmado su eMail. Por favor vaya a su correo y entre en el enlace de activación.")); 
                    
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Datos incorrectos"));
            }

        } catch (Exception e) {

            System.out.println("Fallo en iniciarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public void verificarSesionAdmin() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");

            if (cl == null) {
                context.getExternalContext().redirect("/MrBooleanToys/");
            } else {
                if (cl.getTipo().equals("c")) {
                    this.cliente_sesion = cl;
                    context.getExternalContext().redirect("/MrBooleanToys/faces/protegido/user/principal_cliente.xhtml");
                } else {
                    cl = clienteEJB.find(cl.getIdcliente());
                    this.cliente_sesion = cl;
                }
            }

        } catch (Exception e) {
            System.out.println("Fallo en verificarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public void verificarSesionCliente() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");

            if (cl == null) {
                context.getExternalContext().redirect("/MrBooleanToys/");
            } else {
                if (cl.getTipo().equals("a")) {
                    this.cliente_sesion = cl;
                    context.getExternalContext().redirect("/MrBooleanToys/faces/protegido/admin/principal_admin .xhtml");
                } else {
                    cl = clienteEJB.find(cl.getIdcliente());
                    this.cliente_sesion = cl;
                }
            }

        } catch (Exception e) {
            System.out.println("Fallo en verificarSesion() MenuController..");
            e.printStackTrace();
        }

    }

    public void cerrarSesion() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");

            if (cl == null) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/");

            } else {

                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MrBooleanToys/");

            }

        } catch (Exception e) {

            System.out.println("Fallo en cerrarSesion() MenuController..");
            e.printStackTrace();

        }

    }

    public void cerrarSesionIndex() {

        try {

            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        } catch (Exception e) {

            System.out.println("Fallo en cerrarSesion() MenuController..");
            e.printStackTrace();

        }

    }

    public void activarCliente() {

        Cliente cl = new Cliente();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        int id = Integer.parseInt(request.getParameter("key1"));

        cl = clienteEJB.find(id);

        cl.setEstado(1);

        clienteEJB.edit(cl);

    }
    public void cambiarClave(){
        
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            Cliente cl = (Cliente) context.getExternalContext().getSessionMap().get("cliente");
            
            SendMail.sendEmail(cl, 2);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Revise su email para cambiar su clave"));
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fallo en cambiarClave.......");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fallo enviando email de cambio de clave."));
        }
        
    }

    public void recuperarClave() {

        Cliente cl = new Cliente();

        try {

            cl = clienteEJB.findByEmail(this.email_recu);

            if (cl != null) {
                SendMail.sendEmail(cl, 1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Revise su email para recuperar su clave"));
                PrimeFaces.current().executeScript("PF('wrecu').hide();");
                PrimeFaces.current().executeScript("PF('wlog').hide();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "No ha introducido un email que corresponda a ninguno de nuestros clientes."));
                PrimeFaces.current().executeScript("PF('wrecu').hide();");
                PrimeFaces.current().executeScript("PF('wlog').hide();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fallo en recuperarClave()...................");
        }
    }

    public void loadIdRecu() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        this.id_recu = Integer.parseInt(request.getParameter("key1"));

        PrimeFaces.current().executeScript("PF('wrecuclave').show();");

    }

    public void claveClienteRecu() {

        try {

            Cliente cl = new Cliente();

            cl = clienteEJB.find(this.id_recu);

            this.clave = encriptar(this.clave);

            cl.setClave(this.clave);

            clienteEJB.edit(cl);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Clave cambiada con éxito."));
            System.out.println(cl.getNombre());

        } catch (Exception e) {

            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Fallo al cambiar la clave."));

        }

    }

    /*MODIFICACIÓN DE OTROS DATOS DE USUARIO POR EL USUARIO*/
    public void modUser() {

        String cat = "";
        List<CartItem> items = new ArrayList<CartItem>();
        clienteEJB.edit(this.cliente_sesion);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Nombre modificado correctamente"));
        PrimeFaces.current().executeScript("PF('wuser').hide();");
    }

    /*FUNCIONES DE MODIFICACIÓN DE IMAGEN DE USUARIO*/
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

    public void modificarImagen() {
        String cat = "";
        List<CartItem> items = new ArrayList<CartItem>();
        try {

            upload();
            this.cliente_sesion.setUrl(getFile().getFileName());
            clienteEJB.edit(this.cliente_sesion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Imagen modificada correctamente, espere para recargar carpeta imagenes."));

            PrimeFaces.current().executeScript("PF('wuserimg').hide();");
            PrimeFaces.current().executeScript("PF('wuser').hide();");

            RequestContext.getCurrentInstance().execute("redirectDelayUserPrin();");

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Fallo en modificar Imagen .......");
        }

    }

    private static String encriptar(String s) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
    }

    private static String desencriptar(String s) throws UnsupportedEncodingException {
        byte[] decode = Base64.getDecoder().decode(s.getBytes());
        return new String(decode, "utf-8");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente_sesion() {
        return cliente_sesion;
    }

    public void setCliente_sesion(Cliente cliente_sesion) {
        this.cliente_sesion = cliente_sesion;
    }

    public String getEmail_recu() {
        return email_recu;
    }

    public void setEmail_recu(String email_recu) {
        this.email_recu = email_recu;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getId_recu() {
        return id_recu;
    }

    public void setId_recu(int id_recu) {
        this.id_recu = id_recu;
    }

}
