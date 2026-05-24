package domain.DAOContracts;

import domain.Actor;
import java.util.List;
import java.util.Optional;

public interface IActorDAO {
    List<Actor> getAll();
    Optional<Actor> getById(int id);
    List<Actor> searchByName(String nume);
    boolean insert(Actor a);
    boolean update(Actor a);
    boolean delete(int id);
}
