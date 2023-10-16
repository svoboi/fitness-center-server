package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.SportTypeRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityStateException;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SportTypeService implements ServiceInterface<SportType> {
    private SportTypeRepository repository;

    public SportType create(SportType sportType) throws EntityStateException {
        if (exists(sportType))
            throw new ConflictingEntityExistsException();
        return repository.save(sportType);
    }

    public Optional<SportType> findById(Long id) {
        return repository.findById(id);
    }

    public Collection<SportType> findAll() {
        List<SportType> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public SportType update(SportType sportType, Long pathId) throws EntityStateException {
        if (sportType.getId() == null || !sportType.getId().equals(pathId)) {
            throw new EntityIdentificationException();
        }
        if (!repository.existsById(sportType.getId())) {
            throw new EntityNotFoundException("Room");
        }
        return repository.save(sportType);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Boolean exists(SportType sportType) {
        Long id = sportType.getId();
        return id != null && repository.existsById(id);
    }
}
