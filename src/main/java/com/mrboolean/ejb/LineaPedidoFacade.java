package com.mrboolean.ejb;

import com.mrboolean.model.entities.LineaPedido;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LineaPedidoFacade extends AbstractFacade<LineaPedido> implements LineaPedidoFacadeLocal {

    @PersistenceContext(unitName = "mrbooleanPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaPedidoFacade() {
        super(LineaPedido.class);
    }
    
}
