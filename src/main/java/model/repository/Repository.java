package model.repository;

import lombok.Data;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Data
public class Repository<T> {

    private Class<T> type;
    protected Session session;

    public List<T> getAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();

        return data;
    }

    public void save(T record) {
        session.beginTransaction();
        session.save(record);
        session.getTransaction().commit();
    }

    public void delete(T record){
        session.beginTransaction();
        session.delete(record);
        session.getTransaction().commit();
    }

    public void update(T record){
        session.beginTransaction();
        session.update(record);
        session.getTransaction().commit();
    }
}
