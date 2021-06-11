package com.mrboolean.ejb;

import com.mrboolean.model.LineaPedido;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @Override
    public List<LineaPedido> findByPedidoId(int id_ped) {
       List<LineaPedido> lineas = new ArrayList<LineaPedido>();
       String consulta = "";
       
       try{
             
            consulta = "FROM LineaPedido l WHERE l.pedido.idpedido = ?1";
            
            Query query = em.createQuery(consulta);
            query.setParameter(1, id_ped);
            
            lineas = query.getResultList();
             
         }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fallo en LineaPedidoFacade sacando lista por Id pedido.....................");
        }
        
        return lineas;
       
    }
    
//    @Override
//    public List<Pedido> findByEstadoAndIdCliente(String estado, int id_cl) {
//        
//        List<Pedido> pedidos = new ArrayList<Pedido>();
//        String consulta = "";
//         try{
//             
//             consulta = "FROM Pedido p WHERE p.estado =?1 and p.cliente.idcliente = ?2";
//            
//            Query query = em.createQuery(consulta);
//            query.setParameter(1, estado);
//            query.setParameter(2, id_cl);
//            
//            pedidos = query.getResultList();
//             
//         }catch(Exception e){
//            e.printStackTrace();
//            System.out.println("Fallo en PedidoFacade sacando lista por Estado e Id cliente.....................");
//        }
//        
//        return pedidos;
//    }
}
