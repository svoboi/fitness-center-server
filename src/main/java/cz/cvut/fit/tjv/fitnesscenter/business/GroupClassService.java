package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class GroupClassService {

    GroupClassRepository repository;

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

    public Collection<GroupClass> findAll() {
        List<GroupClass> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
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
