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
public class GroupClassService implements ServiceInterface<GroupClass> {

    GroupClassRepository repository;

    public GroupClass create(GroupClass groupClass) throws EntityStateException {
        Long id = groupClass.getId();
        if (id != null && repository.existsById(id))
            throw new EntityStateException("class with id " + groupClass.getId() + " already exists");
        //todo: check room capacity
        return repository.save(groupClass);
    }

    public Optional<GroupClass> findById(Long id) {
        return repository.findById(id);
    }

    public Collection<GroupClass> findAll() {
        List<GroupClass> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public GroupClass update(GroupClass groupClass, Long pathId) throws EntityStateException {
        if (!groupClass.getId().equals(pathId)) {
            throw new EntityStateException("conficting id in path and in body");
        }
        Long id = groupClass.getId();
        if (id == null)
            throw new EntityStateException("class id missing");
        //todo: check room capacity
        if (repository.existsById(id))
            return repository.save(groupClass);
        else
            throw new EntityStateException("class with id " + id + " does not exist exists");
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

//    public void checkCapacity throws InsufficientCapacityException () {}

}
