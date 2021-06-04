package com.mrboolean.ejb;

import com.mrboolean.model.Producto;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ProductoFacadeLocal {

    void create(Producto producto);

    void edit(Producto producto);

    void remove(Producto producto);

    Producto find(Object id);

    List<Producto> findAll();

    List<Producto> findRange(int[] range);

    int count();
    
    List<Producto> listarProductos(int id_cat) throws Exception;
}
