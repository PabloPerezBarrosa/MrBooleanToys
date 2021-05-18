package com.mrboolean.model;

import com.mrboolean.model.Cliente;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-05-18T15:29:31")
@StaticMetamodel(Pedido.class)
public class Pedido_ { 

    public static volatile SingularAttribute<Pedido, Cliente> cliente;
    public static volatile SingularAttribute<Pedido, Date> fecha_pedido;
    public static volatile SingularAttribute<Pedido, Double> total_pagado;
    public static volatile SingularAttribute<Pedido, Integer> idpedido;
    public static volatile SingularAttribute<Pedido, String> direccion;
    public static volatile SingularAttribute<Pedido, String> provincia;
    public static volatile SingularAttribute<Pedido, Integer> cp;

}