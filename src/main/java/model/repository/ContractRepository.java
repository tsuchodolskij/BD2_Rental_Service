package model.repository;

import model.mapping.Contract;
import org.hibernate.Session;

public class ContractRepository extends Repository<Contract> {

    public ContractRepository(Session session){
        setType(Contract.class);
        setSession(session);
    }
}