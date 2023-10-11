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
    SportTypeRepository repository;

    public SportType create(SportType sportType) throws EntityStateException {
        Long id = sportType.getId();
        if (id != null && repository.existsById(id))
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
        if (!sportType.getId().equals(pathId)) {
            throw new EntityIdentificationException();
        }
        Long id = sportType.getId();
        if (id == null)
            throw new EntityIdentificationException();
        //todo: check room capacity
        if (repository.existsById(id))
            return repository.save(sportType);
        else
            throw new EntityNotFoundException();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
