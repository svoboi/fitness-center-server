package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupClassService {
    public final GroupClassRepository repository;

    public GroupClassService(GroupClassRepository repository) {
        this.repository = repository;
    }

    public GroupClass create(GroupClass entity) throws EntityStateException {
        Long id = entity.getId();
        if (id != null && repository.existsById(id))
            throw new EntityStateException("class with id " + entity.getId() + " already exists");
        //todo: check room capacity
        return repository.save(entity);
    }

    public Optional<GroupClass> findById(Long id) {
        return repository.findById(id);
    }

    public Iterable<GroupClass> findAll() {
        return repository.findAll();
    }

    public GroupClass update(GroupClass entity) throws EntityStateException {
        Long id = entity.getId();
        if (id == null)
            throw new EntityStateException("class id missing");
        //todo: check room capacity
        if (repository.existsById(id))
            return repository.save(entity);
        else
            throw new EntityStateException("class with id " + id + " does not exist exists");
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

//    public void checkCapacity throws InsufficientCapacityException () {}

}
