package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.SportTypeRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class GroupClassService implements ServiceInterface<GroupClass> {

    private GroupClassRepository repository;
    private RoomService roomService;
    private UserRepository userRepository;
    private SportTypeRepository sportTypeRepository;

    public GroupClass create(GroupClass groupClass) throws EntityStateException {
        if (exists(groupClass))
            throw new ConflictingEntityExistsException();
        if (groupClass.getRoom() != null) {
            checkEnoughCapacity(groupClass, false);
        }
        sportTypeCheck(groupClass.getSportType());
        checkTrainersSetOnlyEmployees(groupClass);
        groupClass.getTrainers().forEach(
                trainer -> {
                    User realUser = userRepository.findById(trainer.getId()).get();
                    checkTrainersAvailability(realUser, groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getId());
                }
        );
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
        if (groupClass.getId() == null || !groupClass.getId().equals(pathId)) {
            throw new EntityIdentificationException();
        }
        if (!exists(groupClass))
            throw new EntityNotFoundException("Class");
        sportTypeCheck(groupClass.getSportType());
        if (groupClass.getRoom() != null) {
            checkEnoughCapacity(groupClass, true);
        }
        checkTrainersSetOnlyEmployees(groupClass);
        groupClass.getTrainers().forEach(
                trainer -> {
                    User realUser = userRepository.findById(trainer.getId()).get();
                    checkTrainersAvailability(realUser, groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getId());
                }
        );
        return repository.save(groupClass);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Vector<String> trainersUsernamesInGroupClass(Long classId) {
        var groupClass = repository.findById(classId).orElseThrow(() -> new EntityNotFoundException("Class"));
        Vector<String> usernames = new Vector<String>();
        for (var trainer : groupClass.getTrainers()) {
            usernames.add(userRepository.findById(trainer.getId()).get().getUsername());
        }
        return usernames;
    }

    public GroupClass addTrainer(Long classId, String trainerUsername) {
        var groupClass = repository.findById(classId).orElseThrow(() -> new EntityNotFoundException("Class"));
        var user = userRepository.findByUsername(trainerUsername).orElseThrow(() -> new EntityNotFoundException("User"));
        if (!user.getEmployee()) {
            throw new UserNotTrainerException();
        }
        checkTrainersAvailability(user, groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getId());
        groupClass.addTrainer(user);
        return repository.save(groupClass);
    }

    public void removeTrainer(Long classId, String trainerUsername) {
        var groupClass = repository.findById(classId).orElseThrow(() -> new EntityNotFoundException("Class"));
        var user = userRepository.findByUsername(trainerUsername).orElseThrow(() -> new EntityNotFoundException("User"));

        groupClass.removeTrainer(user);
        repository.save(groupClass);
    }

    public void checkTrainersSetOnlyEmployees(GroupClass groupClass) {
        for (var user : groupClass.getTrainers()) {
            var foundUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User"));
            if (!foundUser.getEmployee()) {
                throw new UserNotTrainerException();
            }
        }
    }

    public Boolean exists(GroupClass groupClass) {
        Long id = groupClass.getId();
        return id != null && repository.existsById(id);
    }


    public void checkEnoughCapacity(GroupClass groupClass, Boolean update) {
        Integer remainingCapacity = countRemainingCapacity(groupClass, update);
        if (groupClass.getCapacity() > remainingCapacity) {
            throw new NotEnoughCapacityException(remainingCapacity.toString());
        }
    }

    public Integer overlappingMaximum(Vector<GroupClassOverlapHelper> data) {
        Collections.sort(data);
        int maximum = 0;
        int curr = 0;
        for (var element : data) {
            if (element.end) {
                curr -= element.capacity;
            } else {
                curr += element.capacity;
            }
            if (maximum < curr) {
                maximum = curr;
            }
        }
        return maximum;
    }

    public Integer countRemainingCapacity(GroupClass groupClass, Boolean update) {
        Room room = roomService.findById(groupClass.getRoom().getId())
                .orElseThrow(() -> new EntityNotFoundException("Room"));
        Collection<GroupClass> overlappingGroupClasses =
                repository.findAllByRoomAndTime(
                        room,
                        groupClass.getTimeFrom(),
                        groupClass.getTimeTo()
                );
        if (update) {
            overlappingGroupClasses.remove(groupClass);
        }
        Vector<GroupClassOverlapHelper> data = new Vector<>();
        for (var overlappingClass : overlappingGroupClasses) {
            data.add(new GroupClassOverlapHelper(overlappingClass.getTimeFrom(), false, overlappingClass.getCapacity()));
            data.add(new GroupClassOverlapHelper(overlappingClass.getTimeTo(), true, overlappingClass.getCapacity()));
        }
        return room.getCapacity() - overlappingMaximum(data);
    }

    public void checkTrainersAvailability(User user, LocalDateTime timeFrom, LocalDateTime timeTo, Long groupClassId) {
        Collection<GroupClass> overlappingGroupClasses =
                repository.findAllByTrainerAndTime(
                        user,
                        timeFrom,
                        timeTo
                );
        if (!overlappingGroupClasses.isEmpty()) {
            // checks if the conflicting class is the same one were trying to change
            if (overlappingGroupClasses.size() == 1
                    && !(groupClassId == null)
                    && overlappingGroupClasses.iterator().next().getId().equals(groupClassId)) {
                return;
            }
            throw new TrainerNotAvailableException(user.getUsername());
        }
    }

    void sportTypeCheck(SportType sportType) {
        if (sportType != null) {
            sportTypeRepository.findById(sportType.getId()).orElseThrow(() -> new EntityNotFoundException("SportType"));
        }
    }
}
