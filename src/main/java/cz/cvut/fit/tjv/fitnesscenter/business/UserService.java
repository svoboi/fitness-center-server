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
        if (user.getEmployee().equals(Boolean.FALSE) && !user.getLeadClasses().isEmpty()) {
            throw new UserNotTrainerException();
        }
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameTakenException();
        }
        validateLeadClasses(user);
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
            throw new EntityIdentificationException();
        }
        if (!exists(user)) {
            throw new EntityNotFoundException("User");
        }
        if (user.getEmployee().equals(Boolean.FALSE) && !user.getLeadClasses().isEmpty()) {
            throw new UserNotTrainerException();
        }
        if (repository.findByUsername(user.getUsername()).isPresent()
                && !repository.findByUsername(user.getUsername()).get().getId().equals(pathId)) {
            throw new UsernameTakenException();
        }
        validateLeadClasses(user);
        removeOriginalLeadClasses(repository.findById(pathId).get());
        User correctIdUser = repository.save(user);
        addUserToLeadClasses(correctIdUser);
        return repository.save(correctIdUser);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Integer countHoursByUserAndMonth(Long userId, LocalDateTime givenTimeFrom, LocalDateTime givenTimeTo) {
        User user = repository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User"));
        Collection<GroupClass> groupClasses =
                groupClassRepository.findAllByTimeFromBetweenAndTrainersContainingOrTimeToBetweenAndTrainersContaining
                        (givenTimeFrom, givenTimeTo, user, givenTimeFrom, givenTimeTo, user);
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

    public Boolean exists(User user) {
        Long id = user.getId();
        return id != null && repository.existsById(id);
    }

    public void validateLeadClasses(User user) {
        for (var groupClass : user.getLeadClasses()) {
            Long groupClassId = groupClass.getId();
            groupClassRepository.findById(groupClassId).orElseThrow(() -> new EntityNotFoundException("Class"));
        }
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
