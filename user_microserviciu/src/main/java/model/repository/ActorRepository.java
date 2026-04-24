package model.repository;

import model.Actor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActorRepository extends PersonaRepository<Actor> {

    public ActorRepository(Connection connection) {
        super(connection, "actori");
    }

    @Override
    protected Actor map(ResultSet rs) throws SQLException {
        Actor actor = new Actor();
        mapPersoana(rs, actor);
        return actor;
    }

    @Override
    protected Actor createInstance() {
        return new Actor();
    }
}
