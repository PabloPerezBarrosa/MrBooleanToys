package com.mrboolean.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpedido;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name ="fecha_pedido", insertable = false)
    private Date fecha_pedido;
    
    @Column(name ="direccion")
    private String direccion;
    
    @Column(name ="provincia")
    private String provincia;
    
    @Column(name ="cp")
    private int cp;
    
    @Column(name ="total_pagado")
    private double total_pagado;
    
    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public Date getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(Date fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public double getTotal_pagado() {
        return total_pagado;
    }

    public void setTotal_pagado(double total_pagado) {
        this.total_pagado = total_pagado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
        
}
