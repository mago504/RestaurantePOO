/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurante.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class FacturaServicio {
    
    //No verifica si la existencia es 0
    public ArrayList<Factura> getFacturas(Date fechaInicial, Date fechaFinal){
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Criteria query = session.createCriteria(Factura.class);
        query.add(Restrictions.ge("fecha", fechaInicial)); 
        query.add(Restrictions.le("fecha", fechaFinal)); 
        query.add(Restrictions.eq("activo", true));
        
        List<Factura> resultado = query.list();
        
        tx.commit();
        session.close();
        
        return new ArrayList<>(resultado);
    }
    
    public void guardar(Factura factura) {    
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
          
        try{
            session.saveOrUpdate(factura);
            
            for(FacturaDetalle detalle: factura.getFacturaDetalle()){
                Integer id = detalle.getItem().getId();
                
                Criteria query = session.createCriteria(Item.class);
                query.add(Restrictions.eq("id", id));
                query.setMaxResults(1);
                
                Item itemExistente = (Item) query.uniqueResult();
                
                Integer nuevaExistencia = 
                        itemExistente.getExistencia() - detalle.getCantidad();
                
                itemExistente.setExistencia(nuevaExistencia);
                
                session.saveOrUpdate(itemExistente);
            }
            
            tx.commit();
                
        } catch (Exception e){
            tx.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
