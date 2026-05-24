package services;

import domain.Scenarist;
import infrastructure.ScenaristEntity;
import infrastructure.ScenaristRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScenaristService {

    private final ScenaristRepository scenaristRepository;

    public ScenaristService(ScenaristRepository scenaristRepository) {
        this.scenaristRepository = scenaristRepository;
    }

    public List<Scenarist> getAll() {
        return scenaristRepository.findAll().stream()
                .map(ScenaristEntity::toScenarist)
                .collect(Collectors.toList());
    }

    public Optional<Scenarist> getById(int id) {
        return scenaristRepository.findById(id)
                .map(ScenaristEntity::toScenarist);
    }

    public boolean insert(Scenarist s) {
        try {
            scenaristRepository.save(new ScenaristEntity(s));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean update(Scenarist s) {
        try {
            if (!scenaristRepository.existsById(s.getId())) return false;
            scenaristRepository.save(new ScenaristEntity(s));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean delete(int id) {
        try {
            if (!scenaristRepository.existsById(id)) return false;
            scenaristRepository.deleteById(id);
            return true;
        } catch (Exception e) { return false; }
    }
}