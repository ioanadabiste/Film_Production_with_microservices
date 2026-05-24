package services;

import domain.Producator;
import infrastructure.ProducatorEntity;
import infrastructure.ProducatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProducatorService {

    private final ProducatorRepository producatorRepository;

    public ProducatorService(ProducatorRepository producatorRepository) {
        this.producatorRepository = producatorRepository;
    }

    public List<Producator> getAll() {
        return producatorRepository.findAll().stream()
                .map(ProducatorEntity::toProducator)
                .collect(Collectors.toList());
    }

    public Optional<Producator> getById(int id) {
        return producatorRepository.findById(id).map(ProducatorEntity::toProducator);
    }

    public boolean insert(Producator p) {
        try {
            producatorRepository.save(new ProducatorEntity(p));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(Producator p) {
        try {
            if (!producatorRepository.existsById(p.getId())) return false;
            producatorRepository.save(new ProducatorEntity(p));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            if (!producatorRepository.existsById(id)) return false;
            producatorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
