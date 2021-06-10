package com.mrboolean.ejb;

import com.mrboolean.model.Pedido;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PedidoFacade extends AbstractFacade<Pedido> implements PedidoFacadeLocal {

    @PersistenceContext(unitName = "mrbooleanPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoFacade() {
        super(Pedido.class);
    }

    @Override
    public List<Pedido> findByEstado(String estado) {

        List<Pedido> pedidos = new ArrayList<Pedido>();
        String consulta = "";

        try {
            consulta = "FROM Pedido p WHERE p.estado = ?1";
            Query query = em.createQuery(consulta);
            query.setParameter(1, estado);
            pedidos = (List<Pedido>) query.getResultList();
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Error en PedidosFacade sacando por estado........");
        }
        
        return pedidos;

    }
    @Override
    public List<Pedido> findByIdCliente(int id_cl) {
        
        List<Pedido> pedidos = new ArrayList<Pedido>();
        String consulta = "";
        
        try{
            consulta = "FROM Pedido p WHERE p.cliente.idcliente = ?1";
            
            Query query = em.createQuery(consulta);
            query.setParameter(1, id_cl);
            
            pedidos = query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fallo en PedidoFacade sacando lista por Id cliente.....................");
        }
        
        return pedidos;
    }

    @Override
    public List<Pedido> findByEstadoAndIdCliente(String estado, int id_cl) {
        
        List<Pedido> pedidos = new ArrayList<Pedido>();
        String consulta = "";
         try{
             
             consulta = "FROM Pedido p WHERE p.estado =?1 and p.cliente.idcliente = ?2";
            
            Query query = em.createQuery(consulta);
            query.setParameter(1, estado);
            query.setParameter(2, id_cl);
            
            pedidos = query.getResultList();
             
         }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fallo en PedidoFacade sacando lista por Estado e Id cliente.....................");
        }
        
        return pedidos;
    }

}
