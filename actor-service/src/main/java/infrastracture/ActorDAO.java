package infrastracture;

import domain.Actor;
import domain.DAOContracts.IActorDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ActorDAO implements IActorDAO {

    private final ActorRepository repository;

    public ActorDAO(ActorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Actor> getAll() {
        return repository.findAll().stream()
                .map(ActorEntity::toActor)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Actor> getById(int id) {
        return repository.findById(id).map(ActorEntity::toActor);
    }

    @Override
    public List<Actor> searchByName(String nume) {
        return repository.findByNumeContainingIgnoreCase(nume).stream()
                .map(ActorEntity::toActor)
                .collect(Collectors.toList());
    }

    @Override
    public boolean insert(Actor a) {
        try { repository.save(new ActorEntity(a)); return true; }
        catch (Exception e) { return false; }
    }

    @Override
    public boolean update(Actor a) {
        try {
            if (!repository.existsById(a.getId())) return false;
            repository.save(new ActorEntity(a)); return true;
        } catch (Exception e) { return false; }
    }

    @Override
    public boolean delete(int id) {
        try {
            if (!repository.existsById(id)) return false;
            repository.deleteById(id); return true;
        } catch (Exception e) { return false; }
    }
}
