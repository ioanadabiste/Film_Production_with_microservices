package model.repository;

import model.Regizor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegizorRepository extends PersonaRepository<Regizor> {

    public RegizorRepository(Connection connection) {
        super(connection, "regizori");
    }

    @Override
    protected Regizor map(ResultSet rs) throws SQLException {
        Regizor regizor = new Regizor();
        mapPersoana(rs, regizor);
        return regizor;
    }

    @Override
    protected Regizor createInstance() {
        return new Regizor();
    }
}
