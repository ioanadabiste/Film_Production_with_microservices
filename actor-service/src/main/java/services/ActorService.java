package services;

import domain.Actor;
import domain.DAOContracts.IActorDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final IActorDAO actorDAO;

    public ActorService(IActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    public List<Actor> getAll() { return actorDAO.getAll(); }
    public Optional<Actor> getById(int id) { return actorDAO.getById(id); }
    public List<Actor> searchByName(String nume) { return actorDAO.searchByName(nume); }
    public boolean insert(Actor a) { return actorDAO.insert(a); }
    public boolean update(Actor a) { return actorDAO.update(a); }
    public boolean delete(int id) { return actorDAO.delete(id); }
}
