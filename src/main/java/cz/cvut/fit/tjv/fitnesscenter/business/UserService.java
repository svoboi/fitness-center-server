package cz.cvut.fit.tjv.fitnesscenter.business;


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
public class UserService implements ServiceInterface<User>{

    UserRepository repository;

    public User create(User user) throws EntityStateException {
        Long id = user.getId();
        if (id != null && repository.existsById(id))
            throw new EntityStateException("person with id " + user.getId() + " already exists");
        return repository.save(user);
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
        Long id = user.getId();
        if (id == null)
            throw new EntityStateException("person id missing");
        if (repository.existsById(id))
            return repository.save(user);
        else
            throw new EntityStateException("person with id " + id + " does not exist exists");
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
