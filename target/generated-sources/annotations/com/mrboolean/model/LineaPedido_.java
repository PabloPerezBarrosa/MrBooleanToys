package com.mrboolean.model;

import com.mrboolean.model.Pedido;
import com.mrboolean.model.Producto;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-05-19T09:25:48")
@StaticMetamodel(LineaPedido.class)
public class LineaPedido_ { 

    public static volatile SingularAttribute<LineaPedido, Integer> idlinea;
    public static volatile SingularAttribute<LineaPedido, Pedido> pedido;
    public static volatile SingularAttribute<LineaPedido, Integer> cantidad;
    public static volatile SingularAttribute<LineaPedido, Producto> producto;

}