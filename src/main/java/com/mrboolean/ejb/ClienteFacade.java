package com.mrboolean.ejb;

import com.mrboolean.model.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> implements ClienteFacadeLocal {

    @PersistenceContext(unitName = "mrbooleanPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }
    @Override
    public Cliente iniciarSesion(Cliente cl){
        Cliente cliente = null;
        String consulta = "";
        try{
            List<Cliente> clientes = new ArrayList<Cliente>();
            
            consulta = "FROM Cliente c WHERE c.nombre = ?1 AND c.clave = ?2";
            Query query = em.createQuery(consulta);
            query.setParameter(1,cl.getNombre());
            query.setParameter(2,cl.getClave());
            
            clientes = query.getResultList();
            if(!clientes.isEmpty()){
                cliente = clientes.get(0);
            }
            
            
        }catch(Exception e){
            System.out.println("Fallo en iniciarSesion() ClienteFacade..");
            e.printStackTrace();
        }
        
        return cliente;
    }
    
}
