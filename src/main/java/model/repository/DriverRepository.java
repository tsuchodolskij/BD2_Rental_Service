package model.repository;

import model.mapping.Driver;
import model.mapping.Station;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DriverRepository extends Repository<Driver> {

    public DriverRepository(Session session){
        setType(Driver.class);
        setSession(session);
    }

    public Driver getByDriversLicenseID(String driversLicenseID){
        session.beginTransaction();

        String hql = "FROM Driver WHERE Drivers_licence_ID = :driversLicenseID";
        Query query = session.createQuery(hql);
        query.setParameter("driversLicenseID", driversLicenseID);
        Driver result = (Driver)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return result;
    }
}