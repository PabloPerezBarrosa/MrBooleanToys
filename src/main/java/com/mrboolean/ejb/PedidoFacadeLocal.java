package com.mrboolean.ejb;

import com.mrboolean.model.Pedido;
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
    
    List<Pedido> findByEstado(String estado);
    
    List<Pedido> findByIdCliente(int id_cl);
    
    List<Pedido> findByEstadoAndIdCliente(String estado, int id_cl);
    
}
