package model.repository;

import model.mapping.Bus;
import model.mapping.Station;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class BusRepository extends Repository<Bus> {

    public BusRepository(Session session){
        setType(Bus.class);
        setSession(session);
    }

    public Bus getByRegistration(String registration){
        session.beginTransaction();

        String hql = "FROM Bus WHERE Registration_number = :registration";
        Query query = session.createQuery(hql);
        query.setParameter("registration", registration);
        Bus result = (Bus)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return result;
    }
}