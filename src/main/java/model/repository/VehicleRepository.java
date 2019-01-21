package model.repository;

import model.mapping.Vehicle;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class VehicleRepository extends Repository<Vehicle> {

    public VehicleRepository(Session session){
        setType(Vehicle.class);
        setSession(session);
    }

    public List<Long> getNotOccupiedVehicleIds(){
        session.beginTransaction();

        String hql = "SELECT id FROM Vehicle WHERE occupancyStatus = false AND vehicleStatus = true";
        Query query = session.createQuery(hql);
        List<Long> result = query.getResultList();

        session.getTransaction().commit();

        return result;
    }

    public Vehicle getVehicleById(Long id){
        session.beginTransaction();

        String hql = "FROM Vehicle WHERE id =:id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        Vehicle vehicle = (Vehicle)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return vehicle;
    }

}
