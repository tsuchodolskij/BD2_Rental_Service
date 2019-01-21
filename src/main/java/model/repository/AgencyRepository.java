package model.repository;

import model.mapping.Agency;
import model.mapping.District;
import model.mapping.Hire;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class AgencyRepository extends Repository<Agency> {

    public AgencyRepository(Session session){
        setType(Agency.class);
        setSession(session);
    }

    public List<Agency> getByDistrict(District district){
        session.beginTransaction();

        String hql = "FROM Agency WHERE District_Name = :district";
        Query query = session.createQuery(hql);
        query.setParameter("district", district);
        List<Agency> result = query.getResultList();

        session.getTransaction().commit();

        return result;
    }

    public Agency getByPk(Long id, District district){
        session.beginTransaction();

        String hql = "FROM Agency WHERE District_Name = :district AND ID = :id";
        Query query = session.createQuery(hql);
        query.setParameter("district", district);
        query.setParameter("id", id);
        Agency result = (Agency)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return result;
    }

    public Agency getByAddress(String house, String code, String street){
        session.beginTransaction();

        String hql = "FROM Agency WHERE Address_House_number = :house AND Address_Post_code = :code AND Address_Street = :street";
        Query query = session.createQuery(hql);
        query.setParameter("house", house);
        query.setParameter("code", code);
        query.setParameter("street", street);
        Agency result = (Agency)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return result;
    }
}
