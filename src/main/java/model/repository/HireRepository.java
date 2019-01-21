package model.repository;

import model.dialog.result.FinishHireDialogResult;
import model.mapping.Hire;
import model.mapping.User;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class HireRepository extends Repository<Hire> {

    public HireRepository(Session session){
        setType(Hire.class);
        setSession(session);
    }

    public List<Hire> getByUser(User user){
        session.beginTransaction();

        String hql = "FROM Hire WHERE user = :user";
        Query query = session.createQuery(hql);
        query.setParameter("user", user);
        List<Hire> result = query.getResultList();

        session.getTransaction().commit();

        return result;
    }

    public void finishHire(FinishHireDialogResult finishHireRequest) {
        session.beginTransaction();

        String hql = "SELECT id FROM Hire WHERE rentFinishDate = null";
        Query query = session.createQuery(hql);
        Long unfinishedHireId = (Long)query.getResultList().stream().findFirst().orElse(null);

        query = session.createSQLQuery("CALL hire_finished(:districtName, :stationId, :terminalId, :hireId, :finishDate)")
                                .setParameter("districtName", finishHireRequest.getDistrictName())
                                .setParameter("stationId", finishHireRequest.getStationId())
                                .setParameter("terminalId", finishHireRequest.getTerminalId())
                                .setParameter("hireId", unfinishedHireId)
                                .setParameter("finishDate", finishHireRequest.getRentFinishDate());

        query.executeUpdate();

       session.getTransaction().commit();
    }
}
