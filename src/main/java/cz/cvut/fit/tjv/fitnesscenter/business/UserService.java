package cz.cvut.fit.tjv.fitnesscenter.business;


import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements ServiceInterface<User> {

    UserRepository repository;
    GroupClassRepository groupClassRepository;

    public User create(User user) throws EntityStateException {
        Long id = user.getId();
        if (id != null && exists(user))
            throw new EntityStateException("person with id " + user.getId() + " already exists");
        if (user.getEmployee().equals(Boolean.FALSE) && !user.getLeadClasses().isEmpty()) {
            throw new EntityStateException("non-employee cant teach classes");
        }
        if (!leadClassesSetValid(user)) {
            throw new EntityStateException("at least one of the classes IDs is invalid");
        }
        User correctIdUser = repository.save(user);
        addUserToLeadClasses(correctIdUser);
        return repository.save(correctIdUser);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Collection<User> findAll() {
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public User update(User user, Long pathId) throws EntityStateException {
        if (!user.getId().equals(pathId)) {
            throw new EntityStateException("conficting id in path and in body");
        }
        if (!exists(user)) {
            throw new EntityStateException("class id missing or class with this id doesnt exist");
        }
        if (user.getEmployee().equals(Boolean.FALSE) && !user.getLeadClasses().isEmpty()) {
            throw new EntityStateException("non-employee cant teach classes");
        }
        if (!leadClassesSetValid(user)) {
            throw new EntityStateException("at least one of the classes IDs is invalid");
        }
        removeOriginalLeadClasses(repository.findById(pathId).get());
        User correctIdUser = repository.save(user);
        addUserToLeadClasses(correctIdUser);
        return repository.save(correctIdUser);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Boolean exists(User user) {
        Long id = user.getId();
        return id != null && repository.existsById(id);
    }

    public Boolean leadClassesSetValid(User user) {
        for (var groupClass : user.getLeadClasses()) {
            Long groupClassId = groupClass.getId();
            if (groupClassRepository.findById(groupClassId).isEmpty()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void addUserToLeadClasses(User user) {
        for (var groupClass : user.getLeadClasses()) {
            var groupClassFromRep = groupClassRepository.findById(groupClass.getId()).get();
            groupClassFromRep.addTrainer(user);
            groupClassRepository.save(groupClassFromRep);
        }
    }

    public void removeOriginalLeadClasses(User user) {
        for (var groupClass : user.getLeadClasses()) {
            var groupClassFromRep = groupClassRepository.findById(groupClass.getId()).get();
            groupClassFromRep.removeTrainer(user);
            groupClassRepository.save(groupClassFromRep);
        }
    }
}
