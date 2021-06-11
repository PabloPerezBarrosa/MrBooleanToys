package com.mrboolean.ejb;

import com.mrboolean.model.LineaPedido;
import java.util.List;
import javax.ejb.Local;

@Local
public interface LineaPedidoFacadeLocal {

    void create(LineaPedido lineaPedido);

    void edit(LineaPedido lineaPedido);

    void remove(LineaPedido lineaPedido);

    LineaPedido find(Object id);

    List<LineaPedido> findAll();

    List<LineaPedido> findRange(int[] range);

    int count();
    
    List<LineaPedido> findByPedidoId(int id_ped);
    
}
