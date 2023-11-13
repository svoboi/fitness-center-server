package cz.cvut.fit.tjv.fitnesscenter.business;


import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements ServiceInterface<User> {

    private UserRepository repository;
    private GroupClassRepository groupClassRepository;

    public User create(User user) throws EntityStateException {
        if (user.getId() != null && exists(user))
            throw new ConflictingEntityExistsException();
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameTakenException();
        }
        User correctIdUser = repository.save(user);
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
            throw new EntityIdentificationException();
        }
        if (!exists(user)) {
            throw new EntityNotFoundException("User");
        }
        if (repository.findByUsername(user.getUsername()).isPresent()
                && !repository.findByUsername(user.getUsername()).get().getId().equals(pathId)) {
            throw new UsernameTakenException();
        }
        User correctIdUser = repository.save(user);
        return repository.save(correctIdUser);
    }

    public void deleteById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User"));
        for (GroupClass groupClass : groupClassRepository.findAllByTrainersContaining(user)) {
            groupClass.removeTrainer(user);
        }
        repository.deleteById(id);
    }

    public Integer countHoursByUserAndTimeFrame(Long userId, LocalDateTime givenTimeFrom, LocalDateTime givenTimeTo) {
        User user = repository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User"));
        Collection<GroupClass> groupClasses =
                groupClassRepository.findAllByTrainerAndTime(user, givenTimeFrom, givenTimeTo);
        int workedMinutes = 0;
        for (GroupClass groupClass : groupClasses) {
            if (groupClass.getTimeFrom().isBefore(givenTimeFrom)) {
                // counting time in hours: https://stackoverflow.com/questions/25747499/java-8-difference-between-two-localdatetime-in-multiple-units
                workedMinutes += Duration.between(givenTimeFrom, groupClass.getTimeTo()).toMinutes();
            } else if (groupClass.getTimeTo().isAfter(givenTimeTo)) {
                workedMinutes += Duration.between(groupClass.getTimeFrom(), givenTimeTo).toMinutes();
            } else {
                workedMinutes += Duration.between(groupClass.getTimeFrom(), groupClass.getTimeTo()).toMinutes();
            }
        }
        return workedMinutes / 60;
    }

    public Boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username).isEmpty();
    }

    public Boolean exists(User user) {
        Long id = user.getId();
        return id != null && repository.existsById(id);
    }
}
