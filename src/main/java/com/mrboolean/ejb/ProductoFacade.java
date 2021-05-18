    package com.mrboolean.ejb;

import com.mrboolean.model.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductoFacade extends AbstractFacade<Producto> implements ProductoFacadeLocal {

    @PersistenceContext(unitName = "mrbooleanPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    @Override
    public List<Producto> listarProductos(int id_cat) throws Exception {
        
         List<Producto> lista = null;
        String consulta;
        
        try{
            
            consulta = "FROM Producto p WHERE p.categoria.idcategoria = ?1";
            
            Query query = em.createQuery(consulta);
            query.setParameter(1, id_cat);
            
            lista = query.getResultList();
                 
        }catch(Exception e){
          
            throw e;
            
        }
        return lista;
        
    }
    
}
