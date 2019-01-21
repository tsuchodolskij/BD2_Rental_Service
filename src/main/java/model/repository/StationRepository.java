package model.repository;

import model.mapping.District;
import model.mapping.Station;
import org.hibernate.Session;

public class StationRepository extends Repository<Station> {

    public StationRepository(Session session){
        setType(Station.class);
        setSession(session);
    }
}