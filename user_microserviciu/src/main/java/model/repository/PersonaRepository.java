package model.repository;

import model.Persoana;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PersonaRepository<T extends Persoana> {

    protected final Connection connection;
    protected final String tableName;

    public PersonaRepository(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }


    protected abstract T map(ResultSet rs) throws SQLException;
    protected abstract T createInstance();

    public void adauga(T persoana) {
        String sql = "INSERT INTO " + tableName + " (id, nume, prenume, an_nastere, nationalitate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, persoana.getId());
            stmt.setString(2, persoana.getNume());
            stmt.setString(3, persoana.getPrenume());
            stmt.setInt(4, persoana.getAnNastere());
            stmt.setString(5, persoana.getNationalitate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare adaugare in " + tableName + ": " + e.getMessage());
        }
    }

    public void actualizeaza(T persoana) {
        String sql = "UPDATE " + tableName + " SET nume=?, prenume=?, an_nastere=?, nationalitate=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, persoana.getNume());
            stmt.setString(2, persoana.getPrenume());
            stmt.setInt(3, persoana.getAnNastere());
            stmt.setString(4, persoana.getNationalitate());
            stmt.setString(5, persoana.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare actualizare in " + tableName + ": " + e.getMessage());
        }
    }

    public void sterge(String id) {
        String sql = "DELETE FROM " + tableName + " WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare stergere din " + tableName + ": " + e.getMessage());
        }
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " ORDER BY nume, prenume";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Eroare listare din " + tableName + ": " + e.getMessage());
        }
        return list;
    }

    public T getById(String id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare cautare in " + tableName + ": " + e.getMessage());
        }
        return null;
    }

    // Metodă helper comună pentru maparea câmpurilor din Persoana
    protected void mapPersoana(ResultSet rs, T persoana) throws SQLException {
        persoana.setId(rs.getString("id"));
        persoana.setNume(rs.getString("nume"));
        persoana.setPrenume(rs.getString("prenume"));
        persoana.setAnNastere(rs.getInt("an_nastere"));
        persoana.setNationalitate(rs.getString("nationalitate"));
    }
}