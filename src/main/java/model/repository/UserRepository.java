package model.repository;

import model.mapping.User;
import org.hibernate.Session;

import javax.persistence.Query;

public class UserRepository extends Repository<User> {

    public UserRepository(Session session){
        setType(User.class);
        setSession(session);
    }

    public User getByUsername(String username){
        session.beginTransaction();

        String hql = "FROM User WHERE Username = :username";
        Query query = session.createQuery(hql);
        query.setParameter("username", username);
        User user = (User)query.getResultList().stream().findFirst().orElse(null);

        session.getTransaction().commit();

        return user;
    }

}
