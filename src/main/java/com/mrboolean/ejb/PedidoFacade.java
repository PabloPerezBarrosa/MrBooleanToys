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
//    @Override
//    public Cliente findByEmail(String email) {
//        List<Cliente>clientes = new ArrayList<Cliente>();
//        Cliente cliente = new Cliente();
//        String consulta = "";
//        
//        try{
//            
//          consulta = "FROM Cliente c WHERE c.email = ?1";
//          Query query = em.createQuery(consulta);
//          query.setParameter(1,email);
//          clientes =(List<Cliente>) query.getResultList();
//          
//          if(clientes != null && clientes.size() > 0){
//              cliente = clientes.get(0);
//          }else{
//              cliente = null;
//          }
//            
//        }catch(Exception e){
//            e.printStackTrace();
//            System.out.println("Error en findByTipo() en ClienteFacade ...");
//        }
//        return cliente;
//    }
}
