package model.repository;

import model.mapping.Address;
import model.mapping.Agency;
import model.mapping.District;
import org.hibernate.Session;

import javax.persistence.Query;

public class AddressRepository extends Repository<Address> {

    public AddressRepository(Session session){
        setType(Address.class);
        setSession(session);
    }
}