package model.repository;

import model.Scenarist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScenaristRepository extends PersonaRepository<Scenarist> {

    public ScenaristRepository(Connection connection) {
        super(connection, "scenaristi");
    }

    @Override
    protected Scenarist map(ResultSet rs) throws SQLException {
        Scenarist scenarist = new Scenarist();
        mapPersoana(rs, scenarist);
        return scenarist;
    }

    @Override
    protected Scenarist createInstance() {
        return new Scenarist();
    }
}