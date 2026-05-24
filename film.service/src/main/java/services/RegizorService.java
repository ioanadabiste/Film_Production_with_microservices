package services;

import domain.Regizor;
import infrastructure.RegizorEntity;
import infrastructure.RegizorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegizorService {

    private final RegizorRepository regizorRepository;

    public RegizorService(RegizorRepository regizorRepository) {
        this.regizorRepository = regizorRepository;
    }

    public List<Regizor> getAll() {
        return regizorRepository.findAll().stream()
                .map(RegizorEntity::toRegizor)
                .collect(Collectors.toList());
    }

    public Optional<Regizor> getById(int id) {
        return regizorRepository.findById(id)
                .map(RegizorEntity::toRegizor);
    }

    public boolean insert(Regizor r) {
        try {
            regizorRepository.save(new RegizorEntity(r));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean update(Regizor r) {
        try {
            if (!regizorRepository.existsById(r.getId())) return false;
            regizorRepository.save(new RegizorEntity(r));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean delete(int id) {
        try {
            if (!regizorRepository.existsById(id)) return false;
            regizorRepository.deleteById(id);
            return true;
        } catch (Exception e) { return false; }
    }
}