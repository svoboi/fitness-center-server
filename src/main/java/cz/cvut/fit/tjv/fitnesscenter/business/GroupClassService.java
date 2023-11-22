package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
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

    public GroupClass create(GroupClass groupClass) throws EntityStateException {
        if (exists(groupClass))
            throw new ConflictingEntityExistsException();
        checkEnoughCapacity(
                groupClass.getCapacity(),
                groupClass.getRoom().getId(),
                groupClass.getTimeFrom(),
                groupClass.getTimeTo()
        );
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
        checkEnoughCapacity(
                groupClass.getCapacity(),
                groupClass.getRoom().getId(),
                groupClass.getTimeFrom(),
                groupClass.getTimeTo()
        );
        checkTrainersSetOnlyEmployees(groupClass);
        groupClass.getTrainers().forEach(
                trainer -> checkTrainersAvailability(trainer, groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getId())
        );
        return repository.save(groupClass);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }


    public GroupClass addTrainer(Long classId, Long trainerId) {
        var groupClass = repository.findById(classId).orElseThrow(() -> new EntityNotFoundException("Class"));
        var user = userRepository.findById(trainerId).orElseThrow(() -> new EntityNotFoundException("User"));
        if (!user.getEmployee()) {
            throw new UserNotTrainerException();
        }
        checkTrainersAvailability(user, groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getId());
        groupClass.addTrainer(user);
        return repository.save(groupClass);
    }

    public void removeTrainer(Long classId, Long trainerId) {
        var groupClass = repository.findById(classId).orElseThrow(() -> new EntityNotFoundException("Class"));
        var user = userRepository.findById(trainerId).orElseThrow(() -> new EntityNotFoundException("User"));

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

    public Collection<GroupClass> findAllByRoom(Long id) {
        var room = roomService.findById(id).orElseThrow(() -> new EntityNotFoundException("Room"));
        return repository.findAllByRoom(room);
    }

    public Boolean exists(GroupClass groupClass) {
        Long id = groupClass.getId();
        return id != null && repository.existsById(id);
    }

    public void checkEnoughCapacity(Integer groupClassCapacity, Long roomId, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room"));
        if (groupClassCapacity > countRemainingCapacity(room, timeFrom, timeTo)) {
            throw new NotEnoughCapacityException();
        }
    }

    public Integer countRemainingCapacity(Room room, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Collection<GroupClass> overlappingGroupClasses =
                repository.findAllByRoomAndTime(
                        room,
                        timeFrom,
                        timeTo
                );
        var biggestOverlappingGroupClassOp =
                overlappingGroupClasses.stream().max(
                        Comparator.comparing(GroupClass::getCapacity)
                );
        return (biggestOverlappingGroupClassOp.isEmpty() ?
                room.getCapacity() :
                room.getCapacity() - biggestOverlappingGroupClassOp.get().getCapacity());
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
}
