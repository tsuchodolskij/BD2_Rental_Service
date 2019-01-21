package model.repository;

import model.mapping.District;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;


public class DistrictRepository extends Repository<District> {

    public DistrictRepository(Session session){
        setType(District.class);
        setSession(session);
    }

    public List<String> getAllNames(){
        session.beginTransaction();

        String hql = "SELECT name FROM District";
        Query query = session.createQuery(hql);
        List<String> result = query.getResultList();

        session.getTransaction().commit();

        return result;
    }

    public District getByName(String name){
        session.beginTransaction();

        String hql = "FROM District WHERE Name = :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        District district = (District)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return district;
    }
}