package model.repository;

import model.mapping.Type;
import model.mapping.User;
import org.hibernate.Session;

public class TypeRepository extends Repository<Type> {

    public TypeRepository(Session session){
        setType(Type.class);
        setSession(session);
    }
}
