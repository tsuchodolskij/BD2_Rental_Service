package model.repository;

import model.mapping.Station;
import model.mapping.Terminal;
import org.hibernate.Session;

public class TerminalRepository extends Repository<Terminal> {

    public TerminalRepository(Session session){
        setType(Terminal.class);
        setSession(session);
    }
}