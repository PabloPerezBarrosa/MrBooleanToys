package com.mrboolean.ejb;

import com.mrboolean.model.entities.Pedido;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PedidoFacadeLocal {

    void create(Pedido pedido);

    void edit(Pedido pedido);

    void remove(Pedido pedido);

    Pedido find(Object id);

    List<Pedido> findAll();

    List<Pedido> findRange(int[] range);

    int count();
    
}
